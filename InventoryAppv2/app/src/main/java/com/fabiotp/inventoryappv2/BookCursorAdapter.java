package com.fabiotp.inventoryappv2;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.fabiotp.inventoryappv2.data.BooksContract.BookEntry;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of book data as its data source. This adapter knows
 * how to create list items for each row of book data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

  /**
   * Constructs a new {@link BookCursorAdapter}.
   *
   * @param context The context
   * @param c       The cursor from which to get the data.
   */
  public BookCursorAdapter(Context context, Cursor c) {
    super(context, c, 0);
  }

  /**
   * Makes a new blank list item view. No data is set (or bound) to the views yet.
   *
   * @param context app context
   * @param cursor  The cursor from which to get the data. The cursor is already
   *                moved to the correct position.
   * @param parent  The parent to which the new view is attached to
   * @return the newly created list item view.
   */
  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent){
    return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
  }

  /**
   * This method binds the book data (in the current row pointed to by cursor) to the given
   * list item layout. For example, the name for the current book can be set on the name TextView
   * in the list item layout.
   *
   * @param view    Existing view, returned earlier by newView() method
   * @param context app context
   * @param cursor  The cursor from which to get the data. The cursor is already moved to the
   *                correct row.
   */
  @Override
  public void bindView(final View view, final Context context, final Cursor cursor) {

    // Find individual views that we want to modify in the list item layout
    TextView bookNameTextView = view.findViewById(R.id.txt_bookName);
    TextView bookPriceTextView = view.findViewById(R.id.txt_bookPrice);
    TextView bookQuantityTextView = view.findViewById(R.id.txt_bookQuantity);
    Button bookSaleButton = view.findViewById(R.id.bt_sale);

    // Find the columns of book attributes that we're interested in
    final int columnIdIndex = cursor.getColumnIndex(BookEntry._ID);
    int bookNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
    int bookPriceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
    int bookQuantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

    // Read the book attributes from the Cursor for the current book
    final String bookID = cursor.getString(columnIdIndex);
    String bookName = cursor.getString(bookNameColumnIndex);
    String bookPrice = cursor.getString(bookPriceColumnIndex);
    final String bookQuantity = cursor.getString(bookQuantityColumnIndex);

    bookSaleButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        MainActivity activity = (MainActivity) context;
        activity.bookSalesCounter(Integer.valueOf(bookID), Integer.valueOf(bookQuantity));
      }
    });

    bookNameTextView.setText(bookName);
    bookPriceTextView.setText(context.getString(R.string.book_price) + " : " + bookPrice + "  " + context.getString(R.string.book_price_currency));
    bookQuantityTextView.setText(context.getString(R.string.book_quantity) + " : " + bookQuantity);

    Button bookEditButton = view.findViewById(R.id.bt_edit);
    bookEditButton.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        Intent intent = new Intent(view.getContext(), EditorActivity.class);
        Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, Long.parseLong(bookID));
        intent.setData(currentBookUri);
        context.startActivity(intent);
      }
    });
  }
}
