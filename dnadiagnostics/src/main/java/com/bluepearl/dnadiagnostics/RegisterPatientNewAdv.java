package com.bluepearl.dnadiagnostics;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class RegisterPatientNewAdv extends BaseActivityDL {

	private static final int PERMISSION_REQUEST_CODE = 208;
	private static final String[] PERMISSIONS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
	};

	public static final String MyPREFERENCES = "MyPrefs";
	static SharedPreferences sharedpreferences;
	Typeface custom_font ,custom_bold_font ;



	LinearLayout LLPopularTest ,LLUploadPrecription, LLSelectTest;
	Button BtnPopularTest ,BtnUploadPrecription, BtnSelectTest ,  RegisterPatiebtBtn;
	EditText EtPatientName,EntertestName;
	TableLayout myTestTableEnterTest;

	Set<String> popularTestDataFromPrefs;
	static String EnteredFromPatientInfo = "";
	String selectedPopularIdFromPatientInfo = "";


	/////////////////////////////////////////////////////////////
	int clickcount=0;
	ImageView imgPrescription;
	ImageView imgPrescriptionTwo;
	ImageView imgPrescriptionThree;
	ImageView imgPrescriptionFour;
	Button btnCapturePicture;
	private static final String TAG = "Upload Photo";
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 101;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO = 112;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE = 123;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR = 134;
	private static final int BROWSER_IMAGE_REQUEST_CODE = 201;
	private static final int BROWSER_IMAGE_REQUEST_CODETWO = 212;
	private static final int BROWSER_IMAGE_REQUEST_CODETHREE = 223;
	private static final int BROWSER_IMAGE_REQUEST_CODEFOUR = 234;
	////////////////////////////////////////////////////////////
	public static final int MEDIA_TYPE_IMAGE = 11;
	public static final int MEDIA_TYPE_IMAGETWO = 22;
	public static final int MEDIA_TYPE_IMAGETHREE = 33;
	public static final int MEDIA_TYPE_IMAGEFOUR = 44;
	private Uri fileUri; // file url to store image
	private Uri fileUriTwo; // file url to store Second image
	private Uri fileUriThree; // file url to store image
	private Uri fileUriFour; // file url to store image
	public static final String IMAGE_DIRECTORY_NAME = "eLAB Prescription";
	public static final String IMAGE_DIRECTORY_NAMETWO = "eLAB PrescriptionTwo";
	public static final String IMAGE_DIRECTORY_NAMETHREE = "eLAB PrescriptionThree";
	public static final String IMAGE_DIRECTORY_NAMEFOUR = "eLAB PrescriptionFour";
	private static String filePath = null;
	private static String filePathTwo = null;
	private static String filePathThree = null;
	private static String filePathFour = null;
	static String encodedImage = "";
	static String encodedImageFromPref = "";
	static String selectedimagefrompatientinfo = "";

	static String encodedImageTwo = "";
	static String encodedImageFromPrefTwo = "";
	static String selectedimagefrompatientinfoTwo = "";

	static String encodedImageThree = "";
	static String encodedImageFromPrefThree = "";
	static String selectedimagefrompatientinfoThree = "";

	static String encodedImageFour = "";
	static String encodedImageFromPrefFour = "";
	static String selectedimagefrompatientinfoFour = "";

	AutoCompleteTextView testname, testprofile, testnamebycategory;
	TableLayout myTestTable;
	Spinner selectTest, category_spinner;
	LinearLayout lineartname, lineartprofile, lineartcategory, lineartnamecategory;
	TextView category_title;
	RelativeLayout rrr;
	String text_testbycateory;
	String ids = "";

	static String idsS = "";

	String my_test_name = "";
	String my_testid = "";
	String my_test_price = "";
	String my_test_discountprice = "";
	String my_test_profile_name = "";
	String my_test_profile_id = "";
	String my_test_category_name = "";
	String selectedtidFromIntent = "";
	int my_flg = -1;
	String test, test_id, final_url, url_reg,t_price,tdiscount,t_price_bycategory,tdiscount_bycategory,profileprice,profilediscounprice;
	String test_pid,spinner_item;

	ArrayList<String> List = new ArrayList<String>();
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
	ArrayList<Integer> selectedtestpid = new ArrayList<Integer>();
	ArrayList<String> selectedNewIDs = new ArrayList<String>();
	ArrayList<String> selectedNewProfileIDs = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> dataAdapter;
	private ArrayAdapter<String> testnameadapter;
	private ArrayAdapter<String> testprofileadapter;

	static String selected_labidfrompref = null;
	JSONArray Testnamebycategory = null;

	private static final String TAG_TESTNAME = "TestName";
	private static final String TAG_TESTID = "TestID";

	private ProgressDialog pDialog;
	static String finalResult = "";

	static String tid = null;

	static String pid = null;
	static String phone = null;
	static String EnteredTest_shrdpref = null;
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
	static String dnameval = null;
	static String family_pid = null;
	static String family_pname = null;
	static String family_page = null;
	static String family_pgender = "0";
	static String pnameval = null;
	static String pin = null;
	static String selectedCenterId = null;
	static String register_patientgender = null;
	static String updated_val = null;
	static String pval = "deepak pandit";
	static String selected_patient_name = null;
	static String selectedPatientId = "";
	static String pRefDoc_fromprofile;
	static String pGender_fromprofile = "";
	static String pname_fromprofile = "";
	static String pAge_fromprofile = "";
	static String homecollection = "";
	static String new_ids = "";
	static String new_profile_ids = "";
	String my_test_Short = "";

	private String appointment_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/PatientAppointMent_Global";

	private static final String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?LabID=";
	private static String myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?LabID=";

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		getLayoutInflater().inflate(R.layout.activity_register_patient_phlebotomy_adv, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		EnteredFromPatientInfo = sharedpreferences.getString("Enteredtestidfromintent", "");
		selectedPopularIdFromPatientInfo = sharedpreferences.getString("populartestidfromintent", "");

		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		//selected_labidfrompref = "84135443-7a22-4f96-b7ac-9fa211b750c5";
		//selected_labidfrompref = "4dc18595-28af-4183-9a07-611bc6ceccf6";



		pid = sharedpreferences.getString("patientId", "");

		selectedimagefrompatientinfo = sharedpreferences.getString("imageFromPatientInfo", "");
		selectedimagefrompatientinfoTwo = sharedpreferences.getString("imageFromPatientInfoTwo", "");
		selectedimagefrompatientinfoThree = sharedpreferences.getString("imageFromPatientInfoThree", "");
		selectedimagefrompatientinfoFour = sharedpreferences.getString("imageFromPatientInfoFour", "");

		filePath = sharedpreferences.getString("filePath", "");
		filePathTwo = sharedpreferences.getString("filePathTwo", "");
		filePathThree = sharedpreferences.getString("filePathThree", "");
		filePathFour = sharedpreferences.getString("filePathFour", "");

		selectedtidFromIntent = sharedpreferences.getString("testIdFromPatientInfo", "");
		date_val = sharedpreferences.getString("DateTime", "");
		dnameval = sharedpreferences.getString("Doctor", "");
		register_patientgender = sharedpreferences.getString("self_patientgender", "");
		selectedCenterId = sharedpreferences.getString("centerid", "");
		date_val_temp = date_val;


		RegisterPatiebtBtn = (Button) findViewById(R.id.RegisterPatiebtBtn_id);

		LLPopularTest = (LinearLayout) findViewById(R.id.LL_PopularTest_id);
		LLUploadPrecription = (LinearLayout) findViewById(R.id.LL_UploadPrescription_id);
		LLSelectTest = (LinearLayout) findViewById(R.id.LL_SelectTest_id);

		BtnPopularTest = (Button) findViewById(R.id.buttonPopularTest_id);
		BtnUploadPrecription = (Button) findViewById(R.id.buttonUploadPrescription_id);
		BtnSelectTest = (Button) findViewById(R.id.buttonSelectTest_id);

		EtPatientName = (EditText) findViewById(R.id.etPnameText);
		EntertestName = (EditText) findViewById(R.id.EntertestName_id);
		myTestTableEnterTest = (TableLayout) findViewById(R.id.myTestTableEnterTest_id);

		btnCapturePicture = (Button) findViewById(R.id.CaptureButton);
		imgPrescription = (ImageView) findViewById(R.id.uploadpic);
		imgPrescriptionTwo = (ImageView) findViewById(R.id.uploadpicTwo);
		imgPrescriptionThree = (ImageView) findViewById(R.id.uploadpicThree);
		imgPrescriptionFour = (ImageView) findViewById(R.id.uploadpicFour);



		testname = (AutoCompleteTextView) findViewById(R.id.etTestName);
		myTestTable = (TableLayout) findViewById(R.id.TestNameTable);
		selectTest = (Spinner) findViewById(R.id.spinner_testSelect);
		testprofile = (AutoCompleteTextView) findViewById(R.id.etProfileSearch);
		lineartname = (LinearLayout) findViewById(R.id.linear2);
		lineartprofile = (LinearLayout) findViewById(R.id.linear3);
		lineartcategory = (LinearLayout) findViewById(R.id.linear4);
		lineartnamecategory = (LinearLayout) findViewById(R.id.linear6);
		testnamebycategory = (AutoCompleteTextView) findViewById(R.id.etNameByCategorySearch);
		category_spinner = (Spinner) findViewById(R.id.spinner_category);
		category_title = (TextView) findViewById(R.id.textView1);
		rrr  = (RelativeLayout) findViewById(R.id.RRRR);


		Bundle bundle = getIntent().getExtras();

		if (bundle != null)
		{
			pname_fromprofile = bundle.getString("StrPatientName");
			pAge_fromprofile = bundle.getString("StrPatientAge");
			pGender_fromprofile = bundle.getString("StrPatientGeder");
			phone  = bundle.getString("StrPatientMobileNo");
			addval = bundle.getString("StrPatientAdress");
			pRefDoc_fromprofile = bundle.getString("StrPatientRefDoctor");
		}
		else
		{
			clearPreferences();

			Intent i = new Intent(RegisterPatientNewAdv.this,
					//PhlebotomyTabOn.class);
					RegisterPatientNew.class);
			//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			//finish();

		}

		EtPatientName.setText(pname_fromprofile);

//////////////////////////////////   ///////////////////////////////////////
		LLPopularTest.setVisibility(View.GONE);
		LLUploadPrecription.setVisibility(View.VISIBLE);
		LLSelectTest.setVisibility(View.GONE);

		BtnUploadPrecription.setBackgroundColor(Color.parseColor("#ffffff"));
		BtnPopularTest.setBackgroundResource(R.drawable.borderbtn);
		BtnSelectTest.setBackgroundResource(R.drawable.borderbtn);

		//	BtnSelectTest.setTextColor(Color.parseColor("#009688"));
		BtnUploadPrecription.setTextColor(Color.parseColor("#000000"));

		EtPatientName.setHintTextColor(Color.parseColor("#009688"));

		if (!checkPermission()) {
			requestPermission();
		}

		BtnPopularTest.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {

						LLPopularTest.setVisibility(View.VISIBLE);
						LLUploadPrecription.setVisibility(View.GONE);
						LLSelectTest.setVisibility(View.GONE);

						BtnPopularTest.setBackgroundColor(Color.parseColor("#ffffff"));
						BtnUploadPrecription.setBackgroundResource(R.drawable.borderbtn);
						BtnSelectTest.setBackgroundResource(R.drawable.borderbtn);

						//BtnSelectTest.setTextColor(Color.parseColor("#009688"));
						//	BtnUploadPrecription.setTextColor(Color.parseColor("#009688"));
						BtnPopularTest.setTextColor(Color.parseColor("#000000"));

					}
				}
		);


		BtnUploadPrecription.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {

						LLPopularTest.setVisibility(View.GONE);
						LLUploadPrecription.setVisibility(View.VISIBLE);
						LLSelectTest.setVisibility(View.GONE);

						BtnUploadPrecription.setBackgroundColor(Color.parseColor("#ffffff"));
						BtnPopularTest.setBackgroundResource(R.drawable.borderbtn);
						BtnSelectTest.setBackgroundResource(R.drawable.borderbtn);

						//BtnSelectTest.setTextColor(Color.parseColor("#009688"));
						BtnUploadPrecription.setTextColor(Color.parseColor("#000000"));
						//BtnPopularTest.setTextColor(Color.parseColor("#009688"));


					}
				}
		);


		BtnSelectTest.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {

						LLPopularTest.setVisibility(View.GONE);
						LLUploadPrecription.setVisibility(View.GONE);
						LLSelectTest.setVisibility(View.VISIBLE);

						BtnSelectTest.setBackgroundColor(Color.parseColor("#ffffff"));
						BtnUploadPrecription.setBackgroundResource(R.drawable.borderbtn);
						BtnPopularTest.setBackgroundResource(R.drawable.borderbtn);

						BtnSelectTest.setTextColor(Color.parseColor("#000000"));
						//	BtnUploadPrecription.setTextColor(Color.parseColor("#009688"));
						//	BtnPopularTest.setTextColor(Color.parseColor("#009688"));


					}
				}
		);
