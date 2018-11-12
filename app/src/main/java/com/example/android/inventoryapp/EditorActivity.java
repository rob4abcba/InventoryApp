package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

/**
 * Allows user to create a new item or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the pet data loader
     */
    private static final int EXISTING_INVENTORY_LOADER = 0;

    /**
     * Content URI for the existing pet (null if it's a new pet)
     */
    private Uri mCurrentInventoryUri;

    /**
     * EditText field to enter the item name
     */
    private EditText mItemNameEditText;

    /**
     * EditText field to enter the item price
     */
    private EditText mItemPriceEditText;

    /**
     * EditText field to enter the item quantity
     */
    private EditText mQuantityEditText;

    /**
     * Spinner field to choose the item supplier
     */
    private Spinner mSupplierNameSpinner;

    /**
     * EditText field to enter the supplier phone number
     */
    private EditText mSupplierPhoneEditText;

    private String mSupplier = "Default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();

        if (mCurrentInventoryUri == null) {
            setTitle(R.string.editor_activity_title_new_inventory);
        } else {
            setTitle(R.string.editor_activity_title_edit_inventory);

            getSupportLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mItemNameEditText = findViewById(R.id.edit_item_product_name);
        mItemPriceEditText = findViewById(R.id.edit_price);
        mQuantityEditText = findViewById(R.id.edit_item_quantity);
        mSupplierNameSpinner = findViewById(R.id.spinner_supplier);
        mSupplierPhoneEditText = findViewById(R.id.edit_supplier_phone);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter supplierSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        supplierSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierNameSpinner.setAdapter(supplierSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_1))) {
                        mSupplier = "Supplier 1"; // Supplier 1
                    } else if (selection.equals(getString(R.string.supplier_2))) {
                        mSupplier = "Supplier 2"; // Supplier 2
                    } else {
                        mSupplier = "Supplier 3"; // Supplier 3
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = "Unknown";
            }
        });
    }

    private void saveInventory() {
        String nameString = mItemNameEditText.getText().toString().trim();
        String priceString = mItemPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, priceString);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantityString);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER, mSupplier);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, supplierPhoneString);

        if (mCurrentInventoryUri == null) {

            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_inventory_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_inventory_successful),
                        Toast.LENGTH_SHORT).show();
            }
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
                saveInventory();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_PRODUCT_SUPPLIER,
                InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentInventoryUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);

            String name = cursor.getString(nameColumnIndex);
            Double price = cursor.getDouble(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int supplier = cursor.getInt(supplierColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            mItemNameEditText.setText(name);
            mItemPriceEditText.setText(Double.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierPhoneEditText.setText(supplierPhone);

            switch (supplier) {
                case InventoryEntry.SUPPLIER1:
                    mSupplierNameSpinner.setSelection(1);
                    break;
                case InventoryEntry.SUPPLIER2:
                    mSupplierNameSpinner.setSelection(2);
                    break;
                case InventoryEntry.SUPPLIER3:
                    mSupplierNameSpinner.setSelection(3);
                    break;
                default:
                    mSupplierNameSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        mItemNameEditText.setText("");
        mItemPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameSpinner.setSelection(0);
        mSupplierPhoneEditText.setText("");
    }
}