package com.example.yuzhong.stressmeter;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

public class ShowPictureActivity extends AppCompatActivity {

    private ImageView mImageView;
    private int mStressLevel;
    private static final String IS_SUBMITTED = "Submitted";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        mImageView = (ImageView) findViewById(R.id.image_view);
        Intent intent = getIntent();
        mImageView.setImageResource(intent.getIntExtra("picture", 0));
        mStressLevel = intent.getIntExtra("stressLevel", 0);
    }

    public void onCancelClick(View view){
        finish();
    }

    public void onSubmitClick(View view){
        //save the stress level and submit time to csv file
        String fileName = "stressData.csv";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file, true);
            Long time = System.currentTimeMillis();
            String line = time + "," + mStressLevel + "\n";

            fOut.write(line.getBytes());
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}