/////////////////////////////// test selection data ///////////////////////////////////

		text_testbycateory = testnamebycategory.getText().toString().toLowerCase(Locale.getDefault());

		// Spinner Drop down elements
		List.add("Search Test");
		List.add("Search Category");
		List.add("Search Profile");

		// changes

		Set<String> allTestNamesFromPrefs = new HashSet<String>();
		Set<String> profilenamefrompref = new HashSet<String>();
		Set<String> profileidfrompref = new HashSet<String>();
		Set<String> categorynamefrompref = new HashSet<String>();

		allTestNamesFromPrefs = sharedpreferences.getStringSet("allTestNames", null);
		profilenamefrompref = sharedpreferences.getStringSet("setprofilename", null);
		profileidfrompref = sharedpreferences.getStringSet("popularTestData", null);
		categorynamefrompref = sharedpreferences.getStringSet("setcategoryname", null);

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
			testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
			testname.setAdapter(testnameadapter);
		}

		if(profilenamefrompref != null)
		{
			Iterator<String> i3 = profilenamefrompref.iterator();
			while(i3.hasNext())
			{
				String my_profile_data = i3.next();
				String[] allProfilesData = my_profile_data.split("#");

				my_test_profile_name = allProfilesData[0];
				my_test_profile_id = allProfilesData[1];

				test_profile_name.add(my_test_profile_name);
				testpid.add(my_test_profile_id);
			}
			testprofileadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_profile_name);
			testprofile.setAdapter(testprofileadapter);
		}

		if(categorynamefrompref != null)
		{
			Iterator<String> i5 = categorynamefrompref.iterator();
			while(i5.hasNext())
			{
				my_test_category_name = i5.next();

				testcategory_Listview.add(my_test_category_name);
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					getBaseContext(),
					android.R.layout.simple_dropdown_item_1line,
					testcategory_Listview);
			category_spinner.setAdapter(dataAdapter);
			dataAdapter.notifyDataSetChanged();
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

				populateTableSelectText(strTestName, myTestTable, strTestId, 1, strTestPrice, strTestDiscountPrice);
			}
		}

		// Creating adapter for spinner
		dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, List);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		selectTest.setAdapter(dataAdapter);

		selectTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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



		testname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
					Toast.makeText(getApplicationContext(),
							"This test name is already selected",
							Toast.LENGTH_LONG).show();
					testname.setText("");
				} else {
					selectedtestid.add(Integer.parseInt(test_id));
					populateTableSelectText(test, myTestTable, test_id, 1, t_price, tdiscount);

					testname.setText("");
				}
			}
		});

		category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

		testnamebycategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
					populateTableSelectText(test, myTestTable, test_id, 2, t_price_bycategory, tdiscount_bycategory);

					testnamebycategory.setText("");

				}
			}
		});


		testprofile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

				if (selectedtestpid.contains(Integer.parseInt(test_pid))) {
					Toast.makeText(getApplicationContext(),
							"This test profile is already selected",
							Toast.LENGTH_LONG).show();
					testprofile.setText("");
				} else {
					selectedtestpid.add(Integer.parseInt(test_pid));
					populateTableSelectText1(test, myTestTable, test_pid, 3);

					testprofile.setText("");
				}
			}
		});



