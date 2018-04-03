package com.fabiotp.tourguide;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SightsFragment extends Fragment {


  public SightsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.location_list, container, false);

    // Create a list of locations
    final ArrayList<Location> locations = new ArrayList<>();
    locations.add(new Location(getString(R.string.sight_location_1),
        getString(R.string.sight_details_1), R.drawable.sight_image_1,
        getString(R.string.sight_uri_1)));
    locations.add(new Location(getString(R.string.sight_location_2),
        getString(R.string.sight_details_2), R.drawable.sight_image_2,
        getString(R.string.sight_uri_2)));
    locations.add(new Location(getString(R.string.sight_location_3),
        getString(R.string.sight_details_3), R.drawable.sight_image_3,
        getString(R.string.sight_uri_3)));
    locations.add(new Location(getString(R.string.sight_location_4),
        getString(R.string.sight_details_4), R.drawable.sight_image_4,
        getString(R.string.sight_uri_4)));
    locations.add(new Location(getString(R.string.sight_location_5),
        getString(R.string.sight_details_5), R.drawable.sight_image_5,
        getString(R.string.sight_uri_5)));
    locations.add(new Location(getString(R.string.sight_location_6),
        getString(R.string.sight_details_6), R.drawable.sight_image_6,
        getString(R.string.sight_uri_1)));
    locations.add(new Location(getString(R.string.sight_location_7),
        getString(R.string.sight_details_7), R.drawable.sight_image_7,
        getString(R.string.sight_uri_7)));
    locations.add(new Location(getString(R.string.sight_location_8),
        getString(R.string.sight_details_8), R.drawable.sight_image_8,
        getString(R.string.sight_uri_8)));
    locations.add(new Location(getString(R.string.sight_location_9),
        getString(R.string.sight_details_9), R.drawable.sight_image_9,
        getString(R.string.sight_uri_9)));

    // Find list view and bind it with the custom adapter
    LocationAdapter adapter = new LocationAdapter(getActivity(), locations, R.color.colorWhite);
    ListView listView = (ListView) rootView.findViewById(R.id.list);
    listView.setAdapter(adapter);

    // Add event listener so we can handle clicks
    listView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Location location = locations.get(position);
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri mapIntentUri = Uri.parse(location.getMapCor());
        Intent intent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
          getActivity().startActivity(intent);
        }
      }
    });
    return rootView;
  }
}
