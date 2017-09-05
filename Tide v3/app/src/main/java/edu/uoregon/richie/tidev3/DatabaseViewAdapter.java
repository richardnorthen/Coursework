package edu.uoregon.richie.tidev3;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseViewAdapter extends CursorAdapter {
    // column to display
    private String column;

    public DatabaseViewAdapter(Context context, Cursor cursor, String column) {
        // constructor
        super(context, cursor, 0);
        this.column = column;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // use item_list layout
        return LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // put column text into text view
        TextView itemListTextView = (TextView) view.findViewById(R.id.itemListTextView);
        String text = "";
        // check if special formatting is needed
        if (column.equals("TideDate")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            Date date = new Date();
            try {
                date = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(column)));
            } catch (ParseException e) {
                Log.e("DatabaseViewAdapter", e.toString());
            }
            sdf.applyPattern("MMM dd yyyy");
            text = sdf.format(date);
        } else {
            text = cursor.getString(cursor.getColumnIndexOrThrow(column));
        }
        itemListTextView.setText(text);
    }
}