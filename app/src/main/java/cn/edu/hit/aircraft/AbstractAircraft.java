package cn.edu.hit.aircraft;


import android.content.Context;

import java.util.List;

import cn.edu.hit.basic.FlyingObject;
import cn.edu.hit.bullet.AbstractBullet;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends FlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;

    public AbstractAircraft(Context context, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(context, locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }


    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    public abstract List<AbstractBullet> shoot();

}


