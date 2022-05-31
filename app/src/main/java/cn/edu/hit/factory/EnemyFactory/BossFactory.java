package cn.edu.hit.factory.EnemyFactory;

import android.app.Activity;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.aircraft.AbstractAircraft;
import cn.edu.hit.aircraft.BossEnemy;
import cn.edu.hit.application.ImageManager;

public class BossFactory implements EnemyFactory{
    @Override
    public AbstractAircraft creatEnemy(Activity context) {
        BossEnemy boss = new BossEnemy(
                context
                , GameActivity.WINDOW_WIDTH / 2,
                ImageManager.BOSS_IMAGE.getHeight(),
                0, 0, 300);
        return boss;
    }
}
