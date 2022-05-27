package cn.edu.hit.application.game;

import android.app.Activity;

import cn.edu.hit.background.EasyGameBackground;
import cn.edu.hit.background.GameBackground;

public class EasyGame extends Game{
    public EasyGame(Activity context, String difficulty, boolean musicEnable) {
        super(context, difficulty, musicEnable);
    }

    @Override
    protected GameBackground createBackground() {
        return new EasyGameBackground(context);
    }

    @Override
    protected void scoreCheck() {

    }
}
