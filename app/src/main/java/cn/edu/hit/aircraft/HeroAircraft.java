package cn.edu.hit.aircraft;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hit.bullet.AbstractBullet;
import cn.edu.hit.bullet.HeroBullet;

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
    public List<AbstractBullet> shoot() {
        List<AbstractBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = direction * 15;
        AbstractBullet abstractBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            abstractBullet = new HeroBullet(context, x + (i * 2 - shootNum + 1) * 10, y - getImageHeight() / 2, speedX, speedY, power);
            res.add(abstractBullet);
        }
        return res;
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
