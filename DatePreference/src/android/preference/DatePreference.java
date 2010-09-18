package android.preference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

public class DatePreference extends DialogPreference implements DatePicker.OnDateChangedListener {
  protected String defaultValue;
  protected String changedValue;
 
  public DatePreference(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  
  public DatePreference(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  
  @Override
  protected View onCreateDialogView() {
    DatePicker datePicker = new DatePicker(getContext());
    Calendar calendar = getDate();
    datePicker.init(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
        this);
    return datePicker;
  }
  
  public Calendar getDate() {
    try {
      Date date = formatter().parse(this.defaultValue);
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      return cal;
    } catch (java.text.ParseException e) {
      e.printStackTrace();
      return defaultDate();
    }
  }
  
  private SimpleDateFormat formatter() {
    return new SimpleDateFormat("yyyy.MM.dd");
  }
  
  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getString(index);
  }
  
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
  
  public void onDateChanged(DatePicker view, int year, int month, int day) {
    Calendar selected = new GregorianCalendar(year, month, day);
    this.changedValue = formatter().format(selected.getTime());
  }
  
  @Override
  protected void onDialogClosed(boolean shouldSave) {
    if (shouldSave) {
      this.defaultValue = this.changedValue;
    }
  }
  
  private Calendar defaultDate() {
    return new GregorianCalendar(1970, 0, 1);
  }
  
  private String defaultValue() {
    if (this.defaultValue == null) {
      this.defaultValue = formatter().format(defaultDate().getTime());
    }
    return this.defaultValue;
  }
}