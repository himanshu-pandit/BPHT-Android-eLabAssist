package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Manual_OTP extends Activity {

	Button btnsubmit;
	EditText Edit_OTPCode;
	//TextView txtResend;
	int MaxResend = 0;
	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;
	static String otp_val = null;

	private ProgressDialog pDialog;
	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";
	static String finalResult = "";
	static String status = "";
	static String mResadd = "";
	static String mOffadd = "";
	static String mPatient_id = "";
	static String mMobile_number = "";
	static String mPassword = "";
	static String mDeviceID = "";
	static String mPname = "";
	static String mPgender = "";
	static String mEmail = "";
	static String mDOB = "";
	static String flgFromEmail = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_manual_otp);

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		mMobile_number = sharedpreferences.getString("MobileNumber", "");
		mPassword = sharedpreferences.getString("Password", "");
		mDeviceID = sharedpreferences.getString("DeviceID", "");
		mPname = sharedpreferences.getString("self_patientName", "");
		mPgender = sharedpreferences.getString("self_patientgender", "");
		mDOB = sharedpreferences.getString("self_patientDOB", "");
		mEmail = sharedpreferences.getString("self_patientEmail", "");

		//txtResend = (TextView) findViewById(R.id.Manual_resend_Otp);
		Edit_OTPCode = (EditText) findViewById(R.id.Manual_Otp_Code);
		otp_val = Edit_OTPCode.getText().toString();

		Intent myIntent = getIntent();
		if(myIntent != null)
		{
			flgFromEmail = myIntent.getStringExtra("isFromValidEmail");
		}

		btnsubmit = (Button) findViewById(R.id.SubmitButton);
		btnsubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				otp_val = Edit_OTPCode.getText().toString();
				if (Edit_OTPCode.getText().toString().equalsIgnoreCase("")) {
					
					LayoutInflater inflater = getLayoutInflater();
				    View layout = inflater.inflate(R.layout.my_dialog,null);

				    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				    text.setText("     Please Enter the OTP     ");
				    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
				    text.setTypeface(typeface);
				    text.setTextColor(Color.WHITE);
				    text.setTextSize(18);
				    Toast toast = new Toast(getApplicationContext());
				    toast.setDuration(Toast.LENGTH_LONG);
				    toast.setView(layout);

				    toast.show();
				    
					/*Toast.makeText(getApplicationContext(),
							"Please Enter the OTP", Toast.LENGTH_SHORT).show();*/
				} else {
					if (isInternetOn(getApplicationContext())) {
						new numberVerfication().execute(new String[] { url });
					} else {
						alertBox();
					}
				}

			}
		});
	}

	private class SendNumber extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(Manual_OTP.this);
			pDialog.setMessage("Please wait... Sending OTP");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return MobilePOST(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result.equalsIgnoreCase("fail") || result.equalsIgnoreCase("")) {
				if (pDialog != null && pDialog.isShowing())
				{
					pDialog.dismiss();
				}
				showAlert("Sorry we fail to send OTP please try again");
			} else if (result.equalsIgnoreCase("User Already Exist.")) {
				if (pDialog != null && pDialog.isShowing())
				{
					pDialog.dismiss();
				}
				showAlert("Number is already exist");
			} else if (result.equalsIgnoreCase("OTP_Resent")) {
				if (pDialog != null && pDialog.isShowing())
				{
					pDialog.dismiss();
				}
			}
		}
	}

	public static String MobilePOST(String url) {
		InputStream inputStream = null;
		String result = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"1\""));
			if(flgFromEmail != null || !(flgFromEmail.equalsIgnoreCase("")) && flgFromEmail.equals("Yes"))
			{
				nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\""+ mEmail + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\""+ mMobile_number + "\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"EmailID\"", "\""+ mEmail + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Password\"", "\""+ mPassword + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"MobileDeviceID\"", "\""+ mDeviceID + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Name\"", "\""+ mPname + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Gender\"", "" + mPgender + ""));
			if(mDOB != null)
			{
				nameValuePairs.add(new BasicNameValuePair("\"DOB\"", "\"" + mDOB + "\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"DOB\"", "\"\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			finalString = "{" + finalString + "}";
			String tempString = "\"objSP\"" + ":";
			String strToServer = tempString + finalString;
			String strToServer1 = "{" + strToServer + "}";

			StringEntity se = new StringEntity(strToServer1);
			se.setContentType("application/json;charset=UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
			httpPost.setEntity(se);

			// Execute POST request to the given URL
			HttpResponse httpResponse = httpClient.execute(httpPost);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				String jsonformattString = result.replaceAll("\\\\", "");
				try {
					if (jsonformattString != null) {
						JSONObject jsonObj = new JSONObject(jsonformattString);
						JSONObject resultObj = (JSONObject) jsonObj.get("d");
						status = resultObj.getString("Result");
						// Code = resultObj.getString("Data");
					} else {
						status = "fail";
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				status = "Did not work!";
		} catch (Exception e) {
			// Log.i("InputStream", e.getLocalizedMessage());
		}
		return status;
	}

	private class numberVerfication extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Manual_OTP.this);
			pDialog.setMessage("Verifying...Please wait");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return POST(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			/*
			 * if (pDialog.isShowing()) { pDialog.dismiss(); }
			 */
			if (result.equals("OTP is Not Matched.")) {
				showAlert("OTP is not matched");
			} else if (result.equals("Success.")) {

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("patientId", mPatient_id);
				editor.putString("self", "yes");
				editor.putString("phno", mMobile_number);

				editor.putString("res_add", mResadd);

				editor.putString("office_add", mOffadd);
				//editor.putString("patient_name", name);

				editor.commit();
				Intent i = new Intent(Manual_OTP.this, LabSelection.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			} else {
				showAlert("Login failed please try again");
			}
		}
	}

	public static String POST(String url) {
		InputStream inputStream = null;
		String result = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"2\""));

			if(flgFromEmail != null  && !(flgFromEmail.equalsIgnoreCase("")) && flgFromEmail.equals("Yes"))
			{
				nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\""+ mEmail + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\""+ mMobile_number + "\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"OTP\"", "\""
					+ otp_val + "\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			finalString = "{" + finalString + "}";
			String tempString = "\"objSP\"" + ":";
			String strToServer = tempString + finalString;
			String strToServer1 = "{" + strToServer + "}";

			StringEntity se = new StringEntity(strToServer1);
			se.setContentType("application/json;charset=UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
			httpPost.setEntity(se);

			// Execute POST request to the given URL
			HttpResponse httpResponse = httpClient.execute(httpPost);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				String jsonformattString = result.replaceAll("\\\\", "");
				try {
					JSONObject jsonObj = new JSONObject(jsonformattString);
					JSONObject resultObj = (JSONObject) jsonObj.get("d");
					finalResult = resultObj.getString("Result");
					mPatient_id = resultObj.getString("UserFID");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				finalResult = "Did not work!";
		} catch (Exception e) {
			// Log.i("InputStream", e.getLocalizedMessage());
		}
		return finalResult;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

	public void showAlert(String message) {
		Builder builder = new Builder(this);
		
		TextView Mytitle = new TextView(this);
		Mytitle.setText("Alert"); 
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		builder.setCustomTitle(Mytitle);
		
		builder.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
	}

	public static boolean isInternetOn(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);

		if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return false;
	}

	private void alertBox() {
		Builder alert = new Builder(this);
		
		TextView Mytitle = new TextView(this);
		Mytitle.setText("Alert"); 
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		alert.setCustomTitle(Mytitle);
		
		alert.setIcon(R.drawable.sign_logoelab);
		alert.setMessage("Internet Connection is not available..!");
		alert.setPositiveButton("OK", null);
		//alert.show();
		
		AlertDialog alert1 = alert.create();
		alert1.show();

		TextView textView = (TextView) alert1.findViewById(android.R.id.message);
		Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
	}

	@Override
	public void onBackPressed() {
		
	}
}