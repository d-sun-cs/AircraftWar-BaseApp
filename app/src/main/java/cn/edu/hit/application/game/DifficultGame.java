package cn.edu.hit.application.game;

import android.app.Activity;

import cn.edu.hit.background.DifficultGameBackground;
import cn.edu.hit.background.EasyGameBackground;
import cn.edu.hit.background.GameBackground;

public class DifficultGame extends Game {
    public DifficultGame(Activity context, String difficulty, boolean musicEnable) {
        super(context, difficulty, musicEnable);
    }

    @Override
    protected GameBackground createBackground() {
        return new DifficultGameBackground(context);
    }
}
