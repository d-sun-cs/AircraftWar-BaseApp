package cn.edu.hit.application.game;

import android.app.Activity;

import cn.edu.hit.background.GameBackground;
import cn.edu.hit.background.SimpleGameBackground;

public class SimpleGame extends Game{
    public SimpleGame(Activity context, String difficulty, boolean musicEnable) {
        super(context, difficulty, musicEnable);
    }

    @Override
    protected GameBackground createBackground() {
        return new SimpleGameBackground(context);
    }
}
