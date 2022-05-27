package cn.edu.hit.aircraft;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hit.bullet.BaseBullet;
import cn.edu.hit.bullet.EnemyBullet;
import cn.edu.hit.prop.AbstractProp;
import cn.edu.hit.prop.BombProp;
import cn.edu.hit.strategy.ScatteringShootStrategy;
import cn.edu.hit.strategy.ShootStrategy;

public class BossEnemy extends MobEnemy {

    /**
     * 攻击方式——子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;
    /**
     * 攻击方式——子弹一次发射数量
     */
    private int shootNum = 4;
    /**
     * 攻击方式——子弹伤害
     */
    private int power = 20;
    private ShootStrategy shootStrategy = new ScatteringShootStrategy();

    public BossEnemy(Context context, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(context, locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        int x = locationX;
        int y = locationY + direction * 14 + getImageHeight() / 2;
        int speedX = -1 * (shootNum - 1);
        int speedY = this.speedY + direction * 6;

        return shootStrategy.shoot(EnemyBullet.class, context, x, y, speedX, speedY, power, shootNum);
    }

    @Override
    public void update(Class<? extends AbstractProp> propClass) {
        if (propClass.equals(BombProp.class)) {
            hp -= 20;
        }
    }
}