////////////////////////////  popular test data  //////////////////////////////////////////

//Set<String> popularTestDataFromPrefs = new HashSet<String>();
		popularTestDataFromPrefs = new HashSet<String>();

		popularTestDataFromPrefs = sharedpreferences.getStringSet("popularTestData", null);



		if (popularTestDataFromPrefs != null) {
			Iterator<String> i1 = popularTestDataFromPrefs.iterator();

			while (i1.hasNext()) {
				String my_test_data = i1.next();
				String[] popularTestsData = my_test_data.split("#");

				my_test_name = popularTestsData[0];
				my_testid = popularTestsData[1];
				my_test_price = popularTestsData[2];
				my_test_discountprice = popularTestsData[3];

				populateTable(my_test_name, my_testid, my_test_price, my_test_discountprice);
			}
		} else {
			//lblpopular.setVisibility(View.GONE);
			//lblOR.setVisibility(View.GONE);
			Toast.makeText(this, "No Popular Test", Toast.LENGTH_SHORT).show();
		}
		// changes ends here
///////////////////////////////  Upload Pricpition////////////////////////////////

		if (filePath.equals("")) {
			filePath = "";
		}
		if (filePathTwo.equals("")) {
			filePathTwo = "";
		}
		if (filePathThree.equals("")) {
			filePathThree = "";
		}
		if (filePathFour.equals("")) {
			filePathFour = "";
		}


		///////////////////////// for first image

		if (selectedimagefrompatientinfo != null && !(selectedimagefrompatientinfo.equalsIgnoreCase(""))) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			final Bitmap bitmap = BitmapFactory.decodeFile(selectedimagefrompatientinfo, options);
			imgPrescription.setImageBitmap(bitmap);

			//bookbtn1.setEnabled(true);


			if (!(selectedimagefrompatientinfo.equals(""))) {
				BitmapFactory.Options options1 = new BitmapFactory.Options();
				options.inSampleSize = 2;
				final Bitmap bitmap1 = BitmapFactory.decodeFile(selectedimagefrompatientinfo, options1);
				/*
				 * MultipartEntity entity1 = new MultipartEntity(
				 * HttpMultipartMode.BROWSER_COMPATIBLE);
				 */
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, bos);
				byte[] data = bos.toByteArray();

				encodedImageFromPref = Base64.encodeToString(data, Base64.NO_WRAP);

			} else {
				encodedImageFromPref = "";
			}
		}
		///////////////////////// for second image

		if (selectedimagefrompatientinfoTwo != null && !(selectedimagefrompatientinfoTwo.equalsIgnoreCase(""))) {
			BitmapFactory.Options optionsTwo = new BitmapFactory.Options();
			optionsTwo.inSampleSize = 8;
			final Bitmap bitmapTwo = BitmapFactory.decodeFile(selectedimagefrompatientinfoTwo, optionsTwo);
			imgPrescriptionTwo.setImageBitmap(bitmapTwo);

			//bookbtn1.setEnabled(true);


			if (!(selectedimagefrompatientinfoTwo.equals(""))) {
				BitmapFactory.Options options1Two = new BitmapFactory.Options();
				optionsTwo.inSampleSize = 2;
				final Bitmap bitmap1Two = BitmapFactory.decodeFile(selectedimagefrompatientinfoTwo, options1Two);

				// MultipartEntity entity1 = new MultipartEntity(
				// HttpMultipartMode.BROWSER_COMPATIBLE);

				ByteArrayOutputStream bosTwo = new ByteArrayOutputStream();
				bitmap1Two.compress(Bitmap.CompressFormat.JPEG, 50, bosTwo);
				byte[] dataTwo = bosTwo.toByteArray();

				encodedImageFromPrefTwo = Base64.encodeToString(dataTwo, Base64.NO_WRAP);

			} else {
				encodedImageFromPrefTwo = "";
			}
		}


		//////////////////////////////// for Third image
		if (selectedimagefrompatientinfoThree != null && !(selectedimagefrompatientinfoThree.equalsIgnoreCase(""))) {
			BitmapFactory.Options optionsThree = new BitmapFactory.Options();
			optionsThree.inSampleSize = 8;
			final Bitmap bitmapThree = BitmapFactory.decodeFile(selectedimagefrompatientinfoThree, optionsThree);
			imgPrescriptionThree.setImageBitmap(bitmapThree);

			//bookbtn1.setEnabled(true);


			if (!(selectedimagefrompatientinfoThree.equals(""))) {
				BitmapFactory.Options options1Three = new BitmapFactory.Options();
				optionsThree.inSampleSize = 2;
				final Bitmap bitmap1Three = BitmapFactory.decodeFile(selectedimagefrompatientinfoThree, options1Three);

				//  MultipartEntity entity1 = new MultipartEntity(
				// HttpMultipartMode.BROWSER_COMPATIBLE);

				ByteArrayOutputStream bosThree = new ByteArrayOutputStream();
				bitmap1Three.compress(Bitmap.CompressFormat.JPEG, 50, bosThree);
				byte[] dataThree = bosThree.toByteArray();

				encodedImageFromPrefThree = Base64.encodeToString(dataThree, Base64.NO_WRAP);

			} else {
				encodedImageFromPrefThree = "";
			}
		}

		///////////////////////////////for fourth image
		if (selectedimagefrompatientinfoFour != null && !(selectedimagefrompatientinfoFour.equalsIgnoreCase(""))) {
			BitmapFactory.Options optionsFour = new BitmapFactory.Options();
			optionsFour.inSampleSize = 8;
			final Bitmap bitmapFour = BitmapFactory.decodeFile(selectedimagefrompatientinfoFour, optionsFour);
			imgPrescriptionFour.setImageBitmap(bitmapFour);

			//bookbtn1.setEnabled(true);


			if (!(selectedimagefrompatientinfoFour.equals(""))) {
				BitmapFactory.Options options1Four = new BitmapFactory.Options();
				optionsFour.inSampleSize = 2;
				final Bitmap bitmap1Four = BitmapFactory.decodeFile(selectedimagefrompatientinfoFour, options1Four);

				//  MultipartEntity entity1 = new MultipartEntity(
				// HttpMultipartMode.BROWSER_COMPATIBLE);

				ByteArrayOutputStream bosFour = new ByteArrayOutputStream();
				bitmap1Four.compress(Bitmap.CompressFormat.JPEG, 50, bosFour);
				byte[] dataFour = bosFour.toByteArray();

				encodedImageFromPrefFour = Base64.encodeToString(dataFour, Base64.NO_WRAP);

			} else {
				encodedImageFromPrefFour = "";
			}
		}

		/*** Capture image button click event */
		btnCapturePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// capture picture
				if(checkPermission()){
					captureImage();
				}else{
					showPermissionSettingsDialog();
				}

			}
		});

		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.my_dialog, null);

			TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			text.setText("     Sorry! Your device doesn't support camera     ");
			Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
			text.setTypeface(typeface);
			text.setTextColor(Color.WHITE);
			text.setTextSize(18);
			Toast toast = new Toast(getApplicationContext());
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);

			toast.show();

			//finish();
		}


		imgPrescription.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, BROWSER_IMAGE_REQUEST_CODE);
			}
		});


		imgPrescriptionTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, BROWSER_IMAGE_REQUEST_CODETWO);
			}
		});

		imgPrescriptionThree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, BROWSER_IMAGE_REQUEST_CODETHREE);
			}
		});

		imgPrescriptionFour.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, BROWSER_IMAGE_REQUEST_CODEFOUR);
			}
		});

