package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.jsonparsing.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class RegisterPatientNew extends BaseActivityDL {

	ScrollView scrollView;

	Typeface custom_font;
	Button btn1,btnOther ,SelectTextButton,UploadImgButton, selectTextbtn;
	EditText patientname;
	static String outputDate = "";
	Bundle data;
	static String finalResult = "";
	GPSTracker gps;
	public static final String MyPREFERENCES = "MyPrefs";
	static SharedPreferences sharedpreferences;
	static String tid = null;
	static String phone = null;
	static String text = "";
	//static String selected_labidfrompref = null;


	ArrayList<String> List = new ArrayList<String>();
	ImageButton btnLabchange;
	public EditText editTxtPatientName,editTxtAge,editTxtTestName,editTxtRefDocName,editTxtPatientMobileNo, editTxtPatientAddress;
	private TextInputLayout inputLayoutPatientName, inputLayoutAge , inputLayoutMobileNo, inputLayoutAdress;
	TextView lblgender, txtlname;
	private static RadioButton rb_male;
	private RadioButton rb_female;
	ImageButton imgbtnClear, imgbtnClear2, imgbtnClear3, imgbtnClear4;
	static String patient_name = "";
	static String pid = "";
	static String selected_labnamefrompref_other = "";
	static String selected_labidfrompref_other = "";

	AlertDialog alertbox;
	static String status = "";
	private ProgressDialog pDialog;
	String StrPatientName , StrPatientAge  , StrPatientMobileNo ,	 StrPatientAdress , StrPatientRefDoctor;
	static String StrPatientGeder = "";
	// URL to get Test Name
	private static final String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?TestName=&LabID=";
	private static String myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?TestName=&LabID=";

	// URL to get Test Profile
	private static final String profileurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchProfile?LabID=";
	private static String profilemyurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchProfile?LabID=";

	// URL to get all categories
	private static final String category_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetTestCategories?LabID=";
	private static String category_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetTestCategories?LabID=";

	private static final String collection_center_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";
	private static String collection_center_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";

	private static final String Affiliation_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";
	private static String Affiliation_myurlll = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";
	ArrayList<String> center_nameId_all = new ArrayList<String>();
	ArrayList<String> centerid_all = new ArrayList<String>();

	ArrayList<String> affiliation_nameId_all = new ArrayList<String>();
	ArrayList<String> affiliation_all = new ArrayList<String>();

	Set<String> allTestName;
	Set<String> allTestID;
	Set<String> allTestPrice;
	Set<String> allTestDiscountPrice;
	Set<String> popularTestName;
	Set<String> popularTestID;
	Set<String> popularTestPrice;
	Set<String> popularTestDiscountPrice;
	Set<String> profileNameSet;
	Set<String> profileIDSet;
	Set<String> categoryNameSet;
	Set<String> categoryIDSet;

	ArrayList<String> test_name_all = new ArrayList<String>();
	ArrayList<String> testid_all = new ArrayList<String>();
	ArrayList<String> test_name = new ArrayList<String>();
	ArrayList<String> testid = new ArrayList<String>();
	ArrayList<String> test_price = new ArrayList<String>();
	ArrayList<String> test_discountprice = new ArrayList<String>();
	ArrayList<String> popular_test_name = new ArrayList<String>();

	ArrayList<String> popular_test_nameTemp = new ArrayList<String>();

	ArrayList<String> testcategory_Listview = new ArrayList<String>();
	ArrayList<String> testbycategory_name = new ArrayList<String>();
	ArrayList<String> testidbycategory = new ArrayList<String>();

	ArrayList<String> test_profile_name = new ArrayList<String>();
	ArrayList<String> testpid = new ArrayList<String>();


	private static final String TAG_TESTNAME = "TestName";
	private static final String TAG_TESTID = "TestID";
	private static final String TAG_TESTPROFILE = "TestProfileName";
	private static final String TAG_TESTCATEGORY = "TestCategoryName";
	private static final String TAG_TESTPROFILEID = "TestProfileId";

	// contacts JSONArray
	JSONArray Testname = null;
	JSONArray Testprofile = null;

	String Test = null;
	String Testprofileid = null;

	JSONArray Testnamebycategory = null;
	JSONArray Testcategory = null;

	int setTestSizee,setCategorySizee,setProfileSizee,setTestPopulorSizee;
	int setCCSizee,setAfilSizee;

	JSONArray Centername = null;

	JSONArray affiliationnamee = null;
	Set<String> allaffiliationNameSet;
	Set<String> allCollectionCenterNameSet;

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		getLayoutInflater().inflate(R.layout.activity_register_patient_phlebotomy, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		pid = sharedpreferences.getString("patientId", "");
		//	selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		phone = sharedpreferences.getString("phno", "");
		selected_labidfrompref_other = sharedpreferences.getString("SelectedLabId", "");
		selected_labnamefrompref_other = sharedpreferences.getString("SelectedLabName", "");


		imgbtnClear = (ImageButton) findViewById(R.id.clear2);
		imgbtnClear2 = (ImageButton) findViewById(R.id.clear3);

		txtlname = (TextView) findViewById(R.id.txtlabname2);
		rb_male = (RadioButton) findViewById(R.id.radioMale2);
		rb_female = (RadioButton) findViewById(R.id.radioFemale2);
		btnLabchange = (ImageButton) findViewById(R.id.changelabbtn2);

		scrollView = (ScrollView) findViewById(R.id.ScrollView_id);
		inputLayoutPatientName = (TextInputLayout) findViewById(R.id.input_layout_name);
		inputLayoutAge = (TextInputLayout) findViewById(R.id.input_layout_age);
		inputLayoutMobileNo = (TextInputLayout) findViewById(R.id.input_layout_mobileNo);
		inputLayoutAdress = (TextInputLayout) findViewById(R.id.input_layout_address);

		editTxtPatientName = (EditText) findViewById(R.id.etPatientName2);
		editTxtAge = (EditText) findViewById(R.id.etPAge2);
		editTxtPatientMobileNo = (EditText) findViewById(R.id.etMobileNo_id);
		editTxtPatientAddress = (EditText) findViewById(R.id.etAddress_id);
		editTxtRefDocName = (EditText) findViewById(R.id.etRefDoctor2);


		editTxtPatientName.addTextChangedListener(new MyTextWatcher(editTxtPatientName));
		editTxtAge.addTextChangedListener(new MyTextWatcher(editTxtAge));
		editTxtPatientMobileNo.addTextChangedListener(new MyTextWatcher(editTxtPatientMobileNo));
		//editTxtPatientAddress.addTextChangedListener(new MyTextWatcher(editTxtPatientAddress));

		selectTextbtn = (Button) findViewById(R.id.TextSelectionButton_id);


		allTestName = new HashSet<String>();
		allTestID = new HashSet<String>();
		allTestPrice = new HashSet<String>();
		allTestDiscountPrice = new HashSet<String>();
		popularTestName = new HashSet<String>();
		popularTestID = new HashSet<String>();
		popularTestPrice = new HashSet<String>();
		popularTestDiscountPrice = new HashSet<String>();
		profileNameSet = new HashSet<String>();
		profileIDSet = new HashSet<String>();
		categoryNameSet = new HashSet<String>();
		categoryIDSet = new HashSet<String>();


		allaffiliationNameSet = new HashSet<String>();

		allCollectionCenterNameSet = new HashSet<String>();

		Set<String> setallCollectionCenterNameSet = new HashSet<String>();
		Set<String> setallaffiliationNameSet = new HashSet<String>();

		Set<String> selectedpopularsetinmain = new HashSet<String>();
		Set<String> selectedtestnamesetinmain = new HashSet<String>();
		Set<String> selectedtestcategorysetinmain = new HashSet<String>();
		Set<String> selectedtestprofilesetinmain = new HashSet<String>();

		selectedpopularsetinmain = sharedpreferences.getStringSet("popularTestData", null);
		selectedtestnamesetinmain = sharedpreferences.getStringSet("allTestNames", null);
		selectedtestprofilesetinmain = sharedpreferences.getStringSet("setprofilename", null);
		selectedtestcategorysetinmain = sharedpreferences.getStringSet("setcategoryname", null);

		if(selectedtestnamesetinmain != null )
		{
			setTestSizee =selectedtestnamesetinmain.size();
		}else{
			setTestSizee = 0;
		}
		if(selectedtestcategorysetinmain != null )
		{
			setCategorySizee =selectedtestcategorysetinmain.size();
		}else{
			setCategorySizee = 0;
		}
		if(selectedtestprofilesetinmain != null )
		{
			setProfileSizee =selectedtestprofilesetinmain.size();
		}else{
			setProfileSizee = 0;
		}
		if(selectedpopularsetinmain != null )
		{
			setTestPopulorSizee =selectedpopularsetinmain.size();
		}else{
			setTestPopulorSizee = 0;
		}


		editTxtPatientName.setTypeface(custom_font);
		editTxtAge.setTypeface(custom_font);
		editTxtPatientMobileNo.setTypeface(custom_font);
		editTxtPatientAddress.setTypeface(custom_font);
		editTxtRefDocName.setTypeface(custom_font);
		//lblgender.setTypeface(custom_font);

		txtlname.setTypeface(custom_font);
		rb_male.setTypeface(custom_font);
		rb_female.setTypeface(custom_font);
		//btnQuickBook.setTypeface(custom_bold_font);

		if (selected_labnamefrompref_other != null || !(selected_labnamefrompref_other.equalsIgnoreCase(""))) {
			txtlname.setText(selected_labnamefrompref_other);
		}


		selectTextbtn.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {

						submitForm();

						//Intent i = new Intent(RegisterPatientPhlebotomy.this, RegisterPatientPhlebotomyAdv.class);
						//startActivity(i);
						//finish();

					}
				}
		);

		myurl = url;
		profilemyurl = profileurl;
		category_myurl = category_url;

		myurl = myurl + selected_labidfrompref_other;
		profilemyurl = profilemyurl + selected_labidfrompref_other;
		category_myurl = category_myurl + selected_labidfrompref_other;


		System.out.println("myurl "+myurl);
		System.out.println("profilemyurl "+profilemyurl);
		System.out.println("category_myurl "+category_myurl);

		btnLabchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor1 = sharedpreferences.edit();

				editor1.remove("CheckedValue");

				editor1.commit();

				Intent i = new Intent(RegisterPatientNew.this, LabSelectionForDoctor.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});






		setallCollectionCenterNameSet = sharedpreferences.getStringSet("setallCollectionCenterNameSet", null);
		setallaffiliationNameSet = sharedpreferences.getStringSet("setallaffiliationNameSet", null);


		if(setallCollectionCenterNameSet != null )
		{
			setCCSizee =setallCollectionCenterNameSet.size();
		}else{
			setCCSizee = 0;
		}

		if(setallaffiliationNameSet != null )
		{
			setAfilSizee =setallaffiliationNameSet.size();
		}else{
			setAfilSizee = 0;
		}

		collection_center_myurl = collection_center_myurl + selected_labidfrompref_other;

		Affiliation_myurlll = Affiliation_myurlll + selected_labidfrompref_other;

	/*	new GetCollectionCenterList().execute();
		new GetAffiliationList().execute();
		new GetTestName().execute();
		new GetTestProfile().execute();
		new GetTestCategory().execute();*/

		imgbtnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editTxtPatientName.setText("");
			}
		});

		imgbtnClear2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editTxtAge.setText("");
			}
		});

		new GetCollectionCenterList().execute();
		new GetAffiliationList().execute();
		new GetTestName().execute();
		new GetTestProfile().execute();
		new GetTestCategory().execute();


	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		alertbox = new Builder(this)
				.setMessage("Do you want to exit?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							// do something when the button is clicked
							public void onClick(DialogInterface arg0, int arg1) {

								/*SharedPreferences.Editor editor = sharedpreferences.edit();
								editor.remove("allTestNames");
								editor.remove("popularTestData");
								editor.remove("setprofilename");
								editor.remove("setcategoryname");

								editor.commit();*/

								// Constant.SIGNIN_STATUS = false;
								Intent i1 = new Intent(Intent.ACTION_MAIN);
								i1.addCategory(Intent.CATEGORY_HOME);
								i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
								i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
								startActivity(i1);
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).show();

		TextView textView = (TextView) alertbox.findViewById(android.R.id.message);
		Button yesButton = alertbox.getButton(DialogInterface.BUTTON_POSITIVE);
		Button noButton = alertbox.getButton(DialogInterface.BUTTON_NEGATIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font);
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
	}





	void scrollDown()
	{
		Thread scrollThread = new Thread(){
			public void run(){
				try {
					sleep(200);
					RegisterPatientNew.this.runOnUiThread(new Runnable() {
						public void run() {
							scrollView.fullScroll(View.FOCUS_DOWN);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		scrollThread.start();
	}

	//editTxtPatientName = (EditText)findViewById(R.id.etPatientName2);
	//editTxtAge = (EditText)findViewById(R.id.etPAge2);
	//editTxtPatientMobileNo = (EditText)findViewById(R.id.etMobileNo_id);
	//editTxtPatientAddress = (EditText)findViewById(R.id.etEmail_id);
//	private TextInputLayout inputLayoutPatientName, inputLayoutAge , inputLayoutMobileNo, inputLayoutAdress ;

	private boolean validatePatientName() {
		if (editTxtPatientName.getText().toString().trim().isEmpty()) {
			inputLayoutPatientName.setError("Please Enter Patient Name");
			requestFocus(editTxtPatientName);
			return false;
		} else {
			inputLayoutPatientName.setErrorEnabled(false);
		}

		return true;
	}


	private boolean validateAge() {
		if (editTxtAge.getText().toString().trim().isEmpty()) {
			inputLayoutAge.setError("Enter Patient Age");
			requestFocus(editTxtAge);
			return false;
		} else {
			inputLayoutAge.setErrorEnabled(false);
		}

		return true;
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

	private boolean validateMobileNo() {

		String PmonileNo = editTxtPatientMobileNo.getText().toString().trim();

		//	if (editTxtPatientMobileNo.getText().toString().trim().isEmpty()) {
		if (PmonileNo.length() < 10 || PmonileNo.length() > 10) {
			inputLayoutMobileNo.setError("Enter Valid Mobile Number");
			requestFocus(editTxtPatientMobileNo);
			return false;
		} else {
			inputLayoutMobileNo.setErrorEnabled(false);
		}

		return true;
	}

	private boolean validateEmail() {
		String email = editTxtPatientAddress.getText().toString().trim();

		if (email.isEmpty() || !isValidEmail(email)) {
			inputLayoutAdress.setError("Enter valid email address");
			requestFocus(editTxtPatientAddress);
			return false;
		} else {
			inputLayoutAdress.setErrorEnabled(false);
		}

		return true;
	}

	private static boolean isValidEmail(String email) {
		return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {
				case R.id.etPatientName2:
					validatePatientName();
					break;
				case R.id.etPAge2:
					validateAge();
					break;
				case R.id.etMobileNo_id:
					validateMobileNo();
					break;
				//case R.id.etEmail_id:
				//	validateEmail();
				//	break;
			}
		}
	}

	private void submitForm() {
		if (!validatePatientName()) {
			return;
		}

		if (!validateAge()) {
			return;
		}

		if (!validateMobileNo()) {
			return;
		}

		//if (!validateEmail()) {
		//	return;
		//}

		StrPatientName = editTxtPatientName.getText().toString();
		StrPatientAge = editTxtAge.getText().toString();

		if(rb_male.isChecked())
		{
			StrPatientGeder="0";
		}
		else
		{
			StrPatientGeder="1";
		}

		StrPatientMobileNo = editTxtPatientMobileNo.getText().toString();
		StrPatientAdress = editTxtPatientAddress.getText().toString();
		StrPatientRefDoctor = editTxtRefDocName.getText().toString();

		Intent intent = new Intent(RegisterPatientNew.this, RegisterPatientNewAdv.class);
		//	Intent intent = new Intent(RegisterPatientNew.this, AlllTestNameForAdmin.class);


		intent.putExtra("StrPatientName", StrPatientName);
		intent.putExtra("StrPatientAge", StrPatientAge);
		intent.putExtra("StrPatientGeder", StrPatientGeder);
		intent.putExtra("StrPatientMobileNo", StrPatientMobileNo);
		intent.putExtra("StrPatientAdress", StrPatientAdress);
		intent.putExtra("StrPatientRefDoctor", StrPatientRefDoctor);

		startActivity(intent);
		//finish();
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}



	/*** Async task class to get json by making HTTP call **/
	private class GetTestName extends
			AsyncTask<String, Void, ArrayList<String>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			test_name.clear();
			testid.clear();

			// Showing progress dialog
//            pDialog = new ProgressDialog(RegisterPatientNew.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(myurl, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					int tempPopuloSize=0;
					// Getting JSON Array node
					Testname = jsonObj.getJSONArray("d");

					int ServiceLenghTestSize = Testname.length();

					popular_test_nameTemp.clear();
					for (int i = 0; i < Testname.length(); i++) {
						JSONObject c = Testname.getJSONObject(i);
						String testname = c.getString(TAG_TESTNAME);
						String tid = c.getString(TAG_TESTID);
						String ispopular = c.getString("IsPopular");
						String price = c.getString("TestPrice");
						String discount_price = c.getString("TestDiscountPrice");

						if (ispopular.equalsIgnoreCase("true")) {
							String strPopularTestData = testname + "#" + tid + "#" + price + "#" + discount_price;
							popular_test_nameTemp.add(strPopularTestData);
						}
						tempPopuloSize =popular_test_nameTemp.size();
					}


					if((ServiceLenghTestSize == setTestSizee )&& (tempPopuloSize ==setTestPopulorSizee) ){
					}else{

						popular_test_name.clear();
						test_name_all.clear();
						allTestName.clear();
						popularTestName.clear();

						// looping through All Contacts
						for (int i = 0; i < Testname.length(); i++) {
							// String listd = new String();
							JSONObject c = Testname.getJSONObject(i);

							String testname = c.getString(TAG_TESTNAME);
							String tid = c.getString(TAG_TESTID);
							String ispopular = c.getString("IsPopular");
							String price = c.getString("TestPrice");
							String discount_price = c.getString("TestDiscountPrice");
							String Short_Name = c.getString("ShortName");


							double discount_priceRate = Double.parseDouble(price) - Double.parseDouble(discount_price);

							String discount_priceRateStrg = String.valueOf(discount_priceRate);

							String strAllTestData = testname + "#" + tid + "#" + price + "#" + discount_priceRateStrg + "# " + Short_Name ;
							test_name_all.add(strAllTestData);
							testid_all.add(tid);
							test_price.add(price);
							test_discountprice.add(discount_price);

							if (ispopular.equalsIgnoreCase("true")) {
								String strPopularTestData = testname + "#" + tid + "#" + price + "#" + discount_priceRateStrg +  "# " + Short_Name;
								popular_test_name.add(strPopularTestData);
							}
						}

						allTestName.addAll(test_name_all);
						popularTestName.addAll(popular_test_name);
						SharedPreferences.Editor edt = sharedpreferences.edit();
						edt.putStringSet("allTestNames", allTestName);
						edt.putStringSet("popularTestData", popularTestName);
						edt.commit();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return test_name;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);

			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			myurl = url;
		}
	}




	//*** Async task class to get json by making HTTP call **/
	private class GetTestProfile extends
			AsyncTask<String, Void, ArrayList<String>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			test_profile_name.clear();
			testpid.clear();
		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(profilemyurl,
					ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					Testprofile = jsonObj.getJSONArray("d");

					int ServiceLenghTestProfileSize = Testprofile.length();
					if(ServiceLenghTestProfileSize == setProfileSizee){
					}else {

						test_profile_name.clear();
						profileNameSet.clear();

						// looping through All Contacts
						for (int i = 0; i < Testprofile.length(); i++) {
							// String listd = new String();
							JSONObject c = Testprofile.getJSONObject(i);

							String listd = c.getString(TAG_TESTPROFILE);
							String test_pid = c.getString(TAG_TESTPROFILEID);

							String strProfileData = listd + "#" + test_pid;
							test_profile_name.add(strProfileData);
						}

						profileNameSet.addAll(test_profile_name);
						SharedPreferences.Editor edt = sharedpreferences.edit();
						edt.putStringSet("setprofilename", profileNameSet);
						edt.commit();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return test_profile_name;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);

			profilemyurl = profileurl;
		}
	}

	//*** Async task class to get json by making HTTP call **//
	private class GetTestCategory extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			for (int i = 0; i < testcategory_Listview.size(); i++) {
				testcategory_Listview.remove(i);

			}

        /*    // Showing progress dialog
            pDialog = new ProgressDialog(BookAppointment.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();*/
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(category_myurl,
					ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					Testcategory = jsonObj.getJSONArray("d");

					int ServiceLenghTestCatSize = Testcategory.length();
					if(ServiceLenghTestCatSize == setCategorySizee){
					}else {

						testcategory_Listview.clear();
						categoryNameSet.clear();

						// looping through All Contacts
						for (int i = 0; i < Testcategory.length(); i++) {
							// String listd = new String();
							JSONObject c = Testcategory.getJSONObject(i);

							String listd = c.getString(TAG_TESTCATEGORY);

							// adding contact to contact list
							testcategory_Listview.add(listd);
						}
						categoryNameSet.addAll(testcategory_Listview);
						SharedPreferences.Editor edt = sharedpreferences.edit();
						edt.putStringSet("setcategoryname", categoryNameSet);
						edt.commit();

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
         /*   if (pDialog.isShowing())
                pDialog.dismiss();*/

			// paintTabs();

			category_myurl = category_url;
		}
	}



	/*** Async task class to get json by making HTTP call **/
	private class GetCollectionCenterList extends
			AsyncTask<String, Void, ArrayList<String>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//center_nameId_all.clear();
			//centerid_all.clear();
			//allCollectionCenterNameSet.clear();


			//SharedPreferences.Editor editorr = sharedpreferences.edit();
			//editor.remove("allaffiliationNameSet");
			//	editorr.remove("setallCollectionCenterNameSet");
			//	editorr.commit();

		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response

			String jsonStr = sh.makeServiceCall(collection_center_myurl, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node

					Centername = jsonObj.getJSONArray("d");

					int ServiceLenghCcSize = Centername.length();



					if(ServiceLenghCcSize == setCCSizee){



					}else{


						center_nameId_all.clear();
						centerid_all.clear();
						allCollectionCenterNameSet.clear();


						// looping through All Contacts
						for (int i = 0; i < Centername.length(); i++) {
							// String listd = new String();
							JSONObject c = Centername.getJSONObject(i);


							String collectionCebterName = c.getString("Name");
							String	center_id = c.getString("CollectionCenterID");

							String strAllcollectionData = collectionCebterName + "#" + center_id;

							center_nameId_all.add(strAllcollectionData);
							centerid_all.add(collectionCebterName);

						}

						allCollectionCenterNameSet.addAll(center_nameId_all);
						SharedPreferences.Editor edt = sharedpreferences.edit();
						edt.putStringSet("setallCollectionCenterNameSet", allCollectionCenterNameSet);
						edt.commit();

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
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);
			System.out.println(allCollectionCenterNameSet);

			collection_center_myurl =collection_center_url;


		}
	}




	//*** Async task class to get json by making HTTP call **//
	private class GetAffiliationList extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			//	affiliation_nameId_all.clear();
			//	affiliation_all.clear();
			//	allaffiliationNameSet.clear();


			// Showing progress dialog
			//	pDialog = new ProgressDialog(GetLabList.this);
			//	pDialog.setMessage("Please wait...");
			//	pDialog.setCancelable(false);
			//	pDialog.show();

			//pDialog = new ProgressDialog(GetLabListReport.this);
			//pDialog.setMessage("Please wait...");
			//pDialog.setCancelable(false);
			//pDialog.show();

			//	SharedPreferences.Editor editorr = sharedpreferences.edit();
			//editorr.remove("setallaffiliationNameSet");

			//editorr.commit();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			//String jsonStr = sh.makeServiceCall(category_myurl,ServiceHandler.GET);
			String jsonStrg = sh.makeServiceCall(Affiliation_myurlll, ServiceHandler.GET);

			if (jsonStrg != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStrg);

					// Getting JSON Array node
					affiliationnamee = jsonObj.getJSONArray("d");


					int ServiceLenghAfilSize = affiliationnamee.length();


					if (ServiceLenghAfilSize == setAfilSizee) {


					} else {


						affiliation_nameId_all.clear();
						affiliation_all.clear();
						allaffiliationNameSet.clear();

						// looping through All Contacts
						for (int i = 0; i < affiliationnamee.length(); i++) {
							// String listd = new String();
							JSONObject c = affiliationnamee.getJSONObject(i);


							String AffiliationName = c.getString("AffiliationCompanyName");
							String Affiliation_id = c.getString("AffiliationID");

							String strAllAffiliationData = AffiliationName + "#" + Affiliation_id;

							affiliation_nameId_all.add(strAllAffiliationData);
							affiliation_all.add(AffiliationName);

						}

						allaffiliationNameSet.addAll(affiliation_nameId_all);
						SharedPreferences.Editor edt = sharedpreferences.edit();
						edt.putStringSet("setallaffiliationNameSet", allaffiliationNameSet);
						edt.commit();


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
			//if (pDialog.isShowing())
			//	pDialog.dismiss();

			Affiliation_myurlll = Affiliation_url;

			//	Intent iintent = new Intent(GetLabListReport.this,ViewReportLabUser.class);
			//  Intent iintent = new Intent(MassagList.this, AllTestDataPhlebo.class);
			//startActivity(iintent);
			//	finish();

		}
	}






}
