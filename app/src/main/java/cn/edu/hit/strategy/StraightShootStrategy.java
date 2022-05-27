package cn.edu.hit.strategy;

import android.app.Activity;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import cn.edu.hit.bullet.BaseBullet;
import cn.edu.hit.bullet.HeroBullet;

public class StraightShootStrategy implements ShootStrategy{
    @Override
    public List<BaseBullet> shoot(Class<? extends BaseBullet> bulletClass, Activity context, int x, int y, int speedX, int speedY, int power, int shootNum) {
        List<BaseBullet> res = new LinkedList<>();
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            Constructor<? extends BaseBullet> constructor = null;
            BaseBullet baseBullet = null;
            try {
                constructor = bulletClass.getDeclaredConstructor(Context.class, int.class, int.class, int.class, int.class, int.class);
                baseBullet = constructor.newInstance(context, x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            } catch (Exception e) {
                e.printStackTrace();
            }
            res.add(baseBullet);
        }
        return res;
    }
}
