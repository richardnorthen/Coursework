package edu.uoregon.richie.brisknote;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity implements TextWatcher {
    // widgets
    private EditText titleEditText,
            bodyEditText;
    private TextView dateTextView;
    // notes database
    private NotesDatabase notesDB;
    // current note
    private Note note;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        // get widgets
        titleEditText = (EditText) findViewById(R.id.noteTitleEditText);
        bodyEditText = (EditText) findViewById(R.id.noteBodyEditText);
        dateTextView = (TextView) findViewById(R.id.noteDateTextView);
        // set listeners
        titleEditText.addTextChangedListener(this);
        bodyEditText.addTextChangedListener(this);
        // load database
        notesDB = new NotesDatabase(this);
        // setup current note
        noteId = getIntent().getIntExtra("id", -1);
        if (noteId == -1) {
            // create new note
            note = new Note();
        } else {
            // load note from database
            note = notesDB.getNote(noteId);
            titleEditText.setText(note.getTitle());
            bodyEditText.setText(note.getBody());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d yyyy", Locale.getDefault());
        String date = "Created: " + dateFormat.format(note.getDate().getTime());
        dateTextView.setText(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (noteId == -1) {
            return false;
        }
        getMenuInflater().inflate(R.menu.actionbar_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_note:
                // prompt user to delete note
                AlertDialog confirmDeleteDialog = new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.delete_confirm))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // remove note and update adapter
                                notesDB.deleteNote(noteId);
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null).show();
                return true;
            case android.R.id.home:
                setResult(MainActivity.START_NEW_NOTE);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // auto-save note when activity is closed
        if (noteId == -1 && (!note.getTitle().trim().isEmpty() || !note.getBody().trim().isEmpty())) {
            // if the new note isn't blank, save it
            notesDB.addNote(note);
        } else {
            // update the note in the database
            notesDB.editNote(note, noteId);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(MainActivity.START_NEW_NOTE);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // nothing
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // automatically update note with edit text data
        if (editable == titleEditText.getEditableText()) {
            note.setTitle(editable.toString());
        } else if (editable == bodyEditText.getEditableText()) {
            note.setBody(editable.toString());
        }
    }
}
