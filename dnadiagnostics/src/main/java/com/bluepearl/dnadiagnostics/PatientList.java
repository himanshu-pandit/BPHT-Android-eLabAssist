package com.bluepearl.dnadiagnostics;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsonparsing.webservice.ServiceHandler;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientList extends BaseActivity
{

	private static final int PERMISSION_REQUEST_CODE = 203;
	private static final String[] PERMISSIONS = {
		Manifest.permission.WRITE_EXTERNAL_STORAGE,
		Manifest.permission.READ_EXTERNAL_STORAGE
	};


	ActionBar actionBar;
	static EditText etpname,etlabcode,etTodate,etFromdate;
	ListView patientliListView ;
	TextView internetView;
	Button btnRefresh;
	private ProgressDialog pDialog;
	boolean flg = false;
	private int mYear, mMonth, mDay;
	AutoCompleteTextView textView;
	PatientAdapter patientAdapter;
	static String pdfFileName = "" ;
	String testRegistrationID = "";
	static String status = "";
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String pid = null;
	static String phone = null;
	static String selected_labidfrompref = null;
	static String seMobileNumberfrompref = null;
	static String selected_labemailfrompref = null;

	static String selected_labmobilefrompref = null;


	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/TestReportList";

	// PDF URL
	private static String getPDFurl = "https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";
	private static final String originalPDF_URL ="https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";

	// contacts JSONArray
	static JSONArray patient = null;

	static ArrayList<PatientListDetails> patientList = new ArrayList<PatientListDetails>();
	ArrayList<PatientListDetails> originallabuserList = new ArrayList<PatientListDetails>();


	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		pid = sharedpreferences.getString("patientId", "");
		phone = sharedpreferences.getString("phno", "");
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		selected_labemailfrompref = sharedpreferences.getString("SelectedLabEmail", "");
		selected_labmobilefrompref = sharedpreferences.getString("SelectedLabMobile", "");
		seMobileNumberfrompref=sharedpreferences.getString("MobileNumber","");
		getLayoutInflater().inflate(R.layout.activity_patient_list, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		patientliListView = (ListView)findViewById(R.id.patientlistview);
		internetView = (TextView)findViewById(R.id.textView10);
		btnRefresh = (Button)findViewById(R.id.button10);

		internetView.setTypeface(custom_bold_font);
		btnRefresh.setTypeface(custom_bold_font);

		Calendar c=Calendar.getInstance();
		mYear=c.get(Calendar.YEAR);
		mMonth=c.get(Calendar.MONTH);
		mDay=c.get(Calendar.DAY_OF_MONTH);

		if (!checkPermission()) {

			requestPermission();

		} else {
			if (isInternetOn(getApplicationContext())) {
				new GetPatientCheck().execute(new String[]{url});
				patientliListView.setVisibility(View.VISIBLE);
				internetView.setVisibility(View.GONE);
				btnRefresh.setVisibility(View.GONE);
			} else {
				//showAlert("Internet Connection is not available..!","Alert");
				patientliListView.setVisibility(View.GONE);
				internetView.setVisibility(View.VISIBLE);
				btnRefresh.setVisibility(View.VISIBLE);
			}
		}
		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				if (isInternetOn(getApplicationContext()))
				{
					new GetPatientCheck().execute(new String[] { url });
					patientliListView.setVisibility(View.VISIBLE);
					internetView.setVisibility(View.GONE);
					btnRefresh.setVisibility(View.GONE);
				}
				else
				{
					patientliListView.setVisibility(View.GONE);
					internetView.setVisibility(View.VISIBLE);
					btnRefresh.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

	public String changeDateFormat(String dateToFormat)
	{
		String outputDate = null;

		if(dateToFormat != null && !(dateToFormat.equalsIgnoreCase("")))
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

	private class GetPatientCheck extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			patientList.clear();

			// Showing progress dialog
			pDialog = new ProgressDialog(PatientList.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			return PatientPOST(params[0]);
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}

			if(patientList.size() == 0)
			{
				showAlert1("No reports are available");
			}
			else
			{
				patientAdapter = new PatientAdapter(PatientList.this,patientList);
				patientliListView.setAdapter(patientAdapter);
			}

			patientliListView.isClickable();
			patientliListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
				{
					// TODO Auto-generated method stub
					PatientListDetails myObj = (PatientListDetails) patientList.get(arg2);
					testRegistrationID = myObj.getTestRegId();
					Intent intent = new Intent(PatientList.this,ViewReportAttachment.class);
					intent.putExtra("testRegistrationID",myObj.getTestRegId());
					intent.putExtra("PMobile","");
					intent.putExtra("AMobile","");
					intent.putExtra("CCMobile","");
					intent.putExtra("DMobile","");
					startActivity(intent);
				}
			});
		}
	}

	public static String PatientPOST(String url)
	{
		InputStream inputStream = null;
		String result = "";
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"UserFID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"FromDate\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"ToDate\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"LabCode\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"PatientName\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"UserType\"", "\"1\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			String strToServer = "{" + finalString + "}";
			StringEntity se = new StringEntity(strToServer);
			se.setContentType("application/json;charset=UTF-8");
			httpPost.setEntity(se);

			// Execute POST request to the given URL
			HttpResponse httpResponse = httpClient.execute(httpPost);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
			{
				result = convertInputStreamToString(inputStream);
				String jsonformattString = result.replaceAll("\\\\", "");
				try
				{
					JSONObject jsonObj = new JSONObject(jsonformattString);

					// Getting JSON Array node
					patient = jsonObj.getJSONArray("d");

					// looping through All Contacts
					for (int i = 0; i < patient.length(); i++)
					{
						PatientListDetails listd = new PatientListDetails();
						JSONObject c = patient.getJSONObject(i);

						String patientname = c.getString("PatientName");
						String doctor_name = c.getString("DoctorName");
						String timeDate = c.getString("RegnDateTimeString");
						String testRegID = c.getString("TestRegnID");
						String balamt = c.getString("BalanceAmt");
						String PDFName = c.getString("PDFFileName");
						String testState = c.getString("CurrentState");
						String lab_code = c.getString("LabCode");

						listd.setPatientName(patientname);
						listd.setDoctorName(doctor_name);
						listd.setDate(timeDate);
						listd.setLabCode(lab_code);
						listd.setBal_Amt(balamt);
						listd.setTestRegId(testRegID);
						listd.setTestState(testState);

						// adding contact to contact list
						patientList.add(listd);
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			else
				status = "Did not work!";
		}
		catch (Exception e)
		{
			// Log.i("InputStream", e.getLocalizedMessage());
		}
		return status;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

	public static boolean isInternetOn(Context context)
	{
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

		if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED)
		{
			return true;
		}
		else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED)
		{
			return false;
		}
		return false;
	}

	/*** Method to show alert dialog **/
	public void showAlert(String message,String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText(title);
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setIcon(R.drawable.sign_logoelab);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = new Intent(PatientList.this,PatientList.class);
						startActivity(i);
						//finish();
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
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();

		Intent i = new Intent(PatientList.this,AddressSelection.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		//finish();
	}

	public void showAlert1(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText("Alert");
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
				.setCancelable(false)
				.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
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




	public void showAlertReportNotGenerated(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText("Alert");
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
				.setCancelable(false)
				.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				})
				.setPositiveButton("More", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

//				Dialog d = new Dialog(getApplicationContext());
//				d.setContentView(R.layout.activity_more);
//				d.setTitle("ElabAssist");
//				d.show();
						Intent i = new Intent(PatientList.this,MoreActivity.class);
						startActivity(i);
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




	private boolean checkPermission() {
		int writePermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int readPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
		return writePermissionResult == PackageManager.PERMISSION_GRANTED && readPermissionResult == PackageManager.PERMISSION_GRANTED;
	}

	private void requestPermission() {
		ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch (requestCode) {
			case PERMISSION_REQUEST_CODE:
				if (grantResults.length > 0) {
					boolean writePermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
					boolean readPermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

					if (writePermissionGranted && readPermissionGranted) {
						// Permissions granted
						// new GetPatientCheck().execute(new String[]{url});
					} else {
						// Permissions denied
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							boolean shouldShowWriteRationale = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
							boolean shouldShowReadRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);

							if (shouldShowWriteRationale || shouldShowReadRationale) {
								// Show rationale and request permission again
								showMessageOKCancel("Save PDFs Offline: Store downloaded PDF reports on your device for offline access.\n" +
												"Display PDFs: Show the PDF reports you download within the app for easy viewing.",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												ActivityCompat.requestPermissions(
														PatientList.this,
														PERMISSIONS,
														PERMISSION_REQUEST_CODE
												);
											}
										},
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// User clicked Cancel, handle accordingly
											}
										});
							} else {
								// User denied without showing rationale
								Toast.makeText(this, "You need to allow permissions in phone settings", Toast.LENGTH_LONG).show();
							}
						}
					}
				}
				break;
		}
	}

	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
		new AlertDialog.Builder(PatientList.this)
				.setTitle("Permission Required")
				.setMessage(message)
				.setPositiveButton("Allow", okListener)
				.setNegativeButton("Cancel", cancelListener)
				.create()
				.show();
	}












}
