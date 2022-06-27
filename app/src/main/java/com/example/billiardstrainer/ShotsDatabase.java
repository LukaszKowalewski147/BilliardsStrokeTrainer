package com.example.billiardstrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class ShotsDatabase extends AppCompatActivity {

    RecyclerView recyclerView;
    ShotsDatabaseHelper shotsDatabaseHelper;
    CustomAdapter adapter;
    CustomAdapter.RecyclerViewItemClickListener itemClickListener;

    ArrayList<Shot> shots;

    int[] ball_png = {R.drawable.ball_1, R.drawable.ball_2, R.drawable.ball_3, R.drawable.ball_4, R.drawable.ball_5,
            R.drawable.ball_6, R.drawable.ball_7, R.drawable.ball_8, R.drawable.ball_9, R.drawable.ball_10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shots_database);

        recyclerView = findViewById(R.id.recycler_view);
        shotsDatabaseHelper = new ShotsDatabaseHelper(ShotsDatabase.this);
        shots = new ArrayList<>();

        shots = Helper.storeDataInArrays(shotsDatabaseHelper, this);
        setAdapter();
    }

    private void setAdapter() {
        setItemClickListener();
        adapter = new CustomAdapter(this, shots, ball_png, itemClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setItemClickListener() {
        itemClickListener = new CustomAdapter.RecyclerViewItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Shot shot = shots.get(position);
                int deviation = ShotAnalyzer.calculateFullDeviation(shot);
                int length = ShotAnalyzer.calculateFullLength(shot);

                Intent intent = new Intent(getApplicationContext(), ShotDetails.class);
                intent.putExtra("shotNumber", position + 1);
                intent.putExtra("power", shot.getPower());
                intent.putExtra("smoothness", shot.getSmoothness());
                intent.putExtra("deviation", deviation);
                intent.putExtra("deviationLeft", shot.getDeviationLeft());
                intent.putExtra("deviationRight", shot.getDeviationRight());
                intent.putExtra("backswingLength", shot.getBackswingLength());
                intent.putExtra("backswingPause", shot.getBackswingPause());
                intent.putExtra("followThrough", shot.getFollowThrough());
                intent.putExtra("endPause", shot.getEndPause());
                intent.putExtra("fullLength", length);
                startActivity(intent);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            deleteAllData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuń wszystko");
        builder.setMessage("Czy na pewno usunąć wszystkie uderzenia?");
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                shotsDatabaseHelper.deleteAllData();
                recreate();
            }
        });
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}