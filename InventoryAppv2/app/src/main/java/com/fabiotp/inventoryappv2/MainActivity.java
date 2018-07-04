package com.fabiotp.inventoryappv2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.fabiotp.inventoryappv2.data.BooksContract.BookEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  /** Identifier for the book data loader */
  private static final int BOOK_LOADER = 0;

  /** Adapter for the ListView */
  BookCursorAdapter mCursorAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Setup FAB to open EditorActivity
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
      }
    });

    // Find the ListView which will be populated with the book data
    ListView inventoryListView = findViewById(R.id.list);

    // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
    View emptyView = findViewById(R.id.empty_text_view);
    inventoryListView.setEmptyView(emptyView);

    // Setup an Adapter to create a list item for each row of book data in the Cursor.
    // There is no book data yet (until the loader finishes) so pass in null for the Cursor.
    mCursorAdapter = new BookCursorAdapter(this, null);
    inventoryListView.setAdapter(mCursorAdapter);

    // Setup the item click listener
    inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
        Intent intent = new Intent(MainActivity.this, BookActivity.class);
        Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
        intent.setData(currentBookUri);
        startActivity(intent);
      }
    });

    getLoaderManager().initLoader(BOOK_LOADER, null, this);
  }

  public void bookSalesCounter(int bookID, int bookQuantity){
    bookQuantity = bookQuantity -1;
    if(bookQuantity >= 0){
      ContentValues values = new ContentValues();
      values.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantity);
      Uri updateUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, bookID);
      int rowsAffected = getContentResolver().update(updateUri, values, null, null);
      Toast.makeText(this, "Quantity was changed", Toast.LENGTH_SHORT).show();

      Log.d("log message", "rowsAffected " + rowsAffected + " - bookID " + bookID +
      " - quantity " + bookQuantity + " ,decreaseCount has been called.");
    } else {
      Toast.makeText(this, "Book is out of stock", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    // Define a projection that specifies the columns from the table we care about.
    String[] projection = {
        BookEntry._ID,
        BookEntry.COLUMN_BOOK_NAME,
        BookEntry.COLUMN_BOOK_PRICE,
        BookEntry.COLUMN_BOOK_QUANTITY,
        BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
        BookEntry.COLUMN_BOOK_SUPPLIER_PHONE
    };
    // This loader will execute the ContentProvider's query method on a background thread
    return new CursorLoader(this,
        BookEntry.CONTENT_URI,
        projection,
        null,
        null,
        null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
    /** Update {@link BookCursorAdapter} with this new cursor containing updated book data */
    mCursorAdapter.swapCursor(cursor);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader){
    // Callback called when the data needs to be deleted
    mCursorAdapter.swapCursor(null);
  }

  private void deleteAllBooks() {
    int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
    Toast.makeText(this, rowsDeleted + " " + getString(R.string.deleted_all_books),
        Toast.LENGTH_SHORT).show();

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu options from the res/menu/menu_books.xml file.
    // This adds menu items to the app bar.
    getMenuInflater().inflate(R.menu.menu_books, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // User clicked on a menu option in the app bar overflow menu
    switch (item.getItemId()) {
      // Respond to a click on the "Delete all books" menu option
      case R.id.action_delete_all_entries:
        showDeleteConfirmationDialog();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showDeleteConfirmationDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.delete_all_dialog_msg);
    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        deleteAllBooks();
      }
    });
    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {

        if (dialog != null) {
          dialog.dismiss();
        }
      }
    });
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }
}