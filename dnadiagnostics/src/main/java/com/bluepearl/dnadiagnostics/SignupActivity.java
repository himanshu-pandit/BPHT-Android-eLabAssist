package com.bluepearl.dnadiagnostics;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

public class SignupActivity extends Activity {

	Boolean numberExists=false;
	Timer timer;
	private int mYear, mMonth, mDay;
	TextView lblgender,lbl_lab;
	Button btnsubmit;
	EditText mobile, password, confirm_password,emailid,newPname,eDob;;
	static String Code = null;
	static String jsonPId = null;
	static String num_value = null;
	static String pass_value = null;
	static String email_val = null;
	static String confirm_pass_value = null;
	static String active = null;
	static String status = "";
	static String finalResult = "";
	static final int DATE_OF_BIRTH = 0;
	//static String Code = "";
	// static String DeviceID = "";
	BroadcastReceiver mIntentReceiver;
	static JSONArray patient = null;
	private ProgressDialog pDialog;
	private ProgressDialog pDialog1;

	ShareExternalServer appUtil;
	static String regId;
	AsyncTask<Void, Void, String> shareRegidTask;
	Context context;
	String message;
	private static final String APP_VERSION = "appVersion";
	static final String TAG = "Register Activity";
	public static final String REG_ID = "regId";
	private RadioGroup radioGroup;
	private RadioButton radioButton,rb_male,rb_female;

	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;

	//static String user_type = "";
	
		//static String user_type_frmpref = "";
	
