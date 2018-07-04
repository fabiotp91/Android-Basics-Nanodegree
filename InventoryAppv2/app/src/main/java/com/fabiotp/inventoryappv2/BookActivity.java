package com.fabiotp.inventoryappv2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.fabiotp.inventoryappv2.data.BooksContract.BookEntry;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final int EXISTING_BOOK_LOADER = 0;
  private Uri mCurrentProductUri;

  private TextView mProductNameViewText;
  private TextView mProductPriceViewText;
  private TextView mProductQuantityViewText;
  private TextView mProductSupplieNameSpinner;
  private TextView mProductSupplierPhoneNumberViewText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_book);

    mProductNameViewText = findViewById(R.id.txt_viewbook_name);
    mProductPriceViewText = findViewById(R.id.txt_viewbook_price);
    mProductQuantityViewText = findViewById(R.id.txt_viewbook_quantity);
    mProductSupplieNameSpinner = findViewById(R.id.txt_viewbook_supplier_name);
    mProductSupplierPhoneNumberViewText = findViewById(R.id.txt_viewbook_supplier_phone);

    Intent intent = getIntent();
    mCurrentProductUri = intent.getData();
    if (mCurrentProductUri == null) {
      invalidateOptionsMenu();
    } else {
      getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
    }

    Log.d("message", "onCreate ViewActivity");

  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    String[] projection = {
        BookEntry._ID,
        BookEntry.COLUMN_BOOK_NAME,
        BookEntry.COLUMN_BOOK_PRICE,
        BookEntry.COLUMN_BOOK_QUANTITY,
        BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
        BookEntry.COLUMN_BOOK_SUPPLIER_PHONE
    };
    return new CursorLoader(this,
        mCurrentProductUri,
        projection,
        null,
        null,
        null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    if (cursor == null || cursor.getCount() < 1) {
      return;
    }
    if (cursor.moveToFirst()) {

      final int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
      int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
      int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
      int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
      int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
      int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

      String currentName = cursor.getString(nameColumnIndex);
      final int currentPrice = cursor.getInt(priceColumnIndex);
      final int currentQuantity = cursor.getInt(quantityColumnIndex);
      int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
      final int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

      mProductNameViewText.setText(currentName);
      mProductPriceViewText.setText(Integer.toString(currentPrice));
      mProductQuantityViewText.setText(Integer.toString(currentQuantity));
      mProductSupplierPhoneNumberViewText.setText(Integer.toString(currentSupplierPhone));


      switch (currentSupplierName) {
        case BookEntry.SUPPLIER_AMAZON:
          mProductSupplieNameSpinner.setText(getText(R.string.supplier_amazon));
          break;
        case BookEntry.SUPPLIER_BERTRAND:
          mProductSupplieNameSpinner.setText(getText(R.string.supplier_bertrand));
          break;
        case BookEntry.SUPPLIER_WOOK:
          mProductSupplieNameSpinner.setText(getText(R.string.supplier_wook));
          break;
        default:
          mProductSupplieNameSpinner.setText(getText(R.string.supplier_unknown));
          break;
      }

      Button productDecreaseButton = findViewById(R.id.bt_decrease);
      productDecreaseButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          decreaseCount(idColumnIndex, currentQuantity);
        }
      });

      Button productIncreaseButton = findViewById(R.id.bt_increase);
      productIncreaseButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          increaseCount(idColumnIndex, currentQuantity);
        }
      });

      Button productDeleteButton = findViewById(R.id.bt_delete);
      productDeleteButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          showDeleteConfirmationDialog();
        }
      });

      Button phoneButton = findViewById(R.id.bt_phone);
      phoneButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          String phone = String.valueOf(currentSupplierPhone);
          Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
          startActivity(intent);
        }
      });

    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {

  }

  public void decreaseCount(int productID, int productQuantity) {
    productQuantity = productQuantity - 1;
    if (productQuantity >= 0) {
      updateProduct(productQuantity);
      Toast.makeText(this, getString(R.string.quantity_change_msg), Toast.LENGTH_SHORT).show();

      Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");
    } else {
      Toast.makeText(this, getString(R.string.quantity_finish_msg), Toast.LENGTH_SHORT).show();
    }
  }

  public void increaseCount(int productID, int productQuantity) {
    productQuantity = productQuantity + 1;
    if (productQuantity >= 0) {
      updateProduct(productQuantity);
      Toast.makeText(this, getString(R.string.quantity_change_msg), Toast.LENGTH_SHORT).show();

      Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");
    }
  }


  private void updateProduct(int productQuantity) {
    Log.d("message", "updateProduct at ViewActivity");

    if (mCurrentProductUri == null) {
      return;
    }
    ContentValues values = new ContentValues();
    values.put(BookEntry.COLUMN_BOOK_QUANTITY, productQuantity);

    if (mCurrentProductUri == null) {
      Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
      if (newUri == null) {
        Toast.makeText(this, getString(R.string.insert_failed),
            Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, getString(R.string.insert_successful),
            Toast.LENGTH_SHORT).show();
      }
    } else {
      int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
      if (rowsAffected == 0) {
        Toast.makeText(this, getString(R.string.update_failed),
            Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, getString(R.string.insert_successful),
            Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void deleteProduct() {
    if (mCurrentProductUri != null) {
      int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
      if (rowsDeleted == 0) {
        Toast.makeText(this, getString(R.string.delete_product_failed),
            Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, getString(R.string.delete_product_successful),
            Toast.LENGTH_SHORT).show();
      }
    }
    finish();
  }

  private void showDeleteConfirmationDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.delete_dialog_msg);
    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        deleteProduct();
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

