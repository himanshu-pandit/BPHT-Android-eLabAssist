package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

public class LabDetails extends Activity {

	TextView txtcall,lbl_lab,lbl_time,txtime,txtaddr,txtlocation;
	TextView txtMon,txtTue,txtWed,txtThur,txtFri,txtSat,txtSun;
	TextView txtMonTime,txtTueTime,txtWedTime,txtThurTime,txtFriTime,txtSatTime,txtSunTime;

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		setContentView(R.layout.activity_lab_details);

		txtcall = (TextView)findViewById(R.id.call);
		lbl_lab = (TextView)findViewById(R.id.txtviewlab);
		lbl_time = (TextView)findViewById(R.id.text_id99);
		txtaddr = (TextView)findViewById(R.id.text_id999999);
		txtlocation = (TextView)findViewById(R.id.locationtxt);
		
		txtMon = (TextView)findViewById(R.id.textView1);
		txtTue = (TextView)findViewById(R.id.textView2);
		txtWed = (TextView)findViewById(R.id.textView3);
		txtThur = (TextView)findViewById(R.id.textView4);
		txtFri = (TextView)findViewById(R.id.textView5);
		txtSat = (TextView)findViewById(R.id.textView6);
		txtSun = (TextView)findViewById(R.id.textView7);
		
		txtMonTime = (TextView)findViewById(R.id.textView11);
		txtTueTime = (TextView)findViewById(R.id.textView21);
		txtWedTime = (TextView)findViewById(R.id.textView31);
		txtThurTime = (TextView)findViewById(R.id.textView41);
		txtFriTime = (TextView)findViewById(R.id.textView51);
		txtSatTime = (TextView)findViewById(R.id.textView61);
		txtSunTime = (TextView)findViewById(R.id.textView71);
		
		lbl_lab.setTypeface(custom_bold_font);
		txtcall.setTypeface(custom_bold_font);
		txtlocation.setTypeface(custom_bold_font);
		lbl_time.setTypeface(custom_bold_font);
		txtaddr.setTypeface(custom_font);
		
		txtMon.setTypeface(custom_font);
		txtTue.setTypeface(custom_font);
		txtWed.setTypeface(custom_font);
		txtThur.setTypeface(custom_font);
		txtFri.setTypeface(custom_font);
		txtSat.setTypeface(custom_font);
		txtSun.setTypeface(custom_font);
		
		txtMonTime.setTypeface(custom_font);
		txtTueTime.setTypeface(custom_font);
		txtWedTime.setTypeface(custom_font);
		txtThurTime.setTypeface(custom_font);
		txtFriTime.setTypeface(custom_font);
		txtSatTime.setTypeface(custom_font);
		txtSunTime.setTypeface(custom_font);
		
		Intent myIntent = getIntent();
		if(myIntent != null)
		{
			String name = myIntent.getStringExtra("LabAddress");
			txtaddr.setText(name);
		}

		PhoneCallListener phoneCallListener = new PhoneCallListener();
		TelephonyManager telManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(phoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);

		txtcall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String number = txtcall.getText().toString();//callIntent.setData(Uri.parse("tel:"+phone_no));
				Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
				phoneCallIntent.setData(Uri.parse("tel:"+number));
				startActivity(phoneCallIntent);
			}
		});
	}

	@Override
	public void onBackPressed() 
	{
		Intent i1 = new Intent(LabDetails.this,LabSelection.class);
		//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i1);
	}

	// monitor phone call states
	private class PhoneCallListener extends PhoneStateListener {

		String TAG = "LOGGING PHONE CALL";

		private boolean phoneCalling = false;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(TAG, "RINGING, number: " + incomingNumber);
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(TAG, "OFFHOOK");

				phoneCalling = true;
			}

			// When the call ends launch the main activity again
			if (TelephonyManager.CALL_STATE_IDLE == state) {

				Log.i(TAG, "IDLE");

				if (phoneCalling) {

					Log.i(TAG, "restart app");

					// restart app
					Intent i = getBaseContext().getPackageManager()
							.getLaunchIntentForPackage(
									getBaseContext().getPackageName());

					//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);

					phoneCalling = false;
				}

			}
		}
	}
}
