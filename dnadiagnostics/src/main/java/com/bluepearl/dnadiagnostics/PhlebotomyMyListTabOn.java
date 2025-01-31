package com.bluepearl.dnadiagnostics;

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
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
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

import androidx.core.content.FileProvider;

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


public class PhlebotomyMyListTabOn extends BaseActivityPhlebotomy
{
	private LinearLayout ll1;
	private ImageButton search ,newRegistration ;
	static EditText etpname,etlabcode,etTodate,etFromdate;
	ListView labuserlistview ;
	private ProgressDialog pDialog;
	boolean flg = false;
	static String FromDate;
	static String ToDate;
	int etpname_length = 0;
	int etlabcode_length = 0;
	int etTodate_length = 0;
	int etFromdate_length = 0;
	private int mYear, mMonth, mDay;
	static final int FROM_DATE = 0;
	static final int TO_DATE = 1;
	AutoCompleteTextView textView;
	PhlebotomMyAdapter patientAdapter;
	String pdfFileName = "" ;
	String testRegistrationID = "";
	String txtfromdate,txttodate;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String lab_user_id = null;
	static String phone = null;
	static String pid = null;
	static String status = "";
	static String etpname_val = "";
	static String etlabcode_val = "";
	static String usertype = "";
	static int isSearch = 0;
	int temp =0;
	static String selected_labidfrompref = null;
	static String testRegID = "";
	static String curState = "";

	static URL downloadUrl = null;

	//private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/TestReportList";
	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/TestReportList";

	// PDF URL
	private static String getPDFurl = "https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";
	private static final String originalPDF_URL ="https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";

	// JSON Node names
	private static final String TAG_BALANCEAMT = "BalanceAmt";
	private static final String TAG_DATETIME  = "RegnDateTimeString";
	private static final String TAG_DOCTORNAME = "DoctorName";
	private static final String TAG_LABCODE = "LabCode";
	private static final String TAG_PATIENTNAME = "PatientName";
	private static final String TAG_TESTREGNID = "TestRegnID";

	// contacts JSONArray
	static JSONArray patient = null;

