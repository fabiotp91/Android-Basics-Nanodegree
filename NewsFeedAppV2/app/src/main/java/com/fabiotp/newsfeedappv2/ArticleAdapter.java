package com.fabiotp.newsfeedappv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Articles> {

  /**
   * Constructs a new {@link ArticleAdapter}
   *
   * @param context of the app
   * @param articles is the list of articles, which is the data source of the adapter
   */
  public ArticleAdapter(Context context, ArrayList<Articles> articles) {
    super(context, 0, articles);
  }

  /**
   * Returns a list item view that displays information about the article at the given position
   * in the list of articles.
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View listItemView = convertView;
    if (listItemView == null) {
      listItemView = LayoutInflater.from(getContext()).inflate(
          R.layout.list_item, parent, false);
    }

    // Find the article at the given position in the list of articles.
    Articles currentArticles = getItem(position);
    // Find and Set the title
    TextView titleTextView = listItemView.findViewById(R.id.title_text_view);
    String titleText = currentArticles.getTitle();
    titleTextView.setText(titleText);
    // Find and Set the section
    TextView sectionTextView = listItemView.findViewById(R.id.category_text_view);
    String sectionText = currentArticles.getSection();
    sectionTextView.setText(sectionText);
    // Find and Set the date
    TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
    String dateText = currentArticles.getDate();
    dateTextView.setText(dateText);
    // Find and Set the author
    TextView authorTextView = listItemView.findViewById(R.id.author_text_view);
    String authorText = currentArticles.getAuthor();

    // Check if the Author is empty
    if (authorText.equals("")) {
      authorText = getContext().getString(R.string.noauthor);
    }
    authorTextView.setText(authorText);

    return listItemView;

  }
}
