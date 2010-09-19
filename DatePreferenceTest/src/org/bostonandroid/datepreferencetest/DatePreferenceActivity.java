package org.bostonandroid.datepreferencetest;

import java.util.Calendar;
import java.util.Date;

import org.bostonandroid.datepreference.DatePreference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

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

  public Date getDateOfDeath() {
    return DatePreference.getDateFor(preferences(),"dod");
  }
  public Calendar getCalendarOfDeath() {
    return DatePreference.getCalendarFor(preferences(),"dod");
  }
  
  private SharedPreferences preferences() {
    return PreferenceManager.getDefaultSharedPreferences(this);
  }
}
