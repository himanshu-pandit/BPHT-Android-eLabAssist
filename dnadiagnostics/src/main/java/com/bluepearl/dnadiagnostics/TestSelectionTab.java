package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TestSelectionTab extends Activity {
	Typeface custom_font;
	Button btn1,btnOther;
	ListView listviewtestname;
	Button btnregisterName;
	ProgressBar mProgressBar,mProgressBarProfile,mProgressBarCategory;
	AutoCompleteTextView testname, testprofile, testnamebycategory;
	int tname_length = 0;
	boolean flg = false;
	TableLayout myTestTable;
	TableLayout myTestTableHeader;
	String test, test_id, final_url, url_reg,t_price,tdiscount,t_price_bycategory,tdiscount_bycategory,profileprice,profilediscounprice;
	String p_name, p_age, p_dob, p_phno, p_email, p_id, spinner_item;
	int p_gender;
	Spinner selectTest, category_spinner;
	String test_pid;
	String test_profileprice;
	String test_profilediscountprice;
	LinearLayout lineartname, lineartprofile, lineartcategory, lineartnamecategory;
	TextView category_title;
	static String new_ids = "";
	static String new_profile_ids = "";
	String ids = "";
	String text_testbycateory;
	EditText patientname;
	static String outputDate = "";
	Bundle data;
	private static String filePath = null;
	private ProgressDialog pDialog;
	static String finalResult = "";
	GPSTracker gps;
	public static final String MyPREFERENCES = "MyPrefs";
	static SharedPreferences sharedpreferences;
	static String tid = null;
	static String testproid = null;
	static String pid = null;
	static String phone = null;
	static String add_val = null;
	static String date_val = null;
	static String date_val_temp = null;
	static String dname_val = null;
	static String selectedDocId = "";
	static String residencial_address = "";
	static String official_address = "";
	static String status = "";
	static String text = "";
	static String entered_test_name = null;
	static String addval = null;
	static String pin = null;
	static String dnameval = null;
	static String new_pid = null;
	static String new_pname = null;
	static String new_pAge = null;
	static int new_pGender = 0;
	static String pnameval = null;
	static String selected_labidfrompref = null;
	static String selectedCenterId = null;
	static String register_patientgender = null;
	static String selected_patient_name = null;
	static String popular_ids = null;
	static String test_value;
	int my_flg = -1;
	static String family_pid = null;
	static String family_pname = null;
	static String family_page = null;
	static String family_pgender = "0";
	static String selectedPatientId = "";
	static String updated_val = null;
	static String pval = null;
	static String selected_labnamefromintent = null;
	String selectedtidFromIntent = "";
	static String homecollection = "";

	String my_test_name = "";
	String my_testid = "";
	String my_test_price = "";
	String my_test_discountprice = "";
	String my_test_profile_name = "";
	String my_test_profile_id = "";
	String my_test_profileprice = "";
	String my_test_profilediscountprice = "";
	String my_test_category_name = "";
	static String strDate = "";
	static String pname_fromprofile = "";

	RelativeLayout rrr;
	private boolean isVisibleTest = false;

	String my_test_Short = "";

	private String appointment_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/PatientAppointMent_Global";
	// URL to get Test Name
	private static final String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?LabID=";
	private static String myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?LabID=";

	// JSON Node names
	private static final String TAG_TESTNAME = "TestName";
	private static final String TAG_TESTID = "TestID";

	// contacts JSONArray
	JSONArray Testname = null;
	JSONArray Testprofile = null;

	String Test = null;
	String Testprofileid = null;

	JSONArray Testnamebycategory = null;
	JSONArray Testcategory = null;

	ArrayList<String> test_name = new ArrayList<String>();
	ArrayList<String> testid = new ArrayList<String>();
	ArrayList<String> testprice = new ArrayList<String>();
	ArrayList<String> testdiscountprice = new ArrayList<String>();
	ArrayList<Integer> selectedtestid = new ArrayList<Integer>();

	ArrayList<String> testcategory_Listview = new ArrayList<String>();
	ArrayList<String> testbycategory_name = new ArrayList<String>();
	ArrayList<Integer> testidbycategory = new ArrayList<Integer>();
	ArrayList<Integer> selectedtestid_bycategory = new ArrayList<Integer>();

	ArrayList<String> test_profile_name = new ArrayList<String>();
	ArrayList<String> testpid = new ArrayList<String>();
	ArrayList<String> testprofileprice = new ArrayList<String>();
	ArrayList<String> testprofilediscountprice = new ArrayList<String>();
	ArrayList<Integer> selectedtestpid = new ArrayList<Integer>();

	ArrayList<String> selectedNewIDs = new ArrayList<String>();

	ArrayList<String> selectedNewProfileIDs = new ArrayList<String>();

	ArrayList<String> adapterList1 = new ArrayList<String>();

	ArrayList<String> List = new ArrayList<String>();

	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> dataAdapter;
	private ArrayAdapter<String> testnameadapter;
	private ArrayAdapter<String> testprofileadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.fragment_select_test);

		custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		tid = sharedpreferences.getString("testid", "");

		testproid = sharedpreferences.getString("testprofileid", "");
		popular_ids = sharedpreferences.getString("popularids", "");
		pid = sharedpreferences.getString("patientId", "");
		phone = sharedpreferences.getString("phno", "");
		date_val = sharedpreferences.getString("DateTime", "");
		date_val_temp = date_val;
		selectedCenterId = sharedpreferences.getString("centerid", "");
		selectedPatientId = sharedpreferences.getString("SelectedPatient", "");
		updated_val = sharedpreferences.getString("updated", "");
		register_patientgender = sharedpreferences.getString("self_patientgender", "");

		filePath = sharedpreferences.getString("filePath", "");
		selected_patient_name = sharedpreferences.getString("SelectedPatientName", "");

		residencial_address = sharedpreferences.getString("res_add", "");
		official_address = sharedpreferences.getString("office_add", "");
		entered_test_name = sharedpreferences.getString("test_name", "");
		pname_fromprofile = sharedpreferences.getString("patient_name", "");

		addval = sharedpreferences.getString("address", "");
		pin = sharedpreferences.getString("pincode", "");
		dnameval = sharedpreferences.getString("Doctor", "");
		selectedDocId = sharedpreferences.getString("doc_id", "");
		pid = sharedpreferences.getString("patientId", "");
		phone = sharedpreferences.getString("phno", "");

		family_pid = sharedpreferences.getString("SelectedPatient", "");
		family_pname = sharedpreferences.getString("SelectedPatientName", "");

		family_page = sharedpreferences.getString("SelectedPatientAge", "");
		family_pgender = sharedpreferences.getString("SelectedPatientGender", "0");

		selectedtidFromIntent = sharedpreferences.getString("testIdFromPatientInfo", "");
		homecollection = sharedpreferences.getString("home_collection", "");

		btn1 = (Button) findViewById(R.id.BookButton);
		btnOther = (Button) findViewById(R.id.MemberButton);
		testname = (AutoCompleteTextView) findViewById(R.id.etTestName);
		myTestTable = (TableLayout) findViewById(R.id.TestNameTable);
		myTestTableHeader = (TableLayout) findViewById(R.id.TestNameTableHeader);
		selectTest = (Spinner) findViewById(R.id.spinner_testSelect);
		testprofile = (AutoCompleteTextView) findViewById(R.id.etProfileSearch);
		lineartname = (LinearLayout) findViewById(R.id.linear2);
		lineartprofile = (LinearLayout) findViewById(R.id.linear3);
		lineartcategory = (LinearLayout) findViewById(R.id.linear4);
		lineartnamecategory = (LinearLayout) findViewById(R.id.linear6);
		testnamebycategory = (AutoCompleteTextView) findViewById(R.id.etNameByCategorySearch);
		category_spinner = (Spinner) findViewById(R.id.spinner_category);
		category_title = (TextView) findViewById(R.id.textView1);
		patientname = (EditText) findViewById(R.id.etPname);

		rrr  = (RelativeLayout) findViewById(R.id.RRRR);

		testname.setTypeface(custom_font);
		testnamebycategory.setTypeface(custom_font);
		testprofile.setTypeface(custom_font);
		category_title.setTypeface(custom_btnfont);
		patientname.setTypeface(custom_font);
		btn1.setTypeface(custom_btnfont);
		btnOther.setTypeface(custom_btnfont);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"dd-MMM-yyyy HH:mm");
		strDate = sdf.format(c.getTime());

		text_testbycateory = testnamebycategory.getText().toString().toLowerCase(Locale.getDefault());

		// Spinner Drop down elements
		List.add("Search Test");
		List.add("Search Category");
		List.add("Search Profile");

		// changes

		Set<String> allTestNamesFromPrefs = new HashSet<String>();
		Set<String> profilenamefrompref = new HashSet<String>();
		//Set<String> profileidfrompref = new HashSet<String>();
		Set<String> categorynamefrompref = new HashSet<String>();

		allTestNamesFromPrefs = sharedpreferences.getStringSet("allTestNames", null);
		profilenamefrompref = sharedpreferences.getStringSet("setprofilename", null);
		//profileidfrompref = sharedpreferences.getStringSet("setprofileid", null);
		categorynamefrompref = sharedpreferences.getStringSet("setcategoryname", null);

		Log.d("setprofilename",""+profilenamefrompref);
		//Iterator<String> i4 = profileidfrompref.iterator();

		if(allTestNamesFromPrefs != null)
		{
			Iterator<String> i1 = allTestNamesFromPrefs.iterator();
			while(i1.hasNext())
			{
				String my_test_data = i1.next();
				String[] allTestsData = my_test_data.split("#");

				my_test_name = allTestsData[0];
				my_testid = allTestsData[1];
				my_test_price = allTestsData[2];
				my_test_discountprice = allTestsData[3];
				my_test_Short= allTestsData[4];

				test_name.add(my_test_name+",( "+my_test_Short+")");
				//test_name.add(my_test_name);
				testid.add(my_testid);
				testprice.add(my_test_price);
				testdiscountprice.add(my_test_discountprice);
			}
			//testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
			testnameadapter = new ArrayAdapter<String>(getBaseContext(), R.layout.listauto_item_test, R.id.item,test_name);

			testname.setAdapter(testnameadapter);
		}

		if(profilenamefrompref != null)
		{
			Iterator<String> i3 = profilenamefrompref.iterator();
			while(i3.hasNext())
			{
				String my_profile_data = i3.next();
				String[] allProfilesData = my_profile_data.split("#");

				try {
					my_test_profile_name = allProfilesData[0];
					my_test_profile_id = allProfilesData[1];
					my_test_profileprice = allProfilesData[2];
					my_test_profilediscountprice = allProfilesData[3];

					test_profile_name.add(my_test_profile_name);
					testpid.add(my_test_profile_id);
					testprofileprice.add(my_test_profileprice);
					testprofilediscountprice.add(my_test_profilediscountprice);
				}catch(ArrayIndexOutOfBoundsException e){
					Log.e("outofindex",""+e);
				}
			}
			//testprofileadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_profile_name);
			testprofileadapter = new ArrayAdapter<String>(getBaseContext(), R.layout.listauto_item_test, R.id.item,test_profile_name);

			testprofile.setAdapter(testprofileadapter);
		}
