package com.fabiotp.newsfeedappv2;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<Articles>> {

  // Adapter for the list of articles
  private ArticleAdapter mAdapter;
  // TextView that is displayed when the list is empty
  private TextView emptyTextView;
  private static String REQUEST_URL = "https://content.guardianapis.com/search?";
  private static String apikey = BuildConfig.guardianApiKey;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news);

    // Find a reference to the {@link ListView} in the layout
    ListView articleListView = findViewById(R.id.lv_item);
    // Create a new adapter that takes an empty list of articles as input
    mAdapter = new ArticleAdapter(this, new ArrayList<Articles>());

    //Find the empty TextView and hook up
    emptyTextView = findViewById(R.id.nocontent_text_view);
    articleListView.setEmptyView(emptyTextView);

    // Set the adapter on the {@link ListView}
    // so the list can be populated in the user interface
    articleListView.setAdapter(mAdapter);
    // Set an item click listener on the ListView, which sends an intent to a web browser
    // to open a website with more information about the selected article.
    articleListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Articles currentArticle = mAdapter.getItem(position);
        Uri articleUri = Uri.parse(currentArticle.getUrl());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);
        startActivity(webIntent);
      }
    });
    // Get a reference to the ConnectivityManager to check state of network connectivity
    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(
        Context.CONNECTIVITY_SERVICE);
    // Get details on the currently active default data network
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    // If there is a network connection, fetch data
    if (networkInfo != null && networkInfo.isConnected()) {
      LoaderManager loaderManager = getLoaderManager();
      loaderManager.initLoader(0, null, this);
    } else {
      // Otherwise, display error
      // First, hide loading indicator so error message will be visible
      View loadingIndicator = findViewById(R.id.loading_indicator);
      loadingIndicator.setVisibility(View.GONE);
      // Update empty state with no connection error message
      emptyTextView.setText(R.string.interneterrormessages);
    }
  }

  @Override
  public Loader<List<Articles>> onCreateLoader(int id, Bundle bundle) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    String minArticles = sharedPreferences.getString(getString(R.string.settings_min_news_key),
        getString(R.string.settings_min_news_default));
    String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key),
        getString(R.string.settings_order_by_default));
    String section = sharedPreferences.getString(getString(R.string.settings_section_news_key),
        getString(R.string.settings_section_news_default));

    Uri baseUri = Uri.parse(REQUEST_URL);
    Uri.Builder uriBuilder = baseUri.buildUpon();

    uriBuilder.appendQueryParameter("api-key", apikey);
    uriBuilder.appendQueryParameter("show-tags", "contributor");
    uriBuilder.appendQueryParameter("page-size", minArticles);
    uriBuilder.appendQueryParameter("order-by", orderBy);

    if (!section.equals(getString(R.string.settings_section_news_default))) {
      uriBuilder.appendQueryParameter("section", section);
    }
    return new ArticlesLoader(this, uriBuilder.toString());
  }

  @Override
  public void onLoadFinished(Loader<List<Articles>> loader, List<Articles> articles) {
    // Hide loading indicator because the data has been loaded
    View loadingIndicator = findViewById(R.id.loading_indicator);
    loadingIndicator.setVisibility(View.GONE);
    // Set empty state text to display "No Articles Found."
    emptyTextView.setText(R.string.contneterrormessages);
    mAdapter.clear();

    // If there is a valid list of {@link Articles}, then add them to the adapter's
    // data set. This will trigger the ListView to update.
    if (articles != null && !articles.isEmpty()) {
      mAdapter.addAll(articles);
    }
  }

  @Override
  public void onLoaderReset(Loader<List<Articles>> loader) {
    mAdapter.clear();
  }

  @Override
  // This method initializes the contents of the Activity's options menu.
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the Options Menu we specified in XML
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_setting) {
      Intent settingsIntent = new Intent(this, SettingsActivity.class);
      startActivity(settingsIntent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