////////////////////////////////////////Register Button /////////////////////////////////////////


		RegisterPatiebtBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Intent i = new Intent(UploadPrescriptionTab.this,
				 * FinalAppointmentActivity.class); startActivity(i);
				 */
				//	pval = patientname.getText().toString();

				String selectedpopularids = "";

				for(int j=0; j< myTestTableEnterTest.getChildCount(); j++)
				{
					TableRow tr = (TableRow)myTestTableEnterTest.getChildAt(j);
					CheckBox chk = (CheckBox)tr.getChildAt(0);
					if(chk.isChecked())
					{
						selectedpopularids = String.valueOf(chk.getId());

						if(selectedpopularids.equals(""))
						{
							//idsS = String.valueOf(chk.getId());
							//idsS = "";
							//selectedpopularids = String.valueOf(chk.getId());
						}
						else
						{
							//idsS = idsS + "," + String.valueOf(chk.getId());
							//idsS = String.valueOf(chk.getId());
							// PopulaerTestiddT = idsS;
							//selectedpopularids = String.valueOf(chk.getId());

							if (selectedtestid.contains(Integer.parseInt(selectedpopularids)))
							{
								//Toast.makeText(RegisterPatientPhlebotomyAdv.this,	"This test name is already selected",Toast.LENGTH_LONG).show();
								//testname.setText("");
							}
							else
							{
								selectedNewIDs.add(String.valueOf(selectedpopularids));
								//Toast.makeText(RegisterPatientPhlebotomyAdv.this, " new test added", Toast.LENGTH_SHORT).show();
							}

						}

						//selectedNewIDs.add(String.valueOf(selectedtestid));
					}

				}



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

				editor.putString("popularids", "Upload");
				editor.putString("testid", new_ids);
				editor.putString("testprofileid", new_profile_ids);


//////////////////////////////////////////////////////////////////////////////
				editor.putString("filePath", filePath);
				editor.putString("filePathTwo", filePathTwo);
				editor.putString("filePathThree", filePathThree);
				editor.putString("filePathFour", filePathFour);
///////////////////////////////////////////////////////////////////////////////
				editor.commit();

				EnteredTest_shrdpref = EntertestName.getText().toString();

				if (isInternetOn(getApplicationContext())) {

					if(
							(EnteredTest_shrdpref.equalsIgnoreCase("null") || EnteredTest_shrdpref.equalsIgnoreCase(""))&&
									(new_profile_ids.equalsIgnoreCase("null") || new_profile_ids.equalsIgnoreCase(""))&&
									(new_ids.equalsIgnoreCase("null") || new_ids.equalsIgnoreCase(""))&&
									(filePath.equalsIgnoreCase("null") || filePath.equalsIgnoreCase(""))&&
									(filePathTwo.equalsIgnoreCase("null") || filePathTwo.equalsIgnoreCase(""))&&
									(filePathThree.equalsIgnoreCase("null") || filePathThree.equalsIgnoreCase(""))&&
									(filePathFour.equalsIgnoreCase("null") || filePathFour.equalsIgnoreCase(""))

					)
					{
						showAlert2(" Please Select , Enter Test Or Upload Prescription(TRF) Image ");
						//new uploadAllData().execute(new String[]{appointment_url});

					}
					else
					{

						new uploadAllData().execute(new String[]{appointment_url});
					}


				} else {
					alertBox("Internet Connection is not available..!");
				}
				//launchUploadActivity(true);
			}
		});


	}
	// onCreat Method End here

