package com.fabiotp.inventoryappv2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.fabiotp.inventoryappv2.data.BooksContract.BookEntry;
import java.util.function.Supplier;

// Allows user to create a new book or edit an existing one.
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  // Identifier for the book data loader
  private static final int EXISTING_BOOK_LOADER = 0;
  // Content URI for the existing book (null if it's a new book)
  private Uri currentBookUri;

  private EditText bookNameEditText;
  private EditText bookPriceEditText;
  private EditText bookQuantityEditText;
  private Spinner bookSupplierNameSpinner;
  private EditText bookSupplierPhoneEditText;

  // Supplier Name of the book. The possible values are in the BookContract.java file.
  private int supplierName = BookEntry.SUPPLIER_UNKNOWN;

  // Boolean flag that keeps track of whether the book has been edited (true) or not (false)
  private boolean bookHasChanged = false;

  /**
   * OnTouchListener that listens for any user touches on a View, implying that they are modifying
   * the view, and we change the bookHasChanged boolean to true.
   */
  private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      bookHasChanged = true;
      Log.d("message", "onTouch");

      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_editor);

    // Examine the intent that was used to launch this activity,
    // in order to figure out if we're creating a new book or editing an existing one.
    Intent intent = getIntent();
    currentBookUri = intent.getData();

    // If the intent DOES NOT contain a book content URI, then we know that we are
    // creating a new book.
    if (currentBookUri == null) {
      setTitle(getString(R.string.add_book));
      invalidateOptionsMenu();
    } else {
      setTitle(getString(R.string.edit_book));
      getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
    }

    // Find all relevant views that we will need to read user input from
    bookNameEditText = findViewById(R.id.txt_edit_book_name);
    bookPriceEditText = findViewById(R.id.txt_edit_book_price);
    bookQuantityEditText = findViewById(R.id.txt_edit_book_quantity);
    bookSupplierNameSpinner = findViewById(R.id.txt_edit_book_supplier_name);
    bookSupplierPhoneEditText = findViewById(R.id.txt_edit_book_supplier_phone);

    // Setup OnTouchListeners on all the input fields, so we can determine if the user
    // has touched or modified them. This will let us know if there are unsaved changes
    // or not, if the user tries to leave the editor without saving.
    bookNameEditText.setOnTouchListener(mTouchListener);
    bookPriceEditText.setOnTouchListener(mTouchListener);
    bookQuantityEditText.setOnTouchListener(mTouchListener);
    bookSupplierNameSpinner.setOnTouchListener(mTouchListener);
    bookSupplierPhoneEditText.setOnTouchListener(mTouchListener);

    setupSpinner();
  }

  //Setup the dropdown spinner that allows the user to select the supplier of the book.
  private void setupSpinner() {
    ArrayAdapter bookSupplierNameSpinnerAdapter = ArrayAdapter.createFromResource(this,
        R.array.array_supplier_options, android.R.layout.simple_spinner_item);

    // Specify dropdown layout style - simple list view with 1 item per line
    bookSupplierNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    // Apply the adapter to the spinner
    bookSupplierNameSpinner.setAdapter(bookSupplierNameSpinnerAdapter);
    // Set the integer selected to the constant values
    bookSupplierNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);
        if (!TextUtils.isEmpty(selection)) {
          if (selection.equals(getString(R.string.supplier_amazon))) {
            supplierName = BookEntry.SUPPLIER_AMAZON;
          } else if (selection.equals(getString(R.string.supplier_bertrand))) {
            supplierName = BookEntry.SUPPLIER_BERTRAND;
          } else if (selection.equals(getString(R.string.supplier_wook))) {
            supplierName = BookEntry.SUPPLIER_WOOK;
          } else {
            supplierName = BookEntry.SUPPLIER_UNKNOWN;
          }
        }
      }
      // Because AdapterView is an abstract class, onNothingSelected must be defined
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        supplierName = BookEntry.SUPPLIER_UNKNOWN;
      }
    });
  }
  //Get user input from editor and save book into database.
  private void saveBook() {
    // Use trim to eliminate leading or trailing white space
    String bookNameString = bookNameEditText.getText().toString().trim();
    String bookPriceString = bookPriceEditText.getText().toString().trim();
    String bookQuantityString = bookQuantityEditText.getText().toString().trim();
    String bookSupplierPhoneString = bookSupplierPhoneEditText.getText().toString().trim();

    // Check if this is supposed to be a new book
    // and check if all the fields in the editor are blank
    if (currentBookUri == null) {
      // Check if EditText is empty, show Book Name is required message if it is
      if (TextUtils.isEmpty(bookNameString)) {
        Toast.makeText(this, getString(R.string.book_name_required),
            Toast.LENGTH_SHORT).show();
        return;
      }
      // Check if EditText is empty, show Book Price is required message if it is
      if (TextUtils.isEmpty(bookPriceString)) {
        Toast.makeText(this, getString(R.string.book_price_required),
            Toast.LENGTH_SHORT).show();
        return;
      }
      // Check if EditText is empty, show Book Quantity is required message if it is
      if (TextUtils.isEmpty(bookQuantityString)) {
        Toast.makeText(this, getString(R.string.book_quantity_required),
            Toast.LENGTH_SHORT).show();
        return;
      }
      // Check if Spinner has Unknown selected, show Supplier Name is required message if it is
      if (supplierName == BookEntry.SUPPLIER_UNKNOWN) {
        Toast.makeText(this, getString(R.string.supplier_name_required),
            Toast.LENGTH_SHORT).show();
        return;
      }
      // Check if EditText is empty, show Supplier Phone is required message if it is
      if (TextUtils.isEmpty(bookSupplierPhoneString)) {
        Toast.makeText(this, getString(R.string.supplier_phone_required),
            Toast.LENGTH_SHORT).show();
        return;
      }

      // Create a ContentValues object where column names are the keys,
      // and book attributes from the editor are the values.
      ContentValues values = new ContentValues();
      values.put(BookEntry.COLUMN_BOOK_NAME, bookNameString);
      values.put(BookEntry.COLUMN_BOOK_PRICE, bookPriceString);
      values.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantityString);
      values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierName);
      values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, bookSupplierPhoneString);

      // This is a NEW book, so insert a new book into the provider,
      // returning the content URI for the new book.
      Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

      // Show a toast message depending on whether or not the insertion was successful.
      if (newUri == null) {
        // If the new content URI is null, then there was an error with insertion.
        Toast.makeText(this, getString(R.string.insert_failed),
            Toast.LENGTH_SHORT).show();
      } else {
        // Otherwise, the insertion was successful and we can display a toast.
        Toast.makeText(this, getString(R.string.insert_successful),
            Toast.LENGTH_SHORT).show();
        finish();
      }
    }else{
      // Check if EditText is empty, show Book Name is required message if it is
      if (TextUtils.isEmpty(bookNameString)) {
        Toast.makeText(this, getString(R.string.book_name_required),
            Toast.LENGTH_SHORT).show();
        return;
      }
      // Check if EditText is empty, show Book Price is required message if it is
      if (TextUtils.isEmpty(bookPriceString)) {
        Toast.makeText(this, getString(R.string.book_price_required),
            Toast.LENGTH_SHORT).show();
        return;
      }
      // Check if EditText is empty, show Book Quantity is required message if it is
      if (TextUtils.isEmpty(bookQuantityString)) {
        Toast.makeText(this, getString(R.string.book_quantity_required),
            Toast.LENGTH_SHORT).show();
        return;
      }
      // Check if Spinner has Unknown selected, show Supplier Name is required message if it is
      if (supplierName == BookEntry.SUPPLIER_UNKNOWN) {
        Toast.makeText(this, getString(R.string.supplier_name_required),
            Toast.LENGTH_SHORT).show();
        return;
      }
      // Check if EditText is empty, show Supplier Phone is required message if it is
      if (TextUtils.isEmpty(bookSupplierPhoneString)) {
        Toast.makeText(this, getString(R.string.supplier_phone_required),
            Toast.LENGTH_SHORT).show();
        return;
      }

      ContentValues values = new ContentValues();

      values.put(BookEntry.COLUMN_BOOK_NAME, bookNameString);
      values.put(BookEntry.COLUMN_BOOK_PRICE, bookPriceString);
      values.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantityString);
      values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierName);
      values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, bookSupplierPhoneString);

      // Otherwise this is an EXISTING book, so update the book with content URI: currentBookUri
      // and pass in the new ContentValues. Pass in null for the selection and selection args
      // because currentBookUri will already identify the correct row in the database that
      // we want to modify.
      int rowsAffected = getContentResolver().update(currentBookUri, values, null, null);
      // Show a toast message depending on whether or not the update was successful.
      if (rowsAffected == 0) {
        // If no rows were affected, then there was an error with the update.
        Toast.makeText(this, getString(R.string.update_failed),
            Toast.LENGTH_SHORT).show();
      } else {
        // Otherwise, the update was successful and we can display a toast.
        Toast.makeText(this, getString(R.string.update_successful),
            Toast.LENGTH_SHORT).show();
        finish();
      }

    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu options from the res/menu/menu_editor.xml file.
    // This adds menu items to the app bar.
    getMenuInflater().inflate(R.menu.menu_editor, menu);
    Log.d("message", "open Editor Activity");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // User clicked on a menu option in the app bar overflow menu
    switch (item.getItemId()) {
      // Respond to a click on the "Save" menu option
      case R.id.action_save:
        saveBook();
        return true;
      case android.R.id.home:
        // If the book hasn't changed, continue with navigating up to parent activity
        // which is the {@link MainActivity}
        if (!bookHasChanged) {
          NavUtils.navigateUpFromSameTask(EditorActivity.this);
          return true;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that
        // changes should be discarded.
        OnClickListener discardButtonClickListener =
            new OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "Discard" button, navigate to parent activity.
                NavUtils.navigateUpFromSameTask(EditorActivity.this);
              }
            };
        // Show a dialog that notifies the user they have unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // This method is called when the back button is pressed.
  @Override
  public void onBackPressed() {
    // If the book hasn't changed, continue with handling back button press
    if (!bookHasChanged) {
      super.onBackPressed();
      return;
    }
    // Otherwise if there are unsaved changes, setup a dialog to warn the user.
    // Create a click listener to handle the user confirming that changes should be discarded.
    OnClickListener discardButtonClickListener =
        new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            // User clicked "Discard" button, close the current activity.
            finish();
          }
        };
    showUnsavedChangesDialog(discardButtonClickListener);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    // Since the editor shows all books attributes, define a projection that contains
    // all columns from the books table
    String[] projection = {
        BookEntry._ID,
        BookEntry.COLUMN_BOOK_NAME,
        BookEntry.COLUMN_BOOK_PRICE,
        BookEntry.COLUMN_BOOK_QUANTITY,
        BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
        BookEntry.COLUMN_BOOK_SUPPLIER_PHONE};

    // This loader will execute the ContentProvider's query method on a background thread
    return new CursorLoader(this,       // Parent activity context
        currentBookUri,                         // Query the content URI for the current book
        projection,                             // Columns to include in the resulting Cursor
        null,                          // No selection clause
        null,                       // No selection arguments
        null);                         // Default sort order
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    // Bail early if the cursor is null or there is less than 1 row in the cursor
    if (cursor == null || cursor.getCount() < 1) {
      return;
    }

    // Proceed with moving to the first row of the cursor and reading data from it
    // (This should be the only row in the cursor)
    if (cursor.moveToFirst()) {
      // Find the columns of book attributes that we're interested in
      int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
      int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
      int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
      int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
      int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

      // Extract out the value from the Cursor for the given column index
      String currentName = cursor.getString(nameColumnIndex);
      int currentPrice = cursor.getInt(priceColumnIndex);
      int currentQuantity = cursor.getInt(quantityColumnIndex);
      int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
      int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

      // Update the views on the screen with the values from the database
      bookNameEditText.setText(currentName);
      bookPriceEditText.setText(Integer.toString(currentPrice));
      bookQuantityEditText.setText(Integer.toString(currentQuantity));
      bookSupplierPhoneEditText.setText(Integer.toString(currentSupplierPhone));

      // Supplier Name is a dropdown spinner, so map the constant value from the database
      // into one of the dropdown options (0 is Unknown, 1 is Amazon, 2 is Bertrand, 3 is Wook).
      // Then call setSelection() so that option is displayed on screen as the current selection.
      switch (currentSupplierName) {
        case BookEntry.SUPPLIER_AMAZON:
          bookSupplierNameSpinner.setSelection(1);
          break;
        case BookEntry.SUPPLIER_BERTRAND:
          bookSupplierNameSpinner.setSelection(2);
          break;
        case BookEntry.SUPPLIER_WOOK:
          bookSupplierNameSpinner.setSelection(3);
          break;
        default:
          bookSupplierNameSpinner.setSelection(0);
          break;
      }
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // If the loader is invalidated, clear out all the data from the input fields.
    bookNameEditText.setText("");
    bookPriceEditText.setText("");
    bookQuantityEditText.setText("");
    bookSupplierPhoneEditText.setText("");
    bookSupplierNameSpinner.setSelection(0);// Select "Unknown" Supplier Name
  }

  /**
   * Show a dialog that warns the user there are unsaved changes that will be lost
   * if they continue leaving the editor.
   *
   * @param discardButtonClickListener is the click listener for what to do when
   *                                   the user confirms they want to discard their changes
   */
  private void showUnsavedChangesDialog(
      DialogInterface.OnClickListener discardButtonClickListener) {
    // Create an AlertDialog.Builder and set the message, and click listeners
    // for the positive and negative buttons on the dialog.
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.unsaved_changes_dialog_msg);
    builder.setPositiveButton(R.string.discard, discardButtonClickListener);
    builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        // User clicked the "Keep editing" button, so dismiss the dialog
        // and continue editing the book.
        if (dialog != null) {
          dialog.dismiss();
        }
      }
    });
    // Create and show the AlertDialog
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }
}
