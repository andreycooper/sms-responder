package com.weezlabs.smsresponder;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        EditTextPreference phoneNumbers = (EditTextPreference) findPreference(getString(R.string.key_preference_phone_number));
        phoneNumbers.setSummary(String.format(getString(R.string.summary_preference_phone_number),
                getString(R.string.delimiter_preference_phone_number)));
    }

}