//////////////////////////////////////// popular test Method ///////////////////////////////

	// Code to populate table
	public void populateTable(String tname, String popularid, String tprice, String tdiscountprice) {
		String rupeestr = "<span class=\"WebRupee\">&#8377;</span>";
		String [] popularTestId = null;
		ArrayList<String> popularList = new ArrayList<String>();

		if(!(selectedPopularIdFromPatientInfo.equalsIgnoreCase("")))
		{
			popularTestId = selectedPopularIdFromPatientInfo.split("#");
		}

		if(popularTestId != null)
		{
			for(int p=0;p<popularTestId.length;p++)
			{
				popularList.add(popularTestId[p]);
			}
		}

		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		tableRow.setGravity(Gravity.LEFT);

		TextView txtTestPrice = new TextView(this);
		CheckBox chkbox = new CheckBox(this);
		TextView txtdiscountprice = new TextView(this);

		chkbox.setLayoutParams(new TableRow.LayoutParams(
				//	TableRow.LayoutParams.WRAP_CONTENT,
				0,
				TableRow.LayoutParams.WRAP_CONTENT,1f));
		chkbox.setText(tname);
		chkbox.setTypeface(custom_font);
		chkbox.setId(Integer.parseInt(popularid));
		chkbox.setBackgroundColor(Color.TRANSPARENT);
		txtdiscountprice.setTextSize(12);

		if(popularList.size() > 0)
		{
			if(popularList.contains(popularid))
			{
				chkbox.setChecked(true);
			}
		}

		txtdiscountprice.setLayoutParams(new TableRow.LayoutParams(
				//TableRow.LayoutParams.WRAP_CONTENT,
				0,
				TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
		txtdiscountprice.setGravity(Gravity.RIGHT);

		if(tdiscountprice.equals("0") || tdiscountprice.equals("0.0"))
		{
			txtdiscountprice.setText((Html.fromHtml(rupeestr)) + " NA");
			txtdiscountprice.setTextColor(Color.BLACK);
			txtdiscountprice.setTypeface(custom_font);
		}
		else
		{
			txtdiscountprice.setText((Html.fromHtml(rupeestr))+tdiscountprice);
			txtdiscountprice.setTextColor(Color.parseColor("#009900"));
			txtdiscountprice.setTypeface(custom_font);
		}
		txtdiscountprice.setTextSize(12);
		txtdiscountprice.setTypeface(custom_font);
		txtdiscountprice.setPadding(0, 0, 10, 0);

		txtTestPrice.setLayoutParams(new TableRow.LayoutParams(
				//TableRow.LayoutParams.WRAP_CONTENT,
				0,
				TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
		txtTestPrice.setGravity(Gravity.RIGHT);

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
		txtTestPrice.setTypeface(custom_font);

		tableRow.addView(chkbox);
		tableRow.addView(txtTestPrice);
		tableRow.addView(txtdiscountprice);

		myTestTableEnterTest.addView(tableRow, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));

	}

//////////////////////////  Uplooad prciption Methods//////////////////////////

	/*** Checking device has camera hardware or not **/
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}
	private void dispatchTakePictureIntent() {

		clickcount=clickcount+1;
		if(clickcount==1)
		{
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile1();
					Log.d("dtp photoFile+",photoFile.toString());


				} catch (IOException ex) {

				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					Uri photoURI = FileProvider.getUriForFile(this,
							BuildConfig.APPLICATION_ID+ ".provider",
							photoFile);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
					startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
				}
			}
			//old code
//			Intent iOne = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//			Log.d("fileUri",fileUri.toString());
//			iOne.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//			getParent().startActivityForResult(iOne,
//					CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

		}


		if(clickcount==2)
		{
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile2();
					Log.d("dtp photoFile+",photoFile.toString());


				} catch (IOException ex) {

				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					Uri photoURI = FileProvider.getUriForFile(this,
							BuildConfig.APPLICATION_ID+ ".provider",
							photoFile);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
					startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO);
				}
			}
			//old code
//			Intent iTwo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			fileUriTwo = getOutputMediaFileUriTwo(MEDIA_TYPE_IMAGETWO);
//			iTwo.putExtra(MediaStore.EXTRA_OUTPUT, fileUriTwo);
//			Log.e("fileUriTwo",fileUriTwo.toString());
//
//			// start the image capture Intent
//			getParent().startActivityForResult(iTwo,
//					CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO);

			//Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO);
		}
		if(clickcount==3)
		{
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile3();
					Log.d("dtp photoFile+",photoFile.toString());


				} catch (IOException ex) {

				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					Uri photoURI = FileProvider.getUriForFile(this,
							BuildConfig.APPLICATION_ID+ ".provider",
							photoFile);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
					startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE);
				}
			}
			//old code
//			Intent iThree = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			fileUriThree = getOutputMediaFileUriThree(MEDIA_TYPE_IMAGETHREE);
//			iThree.putExtra(MediaStore.EXTRA_OUTPUT, fileUriThree);
//			getParent().startActivityForResult(iThree,
//					CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE);

		}
		if(clickcount==4)
		{
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile4();
					Log.d("dtp photoFile+",photoFile.toString());


				} catch (IOException ex) {

				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					Uri photoURI = FileProvider.getUriForFile(this,
							BuildConfig.APPLICATION_ID+ ".provider",
							photoFile);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
					startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR);
				}
			}
			//old code

//			Intent iFour = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			fileUriFour = getOutputMediaFileUriFour(MEDIA_TYPE_IMAGEFOUR);
//			iFour.putExtra(MediaStore.EXTRA_OUTPUT, fileUriFour);
//			getParent().startActivityForResult(iFour,
//					CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR);

			clickcount=0;
		}

	}

	private File createImageFile1() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
//		currentPhotoPath = image.getAbsolutePath();
		filePath = image.getAbsolutePath();

		return image;
	}
	private File createImageFile2() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
//		currentPhotoPath = image.getAbsolutePath();
		filePathTwo = image.getAbsolutePath();

		return image;
	}
	private File createImageFile3() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
//		currentPhotoPath = image.getAbsolutePath();
		filePathThree = image.getAbsolutePath();

		return image;
	}
	private File createImageFile4() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
