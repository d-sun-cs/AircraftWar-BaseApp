package cn.edu.hit.prop;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.activity.MenuActivity;
import cn.edu.hit.aircraft.AbstractAircraft;
import cn.edu.hit.application.MusicService;
import cn.edu.hit.basic.FlyingObject;

public abstract class AbstractProp extends FlyingObject {

    //观察者集合
    protected List<AbstractAircraft> observers = new ArrayList<>();

    public AbstractProp(Context context) {
        super(context);
    }

    public AbstractProp(Context context, int locationX, int locationY, int speedX, int speedY) {
        super(context, locationX, locationY, speedX, speedY);
    }


    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= GameActivity.WINDOW_HEIGHT) {
            vanish();
        }
    }

    public abstract void vanish(boolean musicEnable);

    /**
     * 观察者模式的通知方法
     * （现在还没实现观察者模式，只是定义方法）
     */
    public void notifyObservers(boolean musicEnable) {
        for (AbstractAircraft observer : observers) {
            observer.update(this.getClass());
        }
    }


    public void subscribe(List<AbstractAircraft> aircrafts) {
        if (Objects.nonNull(aircrafts)) {
            observers.addAll(aircrafts);
        }
    }
    public void subscribe(AbstractAircraft aircraft) {
        if (Objects.nonNull(aircraft)) {
            observers.add(aircraft);
        }
    }

    public void unsubscribe(AbstractAircraft aircraft) {
        if (Objects.nonNull(aircraft)) {
            observers.remove(aircraft);
        }
    }

    public void unsubscribe(List<AbstractAircraft> aircrafts) {
        if (Objects.nonNull(aircrafts)) {
            observers.removeAll(aircrafts);
        }
    }
}
