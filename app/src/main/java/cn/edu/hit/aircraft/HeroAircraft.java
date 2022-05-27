package cn.edu.hit.aircraft;


import android.content.Context;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.edu.hit.bullet.BaseBullet;
import cn.edu.hit.bullet.HeroBullet;
import cn.edu.hit.prop.AbstractProp;
import cn.edu.hit.prop.BloodProp;
import cn.edu.hit.prop.BulletProp;
import cn.edu.hit.strategy.ScatteringShootStrategy;
import cn.edu.hit.strategy.ShootStrategy;
import cn.edu.hit.strategy.StraightShootStrategy;

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * 攻击方式
     */
    private int shootNum = 1;     //子弹一次发射数量
    private int power = 30;       //子弹伤害
    private int direction = -1;  //子弹射击方向 (向上发射：1，向下发射：-1)

    private ShootStrategy shootStrategy = new StraightShootStrategy();
    private boolean isScattering = false;
    private Thread scatteringThread;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp        初始生命值
     */
    public HeroAircraft(Context context, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(context, locationX, locationY, speedX, speedY, hp);
    }


    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2 - getImageHeight() / 2;
        int speedX = 0;
        int speedY = direction * 15;

        return shootStrategy.shoot(HeroBullet.class, context, x, y, -1 * (shootNum - 1), speedY, power, shootNum);
    }

    @Override
    public AbstractProp produceProp() {
        return null;
    }

    @Override
    public void update(Class<? extends AbstractProp> propClass) {
        if (propClass.equals(BloodProp.class)) {
            hp += 20;
        } else if (propClass.equals(BulletProp.class)) {
            if (isScattering) {
                scatteringThread.interrupt();
            }
            this.shootStrategy = new ScatteringShootStrategy();
            this.shootNum = 4;
            isScattering = true;
            scatteringThread = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(8);
                    this.shootStrategy = new StraightShootStrategy();
                    this.shootNum = 1;
                    isScattering = false;
                } catch (InterruptedException e) {
                    System.out.println("子弹道具刷新");
                }
            });
            scatteringThread.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (Math.abs(touchX - getLocationX()) > getImageWidth() * zoomFactor|| Math.abs(touchY - getLocationY()) > getImageHeight() * zoomFactor * zoomFactor) {
            return true;
        }
        setLocation(touchX, touchY);
        return true;
    }
}
