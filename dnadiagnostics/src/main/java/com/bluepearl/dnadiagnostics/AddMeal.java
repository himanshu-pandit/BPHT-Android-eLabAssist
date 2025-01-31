package com.bluepearl.dnadiagnostics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.bluepearl.dnadiagnostics.DateTimeSelection.DATE_DIALOG_ID;
import static com.bluepearl.dnadiagnostics.DateTimeSelection.TIME_DIALOG_ID;

public class AddMeal extends AppCompatActivity {
        Button submit;
        DatabaseHelper databaseHelper;

         EditText etname;
         String name ;

    static String AppointmentDateFrom = "0";
    TextView txtDate, txtTime;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    String am_pm = "";
    static String date = null;
    Date date1;
    Date date2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmeal);
        etname = (EditText) findViewById(R.id.etname);
           submit = (Button) findViewById(R.id.submit);


        txtDate = (TextView) findViewById(R.id.txtDate_id);
        txtTime = (TextView) findViewById(R.id.txtTime_id);

        submit = (Button) findViewById(R.id.submit);

        databaseHelper = new DatabaseHelper(this);

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


        txtDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(DATE_DIALOG_ID);
                    }
                }
        );
        txtTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(TIME_DIALOG_ID);
                    }
                }
        );


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etname.getText().toString();


                AppointmentDateFrom = txtDate.getText().toString() + " " + String.valueOf((new StringBuilder().append(pad(mHour)).append(":").append(pad(mMinute))));


                //Toast.makeText(MainActivity.this,name, Toast.LENGTH_SHORT).show();
                if (name.isEmpty() ) {

                    Toast.makeText(AddMeal.this, "please fill details", Toast.LENGTH_SHORT).show();
                } else {

                    databaseHelper.insertdata(name, "" , AppointmentDateFrom, "");
                    etname.setText("");
                    Intent intent = new Intent(AddMeal.this, DiabetesCare.class);
                    startActivity(intent);

                }


            }
        });


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


    private void updateTime() {

        String _24HourTime = String.valueOf((new StringBuilder().append(pad(mHour)).append(":").append(pad(mMinute))));
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");




        Date _24HourDt = null;
        Date d1 =null;

        try {
            _24HourDt = _24HourSDF.parse(_24HourTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(_24HourDt);
        System.out.println(_12HourSDF.format(_24HourDt));


        //Calendar cal = Calendar.getInstance();
        //  cal.setTime(d1);
        // cal.add(Calendar.MINUTE, Integer.parseInt(machin_TimeInterval));


        txtTime.setText(new StringBuilder().append(_12HourSDF.format(_24HourDt)));

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

    // updates the date we display in the TextView
    private void updateDate() {
        date1 = new Date(mYear, mMonth, mDay);
        if (date1.after(date2) || date1.equals(date2)) {
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            txtDate.setText(sdf.format(c.getTime()));

        } else {

            Toast.makeText(this, " Please Select Future Date", Toast.LENGTH_SHORT).show();

        }
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
