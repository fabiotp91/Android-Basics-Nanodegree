package com.fabiotp.inventoryappv2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.fabiotp.inventoryappv2.data.BooksContract.BookEntry;

public class BookProvider extends ContentProvider {

  /** URI matcher code for the content URI for the books table */
  private static final int BOOKS = 100;
  /** URI matcher code for the content URI for a single book in the pets table */
  private static final int BOOKS_ID = 101;

  /**
   * UriMatcher object to match a content URI to a corresponding code.
   * The input passed into the constructor represents the code to return for the root URI.
   * It's common to use NO_MATCH as the input for this case.
   */
  private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH_BOOKS, BOOKS);
    sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH_BOOKS + "/#", BOOKS_ID);
  }

  /** Database helper object */
  private BookDbHelper mDbHelper;

  @Override
  public boolean onCreate() {
    mDbHelper = new BookDbHelper((getContext()));
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    // Get readable database
    SQLiteDatabase database = mDbHelper.getReadableDatabase();

    // This cursor will hold the result of the query
    Cursor cursor;

    // Figure out if the URI matcher can match the URI to a specific code
    int match = sUriMatcher.match(uri);
    switch (match) {
      case BOOKS:
        // For the BOOKS code, query the books table directly with the given
        // projection, selection, selection arguments, and sort order. The cursor
        // could contain multiple rows of the books table.
        cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
            null, null, sortOrder);
        break;
      case BOOKS_ID:
        // For the BOOK_ID code, extract out the ID from the URI.
        // For an example URI such as "content://com.fabiotp.inventoryappv2/books/3",
        // the selection will be "_id=?" and the selection argument will be a
        // String array containing the actual ID of 3 in this case.
        //
        // For every "?" in the selection, we need to have an element in the selection
        // arguments that will fill in the "?". Since we have 1 question mark in the
        // selection, we have 1 String in the selection arguments' String array.
        selection = BookEntry._ID + "=?";
        selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
        // This will perform a query on the books table where the _id equals 3 to return a
        // Cursor containing that row of the table.
        cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
            null, null, sortOrder);
        break;
        default:
          throw new IllegalArgumentException("Cannot Query Unknown URI " + uri);
    }
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  @Override
  public Uri insert(Uri uri, ContentValues contentValues) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case BOOKS:
        return insertBook(uri, contentValues);
        default:
          throw new IllegalArgumentException("Insertion is not supported for " + uri);
    }
  }

  @Override
  public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);
    switch (match){
      case BOOKS:
        return BookEntry.CONTENT_LIST_TYPE;
      case BOOKS_ID:
        return BookEntry.CONTENT_ITEM_TYPE;
      default:
        throw new IllegalArgumentException("Unknown URI" + uri + " with match " + match);
    }
  }

  /**
   * Insert a book into the database with the given content values. Return the new content URI
   * for that specific row in the database.
   */
  private Uri insertBook(Uri uri, ContentValues values){
    // Check that the name is not null
    String nameBook = values.getAsString(BookEntry.COLUMN_BOOK_NAME);
    if (nameBook == null){
      throw new IllegalArgumentException("Book Name can't be empty");
    }

    // Check that the price is not null or less than 0
    Integer priceBook = values.getAsInteger(BookEntry.COLUMN_BOOK_PRICE);
    if(priceBook != null && priceBook < 0){
      throw new IllegalArgumentException("Book Price is not valid");
    }

    // Check that the quantity is not null or less than 0
    Integer quantityBook = values.getAsInteger(BookEntry.COLUMN_BOOK_QUANTITY);
    if(quantityBook != null && quantityBook < 0){
      throw new IllegalArgumentException("Book Quantity is not valid");
    }

    // Check that the supplier name is not set to UNKNOWN
    Integer supplierName = values.getAsInteger(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
    if(supplierName == null || !BookEntry.isValidSupplier(supplierName)){
      throw new IllegalArgumentException("Please choose a Supplier");
    }

    // Check that the supplier number is not null or less than 0
    Integer supplierPhone = values.getAsInteger(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);
    if(supplierPhone != null && supplierPhone < 0){
      throw new IllegalArgumentException("Supplier Phone is not valid");
    }

    // Get writeable database
    SQLiteDatabase database = mDbHelper.getWritableDatabase();
    // Insert the new book with the given values
    long id = database.insert(BookEntry.TABLE_NAME, null, values);
    // If the ID is -1, then the insertion failed. Log an error and return null.
    if(id == -1) {
      Log.v("Message: ", "Failed to insert new row for " + uri);
      return null;
    }
    // Notify all listeners that the data has changed for the book content URI
    getContext().getContentResolver().notifyChange(uri, null);
    // Return the new URI with the ID (of the newly inserted row) appended at the end
    return ContentUris.withAppendedId(uri, id);
  }

  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // Get writeable database
    SQLiteDatabase database = mDbHelper.getWritableDatabase();
    // Track the number of rows that were deleted
    int rowsDeleted;

    final int match = sUriMatcher.match(uri);
    switch (match){
      case BOOKS:
        // Delete all rows that match the selection and selection args
        rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case BOOKS_ID:
        // Delete a single row given by the ID in the URI
        selection = BookEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
        break;
        default:
          throw new IllegalArgumentException("Deletion is not supported for " + uri);
    }
    // If 1 or more rows were deleted, then notify all listeners that the data at the
    // given URI has changed
    if(rowsDeleted != 0){
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
    final int match = sUriMatcher.match(uri);
    switch(match){
      case BOOKS:
        return updateBook(uri, contentValues, selection, selectionArgs);
      case BOOKS_ID:
        selection = BookEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return updateBook(uri, contentValues, selection, selectionArgs);
      default:
        throw new IllegalArgumentException("Update is not supported for " + uri);
    }
  }

  private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    if (values.containsKey(BookEntry.COLUMN_BOOK_NAME)) {
      String nameBook = values.getAsString(BookEntry.COLUMN_BOOK_NAME);
      if (nameBook == null) {
        throw new IllegalArgumentException("Book Name required");
      }
    }
    if (values.containsKey(BookEntry.COLUMN_BOOK_PRICE)) {
      Integer priceBook = values.getAsInteger(BookEntry.COLUMN_BOOK_PRICE);
      if (priceBook != null && priceBook < 0) {
        throw new
            IllegalArgumentException("Book Price needs to be valid");
      }
    }

    if (values.containsKey(BookEntry.COLUMN_BOOK_QUANTITY)) {
      Integer quantityBook = values.getAsInteger(BookEntry.COLUMN_BOOK_QUANTITY);
      if (quantityBook != null && quantityBook < 0) {
        throw new
            IllegalArgumentException("Book quantity needs to be valid");
      }
    }
    if (values.containsKey(BookEntry.COLUMN_BOOK_SUPPLIER_NAME)) {
      Integer supplierName = values.getAsInteger(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
      if (supplierName == null || !BookEntry.isValidSupplier(supplierName)) {
        throw new IllegalArgumentException("Supplier Name needs to be valid");
      }
    }

    if (values.containsKey(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE)) {
      Integer supplierPhone = values.getAsInteger(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);
      if (supplierPhone != null && supplierPhone < 0) {
        throw new
            IllegalArgumentException("Supplier Phone needs to be valid");
      }
    }

    if (values.size() == 0) {
      return 0;
    }

    SQLiteDatabase database = mDbHelper.getWritableDatabase();

    int rowsUpdated = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);

    if (rowsUpdated != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsUpdated;
  }
}
