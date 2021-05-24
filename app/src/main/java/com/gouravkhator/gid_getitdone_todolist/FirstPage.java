package com.gouravkhator.gid_getitdone_todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FirstPage extends AppCompatActivity {

    private MediaPlayer backgroundMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        SharedPreferences s = getSharedPreferences("gidFile", Context.MODE_PRIVATE);
        int count = s.getInt("pageOccurred",0);
        backgroundMusic = MediaPlayer.create(this,R.raw.jack);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(75.0f,75.0f);
        backgroundMusic.start();
        if(count==0){
            SharedPreferences.Editor editor = s.edit();
            editor.putInt("pageOccurred",count+1);
            editor.commit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backgroundMusic.stop();
                    startActivity(new Intent(FirstPage.this,MainActivity.class));
                    finish();
                }
            },5000);
        }
        else{
            backgroundMusic.stop();
            startActivity(new Intent(FirstPage.this,MainActivity.class));
            finish();
        }
    }
}