//		currentPhotoPath = image.getAbsolutePath();
		filePathFour = image.getAbsolutePath();

		return image;
	}
	/*** Launching camera app to capture image */
	private void captureImage() {

		clickcount=clickcount+1;
		if(clickcount==1)
		{
			Intent iOne = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			iOne.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			// start the image capture Intent
			startActivityForResult(iOne,
					CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

			//Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		}


		if(clickcount==2)
		{
			Intent iTwo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUriTwo = getOutputMediaFileUriTwo(MEDIA_TYPE_IMAGETWO);
			iTwo.putExtra(MediaStore.EXTRA_OUTPUT, fileUriTwo);
			// start the image capture Intent
			startActivityForResult(iTwo,
					CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO);

			//Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO);
		}
		if(clickcount==3)
		{
			Intent iThree = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUriThree = getOutputMediaFileUriThree(MEDIA_TYPE_IMAGETHREE);
			iThree.putExtra(MediaStore.EXTRA_OUTPUT, fileUriThree);
			// start the image capture Intent
			startActivityForResult(iThree,
					CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE);

			//Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE);
		}
		if(clickcount==4)
		{
			Intent iFour = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUriFour = getOutputMediaFileUriFour(MEDIA_TYPE_IMAGEFOUR);
			iFour.putExtra(MediaStore.EXTRA_OUTPUT, fileUriFour);
			// start the image capture Intent
			startActivityForResult(iFour,
					CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR);

			//	Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR);
			clickcount=0;
		}
	}

	/***
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on screen orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
		outState.putParcelable("file_uriTwo", fileUriTwo);
		outState.putParcelable("file_uriThree", fileUriThree);
		outState.putParcelable("file_uriFour", fileUriFour);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
		fileUriTwo = savedInstanceState.getParcelable("file_uriTwo");
		fileUriThree = savedInstanceState.getParcelable("file_uriThree");
		fileUriFour = savedInstanceState.getParcelable("file_uriFour");
	}

	/*** Receiving activity result method will be called after closing the camera **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Log.d("onActivity+",filePath);
				File f = new File(filePath);
				imgPrescription.setImageURI(fileUri);

				Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
//				bookbtn1.setEnabled(true);
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 8;
//				final Bitmap bitmap = BitmapFactory.decodeFile(
//						fileUri.getPath(), options);
//				imgPrescription.setImageBitmap(bitmap);
//				filePath = fileUri.getPath();
//				bookbtn1.setEnabled(true);
				// launchUploadActivity(true);
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);

				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     User cancelled image capture     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);

				toast.show();

				/*Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();*/
			} else {
				// failed to capture image
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);

				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Sorry! Failed to capture image     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);

				toast.show();

				/*Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();*/
			}
		}


		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO) {
			if (resultCode == RESULT_OK) {
				File f = new File(filePathTwo);
				imgPrescriptionTwo.setImageURI(fileUriTwo);

				Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
//				bookbtn1.setEnabled(true);
				//odl code
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 8;
//				final Bitmap bitmap = BitmapFactory.decodeFile(fileUriTwo.getPath(), options);
//				imgPrescriptionTwo.setImageBitmap(bitmap);
//				filePathTwo = fileUriTwo.getPath();
//				bookbtn1.setEnabled(true);
				// launchUploadActivity(true);

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     User cancelled image capture     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				// Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();

			} else {
				// failed to capture image
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Sorry! Failed to capture image     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				//Toast.makeText(getApplicationContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE) {
			if (resultCode == RESULT_OK) {
				File f = new File(filePathThree);
				imgPrescriptionThree.setImageURI(fileUriThree);

				Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
//				bookbtn1.setEnabled(true);

//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 8;
//				final Bitmap bitmap = BitmapFactory.decodeFile(fileUriThree.getPath(), options);
//				imgPrescriptionThree.setImageBitmap(bitmap);
//				filePathThree = fileUriThree.getPath();
//				bookbtn1.setEnabled(true);
				// launchUploadActivity(true);

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     User cancelled image capture     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();

			} else {
				// failed to capture image
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Sorry! Failed to capture image     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR) {
			if (resultCode == RESULT_OK) {
				File f = new File(filePathFour);
				imgPrescriptionFour.setImageURI(fileUriFour);

				Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
//				bookbtn1.setEnabled(true);
				//old code
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 8;
//				final Bitmap bitmap = BitmapFactory.decodeFile(fileUriFour.getPath(), options);
//				imgPrescriptionFour.setImageBitmap(bitmap);
//				filePathFour = fileUriFour.getPath();
//				bookbtn1.setEnabled(true);
				// launchUploadActivity(true);

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     User cancelled image capture     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();

			} else {
				// failed to capture image
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Sorry! Failed to capture image     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == BROWSER_IMAGE_REQUEST_CODE) {
			if (requestCode == BROWSER_IMAGE_REQUEST_CODE
					&& resultCode == RESULT_OK && null != data) {
				fileUri = data.getData();
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				/*
				 * imgPrescription.setImageBitmap(BitmapFactory.decodeFile(
				 * picturePath)); filePath = picturePath;
				 */
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				final Bitmap bitmap = BitmapFactory.decodeFile(picturePath,
						options);
				imgPrescription.setImageBitmap(bitmap);
				filePath = picturePath;
				//bookbtn1.setEnabled(true);
			}
		}


		if (requestCode == BROWSER_IMAGE_REQUEST_CODETWO) {
			if (requestCode == BROWSER_IMAGE_REQUEST_CODETWO && resultCode == RESULT_OK && null != data) {
				fileUriTwo = data.getData();
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				final Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
				imgPrescriptionTwo.setImageBitmap(bitmap);
				filePathTwo = picturePath;
				//bookbtn1.setEnabled(true);
			}
		}

		if (requestCode == BROWSER_IMAGE_REQUEST_CODETHREE) {
			if (requestCode == BROWSER_IMAGE_REQUEST_CODETHREE && resultCode == RESULT_OK && null != data) {
				fileUriThree = data.getData();
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				final Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
				imgPrescriptionThree.setImageBitmap(bitmap);
				filePathThree = picturePath;
				//bookbtn1.setEnabled(true);
			}
		}


		if (requestCode == BROWSER_IMAGE_REQUEST_CODEFOUR) {
			if (requestCode == BROWSER_IMAGE_REQUEST_CODEFOUR && resultCode == RESULT_OK && null != data) {
				fileUriFour = data.getData();
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				final Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
				imgPrescriptionFour.setImageBitmap(bitmap);
				filePathFour = picturePath;
				//bookbtn1.setEnabled(true);
			}
		}


	}

	/*** Creating file uri to store image */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}


	public Uri getOutputMediaFileUriTwo(int type) {
		return Uri.fromFile(getOutputMediaFileTwo(type));
	}
	public Uri getOutputMediaFileUriThree(int type) {
		return Uri.fromFile(getOutputMediaFileThree(type));
	}
	public Uri getOutputMediaFileUriFour(int type) {
		return Uri.fromFile(getOutputMediaFileFour(type));
	}

	/*** returning image / video */
	private static File getOutputMediaFile(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs())
			{Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAME+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator	+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	private static File getOutputMediaFileTwo(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAMETWO);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs())
			{Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAMETWO+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGETWO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator	+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	private static File getOutputMediaFileThree(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAMETHREE);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs())
			{Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAMETHREE+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGETHREE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator	+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	private static File getOutputMediaFileFour(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAMEFOUR);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs())
			{Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAMEFOUR+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGEFOUR) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator	+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

//////////////////////////////////// Enter test methods //////////////////////


	// Code to populate table for test profile
	public void populateTableSelectText(String tname, TableLayout myTestTable,
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
		removebtn.setImageResource(R.drawable.delete);
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

				if (RegisterPatientNewAdv.this.selectedtestid
						.contains(idToBeRemoved)) {
					for (int k = 0; k < RegisterPatientNewAdv.this.selectedtestid
							.size(); k++) {
						if (RegisterPatientNewAdv.this.selectedtestid.get(k) == idToBeRemoved) {
							RegisterPatientNewAdv.this.selectedtestid.remove(k);
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


	// Code to populate table
	public void populateTableSelectText1(String tname, TableLayout myTestTable,
										 String selectedtestid, int dropdownid) {

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

		removebtn.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT, 0));
		removebtn.setImageResource(R.drawable.delete);
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

		removebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				TableRow tr1 = (TableRow) v.getParent();
				TableLayout table1 = (TableLayout) tr1.getParent();

				int idToBeRemoved = Integer.parseInt(((TextView) tr1
						.getChildAt(2)).getText().toString());
				table1.removeView(tr1);

				if (RegisterPatientNewAdv.this.selectedtestid
						.contains(idToBeRemoved)) {
					for (int k = 0; k < RegisterPatientNewAdv.this.selectedtestid
							.size(); k++) {
						if (RegisterPatientNewAdv.this.selectedtestid.get(k) == idToBeRemoved) {
							RegisterPatientNewAdv.this.selectedtestid.remove(k);
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

		myTestTable.addView(tableRow, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));
	}


	//*** Async task class to get json by making HTTP call **//*
	private class GetTestNameByCategory extends AsyncTask<String, Void, ArrayList<String>> {
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

			adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,testbycategory_name);

			testnamebycategory.setAdapter(adapter);
		}
	}
////////////////////////////////  Patient Register button methods  ////////////////////////////////////////////////////////////


	private class uploadAllData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog pDialog = new
//			pDialog = new ProgressDialog(RegisterPatientNewAdv.this);
//			pDialog.setMessage("Please wait...");
//			pDialog.setCancelable(false);
//			pDialog.show();
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
				showAlert(" Patient registered successfully  ");

				clearPreferences();

			} else {
				showAlert1("Appointment Not Booked Please Try Again");
				date_val = date_val_temp;

			}
		}
	}
	//////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	public static String POST(String url) {
		InputStream inputStream = null;
		String result = "";

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
//////////////////////////////////////////////////////////////

		if (!(filePathTwo.equals("")) ) {
			BitmapFactory.Options optionsTwo = new BitmapFactory.Options();
			optionsTwo.inSampleSize = 2;
			final Bitmap bitmapTwo = BitmapFactory.decodeFile(filePathTwo, optionsTwo);

			// MultipartEntity entity1 = new MultipartEntity(
			// HttpMultipartMode.BROWSER_COMPATIBLE);

			ByteArrayOutputStream bosTwo = new ByteArrayOutputStream();
			bitmapTwo.compress(Bitmap.CompressFormat.JPEG, 50, bosTwo);
			byte[] dataTwo = bosTwo.toByteArray();

			encodedImageTwo = Base64.encodeToString(dataTwo, Base64.NO_WRAP);

		} else {
			encodedImageTwo = "";
		}
