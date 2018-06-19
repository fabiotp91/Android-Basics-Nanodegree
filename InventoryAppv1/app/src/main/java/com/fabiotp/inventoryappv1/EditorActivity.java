package com.fabiotp.inventoryappv1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.fabiotp.inventoryappv1.data.BookContract.BookEntry;
import com.fabiotp.inventoryappv1.data.BookDbHelper;

// Allows user to create a new book or edit an existing one.
public class EditorActivity extends AppCompatActivity {

  // EditText field to enter the book's name
  private EditText appNameEditText;

  // EditText field to enter the book's price
  private EditText appPriceEditText;

  // EditText field to enter the book's quantity
  private EditText appQuantityEditText;

  // EditText field to enter the book's supplier name
  private EditText appSupplierNameEditText;

  // EditTextfield to enter the book's supplier phone number
  private EditText appSupplierPhoneEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_editor);

    // Find all relevant views that we will need to read user input from
    appNameEditText = findViewById(R.id.edit_book_name);
    appPriceEditText = findViewById(R.id.edit_book_price);
    appQuantityEditText = findViewById(R.id.edit_book_quantity);
    appSupplierNameEditText = findViewById(R.id.edit_book_supplier_name);
    appSupplierPhoneEditText = findViewById(R.id.edit_book_supplier_number);
  }

  // Get user input from editor and save new book into database.
  private void insertBook() {
    // Read from input fields
    // user trim to eliminate leading or trailing white space
    String nameString = appNameEditText.getText().toString().trim();
    String priceString = appPriceEditText.getText().toString().trim();
    int price = Integer.parseInt(priceString);
    String quantityString = appQuantityEditText.getText().toString().trim();
    int quantity = Integer.parseInt(quantityString);
    String supplierNameString = appSupplierNameEditText.getText().toString().trim();
    String supplierPhoneString = appSupplierPhoneEditText.getText().toString().trim();

    BookDbHelper mDbHelper = new BookDbHelper(this);
    // Gets the data repository in write mode
    SQLiteDatabase db = mDbHelper.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(BookEntry.COLUMN_BOOK_NAME, nameString);
    values.put(BookEntry.COLUMN_BOOK_PRICE, price);
    values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
    values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
    values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, supplierPhoneString);

    long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

    if (newRowId == -1) {
      Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu options from the res/menu/menu_editor.xml file.
    // This adds menu items to the app bar.
    getMenuInflater().inflate(R.menu.menu_editor, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // User clicked on a menu option in the app bar overflow menu
    switch (item.getItemId()) {
      // Respond to a click on the "Save" menu option
      case R.id.action_save:
        // Save book to database
        insertBook();
        // Exit activity
        finish();
        return true;
      // Respond to a click on the "Up" arrow button in the app bar
      case android.R.id.home:
        // Navigate back to parent activity (CatalogActivity)
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
