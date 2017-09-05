package edu.uoregon.richie.brisknote;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NotesListAdapter extends CursorAdapter {
    private int maxTitleChars = 20,
            maxBodyChars = 30;

    public NotesListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public long getItemId(int position) {
        // return id of column in database
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndexOrThrow(NotesDatabase.ID));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // create new view from resource
        return LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // show note title
        TextView titleTextView = (TextView) view.findViewById(R.id.noteListTitleTextView);
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesDatabase.TITLE));
        if (title.length() > maxTitleChars) {
            title = title.substring(0, maxTitleChars) + " ...";
        }
        titleTextView.setText(title);
        // show note body
        TextView bodyTextView = (TextView) view.findViewById(R.id.noteListBodyTextView);
        String body = cursor.getString(cursor.getColumnIndexOrThrow(NotesDatabase.BODY));
        if (body.length() > maxBodyChars) {
            body = body.substring(0, maxBodyChars) + " ...";
        }
        if (body.contains("\n")) {
            body = body.substring(0, body.indexOf("\n")) + " ...";
        }
        bodyTextView.setText(body);
    }
}
