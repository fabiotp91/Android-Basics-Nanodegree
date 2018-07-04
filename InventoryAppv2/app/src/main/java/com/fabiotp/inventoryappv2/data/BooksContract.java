package com.fabiotp.inventoryappv2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BooksContract {

  private BooksContract() {}

  /**
   * The "Content authority" is a name for the entire content provider, similar to the
   * relationship between a domain name and its website.  A convenient string to use for the
   * content authority is the package name for the app, which is guaranteed to be unique on the
   * device.
   */
  public static final String CONTENT_AUTHORITY = "com.fabiotp.inventoryappv2";
  /**
   * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
   * the content provider.
   */
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String PATH_BOOKS = "books";

  /**
   * Inner class that defines constant values for the books database table.
   * Each entry in the table represents a single book.
   */
  public final static class BookEntry implements BaseColumns{

    /**
     * The content URI to access the book data in the provider
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

    /**
     * The MIME type of the {@link #CONTENT_URI} for a list of books.
     */
    public static final String CONTENT_LIST_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

    /**
     * The MIME type of the {@link #CONTENT_URI} for a single book.
     */
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

    // Name of Table DB
    public final static String TABLE_NAME = "books";

    // Table DB Columns
    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_BOOK_NAME = "book_name";
    public final static String COLUMN_BOOK_PRICE = "price";
    public final static String COLUMN_BOOK_QUANTITY = "quantity";
    public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";
    public final static String COLUMN_BOOK_SUPPLIER_PHONE = "supplier_phone";

    // Supplier Name List Values
    public final static int SUPPLIER_UNKNOWN = 0;
    public final static int SUPPLIER_AMAZON = 1;
    public final static int SUPPLIER_BERTRAND = 2;
    public final static int SUPPLIER_WOOK = 3;

    public static boolean isValidSupplier(int supplierName) {
      if(supplierName == SUPPLIER_UNKNOWN || supplierName == SUPPLIER_AMAZON
          || supplierName == SUPPLIER_BERTRAND || supplierName == SUPPLIER_WOOK){
        return true;
      }
      return false;
    }
  }
}
