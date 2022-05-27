package cn.edu.hit.strategy;

import android.app.Activity;

import java.util.List;

import cn.edu.hit.bullet.BaseBullet;
import cn.edu.hit.bullet.HeroBullet;

public interface ShootStrategy {

    List<BaseBullet> shoot(Class<? extends BaseBullet> bulletClass, Activity context, int x, int y, int speedX, int speedY, int power, int shootNum);
}
