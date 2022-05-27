package cn.edu.hit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.edu.hit.application.ImageManager;
import cn.edu.hit.application.game.EasyGame;
import cn.edu.hit.application.game.Game;
import cn.edu.hit.factory.GameFactory;

public class GameActivity extends Activity {
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WINDOW_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
        WINDOW_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;

        ImageManager.initImages(this);

        Intent intentToGame = getIntent();
        Bundle dataForGame = intentToGame.getExtras();
        String difficulty = dataForGame.getString("difficulty");
        boolean musicEnable = dataForGame.getBoolean("musicEnable");

        //交给简单工厂去做
        game = GameFactory.createGame(this, difficulty, musicEnable);

        setContentView(game);
        game.action();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        game.getExecutorService().shutdown();
    }
}