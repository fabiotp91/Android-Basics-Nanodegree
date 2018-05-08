package com.fabiotp.newsfeedapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<Articles>>, SwipeRefreshLayout.OnRefreshListener {

  /**
   * Constant value for the earthquake loader ID. We can choose any integer.
   * This really only comes into play if you're using multiple loaders.
   */
  private static int LOADER_ID = 0;

  // Adapter for the list of articles
  private ArticlesAdapter adapter;

  /** TextView that is displayed when the list is empty */
  private TextView emptyStateTextView;

  SwipeRefreshLayout swipe;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Find and Set the Swipe Refresh Color
    swipe = findViewById(R.id.swiperefresh);
    swipe.setOnRefreshListener(this);
    swipe.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

    // Find a reference to the {@link ListView} in the layout
    ListView listView = findViewById(R.id.list_view);

    // Create a new adapter that takes an empty list of earthquakes as input
    adapter = new ArticlesAdapter(this, new ArrayList<Articles>());
    // Set the adapter on the {@link ListView}
    // so the list can be populated in the user interface
    listView.setAdapter(adapter);

    //Find the empty TextView and hook up
    emptyStateTextView = findViewById(R.id.empty_view);
    listView.setEmptyView(emptyStateTextView);

    // Set an item click listener on the ListView, which sends an intent to a web browser
    // to open a website with more information about the selected article.
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Articles articles = adapter.getItem(position);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(articles.getUrl()));
        startActivity(intent);
      }
    });

    // Get a reference to the ConnectivityManager to check state of network connectivity
    ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);

    // Get details on the currently active default data network
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    // If there is a network connection, fetch data
    if (networkInfo != null && networkInfo.isConnected()) {
      /*Get LoadManager
      * Initialize the loader. Pass in the int ID constant defined above and pass in null for
      * the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
      * because this activity implements the LoaderCallbacks interface).
      */
      getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }else{
      // Otherwise, display error
      // First, hide loading indicator so error message will be visible
      View loadingIndicator = findViewById(R.id.loading_indicator);
      loadingIndicator.setVisibility(View.GONE);

      // Update empty state with no connection error message
      emptyStateTextView.setText(R.string.no_internet_connection);
    }
  }

  @Override
  public Loader<List<Articles>> onCreateLoader(int id, Bundle args) {
    // Create a new loader for the given URL
    return new ArticlesLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<Articles>> loader, List<Articles> data) {
    // Hide loading indicator because the data has been loaded
    View loadingIndicator = findViewById(R.id.loading_indicator);
    loadingIndicator.setVisibility(View.GONE);

    // Set empty state text to display "No Articles Found."
    emptyStateTextView.setText(R.string.no_articles);

    // Set swipe to refresh false
    swipe.setRefreshing(false);

    // If there is a valid list of {@link Articles}, then add them to the adapter's
    // data set. This will trigger the ListView to update.
    if (data != null && !data.isEmpty()) {
      adapter.setNotifyOnChange(false);
      adapter.clear();
      adapter.setNotifyOnChange(true);
      adapter.addAll(data);
    }
  }

  @Override
  public void onLoaderReset(Loader<List<Articles>> loader) {

  }

  @Override
  public void onRefresh() {
    getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
  }
}