/////////////////////////////////////////////////////////////////
		if(categorynamefrompref != null)
		{
			Iterator<String> i5 = categorynamefrompref.iterator();
			while(i5.hasNext())
			{
				my_test_category_name = i5.next();

				testcategory_Listview.add(my_test_category_name);
			}
			//ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,testcategory_Listview);
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,testcategory_Listview);

			category_spinner.setAdapter(dataAdapter);
			dataAdapter.notifyDataSetChanged();
		}
//////////////////////////////////		// changes ends here


		if (tid.equals("")) {
			tid = "";
		}
		if (testproid.equals("")) {
			testproid = "";
		}
		if (filePath.equals("")) {
			filePath = "";
		}
		if (entered_test_name.equals("")) {
			entered_test_name = "";
		}
		String strpname=  sharedpreferences.getString("SelectedPatientName","");

		if(strpname.equalsIgnoreCase("null") || strpname.equalsIgnoreCase(""))
		{
			patientname.setText("SELF");
		}
		else
		{
			patientname.setText(selected_patient_name);
		}

		if(selectedtidFromIntent != null && !(selectedtidFromIntent.equalsIgnoreCase("")))
		{
			String [] subStringofMaster = null;
			subStringofMaster = selectedtidFromIntent.split("/");
			for(int k =0; k<subStringofMaster.length;k++)
			{
				String[] testEntries = subStringofMaster[k].split("#");

				String strTestName = testEntries[0];
				String strTestId = testEntries[1];
				String strTestPrice = testEntries[2];
				String strTestDiscountPrice = testEntries[3];

				populateTable(strTestName, myTestTable, strTestId, 1, strTestPrice, strTestDiscountPrice);
			}
		}

		// Creating adapter for spinner
		dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, List);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////////////////////////////////////////////////////////////////////////
		// attaching data adapter to spinner
		selectTest.setAdapter(dataAdapter);

		selectTest.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String selectedItem = arg0.getItemAtPosition(arg2).toString();

				if (selectedItem == "Search Test") {
					lineartname.setVisibility(View.VISIBLE);
					lineartprofile.setVisibility(View.GONE);
					lineartcategory.setVisibility(View.GONE);
				}
				if (selectedItem == "Search Category") {
					lineartname.setVisibility(View.GONE);
					lineartprofile.setVisibility(View.GONE);
					lineartcategory.setVisibility(View.VISIBLE);
				} else if (selectedItem == "Search Profile") {
					lineartname.setVisibility(View.GONE);
					lineartprofile.setVisibility(View.VISIBLE);
					lineartcategory.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
/////////////////////////////////////////////////////////////////////
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pval = patientname.getText().toString();

				Calendar current = Calendar.getInstance();

				SimpleDateFormat sdfnew = new SimpleDateFormat(
						"MM-dd-yyyy HH:mm:ss");

				final String current_date = sdfnew.format(current.getTime());

				for (int k = 0; k < selectedNewIDs.size(); k++) {
					if (new_ids.equals("")) {
						new_ids = selectedNewIDs.get(k);
					} else {
						new_ids = new_ids + "," + selectedNewIDs.get(k);
					}
				}

				for (int k = 0; k < selectedNewProfileIDs.size(); k++) {
					if (new_profile_ids.equals("")) {
						new_profile_ids = selectedNewProfileIDs.get(k);
					} else {
						new_profile_ids = new_profile_ids + ","	+ selectedNewProfileIDs.get(k);
					}
				}

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("testid", new_ids);
				editor.putString("testprofileid", new_profile_ids);

				editor.commit();

				if (isInternetOn(getApplicationContext())) {
					new uploadAllData().execute(new String[] { appointment_url });
				} else {
					alertBox("Internet Connection is not available..!");
				}
			}
		});


		testname.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				my_flg = position;

				if(testid.size() == 1)
				{
					if (my_flg != -1) {
						position = 0;
					}
				}
				test = arg0.getItemAtPosition(position).toString();

				String strSelectedTestName = test;
				int index = -1;

				for(int i=0;i<test_name.size();i++)
				{
					if(strSelectedTestName.equals(test_name.get(i)))
					{
						index = i;
						break;
					}
				}

				test_id = testid.get(index).toString();
				t_price = testprice.get(index).toString();
				tdiscount = testdiscountprice.get(index).toString();

				if (selectedtestid.contains(Integer.parseInt(test_id))) {
					Toast.makeText(getApplicationContext(),	"This test name is already selected",Toast.LENGTH_LONG).show();
					testname.setText("");
				} else {
					selectedtestid.add(Integer.parseInt(test_id));
					populateTable(test, myTestTable, test_id, 1, t_price, tdiscount);

					testname.setText("");
				}
			}
		});
