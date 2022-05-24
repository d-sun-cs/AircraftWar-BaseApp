package cn.edu.hit.factory;

import android.app.Activity;
import android.content.Context;

import cn.edu.hit.application.game.DifficultGame;
import cn.edu.hit.application.game.EasyGame;
import cn.edu.hit.application.game.Game;
import cn.edu.hit.application.game.SimpleGame;

public class GameFactory {
    public static Game createGame(Activity context, String difficulty, boolean musicEnable) {
        switch (difficulty) {
            case "简单":
                return new EasyGame(context, difficulty, musicEnable);
            case "普通":
                return new SimpleGame(context, difficulty, musicEnable);
            case "困难":
                return new DifficultGame(context, difficulty, musicEnable);
            default:
                throw new RuntimeException("游戏难度选择异常");
        }
    }
}
