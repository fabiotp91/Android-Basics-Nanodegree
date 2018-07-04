package com.fabiotp.inventoryappv2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.fabiotp.inventoryappv2.data.BooksContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {

  public static final String LOG_TAG = BookDbHelper.class.getSimpleName();

  // Name of the database file
  private static final String DATABASE_NAME = "bookstore.db";

  // Database version. If you change the database schema, you must increment the database version.
  private static final int DATABASE_VERSION = 1;

  /**
   * Constructs a new instance of {@link BookDbHelper}.
   *
   * @param context of the app
   */
  public BookDbHelper(Context context){
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // This is called when the database is created for the first time.
  @Override
  public void onCreate(SQLiteDatabase db) {
    String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
        + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
        + BookEntry.COLUMN_BOOK_PRICE + " INTEGER NOT NULL, "
        + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL, "
        + BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " INTEGER NOT NULL DEFAULT 0, "
        + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + " INTEGER );";

    db.execSQL(SQL_CREATE_BOOK_TABLE);
  }

  /**
   * This is called when the database needs to be upgraded.
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    db.execSQL("DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME);
  }
}
