package cn.edu.hit.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import cn.edu.hit.R;
import cn.edu.hit.dao.User;

public class RankActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        Intent intentToRank = getIntent();
        Bundle dataForRank = intentToRank.getExtras();
        String difficulty = dataForRank.getString("difficulty");
        int score = dataForRank.getInt("score");

        //存储
        User user = new User();
        user.setUsername("testUsername");
        user.setScore(score);
        user.setCreateTime(System.currentTimeMillis());
        sharedPreferences = getSharedPreferences("rank", MODE_PRIVATE);
        Set<String> rankSet0 = sharedPreferences.getStringSet(difficulty, null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        HashSet<String> rankSet = Objects.isNull(rankSet0) ? new HashSet<>() : new HashSet<>(rankSet0);
        rankSet.add(new Gson().toJson(user, User.class));
        editor.putStringSet(difficulty, rankSet);
        editor.apply();


        TextView difficultyText = findViewById(R.id.difficulty);
        TableLayout rankTable = findViewById(R.id.rankTable);

        difficultyText.setText(String.format("难度：%s", difficulty));
        addTableData(rankTable, rankSet);
    }

    private void addTableData(TableLayout rankTable, Set<String> rankSet) {
        List<User> userList = new ArrayList<>();
        for (String userJsonStr : rankSet) {
            User user = new Gson().fromJson(userJsonStr, User.class);
            userList.add(user);
        }
        Collections.sort(userList, (u1, u2) -> u2.getScore() - u1.getScore());
        for (User user : userList) {
            TableRow tableRow = new TableRow(this);

            TextView username = new TextView(this);
            TextView score = new TextView(this);
            TextView createTime = new TextView(this);

            username.setWidth(getPixelsFromDp(100));
            score.setWidth(getPixelsFromDp(100));
            createTime.setWidth(getPixelsFromDp(100));

            username.setText(user.getUsername());
            score.setText(String.valueOf(user.getScore()));
            createTime.setText(new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA).format(user.getCreateTime()));

            tableRow.addView(username);
            tableRow.addView(score);
            tableRow.addView(createTime);

            rankTable.addView(tableRow);
        }
    }

    private int getPixelsFromDp(int i) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (i * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}