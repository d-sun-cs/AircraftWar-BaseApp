package cn.edu.hit.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import cn.edu.hit.R;
import cn.edu.hit.application.ImageManager;
import cn.edu.hit.application.game.Game;

public class MainActivity extends Activity {
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WINDOW_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
        WINDOW_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;
        ImageManager.initImages(this);
        Game game = new Game(this);
        //game.setBackgroundResource(R.mipmap.bg);
        setContentView(game);
        game.action();
    }
}