////////////////////////////////////////////////////////////////////////////////////////
		if (!(filePathThree.equals("")) ) {
			BitmapFactory.Options optionsThree = new BitmapFactory.Options();
			optionsThree.inSampleSize = 2;
			final Bitmap bitmapThree = BitmapFactory.decodeFile(filePathThree, optionsThree);

			// MultipartEntity entity1 = new MultipartEntity(
			// HttpMultipartMode.BROWSER_COMPATIBLE);

			ByteArrayOutputStream bosThree = new ByteArrayOutputStream();
			bitmapThree.compress(Bitmap.CompressFormat.JPEG, 50, bosThree);
			byte[] dataThree = bosThree.toByteArray();

			encodedImageThree = Base64.encodeToString(dataThree, Base64.NO_WRAP);

		} else {
			encodedImageThree = "";
		}
///////////////////////////////////////////////////////////////////////////////
		if (!(filePathFour.equals("")) ) {
			BitmapFactory.Options optionsFour = new BitmapFactory.Options();
			optionsFour.inSampleSize = 2;
			final Bitmap bitmapFour = BitmapFactory.decodeFile(filePathFour, optionsFour);

			//  MultipartEntity entity1 = new MultipartEntity(
			// HttpMultipartMode.BROWSER_COMPATIBLE);

			ByteArrayOutputStream bosFour = new ByteArrayOutputStream();
			bitmapFour.compress(Bitmap.CompressFormat.JPEG, 50, bosFour);
			byte[] dataFour = bosFour.toByteArray();

			encodedImageFour = Base64.encodeToString(dataFour, Base64.NO_WRAP);

		} else {
			encodedImageFour = "";
		}


//////////////////////////////////////////////////////////////
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			java.util.List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(selected_labidfrompref != null && !(selected_labidfrompref.equalsIgnoreCase("")))
			{
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			}
			else if(selected_labidfrompref != null && !(selected_labidfrompref.equalsIgnoreCase(""))) {
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\""+ selected_labidfrompref + "\""));
			}

			nameValuePairs.add(new BasicNameValuePair("\"PatientID\"", "\"" + pid + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"Username\"", "\"" + phone + "\""));
			//nameValuePairs.add(new BasicNameValuePair("\"Username\"",  "\"\""));

			//	nameValuePairs.add(new BasicNameValuePair("\"SelectedTest\"", "\"\""));
			//nameValuePairs.add(new BasicNameValuePair("\"SelectedProfile\"","\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedTest\"", "\"" + new_ids + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedProfile\"","\"" + new_profile_ids + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"SelectedPopularTest\"","\"\""));
			//nameValuePairs.add(new BasicNameValuePair("\"SelectedPopularTest\"","\"" + idsS + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"EnteredTest\"","\"" + EnteredTest_shrdpref + "\""));

			//nameValuePairs.add(new BasicNameValuePair("\"EnteredTest\"", "\"\""));
/////////////////////////////////////////////////////////////////

			if(date_val.equalsIgnoreCase("null") || date_val.equalsIgnoreCase(""))
			{
				//nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"","\"" + strDate + "\""));
				nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"", "\"\""));
			}
			else
			{
				//nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"","\"" + date_val + "\""));
				nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"", "\"\""));
			}

			nameValuePairs.add(new BasicNameValuePair("\"AppointmentAddress\"","\"" + addval.toString().trim() + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\"" + 0 + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"","\"" + pRefDoc_fromprofile + "\""));

			//nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"", "\"\""));


			//String str=sharedpreferences.getString("self", "");
			//	if (str.equals("yes"))  {


			nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
			//	if(pval.equalsIgnoreCase("null") || pval.equalsIgnoreCase("") || pval.equalsIgnoreCase("self"))
			//{
			//	nameValuePairs.add(new BasicNameValuePair("\"PatientName\"","\"SELF\""));
			//}
			//else
			//{
			nameValuePairs.add(new BasicNameValuePair("\"PatientName\"","\"" + pname_fromprofile + "\""));
			//}
			nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"false\""));

			//nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + 0 + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + pAge_fromprofile + "\""));

			//	if(register_patientgender.equalsIgnoreCase("null") || register_patientgender.equalsIgnoreCase(""))
			//{
			//	nameValuePairs.add(new BasicNameValuePair("\"Gender\"",	"\"" + 0 + "\""));
			//}
			//else
			//{
			nameValuePairs.add(new BasicNameValuePair("\"Gender\"","\"" + pGender_fromprofile + "\""));

			//}

			//	}
			//	else
			//	{
		/*		if(pval.equalsIgnoreCase("self"))
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"PatientName\"","\"SELF\""));
					nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"false\""));

					nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + 0 + "\""));
					if(register_patientgender.equalsIgnoreCase("null") || register_patientgender.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"", "\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",	"\"" + register_patientgender + "\""));
					}
				}
				else
				{
					if(selectedPatientId.equalsIgnoreCase("null") || selectedPatientId.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"PatientName\"","\"" + pval + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"true\""));

						if(family_page.equalsIgnoreCase(""))
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + 0 + "\""));
						}
						else
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + family_page + "\""));
						}
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"","\"" + family_pgender + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\""+ selectedPatientId + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
								"\"\""));
						nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"true\""));

						if(family_page.equalsIgnoreCase(""))
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + 0 + "\""));
						}
						else
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + family_page + "\""));
						}
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",	"\"" + family_pgender + "\""));
					}
				}
				*/
			//}
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

			//nameValuePairs.add(new BasicNameValuePair("\"IsUpdated\"","\"" + updated_val + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"DeviceType\"", "\"" + 1 + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"" + 1 + "\""));

