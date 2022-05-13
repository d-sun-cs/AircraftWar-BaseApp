package cn.edu.hit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cn.edu.hit.R;
import cn.edu.hit.application.game.Game;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        View startGameButton = findViewById(R.id.startGame);
        startGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, GameActivity.class);
            startActivity(intent);
        });
    }
}