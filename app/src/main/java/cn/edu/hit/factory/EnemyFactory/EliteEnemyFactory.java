package cn.edu.hit.factory.EnemyFactory;

import android.app.Activity;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.aircraft.AbstractAircraft;
import cn.edu.hit.aircraft.EliteEnemy;
import cn.edu.hit.application.ImageManager;

public class EliteEnemyFactory implements EnemyFactory{
    @Override
    public AbstractAircraft creatEnemy(Activity context) {
        EliteEnemy enemy = new EliteEnemy(
                context,
                (int) (Math.random() * (GameActivity.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * GameActivity.WINDOW_HEIGHT * 0.2),
                0,
                10,
                30
        );
        return enemy;
    }
}
