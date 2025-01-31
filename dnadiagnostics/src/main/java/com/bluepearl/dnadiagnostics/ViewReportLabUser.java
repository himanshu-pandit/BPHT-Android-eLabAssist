package com.bluepearl.dnadiagnostics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.database.Cursor;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class ViewReportLabUser extends BaseActivityDL
{
	private static final int PERMISSION_REQUEST_CODE = 201;
	private static final String[] PERMISSIONS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE
	};

	private LinearLayout ll1,ll12,ll13;
	private ImageButton search,btnSearchAdv;
	static EditText etlabcode,etTodate,etFromdate;
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
	AutoCompleteTextView textViewCCA ,etpname;
	LabUserAdapter patientAdapter;
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

	static int isEntitySearchS = 0;
	static String EntityTypeS = "";
	static String EntityIdS = "";


	Set<String> allCollectionCenterNameSet = new HashSet<String>();
	Set<String> allaffiliationNameSet = new HashSet<String>();


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




	static JSONArray patient = null;

	static ArrayList<LabListDetails> labuserList = new ArrayList<LabListDetails>();
	static ArrayList<LabListDetails> originallabuserList = new ArrayList<LabListDetails>();
	String[] patientNameArr, doctorNameArr,patientContactArr;
	static List<String> labuser_Listview = new ArrayList<String>();

	static List<String> labuser_ListviewCCnAF = new ArrayList<String>();

	static List<String> labuser_ListviewCCnAFId = new ArrayList<String>();

	ArrayList<CollectionCenterDetails> collectiocenterlist = new ArrayList<CollectionCenterDetails>();
	static List<String> collectiocenterlist_Listview = new ArrayList<String>();





	ArrayList<String> collectiocenterlist_id = new ArrayList<String>();

	ArrayList<AffiliationDetails> Affiliationlist = new ArrayList<AffiliationDetails>();
	static List<String> Affiliationlist_Listview = new ArrayList<String>();
	ArrayList<String> Affiliationlist_id = new ArrayList<String>();


	int tempCountSrch =0;
	String my_center_name = "";
	String my_center_id = "";
	ImageButton imgbtnClear, imgbtnClear2;

	EditText PatientLabCode;
	URL downloadUrl =null;
	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//setContentView(R.layout.activity_view_report_for_labuser);