	static String resadd = "";
	static String offadd = "";
	static String patient_id = "";
	static String mobile_number = "";
	static String genderval= null;
	static String name = null;
	static String p_dob = null;
	static String rb = null;
	//int regOwnNumber = 0;

	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.activity_signup);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		
		
	//	user_type_frmpref = sharedpreferences.getString("usertype", "");
		

	//	mobile_number = sharedpreferences.getString("MobileNumber", "");
	//	Password = sharedpreferences.getString("Password", "");
		//DeviceID = sharedpreferences.getString("DeviceID", "");
	//	name = sharedpreferences.getString("self_patientName", "");
	//	genderval = sharedpreferences.getString("self_patientgender", "");
	//	p_dob = sharedpreferences.getString("self_patientDOB", "");
	//	Email = sharedpreferences.getString("self_patientEmail", "");

		lbl_lab = (TextView)findViewById(R.id.txtviewlab);
		btnsubmit = (Button) findViewById(R.id.SubmitButton);
		mobile = (EditText) findViewById(R.id.etmobile);
		emailid = (EditText) findViewById(R.id.etEmailId);
		password = (EditText) findViewById(R.id.etpassword);
		confirm_password = (EditText) findViewById(R.id.etconfirmpassword);
		newPname = (EditText)findViewById(R.id.name);
		eDob = (EditText)findViewById(R.id.dateofbirth);
		lblgender = (TextView)findViewById(R.id.txtGender);
		radioGroup = (RadioGroup)findViewById(R.id.radioGender);
		rb_male = (RadioButton)findViewById(R.id.radioMale);
		rb_female = (RadioButton)findViewById(R.id.radioFemale);

		TextView textView =(TextView)findViewById(R.id.textView);
		textView.setClickable(true);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='https://www.elabassist.com/Account/TermsAndConditions.aspx'> Registering to this application means you are accepting Terms and Conditions. </a>";
		textView.setText(Html.fromHtml(text));

		lbl_lab.setTypeface(custom_btnfont);
		btnsubmit.setTypeface(custom_btnfont);
		mobile.setTypeface(custom_font);
		emailid.setTypeface(custom_font);
		password.setTypeface(custom_font);
		confirm_password.setTypeface(custom_font);
		newPname.setTypeface(custom_font);
		eDob.setTypeface(custom_font);
		lblgender.setTypeface(custom_font);
		rb_male.setTypeface(custom_font);
		rb_female.setTypeface(custom_font);
		textView.setTypeface(custom_font);

		Calendar c=Calendar.getInstance();
		mYear=c.get(Calendar.YEAR);
		mMonth=c.get(Calendar.MONTH);
		mDay=c.get(Calendar.DAY_OF_MONTH);

		eDob.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				showDialog(DATE_OF_BIRTH);
			}
		});

		btnsubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				num_value = mobile.getText().toString();
				pass_value = password.getText().toString();
				confirm_pass_value = confirm_password.getText().toString();
				email_val = emailid.getText().toString();
				name = newPname.getText().toString();
				p_dob = eDob.getText().toString();
				p_dob =changeDateFormat(p_dob);
				int selectedrb = radioGroup.getCheckedRadioButtonId();
				radioButton = (RadioButton) findViewById(selectedrb);
				rb = radioButton.getText().toString();

				if(rb.equalsIgnoreCase("Male"))
				{
					genderval = "0";
				}
				else if(rb.equalsIgnoreCase("Female"))
				{
					genderval = "1";
				}

				if (isInternetOn(getApplicationContext())) {
					if ((num_value).equalsIgnoreCase("")) {
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter the mobile number     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
						
					} else if (!isValidMobile(num_value)) {
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter the valid mobile number     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
						
					}else if ((pass_value).equalsIgnoreCase("")) {
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter the password     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
						
					} else if (!isValidPassword(pass_value)) {
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Password length should be greater than five characters     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
						
					}else if (!(pass_value.equals(confirm_pass_value))) {
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Both passwords are not identical     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
						
					} else {
						if(!numberExists)
						{
							//registerDeviceWithGCM();

							new SendNumber().execute(new String[] {url});
						}
						else{
							if(numberExists && !(email_val.equalsIgnoreCase("")))
							{
								SharedPreferences.Editor editor = sharedpreferences.edit();
								editor.putString("MobileNumber", num_value);
								editor.putString("Password", pass_value);
								editor.putString("DeviceID", regId);
								editor.putString("patientId", patient_id);
								editor.putString("self_patientName", name);
								editor.putString("self_patientgender", genderval);
								editor.putString("self_patientDOB", p_dob);
								editor.putString("self_patientEmail", email_val);
								editor.putString("patient_name", name);
								editor.commit();

								new SendNumber().execute(new String[] {url});
								
							}
							else
							{
								LayoutInflater inflater = getLayoutInflater();
							    View layout = inflater.inflate(R.layout.my_dialog,null);

							    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
							    text.setText("     Please enter your email id     ");
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
					}
				} else {
					alertBox();
				}

			}
		});
		
	}

	public void recivedSms(String message) {
		try {
			String[] parts = message.split("-");
			Code = parts[0];
			// String message2 = parts[1];
		} catch (Exception e) {
		}
	}

	public void runNumberVerfication() {
		try {
			if (Code != null) {
				//regOwnNumber = 1;

				Thread.sleep(10000);

				new numberVerfication().execute(new String[] { url });
			} 
		} catch (Exception e) {
		}
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
		
		AlertDialog alert1 = alert.create();
		alert1.show();

		TextView textView = (TextView) alert1.findViewById(android.R.id.message);
		Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
	}

	private boolean isValidMobile(String phone2) {
		boolean check;
		if (phone2.length() < 10 || phone2.length() > 10) {
			check = false;
		} else {
			check = true;
		}
		return check;
	}

	private boolean isValidPassword(String pswd) {
		boolean check;
		if (pswd.length() < 5) {
			check = false;
		} else {
			check = true;
		}
		return check;
	}

	private class SendNumber extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(SignupActivity.this);
			pDialog.setMessage("Registering...Please wait");
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

			if (result.equalsIgnoreCase("Failed") || result.equalsIgnoreCase("") || result.equalsIgnoreCase("Exception"))
			{
				if (pDialog != null && pDialog.isShowing())
				{
					pDialog.dismiss();
				}
				showAlert("Registration failed please try again");
			} 
			else if (result.equalsIgnoreCase("User Already Exist. Please Contact Support Center."))
			{
				if (pDialog != null && pDialog.isShowing())
				{
					pDialog.dismiss();
				}
				numberExists = true;
				showAlert("Number is already exist.Please enter your email id");

			}
			else 
			{

						if(pDialog != null)
						{
							if (pDialog.isShowing())
							{
								pDialog.dismiss();
							}
						}	

						SharedPreferences.Editor editor = sharedpreferences.edit();

						editor.putString("MobileNumber", num_value);
						editor.putString("Password", pass_value);
						editor.putString("DeviceID", regId);
						editor.putString("patientId", patient_id);
						editor.putString("self_patientName", name);
						editor.putString("self_patientgender", genderval);
						editor.putString("self_patientDOB", p_dob);
						editor.putString("self_patientEmail", email_val);
						editor.putString("patient_name", name);

						editor.putString("self", "yes");
						editor.putString("phno", num_value);
						editor.putString("res_add", "");
						editor.putString("office_add", "");

						editor.remove("testid");
						editor.remove("testprofileid");
						editor.remove("test_name");
						editor.remove("filePath");
						editor.remove("address");
						editor.remove("doctor");
						editor.remove("DateTime");
						editor.remove("datetime");
						editor.remove("loginchkbox");
						editor.remove("checkboxchecked");
						editor.remove("allTestNames");
						editor.remove("popularTestData");
						editor.remove("setprofilename");
						editor.remove("setcategoryname");
						editor.remove("CheckedValue");
                        editor.remove("SelectedLabId");
                        editor.remove("SelectedLabName");

                        editor.remove("SelectedLabId");
                        editor.remove("LabIdFromLogin");
                        editor.remove("SelectedLabName");
                        editor.remove("Role");
                        editor.remove("usertype");

						editor.commit();
						
						 Toast.makeText(getApplicationContext(),"Registered Successfully Please Login", Toast.LENGTH_LONG).show();

						//	Intent i = new Intent(SignupActivity.this,SigninActivity.class);
						//Intent i = new Intent(SignupActivity.this,Manual_OTP.class);


						//i.putExtra("fromLogout","yes");
						//i.putExtra("fromchange", "no");

						//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						//startActivity(i);
						finish();

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
			nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\""+ num_value + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"EmailID\"", "\""+ email_val + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Password\"", "\""+ pass_value + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"MobileDeviceID\"", "\""+ regId + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Name\"", "\""+ name + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Gender\"", "" + genderval + ""));

			//nameValuePairs.add(new BasicNameValuePair("\"AppID\"", "\"ce490cd8-261e-44bd-ac8f-3ff91c923306\""));
			nameValuePairs.add(new BasicNameValuePair("\"AppID\"", "\"\""));
			//ce490cd8-261e-44bd-ac8f-3ff91c923306

			if(p_dob != null)
			{
				nameValuePairs.add(new BasicNameValuePair("\"DOB\"", "\"" + p_dob + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"DOB\"", "\"\""));
			}

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			finalString = "{" + finalString + "}";
			String tempString = "\"objSP\"" + ":";
			String strToServer = tempString + finalString;
			String strToServer1 = "{" + strToServer + "}";
            Log.d("strToServer1",""+strToServer1);

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
						
						//user_type = resultObj.getString("Role");
						
						status = resultObj.getString("Result");
						//Code = resultObj.getString("Data");
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

			timer.cancel();
			timer.purge();

			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			pDialog1 = new ProgressDialog(SignupActivity.this);
			pDialog1.setMessage("Verifying...Please wait");
			pDialog1.setCancelable(false);
			pDialog1.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return POST(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog1 !=null &&  pDialog1.isShowing())
				pDialog1.dismiss();
			
			if (result.equals("Success.")) {

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("patientId", patient_id);
				editor.putString("self", "yes");
				editor.putString("phno", num_value);
				editor.putString("self_patientgender", genderval);

				editor.commit();

				Intent i = new Intent(SignupActivity.this,
						LabSelection.class);
				
				/* SigninActivity. Manual_OTP.class LabSelection.class AddressSelection.class SigninActivity */
					
				
				startActivity(i);
				//finish();
			} 
			else {
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
			nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\""
					+ num_value + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"OTP\"", "\""
					+ Code + "\""));


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
					patient_id = resultObj.getString("UserFID");

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

	/*** Method to show alert dialog **/
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
				// do nothing

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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(SignupActivity.this, SigninActivity.class);
		i.putExtra("fromLogout","yes");
		i.putExtra("fromchange", "no");
		startActivity(i);
		//finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(this.mIntentReceiver);
		if (pDialog != null && pDialog.isShowing())
		{
			pDialog.dismiss();
		}
		if (pDialog1 != null && pDialog1.isShowing())
		{
			pDialog1.dismiss();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (pDialog != null && pDialog.isShowing())
		{
			pDialog.dismiss();
		}

		if (pDialog1 != null && pDialog1.isShowing())
		{
			pDialog1.dismiss();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
		mIntentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				runNumberVerfication();
			}
		};
		this.registerReceiver(mIntentReceiver, intentFilter);

	}

	@SuppressLint("NewApi") protected Dialog onCreateDialog(int id) 
	{
		switch (id) 
		{
		case DATE_OF_BIRTH:
			DatePickerDialog dialog_from= new DatePickerDialog(this,mDateSetListener_DOB,mYear, mMonth, mDay);
			Calendar currentDate_from = Calendar.getInstance();
			dialog_from.getDatePicker().setMaxDate(currentDate_from.getTimeInMillis());
			return dialog_from;
		}
		return null;
	}

	//DatePickerDialog from date
	private DatePickerDialog.OnDateSetListener mDateSetListener_DOB = new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
		{
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			mMonth = mMonth+1;

			String day = String.valueOf(mDay);
			String month = String.valueOf(mMonth);

			if(day.length() < 2)
			{
				day = "0" + day; 
				eDob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
			else
			{
				eDob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear)); 
			}

			if(month.length() < 2)
			{
				month = "0" + month; 
				eDob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
			else
			{
				eDob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear)); 
			}
		}
	};

	public String changeDateFormat(String dateToFormat)
	{
		String outputDate = null;

		if(dateToFormat != null && !(dateToFormat.equalsIgnoreCase("")) )
		{
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date = null;
			try 
			{
				date = sdfSource.parse(dateToFormat);
			} 
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat sdfDestination = new SimpleDateFormat("MM-dd-yyyy");
			outputDate = sdfDestination.format(date);
		}
		return outputDate;
	}	
}
