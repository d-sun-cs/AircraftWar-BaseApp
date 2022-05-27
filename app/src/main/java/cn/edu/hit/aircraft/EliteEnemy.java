package cn.edu.hit.aircraft;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import cn.edu.hit.bullet.BaseBullet;
import cn.edu.hit.bullet.EnemyBullet;
import cn.edu.hit.bullet.HeroBullet;
import cn.edu.hit.prop.AbstractProp;
import cn.edu.hit.prop.BloodProp;
import cn.edu.hit.prop.BombProp;
import cn.edu.hit.prop.BulletProp;
import cn.edu.hit.strategy.ShootStrategy;
import cn.edu.hit.strategy.StraightShootStrategy;

public class EliteEnemy extends MobEnemy {

    /**
     * 攻击方式——子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;
    /**
     * 攻击方式——子弹一次发射数量
     */
    private int shootNum = 1;
    /**
     * 攻击方式——子弹伤害
     */
    private int power = 20;
    private ShootStrategy shootStrategy = new StraightShootStrategy();


    public EliteEnemy(Context context, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(context, locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        int x = locationX;
        int y = locationY + direction * 2 + getImageHeight() / 2;
        int speedX = 0;
        int speedY = this.speedY + direction * 6;
        return shootStrategy.shoot(EnemyBullet.class, context, x, y, speedX, speedY, power, shootNum);
    }

    @Override
    public AbstractProp produceProp() {
        long rand = System.currentTimeMillis();
        if (rand % 3 == 0) {
            return new BombProp(context, getLocationX(), getLocationY(), 0, 1);
        } else if (rand % 3 == 1) {
            return new BloodProp(context, getLocationX(), getLocationY(), 0, 1);
        } else {
            return new BulletProp(context, getLocationX(), getLocationY(), 0, 1);
        }
    }
}
