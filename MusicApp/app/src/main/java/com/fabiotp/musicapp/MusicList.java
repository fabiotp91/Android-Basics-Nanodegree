package com.fabiotp.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

public class MusicList extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.music_list);

    //Create and populate ArrayList
    final ArrayList<Music> musics = new ArrayList<>();
    musics.add(new Music(getString(R.string.music_Name_1), getString(R.string.band_Name_1),
        R.drawable.album_Img_1));
    musics.add(new Music(getString(R.string.music_Name_2), getString(R.string.band_Name_2),
        R.drawable.album_Img_2));
    musics.add(new Music(getString(R.string.music_Name_3), getString(R.string.band_Name_3),
        R.drawable.album_Img_3));
    musics.add(new Music(getString(R.string.music_Name_4), getString(R.string.band_Name_4),
        R.drawable.album_Img_4));
    musics.add(new Music(getString(R.string.music_Name_5), getString(R.string.band_Name_5),
        R.drawable.album_Img_5));
    musics.add(new Music(getString(R.string.music_Name_6), getString(R.string.band_Name_6),
        R.drawable.album_Img_6));
    musics.add(new Music(getString(R.string.music_Name_7), getString(R.string.band_Name_7),
        R.drawable.album_Img_7));
    musics.add(new Music(getString(R.string.music_Name_8), getString(R.string.band_Name_8),
        R.drawable.album_Img_8));
    musics.add(new Music(getString(R.string.music_Name_9), getString(R.string.band_Name_9),
        R.drawable.album_Img_9));
    musics.add(new Music(getString(R.string.music_Name_10), getString(R.string.band_Name_10),
        R.drawable.album_Img_10));

    //Find list view and bind it with the custom adapter
    MusicAdapter adapter = new MusicAdapter(this, musics);
    final ListView listView = (ListView) findViewById(R.id.list);
    listView.setAdapter(adapter);

    //Add event listener so we can handle clicks
    AdapterView.OnItemClickListener adapterViewListener = new
        AdapterView.OnItemClickListener() {
          //On click
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Music music = musics.get(position);
            Intent intent = new Intent(MusicList.this, PlayActivity.class);
            intent.putExtra("musicName", music.getMusicName());
            intent.putExtra("bandName", music.getBandName());
            intent.putExtra("imageResourceId", music.getImageResourceId());
            startActivity(intent);
          }
        };
    //Set the listener to the list view
    listView.setOnItemClickListener(adapterViewListener);
  }
}
