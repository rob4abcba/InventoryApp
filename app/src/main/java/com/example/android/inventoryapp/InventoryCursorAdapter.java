package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.quantity);
        Button saleButton = view.findViewById(R.id.sale_button);
        Button decrementButton = view.findViewById(R.id.decrease_button);
        Button incrementButton = view.findViewById(R.id.increase_button);

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);
        Double productPrice = cursor.getDouble(priceColumnIndex);
        String productPriceString = Double.toString(productPrice);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQuantity == 0) {
                    //TODO: FIX Toast message.
                    //Toast.makeText(this, getString(R.string.quantity_too_small),Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Put in code to decrement the quantity.
                }
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Put in code to increment the quantity.
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQuantity == 0) {
                    //TODO: FIX Toast message.
                    //Toast.makeText(this, getString(R.string.quantity_too_small),Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Put in code to decrement the quantity.
                }
            }
        });

        nameTextView.setText(productName);
        priceTextView.setText("$" + productPriceString);
        quantityTextView.setText("Quantity: " + productQuantity);
    }
}
