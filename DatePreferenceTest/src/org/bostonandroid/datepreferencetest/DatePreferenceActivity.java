package org.bostonandroid.datepreferencetest;

import java.util.Calendar;

import org.bostonandroid.datepreference.DatePreference;

import android.content.Context;
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
  
  // This is used by the test.
  public Bundle getBundle() {
    return savedInstanceState;
  }
  
  // This is used by the test.
  public Calendar getCalendarOfDeath() {
    return DatePreference.getDateFor(preferences(),"dod");
  }
  
  // This is used by the test.
  public Calendar getCalendarOfArrival() {
    return DatePreference.getDateFor(preferences(),"doa");
  }
  
  private SharedPreferences preferences() {
    return PreferenceManager.getDefaultSharedPreferences(this);
  }
  
  protected void onResume() {
    super.onResume();
    
    SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
    getPreference("dod").setDate(prefs.getString("dod", DatePreference.defaultCalendarString()));
  }
  
  protected void onPause() {
    super.onPause();
    
    SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
    editor.putString("dod", DatePreference.formatter().format(getCalendarOfDeath().getTime()));
    editor.commit();
  }
  
  public void clearSharedPreferences() {
    SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
    editor.clear();
    editor.commit();
  }
    
  
  private DatePreference getPreference(String key) {
    return (DatePreference)getPreferenceManager().findPreference(key);
  }
}
