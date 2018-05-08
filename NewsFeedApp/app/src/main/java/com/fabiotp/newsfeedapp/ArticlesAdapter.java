package com.fabiotp.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ArticlesAdapter extends ArrayAdapter<Articles> {

  /**
   * Constructs a new {@link ArticlesAdapter}
   *
   * @param context of the app
   * @param articles is the list of articles, which is the data source of the adapter
   */
  public ArticlesAdapter(Context context, List<Articles> articles) {
    super(context, -1, articles);
  }


  /**
   * Returns a list item view that displays information about the article at the given position
   * in the list of articles.
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
          parent, false);
    }

    // Find the article at the given position in the list of articles.
    Articles currentArticles = getItem(position);

    // Find and Set the title
    TextView titleView = convertView.findViewById(R.id.txt_title);
    titleView.setText(currentArticles.getTitle());

    // Find and Set the author
    TextView authorView = convertView.findViewById(R.id.txt_author);
    authorView.setText(currentArticles.getAuthor());

    // Find and Set the date
    TextView dateView = convertView.findViewById(R.id.txt_date);
    dateView.setText(currentArticles.getDate());

    // Find and Set the section
    TextView sectionView = convertView.findViewById(R.id.txt_section);
    sectionView.setText(currentArticles.getSection());

    return convertView;
  }
}
