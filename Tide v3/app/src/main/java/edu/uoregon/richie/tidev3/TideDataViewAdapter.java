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

public class TideDataViewAdapter extends CursorAdapter {
    public TideDataViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_tide_data, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // set line 1
        TextView textView1 = (TextView) view.findViewById(R.id.itemTideDataTextView1);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            Date date = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("TideDate")));
            sdf.applyPattern("yyyy/MM/dd EEEE");
            String text = sdf.format(date);
            textView1.setText(text);
        } catch (ParseException e) {
            Log.e("TideDataViewAdapter", e.toString());
        }
        // set line 2
        TextView textView2 = (TextView) view.findViewById(R.id.itemTideDataTextView2);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.getDefault());
            Date date = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("TideTime")));
            sdf.applyPattern("hh:mm a");
            String text = sdf.format(date)
                    + ", " + cursor.getDouble(cursor.getColumnIndexOrThrow("TideValueFt")) + " ft"
                    + " " + cursor.getString(cursor.getColumnIndexOrThrow("TideType"))
                    + " (" + cursor.getDouble(cursor.getColumnIndexOrThrow("TideValueCm")) + " cm)";
            textView2.setText(text);
        } catch (ParseException e) {
            Log.e("TideDataViewAdapter", e.toString());
        }
    }
}