package com.fabiotp.newsfeedapp;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryUtils {

  // Tag for the log messages
  private static final String LOG_TAG = QueryUtils.class.getSimpleName();
  // Get variable for Guardian API KEY
  private static String apikey = BuildConfig.guardianApiKey;
  // Set URL Connection Read Timeout
  private static int readTimeout = 10000;
  // Set URL Connection Connect Timeout
  private static int connectTimeout = 15000;

  // Build Uri to query the guardian API
  static String createStringUrl() {
    Uri.Builder builder = new Uri.Builder();
    builder.scheme("http")
        .encodedAuthority("content.guardianapis.com")
        .appendPath("search")
        .appendQueryParameter("order-by", "newest")
        .appendQueryParameter("show-references", "author")
        .appendQueryParameter("show-tags", "contributor")
        .appendQueryParameter("q", "Android")
        .appendQueryParameter("api-key", apikey);
    String url = builder.build().toString();
    return url;
  }

  // Create URL object
  static URL createUrl() {
    String stringUrl = createStringUrl();
    try {
      return new URL(stringUrl);
    } catch (MalformedURLException e) {
      Log.e(LOG_TAG, "Error creating URL: ", e);
      return null;
    }
  }

  // Return the formatted Date string from a Date Object
  private static String formatDate(String rawDate) {
    String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.getDefault());
    try {
      Date parsedJsonDate = jsonFormatter.parse(rawDate);
      String finalDatePattern = "MMM d, yyy";
      SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern,
          Locale.getDefault());
      return finalDateFormatter.format(parsedJsonDate);
    } catch (ParseException e) {
      Log.e(LOG_TAG, "Error parsing JSON date: ", e);
      return "";
    }
  }

  //Make an HTTP request to the given URL and return a String as the response
  static String makeHttpRequest(URL url) throws IOException {
    String jsonResponse = "";

    // If the URL is null, then return early.
    if (url == null) {
      return jsonResponse;
    }

    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.setReadTimeout(readTimeout /* milliseconds */);
      urlConnection.setConnectTimeout(connectTimeout /* milliseconds */);
      urlConnection.connect();

      // If the request was successful (response code 200),
      // then read the input stream and parse the response.
      if (urlConnection.getResponseCode() == 200) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      } else {
        Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
      }
    } catch (IOException e) {
      Log.e(LOG_TAG, "Error making HTTP request: ", e);
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
        // Closing the input stream could throw an IOException, which is why
        // the makeHttpRequest(URL url) method signature specifies than an IOException
        // could be thrown.
        inputStream.close();
      }
    }
    return jsonResponse;
  }

  /**
   * Convert the {@link InputStream} into a String which contains the
   * whole JSON Response from the server.
   */
  private static String readFromStream(InputStream inputStream) throws IOException {
    StringBuilder output = new StringBuilder();
    if (inputStream != null) {
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
          Charset.forName("UTF-8"));
      BufferedReader reader = new BufferedReader(inputStreamReader);
      String line = reader.readLine();
      while (line != null) {
        output.append(line);
        line = reader.readLine();
      }
    }
    return output.toString();
  }

  /**
   * Return a list of {@link Articles} objects that has been built up from
   * parsing the given JSON response.
   */
  static List<Articles> parseJson(String response) {
    // If the JSON string is empty or null, then return early.
    if (TextUtils.isEmpty(response)) {
      return null;
    }

    // Create an empty ArrayList that we can start adding articles to
    ArrayList<Articles> listOfArticles = new ArrayList<>();

    // Try to parse the JSON response string. If there's a problem with the way the JSON
    // is formatted, a JSONException exception object will be thrown.
    // Catch the exception so the app doesn't crash, and print the error message to the logs.
    try {
      // Create a JSONObject from the JSON response string
      JSONObject jsonResponse = new JSONObject(response);
      JSONObject jsonResults = jsonResponse.getJSONObject("response");

      // Extract the JSONArray associated with the key called "results"
      JSONArray resultsArray = jsonResults.getJSONArray("results");

      // For each article in the articleArray, create an {@link Articles} object
      for (int i = 0; i < resultsArray.length(); i++) {
        // Get a single article at position i within the list of articles
        JSONObject currentResult = resultsArray.getJSONObject(i);

        // For a given article, extract the JSONObject associated with the key
        // Extract the value for the key called "webTitle"
        String webTitle = currentResult.getString("webTitle");
        // Extract the value for the key called "webUrl"
        String url = currentResult.getString("webUrl");
        // Extract the value for the key called "webPublicationDate"
        String date = currentResult.getString("webPublicationDate");
        date = formatDate(date);
        // Extract the value for the key called "sectionName"
        String section = currentResult.getString("sectionName");
        JSONArray tagsArray = currentResult.getJSONArray("tags");
        String author = "";

        if (tagsArray.length() == 0) {
          author = null;
        } else {
          for (int j = 0; j < tagsArray.length(); j++) {
            JSONObject firstObject = tagsArray.getJSONObject(j);
            author += firstObject.getString("webTitle") + ". ";
          }
        }
        listOfArticles.add(new Articles(webTitle, author, url, date, section));
      }
    } catch (JSONException e) {
      // If an error is thrown when executing any of the above statements in the "try" block,
      // catch the exception here, so the app doesn't crash. Print a log message
      // with the message from the exception.
      Log.e(LOG_TAG, "Error parsing JSON response", e);
    }
    // Return the list of articles
    return listOfArticles;
  }
}
