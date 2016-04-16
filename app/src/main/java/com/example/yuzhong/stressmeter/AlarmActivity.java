package com.example.yuzhong.stressmeter;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer alarmMusic;
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_alarm);

        try {
            alarmMusic = new MediaPlayer();
            alarmMusic.setDataSource(this, uri);


            alarmMusic.setLooping(true);
            alarmMusic.prepare();


        } catch (IOException e) {
            e.printStackTrace();
        }
        alarmMusic.start();
    }

    public void onClick() {

        alarmMusic.stop();
        AlarmActivity.this.finish();
    }
}