//		contactPermission();

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//		if(contactExists(this,"9665655566")){
//			Toast.makeText(this, "Exists Contact", Toast.LENGTH_LONG).show();
//
//		}else{
//			Toast.makeText(this, "Not Exists", Toast.LENGTH_LONG).show();
//
//		}
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

		pid = sharedpreferences.getString("patientId", "");
		phone = sharedpreferences.getString("phno", "");
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		usertype = sharedpreferences.getString("usertype","");

		getLayoutInflater().inflate(R.layout.activity_view_report_for_labuser, frameLayout);
		//mDrawerList.setItemChecked(position, true);

		allCollectionCenterNameSet = sharedpreferences.getStringSet("setallCollectionCenterNameSet", null);
		allaffiliationNameSet = sharedpreferences.getStringSet("setallaffiliationNameSet", null);


		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		ll1 = (LinearLayout) findViewById(R.id.ll3);
		ll12 = (LinearLayout) findViewById(R.id.ll32);
		ll13 = (LinearLayout) findViewById(R.id.ll33);

		search = (ImageButton)findViewById(R.id.btnSearch);
		btnSearchAdv = (ImageButton)findViewById(R.id.btnSearchAdv_id);

		etpname = (AutoCompleteTextView)findViewById(R.id.etPatientName);
		etlabcode = (EditText)findViewById(R.id.etLabCode);
		etTodate = (EditText)findViewById(R.id.etToDate);
		etFromdate = (EditText)findViewById(R.id.etFromDate);
		labuserlistview = (ListView)findViewById(R.id.labuserlistview);
		//textView = (AutoCompleteTextView)findViewById(R.id.etPatientName);

		textViewCCA = (AutoCompleteTextView)findViewById(R.id.etColectioAffiltion);
		imgbtnClear = (ImageButton)findViewById(R.id.clearCC);
		imgbtnClear2 = (ImageButton)findViewById(R.id.clearCC2);



		etpname.setTypeface(custom_font);
		//textView.setTypeface(custom_font);
		textViewCCA.setTypeface(custom_font);
		etlabcode.setTypeface(custom_font);
		etTodate.setTypeface(custom_font);
		etFromdate.setTypeface(custom_font);

		textViewCCA.requestFocus();

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

		labuser_ListviewCCnAF.clear();
		labuser_ListviewCCnAFId.clear();

		imgbtnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textViewCCA.setText("");
			}
		});

		imgbtnClear2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etpname.setText("");
			}
		});



		if(allCollectionCenterNameSet != null)
		{
			Iterator<String> i1 = allCollectionCenterNameSet.iterator();
			while(i1.hasNext())
			{
				String my_test_data = i1.next();

				//String[] allTestsData = my_test_data.split(",");
				String[] allTestsData = my_test_data.split("#");


				my_center_name = allTestsData[0];
				my_center_id = allTestsData[1];


				collectiocenterlist_Listview.add(my_center_name);
				collectiocenterlist_id.add(my_center_id);

				labuser_ListviewCCnAF.add(my_center_name);
				labuser_ListviewCCnAFId.add(my_center_id+"#3");

			}
			// testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
			//  testname.setAdapter(testnameadapter);
		}

		if(allaffiliationNameSet != null)
		{
			Iterator<String> i1 = allaffiliationNameSet.iterator();
			while(i1.hasNext())
			{
				String my_test_data = i1.next();

				//String[] allTestsData = my_test_data.split(",");
				String[] allTestsData = my_test_data.split("#");


				my_center_name = allTestsData[0];
				my_center_id = allTestsData[1];

				Affiliationlist_Listview.add(my_center_name);
				Affiliationlist_id.add(my_center_id);

				labuser_ListviewCCnAF.add(my_center_name);
				labuser_ListviewCCnAFId.add(my_center_id+"#4");

			}
			// testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
			//  testname.setAdapter(testnameadapter);
		}



		//	ArrayAdapter<String> adapterC = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, labuser_ListviewCCnAF);
		ArrayAdapter<String> adapterC  = new ArrayAdapter<String>(this, R.layout.listauto_item, R.id.item, labuser_ListviewCCnAF);
		textViewCCA.setAdapter(adapterC);

		btnSearchAdv.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						if(tempCountSrch == 0){
							etpname.setVisibility(View.VISIBLE);

							//btnSearchAdv.setImageResource(R.drawable.uparrowww);

							tempCountSrch++;
						}else {
							etpname.setVisibility(View.VISIBLE);
							btnSearchAdv.setImageResource(R.drawable.downarrowww);
							tempCountSrch =0;
						}


					}
				}
		);

		textViewCCA.addTextChangedListener(

				new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						System.out.println("posi beforeTextChanged ");
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						System.out.println("posi onTextChanged" );

						if (textViewCCA.isPerformingCompletion()) {
							// An item has been selected from the list. Ignore.

							System.out.println("posi onTextChanged If "+ s +" "+start +" "+ before + " "+ count);

							String tempCcnAf = String.valueOf(s);
							int index = -1;
							for(int i=0;i<labuser_ListviewCCnAF.size();i++)
							{
								if(tempCcnAf.equals(labuser_ListviewCCnAF.get(i)))
								{

									isEntitySearchS = 1;
									index = i;
									break;
								}else{
									isEntitySearchS = 0;
								}
							}

							System.out.println("posi onTextChanged IfItem //" + index +"//"+isEntitySearchS );

							String tempCcnAfId = labuser_ListviewCCnAFId.get(index);
							String[] Entitys = tempCcnAfId.split("#");
							String EntityId = Entitys[0];
							String EntityType = Entitys[1];
							System.out.println("posi onTextChanged IfItem //" + tempCcnAfId + "//" + EntityId + "//" + EntityType);
							EntityTypeS = EntityType;
							EntityIdS = EntityId;

							new GetPatientCheck().execute(new String[] { url });


							return;
						}else {

							System.out.println("posi onTextChanged Else");
							isEntitySearchS = 0;

							EntityTypeS = null;
							EntityIdS = null;
						}
					}

					@Override
					public void afterTextChanged(Editable s) {
						System.out.println("posi afterTextChanged" );
					}
				}
		);

		/*textViewCCA.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, ....){
				//if "s" is your "special" text clear the textview
			}
		});*/

		textViewCCA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,	int position, long arg3) {

			/*
				System.out.println("posi onTextChanged IfItem //"+ arg0.getItemAtPosition(position).toString() +"//"+arg1 +"//"+ position + "//"+ arg3);
               String tempCcnAf =arg0.getItemAtPosition(position).toString();
				int index = -1;
				for(int i=0;i<labuser_ListviewCCnAF.size();i++)
				{
					if(tempCcnAf.equals(labuser_ListviewCCnAF.get(i)))
					{

						isEntitySearchS = 1;
						index = i;
						break;
					}else{
						isEntitySearchS = 0;
					}
				}

				System.out.println("posi onTextChanged IfItem //" + index +"//"+isEntitySearchS );

		     	if(index >0) {
					String tempCcnAfId = labuser_ListviewCCnAFId.get(index);
					String[] Entitys = tempCcnAfId.split("#");
					String EntityId = Entitys[0];
					String EntityType = Entitys[1];
					System.out.println("posi onTextChanged IfItem //" + tempCcnAfId + "//" + EntityId + "//" + EntityType);
					EntityTypeS = EntityType;
					EntityIdS = EntityId;
				}else{
					EntityTypeS = null;
					EntityIdS = null;
				}
				new GetPatientCheck().execute(new String[] { url });

				*/

			}
		});

		//textViewCCA.setOnItemSelectedListener()
		textViewCCA.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						Toast.makeText(ViewReportLabUser.this, "posi "+position +" "+id, Toast.LENGTH_SHORT).show();
						System.out.println("posi "+position +" "+id );
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						System.out.println("posi onNothingSelected");
					}
				}
		);



		if (!checkPermission()) {

			requestPermission();

		} else {

			if (isInternetOn(getApplicationContext()))
			{
				isSearch = 0;
				new GetPatientCheck().execute(new String[] { url });
			}
			else
			{
				showAlert("Internet Connection is not available..!","Alert");
			}

		}

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
		etpname.setAdapter(adapter);


		etpname.addTextChangedListener(
				new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

					}

					@Override
					public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

						if (isInternetOn(getApplicationContext())) {
							isSearch = 1;
							etpname_val = etpname.getText().toString() ;
							//etlabcode_val = etlabcode.getText().toString() ;

							int textcharcter =etpname_val.length();
							Log.d("etpname_val ", "onTextChanged: "+textcharcter);
								/*if(textcharcter>2){
									FromDate = etFromdate.getText().toString() ;
									FromDate = FromDate + " 00:00:00";
									ToDate = etTodate.getText().toString() ;
									ToDate = ToDate + " 23:59:59";
									FromDate = changeDateFormat(FromDate);
									ToDate = changeDateFormat(ToDate);
									new GetPatientCheck().execute(new String[] { url });
									flg = true;
								}*/




						} else {
							showAlert("Internet Connection is not available..!",
									"Alert");
						}


					}

					@Override
					public void afterTextChanged(Editable editable) {

					}
				}
		);




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

			//patientAdapter = new LabUserAdapter(ViewReportLabUser.this,labuserList);
			//labuserlistview.setAdapter(patientAdapter);
			labuserlistview.setAdapter(null);

			// Showing progress dialog
			pDialog = new ProgressDialog(ViewReportLabUser.this);
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

			if(isEntitySearchS == 0)
			{
				textViewCCA.setText("");
			}

			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}

			if(labuserList.size() == 0)
			{
				showAlert1("No reports are available");

				patientAdapter = new  LabUserAdapter(ViewReportLabUser.this,labuserList);

				patientAdapter.notifyDataSetChanged();
				labuserlistview.setAdapter(patientAdapter);

				textViewCCA.setText("");
				etpname.setText("");
				etpname_val ="";
				//isEntitySearchS =0;

				EntityTypeS =  null ;
				EntityIdS = null;
			}
			else
			{
				patientAdapter = new LabUserAdapter(ViewReportLabUser.this,labuserList);
				labuserlistview.setAdapter(patientAdapter);
			}
			labuserlistview.isClickable();
			labuserlistview.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
				{
					// TODO Auto-generated method stub
					LabListDetails myObj = (LabListDetails) labuserList.get(arg2);
					Intent intent = new Intent(ViewReportLabUser.this,ViewReportAttachment.class);
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
			//nameValuePairs.add(new BasicNameValuePair("\"LabCode\"", "\"\""));
			if (isSearch == 1) {

				nameValuePairs.add(new BasicNameValuePair("\"LabCode\"","\"" + etpname_val + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"LabCode\"", "\"\""));
			}

			if (isSearch == 1) {
				nameValuePairs.add(new BasicNameValuePair("\"PatientName\"","\"" + etpname_val + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PatientName\"", "\"\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"UserType\"", "\"3\""));

			if (isEntitySearchS == 1) {
				nameValuePairs.add(new BasicNameValuePair("\"EntityTypeId\"","\"" + EntityTypeS + "\""));
				nameValuePairs.add(new BasicNameValuePair("\"EntityId\"","\"" + EntityIdS + "\""));
			}else{
				//nameValuePairs.add(new BasicNameValuePair("\"EntityTypeId\"", "\"\""));
				//nameValuePairs.add(new BasicNameValuePair("\"EntityId\"", "\"\""));
			}

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			String strToServer = "{" + finalString + "}";

			Log.d("PatientPOST", "PatientPOST: "+strToServer);
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
					Log.d("PatientPOSTResp", "inputStream: "+patient);

					labuserList.clear();
					// looping through All Contacts
					for (int i = 0; i < patient.length(); i++)
					{
						LabListDetails listd = new LabListDetails();
						JSONObject c = patient.getJSONObject(i);

						String patient_name = c.getString(TAG_PATIENTNAME);
						String doctor_name = c.getString(TAG_DOCTORNAME);
						String lab_code = c.getString(TAG_LABCODE);
						String timeDate = c.getString(TAG_DATETIME);
						String balamt = c.getString(TAG_BALANCEAMT);
						testRegID = c.getString("TestRegnID");
						curState = c.getString("CurrentState");
						String SelctTest = c.getString("SelectedTest");
						String testlistdetail =c.getString("TotalTestList");
						String ApproveTest = c.getString("ApproveTest");
						String TotalTest = c.getString("TotalTest");
						String PatientMobile = c.getString("PatientMobile");
						String DoctorMobile = c.getString("DoctorMobile");
						String CollectionCenterMobile = c.getString("CollectionCenterMobile");
						String AffiliationMobile = c.getString("AffiliationMobile");

						listd.setPatientName(patient_name);
						listd.setDoctorName(doctor_name);
						listd.setLabCode(lab_code);
						listd.setDate(timeDate);
						listd.setBal_Amt(balamt);
						listd.setTestRegId(testRegID);
						listd.setState(curState);
						listd.setTestName(SelctTest);
						listd.setTestListDetails(testlistdetail);
						listd.setApproveTest(ApproveTest);
						listd.setTotalTest(TotalTest);
						listd.setPMobile(PatientMobile);
						listd.setDMobile(DoctorMobile);
						listd.setAMobile(AffiliationMobile);
						listd.setCCMobile(CollectionCenterMobile);

						// adding contact to contact list
						labuserList.add(listd);

						originallabuserList.add(listd);
						labuser_Listview.add(patient_name);
						labuser_Listview.add(lab_code);
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


	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(flg)
		{
			Intent i = new Intent(ViewReportLabUser.this,ViewReportLabUser.class);
			//	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			//finish();
		}
		Intent i = new Intent(ViewReportLabUser.this,RegisterPatientNew.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		//finish();
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
						Intent i = new Intent(ViewReportLabUser.this,
								ViewReportLabUser.class);
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

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("from++","return from whats app after pdf sharing..");
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
			if(temp ==1){
				ll1.setVisibility(View.VISIBLE);
				ll12.setVisibility(View.VISIBLE);
				ll13.setVisibility(View.VISIBLE);
				//temp ++;
				temp=0;
			}else if (temp ==0){
				ll1.setVisibility(View.GONE);
				ll12.setVisibility(View.GONE);
				ll13.setVisibility(View.GONE);
				//temp=0;
				temp ++;
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
														ViewReportLabUser.this,
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
		new AlertDialog.Builder(ViewReportLabUser.this)
				.setMessage(message)
				.setPositiveButton("Allow", okListener)
				.setNegativeButton("Cancel", cancelListener)
				.create()
				.show();
	}










}
