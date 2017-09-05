package edu.uoregon.richie.tidev3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SQLDataAccessLayer extends SQLiteOpenHelper {
    // database information
    public static final String DATABASE_NAME = "tides.db",
            TABLE_NAME = "Tides",
            ID = "_id",
            STATION_NAME = "StationName",
            STATION_STATE = "StationState",
            STATION_ID = "StationId",
            TIDE_DATE = "TideDate",
            TIDE_TIME = "TideTime",
            TIDE_VALUE_FT = "TideValueFt",
            TIDE_VALUE_CM = "TideValueCm",
            TIDE_TYPE = "TideType";
    private static final int DATABASE_VERSION = 1;

    public SQLDataAccessLayer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STATION_NAME + " TEXT,"
                + STATION_STATE + " TEXT,"
                + STATION_ID + " TEXT,"
                + TIDE_DATE + " TEXT,"
                + TIDE_TIME + " TEXT,"
                + TIDE_VALUE_FT + " REAL,"
                + TIDE_VALUE_CM + " REAL,"
                + TIDE_TYPE + " TEXT"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addStationData(StationData stationData) {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();
        // copy station data into database
        ContentValues content = new ContentValues();
        for (TideData tideData : stationData.getTideData()) {
            content.put(STATION_NAME, stationData.getStationName());
            content.put(STATION_STATE, stationData.getStationState());
            content.put(STATION_ID, stationData.getStationId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            int date = Integer.parseInt(sdf.format(tideData.getDate().getTime()));
            content.put(TIDE_DATE, date);
            sdf.applyPattern("HHmm");
            int time = Integer.parseInt(sdf.format(tideData.getDate().getTime()));
            content.put(TIDE_TIME, time);
            content.put(TIDE_VALUE_FT, tideData.getValueFt());
            content.put(TIDE_VALUE_CM, tideData.getValueCm());
            String tide = "";
            switch (tideData.getTideType()) {
                case LOW: tide = "LOW";
                    break;
                case HIGH: tide = "HIGH";
                    break;
            }
            content.put(TIDE_TYPE, tide);
            // check if tide data entry already exists
            String query = "SELECT " + ID
                    + " FROM " + TABLE_NAME
                    + " WHERE " + STATION_ID + " = ? AND " + TIDE_DATE + " = ? AND " + TIDE_TIME + " = ?";
            String[] selection = new String[]{stationData.getStationId(), Integer.toString(date), Integer.toString(time)};
            Cursor cursor = database.rawQuery(query, selection);
            cursor.moveToNext();
            if (cursor.getCount() == 1) content.put(ID, cursor.getString(cursor.getColumnIndexOrThrow(ID)));
            // replace old entry if it exists
            database.insertWithOnConflict(TABLE_NAME, null, content, SQLiteDatabase.CONFLICT_REPLACE);
            cursor.close();
        }
        database.close();
    }

    public Cursor getStationNames() {
        // open database
        SQLiteDatabase database = this.getReadableDatabase();
        // get list of station names in database
        String query = "SELECT " + ID + ", " + STATION_NAME
                + " FROM " + TABLE_NAME
                + " GROUP BY " + STATION_NAME;
        return database.rawQuery(query, null);
    }

    public String getFullStationName(String stationName) {
        // open database
        SQLiteDatabase database = this.getReadableDatabase();
        // get full station name
        String query = "SELECT DISTINCT " + STATION_NAME + ", " + STATION_STATE + ", " + STATION_ID +
                " FROM " + TABLE_NAME +
                " WHERE " + STATION_NAME + " = ?";
        String[] selection = new String[]{stationName};
        Cursor cursor = database.rawQuery(query, selection);
        // build full name
        cursor.moveToNext();
        String fullStationName = cursor.getString(cursor.getColumnIndexOrThrow(STATION_NAME))
                + ", " + cursor.getString(cursor.getColumnIndexOrThrow(STATION_STATE))
                + " (" + cursor.getString(cursor.getColumnIndexOrThrow(STATION_STATE)) + ")";
        cursor.close();
        return fullStationName;
    }

    public Cursor getStationDates(String stationName) {
        // open database
        SQLiteDatabase database = this.getReadableDatabase();
        // get dates available for given station
        String query = "SELECT " + ID + ", " + TIDE_DATE
                + " FROM " + TABLE_NAME
                + " WHERE " + STATION_NAME + " = ?"
                + " GROUP BY " + TIDE_DATE
                + " ORDER BY " + TIDE_DATE + " ASC";
        String[] selection = new String[]{stationName};
        return database.rawQuery(query, selection);
    }

    public Cursor getTideDataRange(String stationName, String startDate, String endDate) {
        // open database
        SQLiteDatabase database = this.getReadableDatabase();
        // get dates in specified range for given station
        String query = "SELECT *"
                + " FROM " + TABLE_NAME
                + " WHERE " + STATION_NAME + " = ? AND " + TIDE_DATE + " BETWEEN ? AND ?"
                + " ORDER BY " + TIDE_DATE + " ASC";
        String[] selection = new String[]{stationName, startDate, endDate};
        return database.rawQuery(query, selection);
    }
}