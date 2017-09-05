package edu.uoregon.richie.tidev3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    // default xml files
    private final static String FILE_1 = "coosbay.xml", FILE_2 = "fulton.xml", FILE_3 = "pointloma.xml";
    // dialog data
    private File directory;
    private String[] files;
    // widgets
    private Spinner stationSpinner,
            dateSpinner;
    private Button tideDataButton;
    // tide data access layer
    private SQLDataAccessLayer sqlDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get widgets
        stationSpinner = (Spinner) findViewById(R.id.stationSpinner);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        tideDataButton = (Button) findViewById(R.id.tideDataButton);
        // check if asset files have already been added to a new database
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = preferences.getBoolean("pref_first_run", true);
        if (isFirstRun) {
            Log.e("MainActivity", "doing initial load");
            final ProgressDialog progress = ProgressDialog.show(this, "", "Importing files into database...", true);
            // wait for app to start before intensive tasks
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream stream = getAssets().open(FILE_1);
                        importFile(stream);
                        stream = getAssets().open(FILE_2);
                        importFile(stream);
                        stream = getAssets().open(FILE_3);
                        importFile(stream);
                    } catch (IOException e) {
                        Log.e("MainActivity", e.toString());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    });
                }
            }, 2500);
        }
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // load list of station names from database
        sqlDAL = new SQLDataAccessLayer(this);
        // setup station names spinner
        Cursor cursor = sqlDAL.getStationNames();
        DatabaseViewAdapter databaseViewAdapter = new DatabaseViewAdapter(this, cursor, SQLDataAccessLayer.STATION_NAME);
        stationSpinner.setAdapter(databaseViewAdapter);
        stationSpinner.setOnItemSelectedListener(this);
        // add listener
        tideDataButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                // import a file from the specified directory
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.dialog_choose_file));
                directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                files = directory.list();
                builder.setItems(files, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String path = directory.getAbsolutePath() + "/" + files[i];
                            FileInputStream stream = new FileInputStream(path);
                            importFile(stream);
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.dialog_failed) + " " + files[i],
                                    Toast.LENGTH_LONG).show();
                            Log.e("Tidev2", e.toString());
                        }
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // load available dates for selected station
        TextView textView = (TextView) view.findViewById(R.id.itemListTextView);
        Cursor cursor = sqlDAL.getStationDates(textView.getText().toString());
        DatabaseViewAdapter databaseViewAdapter = new DatabaseViewAdapter(this, cursor, SQLDataAccessLayer.TIDE_DATE);
        dateSpinner.setAdapter(databaseViewAdapter);
        SOAPHandler soapHandler = new SOAPHandler(this);
        soapHandler.execute("8454000", "20160716 00:00", "20160716 23:59");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }

    @Override
    public void onClick(View view) {
        // get selected station and date
        TextView stationTextView = (TextView) stationSpinner.getSelectedView().findViewById(R.id.itemListTextView);
        TextView dateTextView = (TextView) dateSpinner.getSelectedView().findViewById(R.id.itemListTextView);
        // open tide data activity using selection
        Intent intent = new Intent(this, StationDataActivity.class);
        intent.putExtra("name", stationTextView.getText().toString());
        intent.putExtra("date", dateTextView.getText().toString());
        startActivity(intent);
    }

    private void importFile(InputStream stream) {
        // import station data from xml into database
        StationData stationData;
        try {
            XMLParser xmlParser = new XMLParser();
            stationData = xmlParser.getStationData(stream);
            // copy station data into database
            sqlDAL.addStationData(stationData);
        } catch (Exception e) {
            Log.e("Tide v2", e.toString());
            Toast.makeText(this, getString(R.string.error_xml), Toast.LENGTH_SHORT).show();
        }
        Cursor cursor = sqlDAL.getStationNames();
        DatabaseViewAdapter databaseViewAdapter = new DatabaseViewAdapter(this, cursor, SQLDataAccessLayer.STATION_NAME);
        stationSpinner.setAdapter(databaseViewAdapter);
        stationSpinner.setOnItemSelectedListener(this);
    }

    public void soapFinished() {
        // TODO open loaded tide data
    }
}