	static ArrayList<PhlebotomyMyListDetails> labuserList = new ArrayList<PhlebotomyMyListDetails>();
	static ArrayList<PhlebotomyMyListDetails> originallabuserList = new ArrayList<PhlebotomyMyListDetails>();
	String[] patientNameArr, doctorNameArr,patientContactArr;
	static List<String> labuser_Listview = new ArrayList<String>();


	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.activity_phlebotomy_mylist_on, null);
		this.setContentView(viewToLoad);

		//setContentView(R.layout.activity_phlebotomy_mylist_on);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

		//pid = sharedpreferences.getString("patientId", "");
		phone = sharedpreferences.getString("phno", "");
		//selected_labidfrompref = sharedpreferences.getString("LabIdFromLogin", "");



       // selected_labidfrompref = "84135443-7a22-4f96-b7ac-9fa211b750c5";
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		pid = sharedpreferences.getString("patientId", "");

		usertype = sharedpreferences.getString("usertype","");




	//	getLayoutInflater().inflate(R.layout.activity_phlebotomy_mylist_on, frameLayout);
		//mDrawerList.setItemChecked(position, true);

		//ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
	//	getSupportActionBar().setBackgroundDrawable(colorDrawable);

		ll1 = (LinearLayout) findViewById(R.id.ll3);
		search = (ImageButton)findViewById(R.id.btnSearch);
		newRegistration = (ImageButton)findViewById(R.id.id_newplus);
		etpname = (AutoCompleteTextView)findViewById(R.id.etPatientName);
		etlabcode = (EditText)findViewById(R.id.etLabCode);
		etTodate = (EditText)findViewById(R.id.etToDate);
		etFromdate = (EditText)findViewById(R.id.etFromDate);
		labuserlistview = (ListView)findViewById(R.id.labuserlistview);
		textView = (AutoCompleteTextView)findViewById(R.id.etPatientName);

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

		FromDate = etFromdate.getText().toString() ;
		FromDate = FromDate + " 00:00:00";

		ToDate = etTodate.getText().toString() ;
		ToDate = ToDate + " 23:59:59";

		FromDate = changeDateFormat(FromDate);
		ToDate = changeDateFormat(ToDate);

		if (isInternetOn(getApplicationContext()))
		{
			isSearch = 0;
			new GetPatientCheck().execute(new String[] { url });
		}
		else
		{
			showAlert("Internet Connection is not available..!","Alert");
		}
		newRegistration.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent iintent = new Intent(PhlebotomyMyListTabOn.this, AlllTestName.class);
						// Intent iintent = new Intent(MassagList.this, AllTestDataPhlebo.class);
						startActivity(iintent);
						//finish();
					}
				}
		);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isInternetOn(getApplicationContext())) {
					isSearch = 1;
					etpname_val = etpname.getText().toString() ;
					//etlabcode_val = etlabcode.getText().toString() ;

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

		Calendar c=Calendar.getInstance();
		mYear=c.get(Calendar.YEAR);
		mMonth=c.get(Calendar.MONTH);
		mDay=c.get(Calendar.DAY_OF_MONTH);

		etFromdate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				showDialog(FROM_DATE);
			}
		});

		etTodate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				showDialog(TO_DATE);
			}
		});
		//	new GetPatient().execute();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, labuser_Listview);
		textView.setAdapter(adapter);
	}

	@SuppressLint("NewApi") protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case FROM_DATE:
			DatePickerDialog dialog_from= new DatePickerDialog(this,mDateSetListener_from,mYear, mMonth, mDay);
			Calendar currentDate_from = Calendar.getInstance();
			dialog_from.getDatePicker().setMaxDate(currentDate_from.getTimeInMillis());
			return dialog_from;

		case TO_DATE:
			DatePickerDialog dialog_to= new DatePickerDialog(this,mDateSetListener_to,mYear, mMonth, mDay);
			Calendar currentDate_to = Calendar.getInstance();
			dialog_to.getDatePicker().setMaxDate(currentDate_to.getTimeInMillis());
			return dialog_to;
		}
		return null;
	}

	//DatePickerDialog from date
	private DatePickerDialog.OnDateSetListener mDateSetListener_from = new DatePickerDialog.OnDateSetListener()
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
				etFromdate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
			else
			{
				etFromdate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}

			if(month.length() < 2)
			{
				month = "0" + month;
				etFromdate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
			else
			{
				etFromdate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
		}
	};

	//DatePickerDialog to date
	private DatePickerDialog.OnDateSetListener mDateSetListener_to = new DatePickerDialog.OnDateSetListener()
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
				etTodate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
			else
			{
				etTodate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}

			if(month.length() < 2)
			{
				month = "0" + month;
				etTodate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
			else
			{
				etTodate.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
		}
	};


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
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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
			SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

			labuserList.clear();
			labuser_Listview.clear();

			// Showing progress dialog
			pDialog = new ProgressDialog(PhlebotomyMyListTabOn.this);
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

			if(labuserList.size() == 0)
			{
				showAlert1("No Registration available");
			}
			else
			{
				patientAdapter = new PhlebotomMyAdapter(PhlebotomyMyListTabOn.this,labuserList);
				labuserlistview.setAdapter(patientAdapter);
			}

			labuserlistview.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
				{
					// TODO Auto-generated method stub
					PhlebotomyMyListDetails myObj = (PhlebotomyMyListDetails) labuserList.get(arg2);


					testRegistrationID = myObj.getTestRegId();
					String PatientName = myObj.getPatientName();
					String DoctorName = myObj.getDoctorName();
					String LabCode = myObj.getLabCode();
					String Bal_Amt = myObj.getBal_Amt();
					String State = myObj.getState();
					String patientMbile = myObj.getpatientMbile();
					String patientAdrs = myObj.getpatientAdrs();
					String SelectedTest = myObj.getSelectedTest();
					String CollectionCenterName = myObj.getCollectionCenterName();

					String TotalAmount = myObj.getTotalAmount();
					String AmountPaid = myObj.getAmountPaid();

					//SharedPreferences.Editor editor = sharedpreferences.edit();
					//editor.putString("FebotestRegistrationID", FebotestRegistrationID);
					//editor.putString("FeboPatientNameTopass", FeboPatientNameTopass);
					//editor.commit();

					// Intent iintent = new Intent(DeviceRequest.this, RegistrationPhlebotomy.class);

					Intent iintent = new Intent(PhlebotomyMyListTabOn.this, PatientDetailsShowOn.class);

					iintent.putExtra("testRegistrationID", testRegistrationID);
					iintent.putExtra("PatientName", PatientName);
					iintent.putExtra("DoctorName", DoctorName);
					iintent.putExtra("LabCode", LabCode);
					iintent.putExtra("PendingBalnc", Bal_Amt);
					iintent.putExtra("State", State);
					iintent.putExtra("patientMbile", patientMbile);
					iintent.putExtra("patientAdrs", patientAdrs);
					iintent.putExtra("SelectedTest", SelectedTest);
					iintent.putExtra("CollectionCenterName", CollectionCenterName);
					iintent.putExtra("TotalAmount", TotalAmount);
					iintent.putExtra("AmountPaid", AmountPaid);



					startActivity(iintent);
					//finish();


					//new GetPDFReport().execute();
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
			nameValuePairs.add(new BasicNameValuePair("\"FromDate\"", "\"" + FromDate + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"ToDate\"", "\"" + ToDate + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"LabCode\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"UserType\"", "\"4\""));

			if (isSearch == 1) {
				nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
						"\"" + etpname_val + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PatientName\"", "\"\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"UserType\"", "\"4\""));


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
						PhlebotomyMyListDetails listd = new PhlebotomyMyListDetails();
						JSONObject c = patient.getJSONObject(i);

						String patient_name = c.getString(TAG_PATIENTNAME);
						String doctor_name = c.getString(TAG_DOCTORNAME);
						String lab_code = c.getString(TAG_LABCODE);
						String timeDate = c.getString(TAG_DATETIME);
						String balamt = c.getString(TAG_BALANCEAMT);
						testRegID = c.getString("TestRegnID");
						curState = c.getString("CurrentState");

						String patientMbile = c.getString("PatientMobile");
						String patientAdrs = c.getString("Add1");
						String CollectionCenterName = c.getString("CollectionCenterName");
						String SelectedTest = c.getString("SelectedTest");
						String TotalAmount = c.getString("Net");
						String AmountPaid = c.getString("AmountPaid");



						listd.setPatientName(patient_name);
						listd.setDoctorName(doctor_name);
						listd.setLabCode(lab_code);
						listd.setDate(timeDate);
						listd.setBal_Amt(balamt);
						listd.setTestRegId(testRegID);
						listd.setState(curState);

						listd.setpatientMbile(patientMbile);
						listd.setpatientAdrs(patientAdrs);
						listd.setCollectionCenterName(CollectionCenterName);
						listd.setSelectedTest(SelectedTest);
						listd.setTotalAmount(TotalAmount);
						listd.setAmountPaid(AmountPaid);

						// adding contact to contact list
						labuserList.add(listd);	
						
						originallabuserList.add(listd);
						labuser_Listview.add(patient_name);	
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

	private class GetPDFReport extends AsyncTask<Void, Void, Void> 
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(PhlebotomyMyListTabOn.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) 
		{
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			
			// Added by Dipen Shah on 20/1/2016
			if(usertype.equals("LabUser_T2"))
			{
				getPDFurl = getPDFurl + testRegistrationID + "&" + "LabID=" + selected_labidfrompref + "&" + "UserTypeID=" + "5";
			}
			else if(usertype.equals("Lab User") ||usertype.equals("Sr. Lab User") ||usertype.equals("Jr. Lab User") || usertype.equals("Admin") || usertype.equals("Pathologist") || usertype.equals("Pathologist_T2") || usertype.equals("Pathologist_T3")  )
			{
				getPDFurl = getPDFurl + testRegistrationID + "&" + "LabID=" + selected_labidfrompref + "&" + "UserTypeID=" + "3";
			}
			
			// Commented by Dipen Shah on 20/1/2016
			//getPDFurl = getPDFurl + testRegistrationID + "&" + "LabID=" + selected_labidfrompref + "&" + "UserTypeID=" + "3";

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(getPDFurl, ServiceHandler.GET);
			
			if (jsonStr != null && jsonStr.length() > 0) 
			{
				try 
				{
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					patient = jsonObj.getJSONArray("d");
					JSONObject obj = patient.getJSONObject(0);

					if(obj.getString("PdfName") != null && !(obj.getString("PdfName").equalsIgnoreCase("Null")) && !(obj.getString("PdfName").equalsIgnoreCase("")))
					{
						String params = obj.getString("PdfName").replace("~", "");
						pdfFileName = params;
					}
					else
					{
						pdfFileName = "";
					}
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			} 
			else 
			{
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			if(pdfFileName != null && !(pdfFileName.equalsIgnoreCase("")))
			{
				new DownloadFile(PhlebotomyMyListTabOn.this,"https://www.elabassist.com/"+pdfFileName).execute();
			}
			else
			{
				showAlert1("Report is not generated yet, Please contact to laboratory");

				getPDFurl = originalPDF_URL;
			}
		}		
	}

	private class DownloadFile extends AsyncTask<String, Void, String>
	{
		private Context mContext;
		private String pdfReportUrl;

		public DownloadFile (Context context,String pdfReceiptUrl){
			mContext = context;
			this.pdfReportUrl = pdfReceiptUrl;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(PhlebotomyMyListTabOn.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... strings)
		{
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

			pdfFileName = "";
			return filepath;

		}

		@Override
		protected void onPostExecute(String filepath) {
			super.onPostExecute(filepath);
			// TODO Auto-generated method stub
			final Handler handler = new Handler(Looper.getMainLooper());
			final String filePath = filepath;

			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filePath);

			final Uri uri = FileProvider.getUriForFile(mContext, "com.bluepearl.dnadiagnostics" + ".provider", file);

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					//Do something after 3000s
					if (pDialog != null && pDialog.isShowing()){
						pDialog.dismiss();
					}
					try{
						Intent intent = new Intent();
						intent.setDataAndType(uri,"application/pdf");
						intent.setAction(Intent.ACTION_VIEW);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
						mContext.startActivity(intent);
					}catch(ActivityNotFoundException exception){
						Toast.makeText(PhlebotomyMyListTabOn.this, "Download might take longer due to poor internet connection.", Toast.LENGTH_SHORT).show();
					}
				}
			}, 4000);

			getPDFurl = originalPDF_URL;
		}
	}

	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(flg)
		{
			Intent i = new Intent(PhlebotomyMyListTabOn.this,PhlebotomyMyListTabOn.class);
		//	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			//finish();
		}
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
				Intent i = new Intent(PhlebotomyMyListTabOn.this,PhlebotomyMyListTabOn.class);
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

/*	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		int id = item.getItemId();

		if(id == R.id.action_search){
			if(temp ==0){
				ll1.setVisibility(View.VISIBLE);

				Intent i = new Intent(PhlebotomyListTabOn.this,MyAppointments.class);
				startActivity(i);
				finish();

				temp ++;
			}else if (temp ==1){
				ll1.setVisibility(View.GONE);
				Intent i = new Intent(PhlebotomyListTabOn.this,PatientList.class);
				startActivity(i);
				finish();
				temp=0;
			}

			return true;
		}



		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	*/
}