//////////////////////////////////////////////////////////////////
		category_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				spinner_item = category_spinner.getSelectedItem()
						.toString();
				try {
					myurl = myurl + selected_labidfrompref + "&" + "CategoryName=" + URLEncoder.encode(spinner_item, "UTF-8") + "&TestName=";
					new GetTestNameByCategory().execute();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
/////////////////////////////////////////////////////////////////////////////////
		testnamebycategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				my_flg = position;

				if(testidbycategory.size() == 1)
				{
					if (my_flg != -1) {
						position = 0;
					}
				}

				test = arg0.getItemAtPosition(position).toString();

				String strSelectedTestName = test;
				int index = -1;
				for(int i=0;i<testbycategory_name.size();i++)
				{
					if(strSelectedTestName.equals(testbycategory_name.get(i)))
					{
						index = i;
						break;
					}
				}
				test_id = testidbycategory.get(index).toString();
				t_price_bycategory = testprice.get(index).toString();
				tdiscount_bycategory = testdiscountprice.get(index).toString();

				if (selectedtestid.contains(Integer
						.parseInt(test_id))) {
					Toast.makeText(getApplicationContext(),
							"This test name is already selected",
							Toast.LENGTH_LONG).show();
					testnamebycategory.setText("");
				} else {
					selectedtestid.add(Integer.parseInt(test_id));
					populateTable(test, myTestTable, test_id, 2, t_price_bycategory, tdiscount_bycategory);

					testnamebycategory.setText("");

				}
			}
		});


		testprofile.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				my_flg = position;

				if(testpid.size() == 1)
				{
					if (my_flg != -1) {
						position = 0;
					}
				}

				test = arg0.getItemAtPosition(position).toString();

				String strSelectedTestName = test;
				int index = -1;
				for(int i=0;i<test_profile_name.size();i++)
				{
					if(strSelectedTestName.equals(test_profile_name.get(i)))
					{
						index = i;
						break;
					}
				}
				test_pid = testpid.get(index).toString();
				test_profileprice = testprofileprice.get(index).toString();
				test_profilediscountprice = testprofilediscountprice.get(index).toString();

				if (selectedtestpid.contains(Integer.parseInt(test_pid))) {
					Toast.makeText(getApplicationContext(),
							"This test profile is already selected",
							Toast.LENGTH_LONG).show();
					testprofile.setText("");
				} else {
					selectedtestpid.add(Integer.parseInt(test_pid));
					populateTable1(test, myTestTable, test_pid, 3,test_profileprice,test_profilediscountprice);

					testprofile.setText("");
				}
			}
		});

		btnOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

