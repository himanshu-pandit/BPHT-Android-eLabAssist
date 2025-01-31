package com.bluepearl.dnadiagnostics;

import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.jsonparsing.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AlllTestName extends BaseActivity {
	TabHost tabHost;
	LocalActivityManager mLocalActivityManager;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String activity_name = null;
	static String selected_labidfrompref = null;
	private ProgressDialog pDialog;
	String selectedPopularIdFromIntent = "";

	// contacts JSONArray
	JSONArray Testname = null;
	JSONArray Testprofile = null;

	String Test = null;
	String Testprofileid = null;

	JSONArray Testnamebycategory = null;
	JSONArray Testcategory = null;

	// JSON Node names
	private static final String TAG_TESTNAME = "TestName";
	private static final String TAG_TESTID = "TestID";
	private static final String TAG_TESTPROFILE = "TestProfileName";
	private static final String TAG_TESTCATEGORY = "TestCategoryName";
	private static final String TAG_TESTPROFILEID = "TestProfileId";

	// URL to get Test Name
	private static final String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?TestName=&LabID=";
	private static String myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?TestName=&LabID=";

	// URL to get Test Profile
	private static final String profileurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchProfile?LabID=";
	private static String profilemyurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchProfile?LabID=";

	// URL to get all categories
	private static final String category_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetTestCategories?LabID=";
	private static String category_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetTestCategories?LabID=";

	ArrayList<String> test_name_all = new ArrayList<String>();
	ArrayList<String> testid_all = new ArrayList<String>();
	ArrayList<String> test_name = new ArrayList<String>();
	ArrayList<String> testid = new ArrayList<String>();
	ArrayList<String> test_price = new ArrayList<String>();
	ArrayList<String> test_discountprice = new ArrayList<String>();
	ArrayList<String> popular_test_name = new ArrayList<String>();

	ArrayList<String> testcategory_Listview = new ArrayList<String>();
	ArrayList<String> testbycategory_name = new ArrayList<String>();
	ArrayList<String> testidbycategory = new ArrayList<String>();

	ArrayList<String> test_profile_name = new ArrayList<String>();
	ArrayList<String> testpid = new ArrayList<String>();

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		getLayoutInflater().inflate(R.layout.activity_book_appointment,
				frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		// setContentView(R.layout.activity_book_appointment);
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

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		activity_name = sharedpreferences.getString("ActivityName", "");
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		//selected_labidfrompref = "84135443-7a22-4f96-b7ac-9fa211b750c5";

		Set<String> selectedpopularsetinmain = new HashSet<String>();
		Set<String> selectedtestnamesetinmain = new HashSet<String>();
		Set<String> selectedtestcategorysetinmain = new HashSet<String>();
		Set<String> selectedtestprofilesetinmain = new HashSet<String>();

		selectedpopularsetinmain = sharedpreferences.getStringSet("popularTestData", null);
		selectedtestnamesetinmain = sharedpreferences.getStringSet("allTestNames", null);
		selectedtestcategorysetinmain = sharedpreferences.getStringSet("setprofilename", null);
		selectedtestprofilesetinmain = sharedpreferences.getStringSet("setcategoryname", null);

		// create the TabHost that will contain the Tabs
	//	tabHost = (TabHost) findViewById(android.R.id.tabhost);

		/*tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {

				setTabColor(tabHost);
			}
		});*/


	//	mLocalActivityManager = new LocalActivityManager(this, false);
	//	mLocalActivityManager.dispatchCreate(savedInstanceState);
//		tabHost.setup(mLocalActivityManager);

		if(selectedpopularsetinmain != null || selectedtestnamesetinmain != null || selectedtestprofilesetinmain !=null || selectedtestcategorysetinmain != null)
		{
			Intent i = new Intent(AlllTestName.this,RegisterPatientPhlebotomy.class);
			startActivity(i);
			//finish();
		}

		if(selectedpopularsetinmain == null || selectedtestnamesetinmain == null)
		{
			myurl = myurl + selected_labidfrompref;
			new GetTestName().execute();
		}

		if(selectedtestprofilesetinmain == null)
		{
			profilemyurl = profilemyurl + selected_labidfrompref;
			new GetTestProfile().execute();
		}

		if(selectedtestcategorysetinmain == null)
		{
			category_myurl = category_myurl + selected_labidfrompref;
			new GetTestCategory().execute();
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

					// Getting JSON Array node
					Testname = jsonObj.getJSONArray("d");

					// looping through All Contacts
					for (int i = 0; i < Testname.length(); i++) {
						// String listd = new String();
						JSONObject c = Testname.getJSONObject(i);

						String testname = c.getString(TAG_TESTNAME);
						String tid = c.getString(TAG_TESTID);
						String ispopular = c.getString("IsPopular");
						String price = c.getString("TestPrice");
						String discount_price = c.getString("TestDiscountPrice");

						double discount_priceRate = Double.parseDouble(price) - Double.parseDouble(discount_price);

						String discount_priceRateStrg=String.valueOf(discount_priceRate);

						String strAllTestData = testname + "#" + tid + "#" + price + "#" + discount_priceRateStrg;
						test_name_all.add(strAllTestData);
						testid_all.add(tid);
						test_price.add(price);
						test_discountprice.add(discount_price);

						if(ispopular.equalsIgnoreCase("true"))
						{
							String strPopularTestData = testname + "#" + tid + "#" + price + "#" + discount_priceRateStrg;
							popular_test_name.add(strPopularTestData);
						}
					}

					allTestName.addAll(test_name_all);

					popularTestName.addAll(popular_test_name);
					SharedPreferences.Editor edt = sharedpreferences.edit();
					edt.putStringSet("allTestNames", allTestName);
					edt.putStringSet("popularTestData", popularTestName);
					edt.commit();
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

			// Showing progress dialog
			pDialog = new ProgressDialog(AlllTestName.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
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


			category_myurl = category_url;

			Intent i = new Intent(AlllTestName.this,RegisterPatientPhlebotomy.class);
			startActivity(i);
			//finish();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent i = new Intent(AlllTestName.this,PhlebotomyTabOn.class);
		/*i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
		startActivity(i);
		//finish();
	}

}
