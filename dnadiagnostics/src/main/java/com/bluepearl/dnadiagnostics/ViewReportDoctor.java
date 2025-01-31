package com.bluepearl.dnadiagnostics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
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

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ViewReportDoctor extends BaseActivityDL {

	private static final int PERMISSION_REQUEST_CODE = 202;
	private static final String[] PERMISSIONS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE
	};

	private LinearLayout ll1;
	private ImageButton search;
	ListView doctorliListView;
	EditText etpname, etlabcode, etTodate, etFromdate;
	private int mYear, mMonth, mDay;
	static final int FROM_DATE = 0;
	static final int TO_DATE = 1;
	ListView doctorlistview;
	DoctorAdapter doctorAdapter;
	private ProgressDialog pDialog;
	static String FromDate;
	static String ToDate;
	boolean flg = false;
	static String etpname_val = "";
	static String etlabcode_val = "";
	AutoCompleteTextView textView;
	String pdfFileName = "";
	String testRegistrationID = "";
	String txtfromdate, txttodate;
	public static final String MyPREFERENCES = "MyPrefs";
	public static final int Permission_Request=56;

	SharedPreferences sharedpreferences;
	static String doc_id = null;
	static String phone = null;
	static String usertype = "";
	static String status = "";
	static int isSearch = 0;
	int temp =0;
	static String pid = null;
	static String selected_labidfrompref = null;

	static String curState = "";
	static String ReleseDoctorState = "";
	static String RelesePatientState = "";
	ImageButton imgbtnClear ,imgbtnClear2 ;


	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/TestReportList";

	// PDF URL
	private static String getPDFurl = "https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";
	private static final String originalPDF_URL = "https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";

	// JSON Node names
	private static final String TAG_DATETIME = "RegnDateTimeString";
	private static final String TAG_LABCODE = "LabCode";
	private static final String TAG_PATIENTNAME = "PatientName";
	private static final String TAG_TESTREGNID = "TestRegnID";

	// contacts JSONArray
	static JSONArray patient = null;

	static ArrayList<DoctorListDetails> doctorList = new ArrayList<DoctorListDetails>();
	static ArrayList<DoctorListDetails> originaldoctorList = new ArrayList<DoctorListDetails>();
	String[] patientNameArr, doctorNameArr, patientContactArr;
	static List<String> doctor_Listview = new ArrayList<String>();


	static String pdfReportFileName = "" ;
	URL downloadUrl = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		// setContentView(R.layout.activity_view_report_for_doctor);

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		phone = sharedpreferences.getString("phno", "");
		pid = sharedpreferences.getString("patientId", "");
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		usertype = sharedpreferences.getString("usertype", "");

		getLayoutInflater().inflate(R.layout.activity_view_report_for_doctor,
				frameLayout);
		// mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		ll1 = (LinearLayout) findViewById(R.id.ll3);
		search = (ImageButton) findViewById(R.id.btnSearch);
		etpname = (AutoCompleteTextView) findViewById(R.id.etPatientName);
		etlabcode = (EditText) findViewById(R.id.etLabCode);
		etTodate = (EditText) findViewById(R.id.etToDate);
		etFromdate = (EditText) findViewById(R.id.etFromDate);
		doctorlistview = (ListView) findViewById(R.id.doctorlistview);
		textView = (AutoCompleteTextView) findViewById(R.id.etPatientName);
		imgbtnClear2 = (ImageButton) findViewById(R.id.clearCC2);
		etpname.setTypeface(custom_font);
		textView.setTypeface(custom_font);
		etlabcode.setTypeface(custom_font);
		etTodate.setTypeface(custom_font);
		etFromdate.setTypeface(custom_font);

		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		final String strDatefrom = sdf.format(from.getTime());
		final String strDateto = sdf.format(to.getTime());

		etFromdate.setText(strDatefrom);
		etTodate.setText(strDateto);

		FromDate = etFromdate.getText().toString();
		FromDate = FromDate + " 00:00:00";

		ToDate = etTodate.getText().toString();
		ToDate = ToDate + " 23:59:59";

		FromDate = changeDateFormat(FromDate);
		ToDate = changeDateFormat(ToDate);

		imgbtnClear2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textView.setText("");
			}
		});

		if (!checkPermission()) {

			requestPermission();

		}else{
			if (isInternetOn(getApplicationContext())) {
				isSearch = 0;
				new GetPatientCheck().execute(new String[]{url});
			} else {
				showAlert("Internet Connection is not available..!", "Alert");
			}

		}

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isInternetOn(getApplicationContext())) {
					isSearch = 1;
					etpname_val = etpname.getText().toString() ;
					etlabcode_val = etlabcode.getText().toString() ;

					FromDate = etFromdate.getText().toString() ;
					FromDate = FromDate + " 00:00:00";

					ToDate = etTodate.getText().toString() ;
					ToDate = ToDate + " 23:59:59";

					FromDate = changeDateFormat(FromDate);
					ToDate = changeDateFormat(ToDate);

					new GetPatientCheck().execute(new String[] { url });
					flg = true;

				} else {
					showAlert("Internet Connection is not available..!",
							"Alert");
				}
			}
		});

		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		etFromdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(FROM_DATE);
			}
		});

		etTodate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TO_DATE);
			}
		});
		// new GetPatient().execute();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, doctor_Listview);
		textView.setAdapter(adapter);
	}


	@SuppressLint("NewApi")
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case FROM_DATE:
				DatePickerDialog dialog_from = new DatePickerDialog(this,
						mDateSetListener_from, mYear, mMonth, mDay);
				Calendar currentDate_from = Calendar.getInstance();
				dialog_from.getDatePicker().setMaxDate(
						currentDate_from.getTimeInMillis());
				return dialog_from;

			case TO_DATE:
				DatePickerDialog dialog_to = new DatePickerDialog(this,
						mDateSetListener_to, mYear, mMonth, mDay);
				Calendar currentDate_to = Calendar.getInstance();
				dialog_to.getDatePicker().setMaxDate(
						currentDate_to.getTimeInMillis());
				return dialog_to;
		}
		return null;
	}

	// DatePickerDialog from date
	private DatePickerDialog.OnDateSetListener mDateSetListener_from = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			mMonth = mMonth + 1;

			String day = String.valueOf(mDay);
			String month = String.valueOf(mMonth);

			if (day.length() < 2) {
				day = "0" + day;
				etFromdate.setText(new StringBuilder().append(day).append("-")
						.append(month).append("-").append(mYear));
			} else {
				etFromdate.setText(new StringBuilder().append(day).append("-")
						.append(month).append("-").append(mYear));
			}

			if (month.length() < 2) {
				month = "0" + month;
				etFromdate.setText(new StringBuilder().append(day).append("-")
						.append(month).append("-").append(mYear));
			} else {
				etFromdate.setText(new StringBuilder().append(day).append("-")
						.append(month).append("-").append(mYear));
			}
		}
	};

	// DatePickerDialog to date
	private DatePickerDialog.OnDateSetListener mDateSetListener_to = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			mMonth = mMonth + 1;

			String day = String.valueOf(mDay);
			String month = String.valueOf(mMonth);

			if (day.length() < 2) {
				day = "0" + day;
				etTodate.setText(new StringBuilder().append(day).append("-")
						.append(month).append("-").append(mYear));
			} else {
				etTodate.setText(new StringBuilder().append(day).append("-")
						.append(month).append("-").append(mYear));
			}

			if (month.length() < 2) {
				month = "0" + month;
				etTodate.setText(new StringBuilder().append(day).append("-")
						.append(month).append("-").append(mYear));
			} else {
				etTodate.setText(new StringBuilder().append(day).append("-")
						.append(month).append("-").append(mYear));
			}
		}
	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		if(flg)
		{
			Intent i = new Intent(ViewReportDoctor.this,ViewReportDoctor.class);
			startActivity(i);
			//finish();
		}
		Intent i = new Intent(ViewReportDoctor.this,RegisterPatientNew.class);
		//  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		// finish();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); return
	 * super.onOptionsItemSelected(item); }
	 */

	public String changeDateFormat(String dateToFormat) {
		String outputDate = null;

		if (dateToFormat != null && !(dateToFormat.equalsIgnoreCase(""))) {
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			java.util.Date date = null;

			try {
				date = sdfSource.parse(dateToFormat);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			outputDate = sdfDestination.format(date);
		}
		return outputDate;
	}

	private class GetPatientCheck extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			doctorList.clear();
			doctor_Listview.clear();

			// Showing progress dialog
			pDialog = new ProgressDialog(ViewReportDoctor.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return PatientPOST(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			if(isSearch == 1)
			{
				isSearch = 0;
			}

			if (doctorList.size() == 0) {
				showAlert1("No reports are available");

			} else {
				doctorAdapter = new DoctorAdapter(ViewReportDoctor.this,
						doctorList);
				doctorlistview.setAdapter(doctorAdapter);
			}

			doctorlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
										int arg2, long arg3) {
					// TODO Auto-generated method stub
					DoctorListDetails myObj = (DoctorListDetails) doctorList.get(arg2);
					Intent intent = new Intent(ViewReportDoctor.this,ViewReportAttachment.class);
					intent.putExtra("testRegistrationID",myObj.getTestRegId());
					intent.putExtra("PMobile",myObj.getPMobile());
					intent.putExtra("AMobile",myObj.getAMobile());
					intent.putExtra("CCMobile",myObj.getCCMobile());
					intent.putExtra("DMobile",myObj.getDMobile());
					Log.d("ViewReportAdmin",myObj.getTestRegId()+myObj.getPMobile()+myObj.getAMobile()+myObj.getDMobile());
					startActivity(intent);
				}
			});
		}
	}

	public static String PatientPOST(String url) {
		InputStream inputStream = null;
		String result = "";

		/*Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd");

		strDatefromnew = sdfnew.format(from.getTime());
		strDatefromnew = strDatefromnew + " 00:00:00";

		strDatetonew = sdfnew.format(to.getTime());
		strDatetonew = strDatetonew + " 23:59:59";*/

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			Log.d("url++",url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"UserFID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"FromDate\"", "\"" + FromDate + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"ToDate\"", "\"" + ToDate + "\""));
			//nameValuePairs.add(new BasicNameValuePair("\"LabCode\"", "\"\""));
			if (isSearch == 1) {
				nameValuePairs.add(new BasicNameValuePair("\"LabCode\"","\"" + etpname_val + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"LabCode\"", "\"\""));
			}

			if (isSearch == 1)
			{
				nameValuePairs.add(new BasicNameValuePair("\"PatientName\"","\"" + etpname_val + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PatientName\"", "\"\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"UserType\"", "\"5\""));


			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			String strToServer = "{" + finalString + "}";
			Log.d("strToServer++",strToServer);

			StringEntity se = new StringEntity(strToServer);
			se.setContentType("application/json;charset=UTF-8");
			httpPost.setEntity(se);

			// Execute POST request to the given URL
			HttpResponse httpResponse = httpClient.execute(httpPost);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				//String jsonformattString = result.replaceAll("\\\\", "");
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					patient = jsonObj.getJSONArray("d");
					Log.d("httpResponsecc++",patient.toString());

					// looping through All Contacts
					for (int i = 0; i < patient.length(); i++) {
						DoctorListDetails listd = new DoctorListDetails();
						JSONObject c = patient.getJSONObject(i);

						String patient_name = c.getString(TAG_PATIENTNAME);
						String lab_code = c.getString(TAG_LABCODE);
						String testRegID = c.getString("TestRegnID");
						String balamt = c.getString("BalanceAmt");
						curState = c.getString("CurrentState");
						RelesePatientState = c.getString("ReleseReport");
						ReleseDoctorState = c.getString("ReleseDoctorReport");
						String timeDate = c.getString(TAG_DATETIME);
						String doctor_name = c.getString("DoctorName");

						String testlistdetail =c.getString("TotalTestList");
						String ApproveTest = c.getString("ApproveTest");
						String TotalTest = c.getString("TotalTest");
						String PatientMobile = c.getString("PatientMobile");
						String DoctorMobile = c.getString("DoctorMobile");
						String CollectionCenterMobile = c.getString("CollectionCenterMobile");
						String AffiliationMobile = c.getString("AffiliationMobile");
						listd.setPatientName(patient_name);
						listd.setLabCode(lab_code);
						listd.setBal_Amt(balamt);
						listd.setDoctorName(doctor_name);
						listd.setDate(timeDate);
						listd.setTestRegId(testRegID);
						listd.setState(curState);
						listd.setReleaserPatient(RelesePatientState);
						listd.setReleaserDoctor(ReleseDoctorState);
						listd.setTestListDetails(testlistdetail);
						listd.setApproveTest(ApproveTest);
						listd.setTotalTest(TotalTest);
						listd.setPMobile(PatientMobile);
						listd.setDMobile(DoctorMobile);
						listd.setAMobile(AffiliationMobile);
						listd.setCCMobile(CollectionCenterMobile);
						// adding contact to contact list
						doctorList.add(listd);
						originaldoctorList.add(listd);
						doctor_Listview.add(patient_name);

						doctor_Listview.add(lab_code);
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

	private class GetPDFReport extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(ViewReportDoctor.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Added by Dipen Shah on 20/1/2016
			if(usertype.equals("Doctor"))
			{
				getPDFurl = getPDFurl + testRegistrationID + "&" + "LabID=" + selected_labidfrompref + "&" + "UserTypeID=" + "2";
			}
			else if(usertype.equals("AffiliationUser"))
			{
				getPDFurl = getPDFurl + testRegistrationID + "&" + "LabID=" + selected_labidfrompref + "&" + "UserTypeID=" + "4";
			}
			else if(usertype.equals("Collection Center User") || usertype.equals("Collection Center Lab User"))
			{
				getPDFurl = getPDFurl + testRegistrationID + "&" + "LabID=" + selected_labidfrompref + "&" + "UserTypeID=" + "3";
			}
			else if(usertype.equals("Collection Center Admin"))
			{
				getPDFurl = getPDFurl + testRegistrationID + "&" + "LabID=" + selected_labidfrompref + "&" + "UserTypeID=" + "3";
			}

			// Commented by Dipen Shah on 20/1/2016
			//getPDFurl = getPDFurl + testRegistrationID + "&" + "LabID=" + selected_labidfrompref + "&" + "UserTypeID=" + "2";

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(getPDFurl, ServiceHandler.GET);
			Log.d("getPDFurl+",getPDFurl);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					patient = jsonObj.getJSONArray("d");
					JSONObject obj = patient.getJSONObject(0);
					Log.d("affreport+",patient.toString());
					if(obj.getString("PdfName") != null && !(obj.getString("PdfName").equalsIgnoreCase("Null")) && !(obj.getString("PdfName").equalsIgnoreCase("")))
					{
						String params = obj.getString("PdfName").replace("~/", "");
						pdfReportFileName = "https://www.elabassist.com/"+params;
					} else {
						pdfReportFileName = "";
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}

			if (pdfReportFileName != null && !(pdfReportFileName.equalsIgnoreCase("")))
			{
				//new DownloadFile(getApplicationContext()).execute("https://www.elabassist.com/"+ pdfFileName, pdfFileName);
				new ViewReportDoctor.DownloadFile(ViewReportDoctor.this,pdfReportFileName).execute();
			}
			else
			{
				showAlert1("Report is not generated yet, Please contact to laboratory");

				getPDFurl = originalPDF_URL;
			}
		}
	}

	private class DownloadFile extends AsyncTask<String, Void, String> {
		private Context mContext;
		private String pdfReportUrl;

		public DownloadFile (Context context,String pdfReceiptUrl){
			mContext = context;
			this.pdfReportUrl = pdfReceiptUrl;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(ViewReportDoctor.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... strings) {

			try {
				downloadUrl = new URL(pdfReportUrl);
				Log.d("MalformedURLException",""+downloadUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Log.d("MalformedURLException",""+e);
			}
			String filepath = downloadUrl.getPath();
			filepath = filepath.substring(filepath.lastIndexOf("/")+1);

			Log.d("filepath",""+filepath);

			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(downloadUrl)));
			request.setTitle(filepath);
			request.setMimeType("application/pdf");

			//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filepath);

			DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
			dm.enqueue(request);

			pdfReportFileName = "";
			return filepath;
		}

		@Override
		protected void onPostExecute(String filepath) {
			// TODO Auto-generated method stub

			final Handler handler = new Handler(Looper.getMainLooper());
			final String filePath = filepath;

			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filePath);

			final Uri uri = FileProvider.getUriForFile(mContext, "com.bluepearl.dnadiagnostics" + ".provider", file);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					//Do something after 5000s
					if (pDialog != null && pDialog.isShowing())
					{
						pDialog.dismiss();
					}

					Intent intent = new Intent();
					intent.setDataAndType(uri,"application/pdf");
					intent.setAction(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
					mContext.startActivity(intent);
				}
			}, 4000);

			getPDFurl = originalPDF_URL;
		}
	}

	/*** Method to show alert dialog **/
	public void showAlert(String message, String title) {
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
						Intent i = new Intent(ViewReportDoctor.this,
								ViewReportDoctor.class);
						//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		int id = item.getItemId();

		if(id == R.id.action_search){
			if(temp ==0){
				ll1.setVisibility(View.VISIBLE);
				temp ++;
			}else if (temp ==1){
				ll1.setVisibility(View.GONE);
				temp=0;
			}

			return true;
		}

		switch (item.getItemId()) {
			default:
				return super.onOptionsItemSelected(item);
		}
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
														ViewReportDoctor.this,
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
		new AlertDialog.Builder(ViewReportDoctor.this)
				.setTitle("Permission Required")
				.setMessage(message)
				.setPositiveButton("Allow", okListener)
				.setNegativeButton("Cancel", cancelListener)
				.create()
				.show();
	}


}
