package cn.edu.hit.application.game;

import android.app.Activity;

import java.util.Objects;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.aircraft.BossEnemy;
import cn.edu.hit.aircraft.HeroAircraft;
import cn.edu.hit.application.ImageManager;
import cn.edu.hit.application.MusicService;
import cn.edu.hit.background.GameBackground;
import cn.edu.hit.background.SimpleGameBackground;

public class SimpleGame extends Game {
    protected int step = 100;

    public SimpleGame(Activity context, String difficulty, boolean musicEnable) {
        super(context, difficulty, musicEnable);
    }

    @Override
    protected GameBackground createBackground() {
        return new SimpleGameBackground(context);
    }

    @Override
    protected void scoreCheck() {
        if (score >= step) {
            //没有boss则产生boss，已经有boss则刷新boss
            if (Objects.nonNull(boss)) {
                context.runOnUiThread(()-> this.removeView(boss));
                boss.vanish();
                System.out.println("boss刷新");
            }
            boss = new BossEnemy(
                    context
                    , GameActivity.WINDOW_WIDTH / 2,
                    ImageManager.BOSS_IMAGE.getHeight(),
                    0, 0, 300);
            MusicService.isBossAlive = true;
            context.runOnUiThread(() -> this.addView(boss));
            step += step;
        }
    }
}