/*
				if (isVisibleTest) {

					rrr.setVisibility(View.GONE);
					isVisibleTest = false;
				} else if (!isVisibleTest) {

					rrr.setVisibility(View.VISIBLE);
					isVisibleTest = true;
				}

*/
			// TODO Auto-generated method stub
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("ActivityName", "Select");
				editor.putString("testid", new_ids);
				editor.putString("testprofileid", new_profile_ids);

				editor.commit();

				String masterEntries = "";
				String testEntries = "";
				for (int j = 0; j < myTestTable.getChildCount(); j++) {
					TableRow tr = (TableRow) myTestTable.getChildAt(j);

					String strTestName = ((TextView) tr.getChildAt(1)).getText().toString();
					String strId = ((TextView) tr.getChildAt(2)).getText().toString();
					String strPrice = ((TextView) tr.getChildAt(3)).getText().toString();
					String strDiscount = ((TextView) tr.getChildAt(4)).getText().toString();

					testEntries = strTestName + "#" + strId + "#" + strPrice + "#" + strDiscount;

					if (masterEntries.equalsIgnoreCase("")) {
						masterEntries = testEntries;
					} else {
						masterEntries = masterEntries + "/" + testEntries;
					}
				}
				Intent i = new Intent(TestSelectionTab.this, PatientInformation.class);
				i.putExtra("isFromSelectTest", "Yes");
				i.putExtra("testIdsFromIntent", masterEntries);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();


			}

		});
	}

	//*** Async task class to get json by making HTTP call **//*
	private class GetTestNameByCategory extends	AsyncTask<String, Void, ArrayList<String>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			testbycategory_name.clear();
			testidbycategory.clear();
		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(myurl,
					ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					Testnamebycategory = jsonObj.getJSONArray("d");

					// looping through All Contacts
					for (int i = 0; i < Testnamebycategory.length(); i++) {
						// String listd = new String();

						JSONObject c = Testnamebycategory.getJSONObject(i);

						String listd = c.getString(TAG_TESTNAME);
						int test_id = c.getInt(TAG_TESTID);
						String price = c.getString("TestPrice");
						String discount_price = c.getString("TestDiscountPrice");

						testbycategory_name.add(listd);
						testidbycategory.add(test_id);
						testprice.add(price);
						testdiscountprice.add(discount_price);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return testbycategory_name;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);

			myurl = url;

			//adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,testbycategory_name);
			adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.listauto_item_test, R.id.item,testbycategory_name);


			testnamebycategory.setAdapter(adapter);
		}
	}

	private class uploadAllData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog pDialog = new
			pDialog = new ProgressDialog(TestSelectionTab.this);
			pDialog.setMessage("Please wait...");
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

			if (result.equals("Appointment_Booked")) {
				showAlert("Your Appointment booked successfully. Please make yourself available on time at your booking location.");
				clearPreferences();

			} else {
				showAlert1("Appointment not booked");
				date_val = date_val_temp;

			}
		}
	}

	@SuppressWarnings("deprecation")
	public static String POST(String url) {
		InputStream inputStream = null;
		String result = "";
		String encodedImage = "";

		if (!(filePath.equals("")) ) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
			/*
			 * MultipartEntity entity1 = new MultipartEntity(
			 * HttpMultipartMode.BROWSER_COMPATIBLE);
			 */
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
			byte[] data = bos.toByteArray();

			encodedImage = Base64.encodeToString(data, Base64.NO_WRAP);
		} else {
			encodedImage = "";
		}

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(selected_labidfrompref != null && !(selected_labidfrompref.equalsIgnoreCase("")))
			{
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			}

			nameValuePairs.add(new BasicNameValuePair("\"PatientID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Username\"", "\"" + phone + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedTest\"", "\"" + new_ids + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedProfile\"","\"" + new_profile_ids + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedPopularTest\"","\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"EnteredTest\"", "\"" + test_value + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Prescription\"", "\""+ encodedImage + "\""));
			if(date_val.equalsIgnoreCase("null") || date_val.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"","\"" + strDate + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"","\"" + date_val + "\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"AppointmentAddress\"","\"" + addval.toString().trim() + "\""));


			if (dnameval.equalsIgnoreCase("") || dnameval.equalsIgnoreCase("null")) {

				nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\"" + 0 + "\""));
				nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"",
						"\"SELF\""));


			} else {
				if(selectedDocId.equalsIgnoreCase("null") || selectedDocId.equalsIgnoreCase(""))
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\"" + 0 + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"",
							"\"" + dnameval + "\""));
				}
				else
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\""
							+ selectedDocId + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"",
							"\"\""));
				}
			}
			String str=sharedpreferences.getString("self", "");
			if (str.equals("yes"))  {


				nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
				if(pval.equalsIgnoreCase("null") || pval.equalsIgnoreCase("") || pval.equalsIgnoreCase("self"))
				{
					nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
							"\"SELF\""));
				}
				else
				{
					nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
							"\"" + pname_fromprofile + "\""));
				}
				nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"false\""));

				nameValuePairs.add(new BasicNameValuePair("\"Age\"",
						"\"" + 0 + "\""));

				if(register_patientgender.equalsIgnoreCase("null") || register_patientgender.equalsIgnoreCase(""))
				{
					nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
							"\"" + 0 + "\""));
				}
				else
				{
					nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
							"\"" + register_patientgender + "\""));
				}

			}
			else
			{
				if(pval.equalsIgnoreCase("self"))
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
							"\"SELF\""));
					nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"false\""));

					nameValuePairs.add(new BasicNameValuePair("\"Age\"",
							"\"" + 0 + "\""));
					if(register_patientgender.equalsIgnoreCase("null") || register_patientgender.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + register_patientgender + "\""));
					}
				}
				else
				{
					if(selectedPatientId.equalsIgnoreCase("null") || selectedPatientId.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
								"\"" + pval + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"true\""));

						if(family_page.equalsIgnoreCase(""))
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"",
									"\"" + 0 + "\""));
						}
						else
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + family_page + "\""));
						}
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + family_pgender + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\""
								+ selectedPatientId + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
								"\"\""));
						nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"true\""));

						if(family_page.equalsIgnoreCase(""))
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"",
									"\"" + 0 + "\""));
						}
						else
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + family_page + "\""));
						}
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + family_pgender + "\""));
					}
				}
			}
			nameValuePairs.add(new BasicNameValuePair("\"Pincode\"","\"" + pin + "\""));

			if(selectedCenterId.equalsIgnoreCase("null") || selectedCenterId.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"CollectionCenterID\"","\"" + 0 + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"CollectionCenterID\"","\"" + selectedCenterId + "\""));
			}

			if(homecollection.equalsIgnoreCase("true"))
			{
				nameValuePairs.add(new BasicNameValuePair("\"IsHomeCollection\"","\"true\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"IsHomeCollection\"","\"false\""));
			}

			nameValuePairs.add(new BasicNameValuePair("\"IsUpdated\"","\"" + updated_val + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"DeviceType\"", "\"" + 1 + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"patientaddress\"","\""+addval+"\""));
			nameValuePairs.add(new BasicNameValuePair("\"emailid\"","\"\""));

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
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				String jsonformattString = result.replaceAll("\\\\", "");
				try {
					JSONObject jsonObj = new JSONObject(jsonformattString);
					JSONObject resultObj = (JSONObject) jsonObj.get("d");
					finalResult = resultObj.getString("Result");
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

	// Code to populate table
	public void populateTable1(String tname, TableLayout myTestTable,
			String selectedtestid, int dropdownid,String profileprice, String profilediscountprice) {
		String rupeestr = "<span class=\"WebRupee\">&#8377;</span>";
		Log.d("pprice1065+",""+profileprice+profilediscountprice);
		if (dropdownid == 3) {
			if (ids.equals("")) {
				selectedNewProfileIDs.add(selectedtestid);
			} else {
				selectedNewProfileIDs.add(selectedtestid);
			}

		} else {
			if (ids.equals("")) {
				selectedNewIDs.add(selectedtestid);
			} else {
				selectedNewIDs.add(selectedtestid);
			}

		}

		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		tableRow.setGravity(Gravity.CENTER);

		TextView txtTestname = new TextView(this);
		ImageButton removebtn = new ImageButton(this);
		TextView txtTestID = new TextView(this);
		TextView txtTestPrice = new TextView(this);
		TextView txtTestDiscountPrice = new TextView(this);

		removebtn.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT, 0));
		removebtn.setImageResource(R.drawable.delete_new);
		removebtn.setBackgroundColor(Color.TRANSPARENT);

		txtTestname.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, 1f));
		txtTestname.setText(tname);
		txtTestname.setTextColor(Color.BLACK);
		txtTestname.setTextSize(12);
		txtTestname.setTypeface(custom_font);

		txtTestID.setLayoutParams(new TableRow.LayoutParams(
				0,
				TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
		txtTestID.setText(selectedtestid);
		txtTestID.setTextColor(Color.BLACK);
		txtTestID.setTextSize(12);
		txtTestID.setVisibility(View.GONE);

		txtTestPrice.setLayoutParams(new TableRow.LayoutParams(
				0,
				TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
		txtTestPrice.setTextColor(Color.BLACK);
		txtTestPrice.setTextSize(12);
		//txtTestPrice.setPadding(10, 0, 0, 0);

		if(profileprice.equals("0") || profileprice.equals("0.0"))
		{
			txtTestPrice.setText((Html.fromHtml(rupeestr)) + " NA");
			txtTestPrice.setTextColor(Color.BLACK);
			txtTestPrice.setTypeface(custom_font);
		}
		else
		{
			txtTestPrice.setText((Html.fromHtml(rupeestr))+profileprice);
			txtTestPrice.setTextColor(Color.parseColor("#f00000"));
			txtTestPrice.setPaintFlags(txtTestPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			txtTestPrice.setTypeface(custom_font);
		}
		txtTestPrice.setTextSize(12);
		//txtTestPrice.setPadding(0, 0, 10, 0);

		txtTestDiscountPrice.setLayoutParams(new TableRow.LayoutParams(
				0,
				TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
		txtTestDiscountPrice.setTextColor(Color.BLACK);
		txtTestDiscountPrice.setTextSize(12);
		txtTestDiscountPrice.setTypeface(custom_font);

		if(profilediscountprice.equals("0") || profilediscountprice.equals("0.0"))
		{
			txtTestDiscountPrice.setText((Html.fromHtml(rupeestr)) + " NA");
			txtTestDiscountPrice.setTextColor(Color.BLACK);
			txtTestDiscountPrice.setTypeface(custom_font);
		}
		else
		{
			txtTestDiscountPrice.setText((Html.fromHtml(rupeestr))+profilediscountprice);
			txtTestDiscountPrice.setTextColor(Color.parseColor("#009900"));
			txtTestDiscountPrice.setTypeface(custom_font);
		}
		txtTestDiscountPrice.setTextSize(12);

		removebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				TableRow tr1 = (TableRow) v.getParent();
				TableLayout table1 = (TableLayout) tr1.getParent();

				int idToBeRemoved = Integer.parseInt(((TextView) tr1
						.getChildAt(2)).getText().toString());
				table1.removeView(tr1);

				if (TestSelectionTab.this.selectedtestid
						.contains(idToBeRemoved)) {
					for (int k = 0; k < TestSelectionTab.this.selectedtestid
							.size(); k++) {
						if (TestSelectionTab.this.selectedtestid.get(k) == idToBeRemoved) {
							TestSelectionTab.this.selectedtestid.remove(k);
						}
					}

				} else if (selectedtestid_bycategory.contains(idToBeRemoved)) {
					for (int k = 0; k < selectedtestid_bycategory.size(); k++) {
						if (selectedtestid_bycategory.get(k) == idToBeRemoved) {
							selectedtestid_bycategory.remove(k);
						}
					}
				} else if (selectedtestpid.contains(idToBeRemoved)) {
					for (int k = 0; k < selectedtestpid.size(); k++) {
						if (selectedtestpid.get(k) == idToBeRemoved) {
							selectedtestpid.remove(k);
						}
					}
				}
				if (selectedNewIDs.contains(String.valueOf(idToBeRemoved))) {
					for (int k = 0; k < selectedNewIDs.size(); k++) {
						if (selectedNewIDs.get(k).equalsIgnoreCase(
								String.valueOf(idToBeRemoved))) {
							selectedNewIDs.remove(k);
						}
					}
				}
			}
		});

		tableRow.addView(removebtn);
		tableRow.addView(txtTestname);
		tableRow.addView(txtTestID);
		tableRow.addView(txtTestPrice);
		tableRow.addView(txtTestDiscountPrice);

		myTestTable.addView(tableRow, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed();
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

	private void alertBox(String msg) {
		Builder alert = new Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText("Alert");
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		alert.setCustomTitle(Mytitle);

		alert.setIcon(R.drawable.sign_logoelab);
		alert.setMessage(msg);
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

	public void showAlert(String message) {
		Builder builder = new Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText(Html.fromHtml("<font color='#33cc33'>!!Success!!</font>"));
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				clearPreferences();

				Intent i = new Intent(TestSelectionTab.this,
						AddressSelection.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
		Button noButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
	}

	public void showAlert1(String message) {
		Builder builder = new Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText(Html.fromHtml("<font color='#ff0000'>Failed</font>"));
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				Intent i = new Intent(TestSelectionTab.this,
						AddressSelection.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
		Button noButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
	}

	public void clearPreferences() {
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.remove("testid");
		editor.remove("testprofileid");
		editor.remove("test_name");
		editor.remove("filePath");
		editor.remove("Doctor");
		editor.remove("DateTime");
		editor.remove("datetime");
		editor.remove("doc_id");
		editor.remove("ActivityName");
		editor.remove("updated");
		editor.remove("popularids");
		editor.remove("SelectedPatient");
		editor.remove("SelectedPatientAge");
		editor.remove("SelectedPatientGender");
		editor.remove("address");
		editor.remove("pincode");
		editor.remove("selectedtidFromIntent");
		editor.remove("homecollection");
		editor.remove("centerid");

		selectedDocId = "";
		new_ids = "";
		new_profile_ids = "";
		ids = "";

		editor.commit();
	}
	
	
	// Code to populate table for test profile
	public void populateTable(String tname, TableLayout myTestTable,
			String selectedtestid, int dropdownid, String tprice, String tdiscountprice) {
		String rupeestr = "<span class=\"WebRupee\">&#8377;</span>";

		if (dropdownid == 3) {
			if (ids.equals("")) {
				selectedNewProfileIDs.add(selectedtestid);
			} else {
				selectedNewProfileIDs.add(selectedtestid);
			}

		} else {
			if (ids.equals("")) {
				selectedNewIDs.add(selectedtestid);
			} else {
				selectedNewIDs.add(selectedtestid);
			}

		}

		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		tableRow.setGravity(Gravity.CENTER);

		TextView txtTestname = new TextView(this);
		ImageButton removebtn = new ImageButton(this);
		TextView txtTestID = new TextView(this);
		TextView txtTestPrice = new TextView(this);
		TextView txtTestDiscountPrice = new TextView(this);

		removebtn.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT, 0));
		removebtn.setImageResource(R.drawable.delete_new);
		removebtn.setBackgroundColor(Color.TRANSPARENT);


		txtTestname.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, 1f));
		txtTestname.setText(tname);
		txtTestname.setTextColor(Color.BLACK);
		txtTestname.setTextSize(12);
		txtTestname.setTypeface(custom_font);

		txtTestID.setLayoutParams(new TableRow.LayoutParams(
				0,
				TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
		txtTestID.setText(selectedtestid);
		txtTestID.setTextColor(Color.BLACK);
		txtTestID.setTextSize(12);
		txtTestID.setVisibility(View.GONE);

		txtTestPrice.setLayoutParams(new TableRow.LayoutParams(
				0,
				TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
		txtTestPrice.setTextColor(Color.BLACK);
		txtTestPrice.setTextSize(12);
		//txtTestPrice.setPadding(10, 0, 0, 0);

		if(tprice.equals("0") || tprice.equals("0.0"))
		{
			txtTestPrice.setText((Html.fromHtml(rupeestr)) + " NA");
			txtTestPrice.setTextColor(Color.BLACK);
			txtTestPrice.setTypeface(custom_font);
		}
		else
		{
			txtTestPrice.setText((Html.fromHtml(rupeestr))+tprice);
			txtTestPrice.setTextColor(Color.parseColor("#f00000"));
			txtTestPrice.setPaintFlags(txtTestPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			txtTestPrice.setTypeface(custom_font);
		}
		txtTestPrice.setTextSize(12);
		//txtTestPrice.setPadding(0, 0, 10, 0);

		txtTestDiscountPrice.setLayoutParams(new TableRow.LayoutParams(
				0,
				TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
		txtTestDiscountPrice.setTextColor(Color.BLACK);
		txtTestDiscountPrice.setTextSize(12);
		txtTestDiscountPrice.setTypeface(custom_font);

		if(tdiscountprice.equals("0") || tdiscountprice.equals("0.0"))
		{
			txtTestDiscountPrice.setText((Html.fromHtml(rupeestr)) + " NA");
			txtTestDiscountPrice.setTextColor(Color.BLACK);
			txtTestDiscountPrice.setTypeface(custom_font);
		}
		else
		{
			txtTestDiscountPrice.setText((Html.fromHtml(rupeestr))+tdiscountprice);
			txtTestDiscountPrice.setTextColor(Color.parseColor("#009900"));
			txtTestDiscountPrice.setTypeface(custom_font);
		}
		txtTestDiscountPrice.setTextSize(12);
		//txtTestDiscountPrice.setPadding(0, 0, 10, 0);

		removebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				TableRow tr1 = (TableRow) v.getParent();
				TableLayout table1 = (TableLayout) tr1.getParent();

				int idToBeRemoved = Integer.parseInt(((TextView) tr1
						.getChildAt(2)).getText().toString());
				table1.removeView(tr1);

				if (TestSelectionTab.this.selectedtestid
						.contains(idToBeRemoved)) {
					for (int k = 0; k < TestSelectionTab.this.selectedtestid
							.size(); k++) {
						if (TestSelectionTab.this.selectedtestid.get(k) == idToBeRemoved) {
							TestSelectionTab.this.selectedtestid.remove(k);
						}
					}

				} else if (selectedtestid_bycategory.contains(idToBeRemoved)) {
					for (int k = 0; k < selectedtestid_bycategory.size(); k++) {
						if (selectedtestid_bycategory.get(k) == idToBeRemoved) {
							selectedtestid_bycategory.remove(k);
						}
					}
				} else if (selectedtestpid.contains(idToBeRemoved)) {
					for (int k = 0; k < selectedtestpid.size(); k++) {
						if (selectedtestpid.get(k) == idToBeRemoved) {
							selectedtestpid.remove(k);
						}
					}
				}
				if (selectedNewIDs.contains(String.valueOf(idToBeRemoved))) {
					for (int k = 0; k < selectedNewIDs.size(); k++) {
						if (selectedNewIDs.get(k).equalsIgnoreCase(
								String.valueOf(idToBeRemoved))) {
							selectedNewIDs.remove(k);
						}
					}
				}
			}
		});

		tableRow.addView(removebtn);
		tableRow.addView(txtTestname);
		tableRow.addView(txtTestID);
		tableRow.addView(txtTestPrice);
		tableRow.addView(txtTestDiscountPrice);

		myTestTable.addView(tableRow, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));
	}
}
