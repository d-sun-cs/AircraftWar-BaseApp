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

import cn.edu.hit.R;
import cn.edu.hit.application.game.Game;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button easyGameButton = findViewById(R.id.startEasyGame);
        Button simpleGameButton = findViewById(R.id.startSimpleGame);
        Button difficultGameButton = findViewById(R.id.startDifficultGame);
        Spinner musicChoice = findViewById(R.id.musicChoice);

        easyGameButton.setOnClickListener(v -> startGame("简单", (String) musicChoice.getSelectedItem()));
        simpleGameButton.setOnClickListener(v -> startGame("普通", (String) musicChoice.getSelectedItem()));
        difficultGameButton.setOnClickListener(v -> startGame("困难", (String) musicChoice.getSelectedItem()));


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