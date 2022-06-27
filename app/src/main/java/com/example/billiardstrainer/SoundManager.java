package com.example.billiardstrainer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

public class SoundManager extends AppCompatActivity {

    private Context context;
    private SoundPool soundPool;
    private int countdown;
    private int start_shooting;
    private int end_shooting;
    private int contact;

    public SoundManager(Context context) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder().setMaxStreams(3).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        countdown = soundPool.load(context, R.raw.countdown, 1);
        start_shooting = soundPool.load(context, R.raw.start_shooting, 1);
        end_shooting = soundPool.load(context, R.raw.end_shooting, 1);
        contact = soundPool.load(context, R.raw.contact, 1);
    }

    public void playCountdownSound() {
        soundPool.play(countdown, 1.0f, 1.0f, 0, 0, 1);
    }

    public void playStartShootingSound() {
        soundPool.play(start_shooting, 1.0f, 1.0f, 0, 0, 1);
    }

    public void playEndShootingSound() {
        soundPool.play(end_shooting, 1.0f, 1.0f, 0, 0, 1);
    }

    public void playContactSound() {
        soundPool.play(contact, 1.0f, 1.0f, 0, 0, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
