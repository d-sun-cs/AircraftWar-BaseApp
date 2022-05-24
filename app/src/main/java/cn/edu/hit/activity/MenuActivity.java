package cn.edu.hit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import cn.edu.hit.R;
import cn.edu.hit.application.game.Game;
import cn.edu.hit.request.HttpRequest;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        HttpRequest httpRequest = new HttpRequest(this);

        Button easyGameButton = findViewById(R.id.startEasyGame);
        Button simpleGameButton = findViewById(R.id.startSimpleGame);
        Button difficultGameButton = findViewById(R.id.startDifficultGame);
        Spinner musicChoice = findViewById(R.id.musicChoice);
        View registerButton = findViewById(R.id.registerButton);

        easyGameButton.setOnClickListener(v -> startGame("简单", (String) musicChoice.getSelectedItem()));
        simpleGameButton.setOnClickListener(v -> startGame("普通", (String) musicChoice.getSelectedItem()));
        difficultGameButton.setOnClickListener(v -> startGame("困难", (String) musicChoice.getSelectedItem()));

        registerButton.setOnClickListener(v -> {

            new Thread(){
                @Override
                public void run()
                {
                    //把网络访问的代码放在这里
                    httpRequest.get("http://192.168.137.1:8888/ideas/hot", new HashMap<>());
                }
            }.start();
        });
    }

    private void startGame(String difficulty, String musicChoice) {
        Bundle dataForGame = new Bundle();
        dataForGame.putString("difficulty", difficulty);
        dataForGame.putBoolean("musicEnable", "开".equals(musicChoice));
        Intent intentToGame = new Intent(MenuActivity.this, GameActivity.class);
        intentToGame.putExtras(dataForGame);
        startActivity(intentToGame);
    }
}