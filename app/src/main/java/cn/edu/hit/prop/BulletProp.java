package cn.edu.hit.prop;

import android.content.Context;

import cn.edu.hit.R;
import cn.edu.hit.application.MusicService;

public class BulletProp extends AbstractProp{
    public BulletProp(Context context) {
        super(context);
    }

    public BulletProp(Context context, int locationX, int locationY, int speedX, int speedY) {
        super(context, locationX, locationY, speedX, speedY);
    }

    @Override
    public void vanish(boolean musicEnable) {
        super.vanish();
        //播放游戏supply音频
        MusicService.playSound(musicEnable, R.raw.bullet);
    }


}
