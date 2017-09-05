package edu.uoregon.richie.brisknote;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    // preference keys
    public static final String KEY_START_NEW_NOTE = "pref_start_new_note_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
