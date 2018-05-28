package com.fabiotp.newsfeedappv2;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

public class ArticlesLoader extends AsyncTaskLoader<List<Articles>> {

  // private static String REQUEST_URL ="http://content.guardianapis.com/search?q=debates&api-key=test";
  String appUrl;

  public ArticlesLoader(Context context, String url) {
    super(context);
    this.appUrl = url;
  }

  @Override
  protected void onStartLoading() {
    forceLoad();
  }

  // This is on a background thread.
  @Override
  public List<Articles> loadInBackground() {
    if (appUrl == null) {
      return null;
    }

    List<Articles> articlesList = QueryUtils.fetchArticlesData(appUrl);
    return articlesList;
  }
}
