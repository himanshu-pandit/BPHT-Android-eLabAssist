package com.bluepearl.dnadiagnostics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeSelection extends BaseActivity implements OnClickListener {

	Button btn_date, btn_time, btn_proceed;
	TextView txtDate, txtTime, lblDate, lblTime;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	Date date1;
	Date date2;

	static String addr = null;
	static String date = null;
	static String dname = null;

	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;

	static final int TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 0;
	String am_pm = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		date = sharedpreferences.getString("datetime", "");

		addr = sharedpreferences.getString("address", "");

		dname = sharedpreferences.getString("doctor", "");

		getLayoutInflater().inflate(R.layout.date_time_selection, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);
		lblDate = (TextView) findViewById(R.id.datelabel);
		lblTime = (TextView) findViewById(R.id.Timelabel);
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtTime = (TextView) findViewById(R.id.txtTime);
		btn_date = (Button) findViewById(R.id.button_date);
		btn_time = (Button) findViewById(R.id.button_time);
		btn_proceed = (Button) findViewById(R.id.buttonProceed);

		lblDate.setTypeface(custom_btnfont);
		lblTime.setTypeface(custom_btnfont);
		txtDate.setTypeface(custom_btnfont);
		txtTime.setTypeface(custom_btnfont);
		btn_proceed.setTypeface(custom_btnfont);

		btn_date.setOnClickListener(this);
		btn_time.setOnClickListener(this);
		btn_proceed.setOnClickListener(this);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		if (c.get(Calendar.AM_PM) == Calendar.AM) {
			am_pm = "AM";
		} else if (c.get(Calendar.AM_PM) == Calendar.PM) {
			am_pm = "PM";
		}
		if (date != null && !(date.equals(""))) {
			String Date1[] = date.split(" ");
			String Date2 = Date1[0];
			String Time2 = Date1[1];
			txtDate.setText(Date2);
			txtTime.setText(Time2);
		} else {

			// display the current date (this method is below)
			GregorianCalendar cal = new GregorianCalendar(mYear, mMonth, mDay);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			txtDate.setText(sdf.format(cal.getTime()));

			updateTime();
		}
		date2 = new Date(mYear, mMonth, mDay);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonProceed:

			if(txtDate.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(),
						"Please select date",
						Toast.LENGTH_LONG).show();
			}
			else if(txtTime.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(),
						"Please select time",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				String dateTime_Val = txtDate.getText().toString() + " "
						+ txtTime.getText().toString();

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("DateTime", dateTime_Val);

				editor.commit();

				Intent i = new Intent(DateTimeSelection.this,BookAppointment.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
			break;
		case R.id.button_date:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.button_time:
			showDialog(TIME_DIALOG_ID);
			break;
		default:
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog da = new DatePickerDialog(this, mDateSetListener,
					mYear, mMonth, mDay);
			da.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
			return da;
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);

		}
		return null;
	}

	// updates the date we display in the TextView
	private void updateDate() {
		date1 = new Date(mYear, mMonth, mDay);
		if (date1.after(date2) || date1.equals(date2)) {
			GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			txtDate.setText(sdf.format(c.getTime()));

		} else {
			LayoutInflater inflater = getLayoutInflater();
		    View layout = inflater.inflate(R.layout.my_dialog,null);

		    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
		    text.setText("     Please Select Future Date     ");
		    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
		    text.setTypeface(typeface);
		    text.setTextColor(Color.WHITE);
		    text.setTextSize(18);
		    Toast toast = new Toast(getApplicationContext());
		    toast.setDuration(Toast.LENGTH_LONG);
		    toast.setView(layout);

		    toast.show();
		}
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDate();
		}
	};

	// updates the time we display in the TextView
	private void updateTime() {
		txtTime.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)).append(" ").append(am_pm));
	}

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		Calendar c = Calendar.getInstance();

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			Calendar datetime = Calendar.getInstance();
			datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			datetime.set(Calendar.MINUTE, minute);
			mHour = hourOfDay;
			mMinute = minute;

			if (datetime.get(Calendar.AM_PM) == Calendar.AM)
				am_pm = "AM";
			else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
				am_pm = "PM";
			updateTime();
		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	protected void onResume() {
		super.onResume();
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		if (c.get(Calendar.AM_PM) == Calendar.AM) {
			am_pm = "AM";
		} else if (c.get(Calendar.AM_PM) == Calendar.PM) {
			am_pm = "PM";
		}
		if (date != null && !(date.equals(""))) {
			String Date1[] = date.split(" ");
			String Date2 = Date1[0];
			String Time2 = Date1[1];
			txtDate.setText(Date2);
			txtTime.setText(Time2);
		} else {

			// display the current date (this method is below)
			GregorianCalendar cal = new GregorianCalendar(mYear, mMonth, mDay);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			txtDate.setText(sdf.format(cal.getTime()));

			updateTime();
		}
		date2 = new Date(mYear, mMonth, mDay);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(DateTimeSelection.this,AddressSelection.class);
		startActivity(i);
		//finish();
	}
}
