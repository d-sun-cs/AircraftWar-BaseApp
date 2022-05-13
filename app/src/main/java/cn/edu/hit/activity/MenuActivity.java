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
        Log.d("TAG", "onCreate: menu on create");
        setContentView(R.layout.activity_menu);

        Button startGameButton = findViewById(R.id.startGame);
        Spinner musicChoice = findViewById(R.id.musicChoice);


        startGameButton.setOnClickListener(v -> {
            Toast.makeText(MenuActivity.this, "音效：" + musicChoice.getSelectedItem(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(MenuActivity.this, GameActivity.class));
        });


    }
}