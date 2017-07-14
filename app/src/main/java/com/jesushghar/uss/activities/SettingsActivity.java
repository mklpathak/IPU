package com.jesushghar.uss.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.jesushghar.uss.R;

/**
 * Created by PAT on 5/2/2016.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
    }
}
