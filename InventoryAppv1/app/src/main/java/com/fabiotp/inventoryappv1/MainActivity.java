package com.fabiotp.inventoryappv1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.fabiotp.inventoryappv1.data.BookContract.BookEntry;
import com.fabiotp.inventoryappv1.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

  private BookDbHelper mDbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Setup FAB to open EditorActivity
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
      }
    });

    // To access our database, we instatiate our subclass of SQLiteOpenHelper
    // and pass the content, which is the current activity.
    mDbHelper = new BookDbHelper(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    displayDatabaseInfo();
  }

  // Temporary helper method to display information in the onscreen TextView about the state of the
  // Book store database.
  private void displayDatabaseInfo() {
    // Create and/or open a database to read from it
    SQLiteDatabase db = mDbHelper.getReadableDatabase();

    String[] projection = {
        BookEntry._ID,
        BookEntry.COLUMN_BOOK_NAME,
        BookEntry.COLUMN_BOOK_PRICE,
        BookEntry.COLUMN_BOOK_QUANTITY,
        BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
        BookEntry.COLUMN_BOOK_SUPPLIER_PHONE
    };

    Cursor cursor = db.query(
        BookEntry.TABLE_NAME,
        projection,
        null,
        null,
        null,
        null,
        null);

    TextView displayView = findViewById(R.id.text_view_pet);

    try {
      /*
       * Create a header in the Text View
       *
       * In the while long below, iterate through the rows of the cursor and display
       * the information from each column in this order.
       */
      displayView.setText("The Books table contains " + cursor.getCount() + " books\n\n");
      displayView.append(BookEntry._ID + " - " +
          BookEntry.COLUMN_BOOK_NAME + " - " +
          BookEntry.COLUMN_BOOK_PRICE + " - " +
          BookEntry.COLUMN_BOOK_QUANTITY + " - " +
          BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " - " +
          BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + "\n");

      // Figure out the index of each column
      int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
      int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
      int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
      int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
      int supNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
      int supPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

      // Iterate through all the returned rows in the cursor
      while (cursor.moveToNext()) {
        // Use that index to extract the String or Int value of the word
        // at the current row the cursor is on.
        int currentID = cursor.getInt(idColumnIndex);
        String currentName = cursor.getString(nameColumnIndex);
        int currentPrice = cursor.getInt(priceColumnIndex);
        int currentQuantity = cursor.getInt(quantityColumnIndex);
        String currentSupName = cursor.getString(supNameColumnIndex);
        String currentSupPhone = cursor.getString(supPhoneColumnIndex);

        // Display the values from each column of the current row in the cursor in the TextView
        displayView.append(("\n" + currentID + " - " +
            currentName + " - " +
            currentPrice + " - " +
            currentQuantity + " - " +
            currentSupName + " - " +
            currentSupPhone));
      }
    } finally {
      // Always close the cursor when you're done reading from it.
      // This releases all it's resources and makes it invalid.
      cursor.close();
    }
  }

  private void insertBook() {
    // Gets the data repository in write mode
    SQLiteDatabase db = mDbHelper.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(BookEntry.COLUMN_BOOK_NAME, "Clockwork Orange");
    values.put(BookEntry.COLUMN_BOOK_PRICE, 12);
    values.put(BookEntry.COLUMN_BOOK_QUANTITY, 20);
    values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Amazon");
    values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, "+351 213 456 789");

    long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

    Log.v("MainActivity", "New Row ID " + newRowId);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu options from res/menu/menu_main.xml file.
    // This adds menu items to the app bar.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // User clicked on a menu option in the app bar overflow menu
    switch (item.getItemId()) {
      // Respond to a click on the "Insert dummy data" menu option
      case R.id.action_insert_dummy_data:
        insertBook();
        displayDatabaseInfo();
        return true;

    }
    return super.onOptionsItemSelected(item);
  }
}
