package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView t_display,t_song,t_time;
    Button b_play,b_pause,b_rewind,b_forward;
    SeekBar seekbar;
    ImageView img_music;

    MediaPlayer mediaPlayer;
    Handler handler= new Handler();

    double startTime = 0;
    double FinalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t_display=findViewById(R.id.txt_display);
        t_song=findViewById(R.id.txt_song_name);
        t_time=findViewById(R.id.txt_time);
        b_play=findViewById(R.id.play_btn);
        b_pause=findViewById(R.id.pause_btn);
        b_rewind=findViewById(R.id.rewind_btn);
        b_forward=findViewById(R.id.forward_btn);
        img_music=findViewById(R.id.img_display);
        seekbar=findViewById(R.id.seekbar);


        mediaPlayer = MediaPlayer.create(this,R.raw.kadhalvandhum1);
        t_song.setText(getResources().getIdentifier("kadhalvandhum","raw",getPackageName()
        ));

        seekbar.setClickable(false);


        b_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic();
            }
        });

        b_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
        b_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if((temp + forwardTime)<=FinalTime){
                    startTime=startTime+forwardTime;
                    mediaPlayer.seekTo((int) startTime);

                }else {
                    Toast.makeText(MainActivity.this, "can't jump!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        b_rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if((temp - backwardTime)>0){
                    startTime=startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);

                }else {
                    Toast.makeText(MainActivity.this, "can't back!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void PlayMusic() {
        mediaPlayer.start();

        FinalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if(oneTimeOnly == 0){
            seekbar.setMax((int) FinalTime);
            oneTimeOnly = 1;
        }
        t_time.setText(String.format(
                "%d min,%d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) FinalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) FinalTime)-
                TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) FinalTime))
        ));

        seekbar.setProgress((int) startTime);
        Handler handler = new Handler();
        handler.postDelayed(UpdateSongTime,100);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            t_time.setText(String.format(
                    "%d min,%d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                    -  TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));

            seekbar.setProgress((int)startTime);
            handler.postDelayed(this,100);
        }



    };
}