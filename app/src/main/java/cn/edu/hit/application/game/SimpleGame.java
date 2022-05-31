package cn.edu.hit.application.game;

import android.app.Activity;

import java.util.Objects;

import cn.edu.hit.application.MusicService;
import cn.edu.hit.background.GameBackground;
import cn.edu.hit.background.SimpleGameBackground;
import cn.edu.hit.factory.EnemyFactory.BossFactory;

public class SimpleGame extends Game {
    protected int step = 100;
    private BossFactory bossFactory;
    public SimpleGame(Activity context, String difficulty, boolean musicEnable) {
        super(context, difficulty, musicEnable);
        bossFactory = new BossFactory();
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
            boss = bossFactory.creatEnemy(context);
            MusicService.isBossAlive = true;
            context.runOnUiThread(() -> this.addView(boss));
            step += step;
        }
    }
}
