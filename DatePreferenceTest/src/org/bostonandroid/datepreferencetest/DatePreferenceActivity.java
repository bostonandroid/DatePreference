package org.bostonandroid.datepreferencetest;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DatePreferenceActivity extends PreferenceActivity {
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
  }
}
