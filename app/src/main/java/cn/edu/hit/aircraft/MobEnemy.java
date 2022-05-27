package cn.edu.hit.aircraft;



import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.bullet.BaseBullet;
import cn.edu.hit.prop.AbstractProp;
import cn.edu.hit.prop.BloodProp;
import cn.edu.hit.prop.BombProp;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {

    public MobEnemy(Context context, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(context, locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= GameActivity.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    public AbstractProp produceProp() {
        return null;
    }

    @Override
    public void update(Class<? extends AbstractProp> propClass) {
        if (propClass.equals(BombProp.class)) {
            vanish();
        }
    }

}
