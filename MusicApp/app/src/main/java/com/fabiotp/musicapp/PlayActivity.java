package com.fabiotp.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play);

    //Set activity tittle
    setTitle(R.string.act_play_music);

    //Set the back (up) button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //Find all our view components
    ImageView imageView = findViewById(R.id.img_album_play);
    TextView musictextView = findViewById(R.id.txt_music_play);
    TextView bandTextView = findViewById(R.id.txt_band_play);

    //Collect our intent and populate our layout
    Intent intent = getIntent();
    Bundle extras = getIntent().getExtras();
    int imageId = extras.getInt("imageResourceId");
    String music = intent.getStringExtra("musicName");
    String band = intent.getStringExtra("bandName");

    //Set elements
    imageView.setImageResource(imageId);
    musictextView.setText(music);
    bandTextView.setText(band);
  }
}
