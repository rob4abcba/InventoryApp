package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDbHelper extends SQLiteOpenHelper {
    private static final String DATABESE_NAME = "inventory.db";

    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context){
        super(context, DATABESE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + "("
                + InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE + " DOUBLE NOT NULL DEFAULT 0.00, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER + " INTEGER NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
