package edu.uoregon.richie.tidev3;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StationDataActivity extends AppCompatActivity {
    // widgets
    private TextView stationDataTextView;
    private ListView tideDataListView;
    // tide data access layer
    private SQLDataAccessLayer sqlDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get extras
        String stationName = getIntent().getStringExtra("name");
        String tideDate = getIntent().getStringExtra("date");
        // get widgets
        stationDataTextView = (TextView) findViewById(R.id.stationDataTextView);
        tideDataListView = (ListView) findViewById(R.id.tideDataListView);
        // load database
        sqlDAL = new SQLDataAccessLayer(this);
        // setup text view
        stationDataTextView.setText(sqlDAL.getFullStationName(stationName));
        // setup list view
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
        try {
            // get data from extra and work out next date
            Date date = sdf.parse(tideDate);
            Calendar dateStart = Calendar.getInstance();
            dateStart.setTime(date);
            Calendar dateEnd = (Calendar) dateStart.clone();
            dateEnd.add(Calendar.DAY_OF_MONTH, 1);
            sdf.applyPattern("yyyyMMdd");
            // get tide data from database
            Cursor cursor = sqlDAL.getTideDataRange(stationName,
                    sdf.format(dateStart.getTime()),
                    sdf.format(dateEnd.getTime()));
            // setup adapter
            TideDataViewAdapter tideDataViewAdapter = new TideDataViewAdapter(this, cursor);
            tideDataListView.setAdapter(tideDataViewAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}