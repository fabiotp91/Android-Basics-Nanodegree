package com.fabiotp.tourguide;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by ftp91 on 26-Mar-18.
 */

public class LocationAdapter extends ArrayAdapter<Location> {

  //Resource ID for the background color for this type of location
  private int appColorResourceId;

  public LocationAdapter(Activity context, ArrayList<Location> locations, int colorResourceId) {
    /*
      Here, we initialize the ArrayAdapter's internal storage for the context and the list.
      the second argument is used when the ArrayAdapter is populating a single TextView.
      Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
      going to use this second argument, so it can be any value. Here, we used 0.
     */
    super(context, 0, locations);
    appColorResourceId = colorResourceId;
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
      listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
          parent, false);
    }
    // Get the {@link Location} object located at this position in the list
    Location currentLocation = getItem(position);
    //Find the TextView in the list_item.xml with the ID text_location
    TextView locationTextView = (TextView) listItemView.findViewById(R.id.text_location);
    //Get the location and set this text on the TextView
    locationTextView.setText(currentLocation.getLocationName());

    //Find the Textview in the list_item.xml with the ID text_details
    TextView detailsTextView = (TextView) listItemView.findViewById(R.id.text_details);
    //Get the details and set this text on the TextView
    detailsTextView.setText(currentLocation.getDetailsText());

    ImageView imageResourceId = (ImageView) listItemView.findViewById(R.id.image);

    if (currentLocation.hasImage()) {
      imageResourceId.setImageResource(currentLocation.getImageResourceId());
    } else {
      imageResourceId.setVisibility(View.GONE);
    }

    // Set the theme color for the list item
    View textContainer = listItemView.findViewById(R.id.text_container);
    // Find the color that the resource ID maps to
    int color = ContextCompat.getColor(getContext(), appColorResourceId);
    // Set the background color of the text container view
    textContainer.setBackgroundColor(color);

    // Return the whole list item layout so that it can be show in the ListView
    return listItemView;
  }
}
