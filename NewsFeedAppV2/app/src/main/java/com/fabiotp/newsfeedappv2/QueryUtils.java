package com.fabiotp.newsfeedappv2;

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
  // Set URL Connection Read Timeout
  private static int readTimeout = 10000;
  // Set URL Connection Connect Timeout
  private static int connectTimeout = 15000;

  private QueryUtils() {
  }

  public static List<Articles> fetchArticlesData(String requestUrl) {
    URL url = createUrl(requestUrl);
    String jsonResponse = null;

    try {
      jsonResponse = makeHttpRequest(url);
    } catch (IOException e) {
      Log.e(LOG_TAG, "IO Exception: ", e);
    }

    List<Articles> articlesList = extractFeatureFromJson(jsonResponse);

    return articlesList;
  }

  private static URL createUrl(String stringUrl) {
    URL url = null;
    try {
      url = new URL(stringUrl);
    } catch (MalformedURLException e) {
      Log.e(LOG_TAG, "Error creating URL: ", e);
    }
    return url;
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
  private static String makeHttpRequest(URL url) throws IOException {
    String jsonResponse = "";
    // If the URL is null, then return early.
    if (url == null) {
      return jsonResponse;
    }

    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setReadTimeout(readTimeout /* Milliseconds */);
      urlConnection.setConnectTimeout(connectTimeout /* Milliseconds */);
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      // If the request was successful (response code 200),
      // then read the input stream and parse the response.
      if (urlConnection.getResponseCode() == 200) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      } else {
        Log.d("Error response code: ", String.valueOf(urlConnection.getResponseCode()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
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
  private static List<Articles> extractFeatureFromJson(String response) {
    // If the JSON string is empty or null, then return early.
    if (TextUtils.isEmpty(response)) {
      return null;
    }

    // Create an empty ArrayList that we can start adding articles to
    List<Articles> articlesList = new ArrayList<>();

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
        JSONObject currentResults = resultsArray.getJSONObject(i);
        // For a given article, extract the JSONObject associated with the key
        // Extract the value for the key called "webTitle"
        String webTitle = currentResults.getString("webTitle");
        // Extract the value for the key called "sectionName"
        String section = currentResults.getString("sectionName");
        // Extract the value for the key called "webPublicationDate"
        String date = currentResults.getString("webPublicationDate");
        date = formatDate(date);
        // Extract the value for the key called "webUrl"
        String url = currentResults.getString("webUrl");
        JSONArray tagsAuthor = currentResults.getJSONArray("tags");
        String author = "";
        if (tagsAuthor.length() != 0) {
          JSONObject currentTagsAuthor = tagsAuthor.getJSONObject(0);
          author = currentTagsAuthor.getString("webTitle");
        }

        Articles articles = new Articles(webTitle, section, date, url, author);
        articlesList.add(articles);
      }
    } catch (JSONException e) {
      // If an error is thrown when executing any of the above statements in the "try" block,
      // catch the exception here, so the app doesn't crash. Print a log message
      // with the message from the exception.
      Log.e(LOG_TAG, "Error parsing JSON response: ", e);
    }
    // Return the list of articles
    return articlesList;
  }
}
