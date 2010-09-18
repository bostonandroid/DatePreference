package org.bostonandroid.datepreferencetest;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DatePreferenceActivity extends PreferenceActivity {
  private Bundle savedInstanceState;
  
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.savedInstanceState = savedInstanceState;
    addPreferencesFromResource(R.xml.preferences);
  }
  
  public Bundle getBundle() {
    return savedInstanceState;
  }
}
