package com.fabiotp.musicapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by ftp91 on 09-Mar-18.
 */

public class MusicAdapter extends ArrayAdapter<Music> {

  public MusicAdapter(Activity context, ArrayList<Music> musics) {
    // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
    // the second argument is used when the ArrayAdapter is populating a single TextView.
    // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
    // going to use this second argument, so it can be any value. Here, we used 0.
    super(context, 0, musics);
  }

  /**
   * Provides a view for an AdapterView (ListView, GridView, etc.)
   *
   * @param position The position in the list of data that should be displayed in the list item
   * view.
   * @param convertView The recycled view to populate.
   * @param parent The parent ViewGroup that is used for inflation.
   * @return The View for the position in the AdapterView.
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Check if the existing view is being reused, otherwise inflate the view
    View listItemView = convertView;
    if (listItemView == null) {
      listItemView = LayoutInflater.from(getContext()).inflate(
          R.layout.list_item, parent, false);
    }

    // Get the {@link Music} object located at this position in the list
    Music currentMusic = getItem(position);

    // Find the TextView in the list_item.xml layout with the ID txt_music
    TextView musicTextView = (TextView) listItemView.findViewById(R.id.txt_music);
    // Get the music name from the current Music object and
    // set this text on the name TextView
    musicTextView.setText(currentMusic.getMusicName());

    // Find the TextView in the list_item.xml layout with the ID txt_band
    TextView bandTextView = (TextView) listItemView.findViewById(R.id.txt_band);
    // Get the band name from the current Music object and
    // set this text on the number TextView
    bandTextView.setText(currentMusic.getBandName());

    // Find the ImageView in the list_item.xml layout with the ID img_album
    ImageView albumView = (ImageView) listItemView.findViewById(R.id.img_album);
    // Get the image resource ID from the current Music object and
    // set the image to imageView
    albumView.setImageResource(currentMusic.getImageResourceId());

    // Return the whole list item layout (containing 2 TextViews and an ImageView)
    // so that it can be shown in the ListView
    return listItemView;
  }

}