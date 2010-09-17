package org.bostonandroid.datepreferencetest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.preference.DatePreference;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

public class DatePreferenceActivityTest extends
    ActivityInstrumentationTestCase2<DatePreferenceActivity> {
  
  public DatePreferenceActivityTest() {
    super("org.bostonandroid.datepreferencetest", DatePreferenceActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    setActivityInitialTouchMode(false);
  }
  
  public void testDefaultDateUnset() {
    DatePreferenceActivity activity = getActivity();
    PreferenceManager preferenceManager = activity.getPreferenceManager();
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference("dod");
    Calendar givenDate = datePreference.getDate();
    assertCalendarDateEquals(defaultDate(), givenDate);
  }
  
  public void testDefaultDateFromXml() {
    DatePreferenceActivity activity = getActivity();
    PreferenceManager preferenceManager = activity.getPreferenceManager();
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference("dob");
    Calendar defaultDate = datePreference.getDate();
    Calendar expected = new GregorianCalendar(1991,0,1);
    assertCalendarDateEquals(expected, defaultDate);
  }
  
  public void testDefaultDateForBadDate() {
    DatePreferenceActivity activity = getActivity();
    PreferenceManager preferenceManager = activity.getPreferenceManager();
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference("doa");
    Calendar defaultDate = datePreference.getDate();
    assertCalendarDateEquals(defaultDate(), defaultDate);
  }
  
  public void testDateChanged() {
    DatePreferenceActivity activity = getActivity();
    assertNotNull(activity);
    PreferenceManager preferenceManager = activity.getPreferenceManager();
    assertNotNull(preferenceManager);
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference("dod");
    assertNotNull(datePreference);
    final Dialog datePickerDialog = datePreference.getDialog();
    assertNotNull(datePickerDialog); // XXX: This is null.
    
    activity.runOnUiThread(new Runnable() {
      public void run() {
        datePickerDialog.show();
      }
    });
    sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_CENTER,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_CENTER);
    
    Calendar newDate = datePreference.getDate();
    Calendar expected = defaultDate();
    expected.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
    
    assertCalendarDateEquals(expected, newDate);
  }
  
  private Calendar defaultDate() {
    return new GregorianCalendar(1970, 0, 1);
  }
  
  protected void assertCalendarDateEquals(Calendar expected, Calendar actual) {
    String msg = "Expected: " + formatter().format(expected.getTime()) + " but got: " + formatter().format(actual.getTime());
    assertEquals(msg, expected.get(Calendar.YEAR), actual.get(Calendar.YEAR));
    assertEquals(msg, expected.get(Calendar.MONTH), actual.get(Calendar.MONTH));
    assertEquals(msg, expected.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH));
  }
  
  private SimpleDateFormat formatter() {
    return new SimpleDateFormat("yyyy.MM.dd");
  }
}