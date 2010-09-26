package org.bostonandroid.datepreferencetest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bostonandroid.datepreference.DatePreference;

import android.content.DialogInterface;
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
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference("doa");
    Calendar givenDate = datePreference.getDate();
    assertCalendarDateEquals(defaultDate(), givenDate);
    String defaultSummary = (String)datePreference.getSummary();
    assertNull("expected null but got " + defaultSummary, defaultSummary);
  }
  
  public void testDefaultDateFromXml() {
    DatePreferenceActivity activity = getActivity();
    PreferenceManager preferenceManager = activity.getPreferenceManager();
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference("dob");
    Calendar defaultDate = datePreference.getDate();
    String newDateSummary = (String)datePreference.getSummary();
    assertNotNull(newDateSummary);
    Calendar expected = new GregorianCalendar(1991,0,1);
    assertCalendarDateEquals(expected, defaultDate);
    assertSummary(expected, newDateSummary);
  }
  
  public void testDefaultDateForBadDate() {
    DatePreferenceActivity activity = getRestoredActivity();
    PreferenceManager preferenceManager = activity.getPreferenceManager();
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference("doa");
    Calendar defaultDate = datePreference.getDate();
    assertCalendarDateEquals(defaultDate(), defaultDate);
  }
  
  public void testDateChanged() {
    DatePreferenceActivity activity = getRestoredActivity();
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
    String newDateSummary = getDatePreference(activity).getSummary().toString();
    
    assertCalendarDateEquals(expected, newDate);
    assertSummary(expected, newDateSummary);
  }
  
  public void testDateCanceled() {
    DatePreferenceActivity activity = getRestoredActivity();
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
    DatePreferenceActivity activity = getRestoredActivity();
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
    DatePreferenceActivity activity = getRestoredActivity();
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
    DatePreferenceActivity activity = getRestoredActivity();
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

    Calendar expected = defaultDate();
    expected.add(Calendar.DAY_OF_MONTH, 1);
    assertCalendarDateEquals(expected, activity.getCalendarOfDeath());
  }
  
  public void testDestroyedAndPresistence() {
    DatePreferenceActivity activity = getRestoredActivity();
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
    activateDoaPreference(); // android:persistent=false
    // increment the day
    sendKeys(KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_CENTER);
    pressOK();
    activity.finish();
    activity = getActivity();
 
    Calendar expected = defaultDate();
    expected.add(Calendar.DAY_OF_MONTH, 1);

    assertCalendarDateEquals(expected, getDatePreference(activity, "dod").getDate());
//    assertCalendarDateEquals(defaultDate(), getDatePreference(activity, "doa").getDate()); // not yet possible to test
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
  
  protected void assertSummary(Calendar expected, String actual) {
    assertEquals(summaryFormatter().format(expected.getTime()), actual);
  }

  
  private SimpleDateFormat formatter() {
    return new SimpleDateFormat("yyyy.MM.dd");
  }
  
  private SimpleDateFormat summaryFormatter() {
    return new SimpleDateFormat("MMMM dd, yyyy");
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
  
  private void activateDoaPreference() {
    sendKeys(KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_CENTER);
  }
  
  private DatePreference getDatePreference(DatePreferenceActivity activity, String field) {
    PreferenceManager preferenceManager = activity.getPreferenceManager();
    DatePreference datePreference = (DatePreference)preferenceManager.findPreference(field);
    return datePreference;
  }
  
  private DatePreference getDatePreference(DatePreferenceActivity activity) {
    return getDatePreference(activity, "dod");
  }
  
  private DatePreferenceActivity getRestoredActivity() {
    DatePreferenceActivity a = getActivity();
 //   a.clearSharedPreferences();
    getDatePreference(a,"dob").setDate("1970.01.01");
    getDatePreference(a,"dod").setDate("1970.01.01");
    getDatePreference(a,"doa").setDate("1970.01.01");
    return a;
  }
}