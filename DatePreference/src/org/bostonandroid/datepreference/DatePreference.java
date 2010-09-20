package org.bostonandroid.datepreference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

public class DatePreference extends DialogPreference implements
    DatePicker.OnDateChangedListener {
  private String defaultValue;
  private String changedValueCanBeNull;
  private DatePicker datePicker;

  public DatePreference(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public DatePreference(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Produces a DatePicker set to the date produced by {@link getDate}. When
   * overriding be sure to call the super.
   * 
   * @return a DatePicker with the date set
   */
  @Override
  protected View onCreateDialogView() {
    this.datePicker = new DatePicker(getContext());
    Calendar calendar = getDate();
    datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH), this);
    return datePicker;
  }

  /**
   * Produces the date used for the date picker. If the user has not selected a
   * date, produces the default from the XML's android:defaultValue. If the
   * default is not set in the XML or if the XML's default is invalid it uses
   * the value produced by {@link defaultDate}.
   * 
   * @return the Calendar for the date picker
   */
  public Calendar getDate() {
    try {
      Date date = formatter().parse(defaultValue());
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      return cal;
    } catch (java.text.ParseException e) {
      e.printStackTrace();
      return defaultDate();
    }
  }

  /**
   * Produces the date formatter used for all dates. The default is yyyy.MM.dd.
   * Override this to change that.
   * 
   * @return the SimpleDateFormat used for all dates
   */
  public static SimpleDateFormat formatter() {
    return new SimpleDateFormat("yyyy.MM.dd");
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getString(index);
  }

  /**
   * Called when the date picker is shown or restored. If it's a restore it gets
   * the persisted value, otherwise it persists the value.
   */
  @Override
  protected void onSetInitialValue(boolean restoreValue, Object def) {
    if (restoreValue) {
      this.defaultValue = getPersistedString(defaultValue());
    } else {
      String value = (String) def;
      this.defaultValue = value;
      persistString(value);
    }
  }

  /**
   * Called when the user changes the date.
   */
  public void onDateChanged(DatePicker view, int year, int month, int day) {
    Calendar selected = new GregorianCalendar(year, month, day);
    this.changedValueCanBeNull = formatter().format(selected.getTime());
  }

  /**
   * Called when the dialog is closed. If the close was by pressing "OK" it
   * saves the value.
   */
  @Override
  protected void onDialogClosed(boolean shouldSave) {
    if (shouldSave && this.changedValueCanBeNull != null) {
      this.defaultValue = this.changedValueCanBeNull;
      this.changedValueCanBeNull = null;
    }
  }

  /**
   * The default date to use when the XML does not set it or the XML has an
   * error.
   * 
   * @return the Calendar set to the default date
   */
  public static Calendar defaultDate() {
    return new GregorianCalendar(1970, 0, 1);
  }

  /**
   * The defaultDate() as a string using the {@link formatter}.
   * 
   * @return a String representation of the default date
   */
  public static String defaultDateString() {
    return formatter().format(defaultDate().getTime());
  }

  private String defaultValue() {
    if (this.defaultValue == null) {
      this.defaultValue = defaultDateString();
    }
    return this.defaultValue;
  }

  /**
   * Called whenever the user clicks on a button. Invokes {@link onDateChanged} and
   * {@link onDialogClosed}. Be sure to call the super when overriding.
   */
  @Override
  public void onClick(DialogInterface dialog, int which) {
    super.onClick(dialog, which);
    datePicker.clearFocus();
    onDateChanged(datePicker, datePicker.getYear(), datePicker.getMonth(),
        datePicker.getDayOfMonth());
    onDialogClosed(which == DialogInterface.BUTTON1); // OK?
  }

  /**
   * Produces the date the user has selected for the given preference. If there
   * are any internal errors it produces the {@link defaultDate} instead.
   * 
   * @param preferences
   *          the SharedPreferences to get the date from
   * @param name
   *          the name of the preference to get the date from
   * @return a Date that the user has selected
   */
  public static Date getDateFor(SharedPreferences preferences, String field) {
    return stringToDate(preferences.getString(field, defaultDateString()));
  }

  /**
   * Produces the date the user has selected for the given preference, as a
   * calendar.
   * 
   * @param preferences
   *          the SharedPreferences to get the date from
   * @param name
   *          the name of the preference to get the date from
   * @return a Calendar that the user has selected
   */
  public static Calendar getCalendarFor(SharedPreferences preferences,
      String field) {
    Date date = getDateFor(preferences, field);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }
  
  public void setDate(String dateString) {
    this.defaultValue = dateString;
  }
  
  private static Date stringToDate(String dateString) {
    try {
      return formatter().parse(dateString);
    } catch (ParseException e) {
      return defaultDate().getTime();
    }
  }
}