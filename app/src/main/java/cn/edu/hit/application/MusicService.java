package cn.edu.hit.application;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.edu.hit.R;
import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.aircraft.MobEnemy;

public class MusicService extends Service {
    private MediaPlayer bgmPlayer;
    private MediaPlayer bossBgmPlayer;
    private static SoundPool soundPool;
    private static Map<Integer, Integer> soundMap;

    public static boolean isBossAlive = false;

    //Scheduled 线程池，用于定时任务调度
    ScheduledExecutorService executorService;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        bgmPlayer = MediaPlayer.create(this, R.raw.bgm);
        bossBgmPlayer = MediaPlayer.create(this, R.raw.bgm_boss);
        executorService = new ScheduledThreadPoolExecutor(1);

        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(10)
                .build();
        soundMap = new HashMap<>();
        soundMap.put(R.raw.bomb_explosion, soundPool.load(this, R.raw.bomb_explosion, 1));
        soundMap.put(R.raw.bullet, soundPool.load(this, R.raw.bullet, 1));
        soundMap.put(R.raw.bullet_hit, soundPool.load(this, R.raw.bullet_hit, 1));
        soundMap.put(R.raw.game_over, soundPool.load(this, R.raw.game_over, 1));
        soundMap.put(R.raw.get_supply, soundPool.load(this, R.raw.get_supply, 1));
    }

    public static void playSound(boolean musicEnable, int key) {
        if (musicEnable) {
            soundPool.play(soundMap.get(key), 1, 1, 0, 0, 1);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable task = () -> {
            if (MusicService.isBossAlive) {
                bgmPlayer.stop();
                if (!bossBgmPlayer.isPlaying()) {
                    bossBgmPlayer.start();
                }
            } else {
                bossBgmPlayer.stop();
                if (!bgmPlayer.isPlaying()) {
                    bgmPlayer.start();
                }
            }
        };
        int timeInterval = 40;
        executorService.scheduleWithFixedDelay(task, 0, timeInterval, TimeUnit.MILLISECONDS);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bgmPlayer.stop();
        bossBgmPlayer.stop();
        bgmPlayer.release();
        bossBgmPlayer.release();
    }

    public boolean isRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServices = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(10);
        for (int i = 0; i < runningServices.size(); i++) {
            if ("cn.edu.hit.application.MusicService".equals(runningServices.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}