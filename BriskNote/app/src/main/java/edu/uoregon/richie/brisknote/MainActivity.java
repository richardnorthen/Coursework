package edu.uoregon.richie.brisknote;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    // widgets
    private ListView notesListView;
    // adapter
    private NotesListAdapter notesListAdapter;
    // notes database
    private NotesDatabase notesDB;
    // flag for first launch
    public static final int START_NEW_NOTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // skip to new note according to preference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean(SettingsActivity.KEY_START_NEW_NOTE, false)) {
            Intent intent = new Intent(this, NoteActivity.class);
            startActivityForResult(intent, START_NEW_NOTE);
        }
        // load default preferences if none exist
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // get widgets
        notesListView = (ListView) findViewById(R.id.notesListView);
        // load database
        notesDB = new NotesDatabase(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set list adapter
        notesListAdapter = new NotesListAdapter(this, notesDB.getNotes());
        notesListView.setAdapter(notesListAdapter);
        notesListView.setOnItemClickListener(this);
        notesListView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_note:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // open note in note activity
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("id", (int) id);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int noteId = (int) id;
        // prompt user to delete note
        AlertDialog confirmDeleteDialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_confirm))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // remove note and update adapter
                        notesDB.deleteNote(noteId);
                        notesListAdapter.changeCursor(notesDB.getNotes());
                    }
                })
                .setNegativeButton(getString(R.string.no), null).show();
        return true;
    }
}
