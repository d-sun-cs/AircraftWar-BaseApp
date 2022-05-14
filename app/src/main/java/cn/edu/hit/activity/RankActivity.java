package cn.edu.hit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.edu.hit.R;

public class RankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        TextView difficulty = findViewById(R.id.difficulty);
        difficulty.setText(String.format("难度：%s",  getIntent().getStringExtra("difficulty")));
    }
}