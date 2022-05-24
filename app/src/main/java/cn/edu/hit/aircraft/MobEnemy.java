package cn.edu.hit.aircraft;



import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.bullet.AbstractBullet;

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
    public List<AbstractBullet> shoot() {
        return new LinkedList<>();
    }

}
