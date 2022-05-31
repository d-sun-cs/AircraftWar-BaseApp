package cn.edu.hit.factory.EnemyFactory;

import android.app.Activity;

import cn.edu.hit.aircraft.AbstractAircraft;

public interface EnemyFactory {
    AbstractAircraft creatEnemy(Activity context);
}