///////////////////////////// first image//////////////////////////////

			if(selectedimagefrompatientinfo.equalsIgnoreCase("null") || selectedimagefrompatientinfo.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"Prescription\"", "\""+ encodedImage + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"Prescription\"", "\""+ encodedImageFromPref + "\""));
			}
///////////////////////////////////// second image///////////////////////////////////////////////


			if(selectedimagefrompatientinfoTwo.equalsIgnoreCase("null") || selectedimagefrompatientinfoTwo.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionTwo\"", "\""+ encodedImageTwo + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionTwo\"", "\""+ encodedImageFromPrefTwo + "\""));
			}
//////////////////////////////////////// three image/////////////////////////////////////////////////////////////////////////////

			if(selectedimagefrompatientinfoThree.equalsIgnoreCase("null") || selectedimagefrompatientinfoThree.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionThree\"", "\""+ encodedImageThree + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionThree\"", "\""+ encodedImageFromPrefThree + "\""));
			}
//////////////////////////////////////////// fourth image//////////////////////////////////////////////////////////////

			if(selectedimagefrompatientinfoFour.equalsIgnoreCase("null") || selectedimagefrompatientinfoFour.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionFour\"", "\""+ encodedImageFour + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionFour\"", "\""+ encodedImageFromPrefFour + "\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"patientaddress\"", "\""+ addval + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"emailid\"", "\"\""));


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


			String finalString = nameValuePairs.toString();
			//	System.out.println("aaaaa" + finalString);
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			String strToServer = "{" + finalString + "}";

			System.out.println("strToServer "+strToServer);

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



/////////////////////////////////////////// end other method /////////////////////////////////

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
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText(Html.fromHtml("<font color='#33cc33'>!!Success!!</font>"));
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						clearPreferences();

						Intent i = new Intent(RegisterPatientNewAdv.this,RegisterPatientNew.class);
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
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		textView.setTypeface(custom_font);
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
	}

	public void showAlert1(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText(Html.fromHtml("<font color='#ff0000'>Failed</font>"));
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message + "\nCall us for appointment booking..")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						//Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppConfig.call_number));
						//Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppConfig.call_number));
						//startActivity(intent);
						//finish();

						clearPreferences();

						Intent i = new Intent(RegisterPatientNewAdv.this,
								RegisterPatientNew.class);
						//RegisterPatientPhlebotomyAdv.class);
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
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		textView.setTypeface(custom_font);
		yesButton.setTypeface(custom_bold_font);
	}

	public void showAlert2(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText(Html.fromHtml("<font color='#33cc33'>!!Alert!!</font>"));
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//clearPreferences();

						//Intent i = new Intent(RegisterPatientPhlebotomyAdv.this,RegisterPatientPhlebotomyAdv.class);
						//.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						//startActivity(i);
						//finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
		Button noButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		textView.setTypeface(custom_font);
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
	}

	public void clearPreferences() {
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.remove("testid");
		editor.remove("testprofileid");
		editor.remove("test_name");
///////////////////////////////////////////////////
		editor.remove("filePath");
		editor.remove("filePathTwo");
		editor.remove("filePathThree");
		editor.remove("filePathFour");
////////////////////////////////////////////////////
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
///////////////////////////////////////////////////////////////////
		editor.remove("imageFromPatientInfo");
		editor.remove("imageFromPatientInfoTwo");
		editor.remove("imageFromPatientInfoThree");
		editor.remove("imageFromPatientInfoFour");
/////////////////////////////////////////////////////////////////
		editor.remove("homecollection");
		editor.remove("centerid");

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
		idsS = "";
		//selectedDocId = "";

		editor.commit();
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


	private boolean checkPermission() {
		int writePermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int readPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
		int cameraPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
		return writePermissionResult == PackageManager.PERMISSION_GRANTED && readPermissionResult == PackageManager.PERMISSION_GRANTED && cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
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
					boolean cameraPermissionGranted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

					if (writePermissionGranted && readPermissionGranted && cameraPermissionGranted) {
						// Permissions granted
						// new GetPatientCheck().execute(new String[]{url});
					} else {
						// Permissions denied
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							boolean shouldShowWriteRationale = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
							boolean shouldShowReadRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
							boolean shouldShowCameraRationale = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);

							if (shouldShowWriteRationale || shouldShowReadRationale || shouldShowCameraRationale) {
								// Show rationale and request permission again
								showMessageOKCancel("Take a Photo: We need access to your camera to allow you to capture clear images of your medical prescriptions.\n" +
												"Upload Prescription: With your permission, we can seamlessly upload the photos you take to our app, ensuring a hassle-free experience.",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												ActivityCompat.requestPermissions(
														RegisterPatientNewAdv.this,
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
		new AlertDialog.Builder(RegisterPatientNewAdv.this)
				.setTitle("Permission Required")
				.setMessage(message)
				.setPositiveButton("Allow", okListener)
				.setNegativeButton("Cancel", cancelListener)
				.create()
				.show();
	}

	private void showPermissionSettingsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Permissions Required")
				.setMessage("To take a photo of prescriptions & directly upload it to the app, the eLAB Assist requires camera and storage permissions. Please enable these permissions in the app settings.")
				.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						openAppSettings();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
	}

	private void openAppSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
				Uri.fromParts("package", getPackageName(), null));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}


	private void alertBox(String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText("Alert");
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fontsRoboto-Regular.ttf"));
		alert.setCustomTitle(Mytitle);

		//alert.setIcon(R.drawable.sign_logo);
		alert.setMessage(msg);
		alert.setPositiveButton("OK", null);

		AlertDialog alert1 = alert.create();
		alert1.show();

		TextView textView = (TextView) alert1.findViewById(android.R.id.message);
		Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		textView.setTypeface(custom_font);
		yesButton.setTypeface(custom_bold_font);
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(RegisterPatientNewAdv.this, RegisterPatientNew.class);
		startActivity(i);
		//finish();
	}

}

