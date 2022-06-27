package com.example.billiardstrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private RecyclerViewItemClickListener itemClicklistener;
    private Context context;
    private ArrayList<Shot> shots;
    private int[] ballImages;

    public interface RecyclerViewItemClickListener {
        void onClick(View v, int position);
    }

    public CustomAdapter (Context context, ArrayList<Shot> shots, int[] ballImages, RecyclerViewItemClickListener itemClicklistener) {
        this.context = context;
        this.shots = shots;
        this.ballImages = ballImages;
        this.itemClicklistener = itemClicklistener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.db_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Shot shot = shots.get(position);
        int shot_number = position + 1;
        int deviation = ShotAnalyzer.calculateFullDeviation(shot);
        int length = ShotAnalyzer.calculateFullLength(shot);

        holder.shot_number_txt.setBackgroundResource(ballImages[position % 10]);
        holder.shot_number_txt.setText(String.valueOf(shot_number));
        holder.power_txt.setText("Siła: " + shot.getPower() + "N");
        holder.deviation_txt.setText("Błąd: " + deviation + "mm");
        holder.length_txt.setText("Długość: " + length + "cm");
        holder.smoothness_txt.setText("Płynność: " + shot.getSmoothness() + "%");
    }

    @Override
    public int getItemCount() {
        return shots.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView shot_number_txt, power_txt, deviation_txt, length_txt, smoothness_txt;
        Button delete_item_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            shot_number_txt = itemView.findViewById(R.id.cardview_shot_number_txt);
            power_txt = itemView.findViewById(R.id.cardview_power_txt);
            deviation_txt = itemView.findViewById(R.id.cardview_deviation_txt);
            length_txt = itemView.findViewById(R.id.cardview_length_txt);
            smoothness_txt = itemView.findViewById(R.id.cardview_smoothness_txt);
            delete_item_btn = itemView.findViewById(R.id.cardview_delete_btn);

            delete_item_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem();
                }
            });
        }

        private void deleteItem() {
            int shotNumber = getAdapterPosition() + 1;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Usuń uderzenie " + shotNumber);
            builder.setMessage("Czy na pewno usunąć uderzenie " + shotNumber + "?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ShotsDatabaseHelper databaseHelper = new ShotsDatabaseHelper(context);
                    databaseHelper.deleteOneRow(String.valueOf(shots.get(getAdapterPosition()).getId()));
                    ((Activity) context).recreate();
                }
            });
            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }

        @Override
        public void onClick(View view) {
            itemClicklistener.onClick(view, getAdapterPosition());
        }
    }
}
