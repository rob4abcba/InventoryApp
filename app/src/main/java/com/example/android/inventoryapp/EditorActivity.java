package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
import com.example.android.inventoryapp.data.InventoryDbHelper;

/**
 * Allows user to create a new item or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

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

    /**
     * Name of the supplier. The possible values are:
     * 0 for supplier1, 1 for supplier2, 2 for supplier3.
     */
    private String mSupplier = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mItemNameEditText = findViewById(R.id.edit_item_name);
        mQuantityEditText = findViewById(R.id.edit_item_quantity);
        mSupplierPhoneEditText = findViewById(R.id.edit_supplier_phone);
        mSupplierNameSpinner = findViewById(R.id.spinner_supplier);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierNameSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_1))) {
                        mSupplier = getString(R.string.supplier_1); // Supplier 1
                    } else if (selection.equals(getString(R.string.supplier_2))) {
                        mSupplier = getString(R.string.supplier_2); // Supplier 2
                    } else {
                        mSupplier = getString(R.string.supplier_3); // Supplier 3
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = "0"; // Unknown
            }
        });
    }

    private void insertPet() {
        String nameString = mItemNameEditText.getText().toString().trim();
        String priceString = mItemPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, priceString);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantityString);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER, mSupplier);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, supplierPhoneString);

        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error saving item!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Item saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
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
                insertPet();
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
}