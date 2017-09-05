package edu.uoregon.richie.brisknote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotesDatabase extends SQLiteOpenHelper {
    // database information
    private static final String DATABASE_NAME = "notes.db",
            TABLE_NAME = "Notes";
    private static final int DATABASE_VERSION = 1;
    // column information
    public static final String ID = "_id", // CursorAdapter
            TITLE = "Title",
            BODY = "Body",
            DATE = "Date";
    // shared date formatter
    private SimpleDateFormat dateFormat;

    public NotesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault());
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT,"
                + BODY + " TEXT,"
                + DATE + " TEXT"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT *"
                + " FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public void addNote(Note note) {
        ContentValues row = new ContentValues();
        row.put(TITLE, note.getTitle());
        row.put(BODY, note.getBody());
        row.put(DATE, dateFormat.format(note.getDate().getTime()));
        SQLiteDatabase db =  this.getWritableDatabase();
        db.insert(TABLE_NAME, null, row);
        db.close();
    }

    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + TITLE + ", " + BODY + ", " + DATE
                + " FROM " + TABLE_NAME
                + " WHERE " + ID + " = ?";
        String[] selection = new String[]{Integer.toString(id)};
        Cursor cursor = db.rawQuery(query, selection);
        // TODO check if note id exists
        cursor.moveToNext();
        Note note = new Note();
        // TODO catch
        note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
        note.setBody(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
        Calendar date = Calendar.getInstance();
        try {
            date.setTime(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(DATE))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        note.setDate(date);
        cursor.close();
        db.close();
        return note;
    }

    public void editNote(Note note, int id) {
        ContentValues row = new ContentValues();
        row.put(TITLE, note.getTitle());
        row.put(BODY, note.getBody());
        row.put(DATE, dateFormat.format(note.getDate().getTime()));
        SQLiteDatabase db = this.getWritableDatabase();
        // TODO check affected rows
        db.update(TABLE_NAME, row, ID + " = " + id, null);
        db.close();
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // TODO check affect rows
        db.delete(TABLE_NAME, ID + " = " + id, null);
        db.close();
    }
}
