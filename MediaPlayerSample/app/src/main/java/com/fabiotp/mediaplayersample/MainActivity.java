package com.fabiotp.mediaplayersample;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private MediaPlayer mediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mediaPlayer = MediaPlayer.create(this, R.raw.sampleaudio);

    Button play = findViewById(R.id.play);
    play.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
          @Override
          public void onCompletion(MediaPlayer mediaPlayer) {
            Toast.makeText(MainActivity.this, "It's over", Toast.LENGTH_SHORT).show();
          }
        });
      }
    });

    Button pause = findViewById(R.id.pause);
    pause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mediaPlayer.pause();
      }
    });

  }
}
