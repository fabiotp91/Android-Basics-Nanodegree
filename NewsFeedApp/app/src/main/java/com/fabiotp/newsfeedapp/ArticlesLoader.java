package com.fabiotp.newsfeedapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.fabiotp.newsfeedapp.Articles;
import com.fabiotp.newsfeedapp.QueryUtils;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ArticlesLoader extends AsyncTaskLoader<List<Articles>> {

  // Tag for log messages
  private static final String LOG_TAG = ArticlesLoader.class.getName();

  public ArticlesLoader(Context context) {
    super(context);
  }

  @Override
  protected void onStartLoading() {
    super.onStartLoading();
    forceLoad();
  }

  // This is on a background thread.
  @Override
  public List<Articles> loadInBackground() {
    List<Articles> listOfArticles = null;
    try {
      URL url = QueryUtils.createUrl();
      String jsonResponse = QueryUtils.makeHttpRequest(url);
      listOfArticles = QueryUtils.parseJson(jsonResponse);
    } catch (IOException e) {
      Log.e(LOG_TAG, "Error Loader LoadInBackground: ", e);
    }
    return listOfArticles;
  }
}
