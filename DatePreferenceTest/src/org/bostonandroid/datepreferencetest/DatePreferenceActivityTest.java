package org.bostonandroid.datepreferencetest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.DialogInterface;
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
    
    activateDodPreference();
    
    // increment the day
    sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_CENTER);

    pressOK();
    
    Calendar newDate = getDatePreference(activity).getDate();
    Calendar expected = defaultDate();
    expected.add(Calendar.DAY_OF_MONTH, 1);
    
    assertCalendarDateEquals(expected, newDate);
  }
  
  public void testDateCanceled() {
    DatePreferenceActivity activity = getActivity();
    assertNotNull(activity);
    
    activateDodPreference();

    // increment the day
    sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_CENTER);
    
    pressCancel();
    
    Calendar newDate = getDatePreference(activity).getDate();
    Calendar expected = defaultDate();
    
    assertCalendarDateEquals(expected, newDate);
  }
  
  public void testDateEdited() {
    DatePreferenceActivity activity = getActivity();
    assertNotNull(activity);
    
    activateDodPreference();

    // change the day
    sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DEL,
        KeyEvent.KEYCODE_DEL,
        KeyEvent.KEYCODE_0,
        KeyEvent.KEYCODE_2);


    final DatePreference datePreference = getDatePreference(activity);
    activity.runOnUiThread(new Runnable() {
      public void run() {
        clickOK(datePreference);
      }
    });
    getInstrumentation().waitForIdleSync();
    
    Calendar newDate = datePreference.getDate();
    Calendar expected = defaultDate();
    expected.add(Calendar.DAY_OF_MONTH, 1);
    
    assertCalendarDateEquals(expected, newDate);
  }
  
  public void testCancelThenOK() {
    DatePreferenceActivity activity = getActivity();
    assertNotNull(activity);
    
    activateDodPreference();

    // increment the day
    sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_CENTER);
    
    pressCancel();
    activateDodPreference();
    pressOK();
    activateDodPreference();
    pressOK();

    Calendar newDate = getDatePreference(activity).getDate();
    Calendar expected = defaultDate();
    
    assertCalendarDateEquals(expected, newDate);
  }
  
  public void testSharedPreferences() {
    DatePreferenceActivity activity = getActivity();
    assertNotNull(activity);
    
    activateDodPreference();
    // increment the day
    sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_CENTER);
    pressOK();

    assertDateEquals(defaultDate().getTime(), activity.getDateOfDeath());
    assertCalendarDateEquals(defaultDate(), activity.getCalendarOfDeath());
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
  
  protected void assertDateEquals(Date expected, Date actual) {
    String msg = "Expected: " + formatter().format(expected.getTime()) + " but got: " + formatter().format(actual.getTime());
    assertEquals(msg, expected.getYear(), actual.getYear());
    assertEquals(msg, expected.getMonth(), actual.getMonth());
    assertEquals(msg, expected.getDay(), actual.getDay());
  }

  
  private SimpleDateFormat formatter() {
    return new SimpleDateFormat("yyyy.MM.dd");
  }
  
  private void pressOK() {
    sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_LEFT,
        KeyEvent.KEYCODE_DPAD_CENTER);
  }
  
  // actually different than pressing OK
  // must run within the UI thread
  private void clickOK(DatePreference dialog) {
    dialog.onClick(null, DialogInterface.BUTTON1);
  }
  
  private void pressCancel() {
      sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
          KeyEvent.KEYCODE_DPAD_DOWN,
          KeyEvent.KEYCODE_DPAD_DOWN,
          KeyEvent.KEYCODE_DPAD_RIGHT,
          KeyEvent.KEYCODE_DPAD_CENTER);
  }
  
  private void activateDodPreference() {
    sendKeys(KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_CENTER);
  }
  
  private DatePreference getDatePreference(DatePreferenceActivity activity) {
    PreferenceManager preferenceManager = activity.getPreferenceManager();
    assertNotNull(preferenceManager);
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference("dod");
    assertNotNull(datePreference);
    return datePreference;
  }
}