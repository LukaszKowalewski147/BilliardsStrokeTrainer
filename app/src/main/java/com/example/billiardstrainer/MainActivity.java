package com.example.billiardstrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button goToShotListenerBtn;
    Button databaseBtn;
    Button statsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToShotListenerBtn = findViewById(R.id.main_go_to_shot_listener_btn);
        databaseBtn = findViewById(R.id.main_go_to_database_btn);
        statsBtn = findViewById(R.id.main_go_to_stats_btn);

        goToShotListenerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShotListener();
            }
        });

        databaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShotsDatabase();
            }
        });

        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStats();
            }
        });
    }

    private void openShotListener()
    {
        Intent intent = new Intent(this, ShotListener.class);
        startActivity(intent);
    }

    private void openShotsDatabase() {
        Intent intent = new Intent(this, ShotsDatabase.class);
        startActivity(intent);
    }

    private void openStats() {
        Intent intent = new Intent(this, Stats.class);
        startActivity(intent);
    }
}