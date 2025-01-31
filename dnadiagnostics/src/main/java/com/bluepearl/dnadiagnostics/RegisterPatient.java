package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jsonparsing.webservice.ServiceHandler;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegisterPatient extends BaseActivityDL {

	Button btnQuickBook;//,btnSelectTest;
	ImageButton btnLabchange;
	static EditText editTxtPatientName,editTxtAge,editTxtTestName,editTxtRefDocName;
	TextView lblgender, txtlname;
	private static RadioButton rb_male;
	private RadioButton rb_female;
	ImageButton imgbtnClear, imgbtnClear2, imgbtnClear3, imgbtnClear4;
	static String patient_name = "";
	static String patient_age = "";
	static String patient_gender = "";
	static String test_name = "";
	static String refDocName = "";
	static String genderval = "";
	static String pid = "";
	static String selected_labidfrompref_other = "";
	static String selected_labnamefrompref_other = "";
	static String role = "";
	AlertDialog alertbox;
	static String status = "";

	static String statusAutho = "";
	static String num_valueAutho = null;
	static String password_valueAutho = null;
	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";




	private ProgressDialog pDialog;


	LinearLayout ReferddoctorLayout;

	JSONArray Centername = null;

	JSONArray affiliationnamee = null;

	Set<String> allCollectionCenterNameSet;
	Set<String> allTestID;
	Set<String> allTestPrice;
	Set<String> allTestDiscountPrice;
	Set<String> popularTestName;
	Set<String> popularTestID;
	Set<String> popularTestPrice;
	Set<String> popularTestDiscountPrice;
	Set<String> profileNameSet;
	Set<String> profileIDSet;
	Set<String> allaffiliationNameSet;
	Set<String> categoryIDSet;


	int setCCSizee,setAfilSizee;

	private static final String collection_center_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";
	private static String collection_center_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";

	private static final String Affiliation_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";
	private static String Affiliation_myurlll = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";

	ArrayList<String> center_nameId_all = new ArrayList<String>();
	ArrayList<String> centerid_all = new ArrayList<String>();

	ArrayList<String> affiliation_nameId_all = new ArrayList<String>();
	ArrayList<String> affiliation_all = new ArrayList<String>();


	private static String new_quickbook_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/QuickBook";
	
	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		
		getLayoutInflater().inflate(R.layout.activity_register_patient, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));     
		getSupportActionBar().setBackgroundDrawable(colorDrawable);
		
		sharedpreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");
		selected_labidfrompref_other = sharedpreferences.getString("SelectedLabId", "");
		selected_labnamefrompref_other = sharedpreferences.getString("SelectedLabName", "");
		role = sharedpreferences.getString("Role","");

		num_valueAutho = sharedpreferences.getString("phno","");
		password_valueAutho = sharedpreferences.getString("password","");

		
		btnQuickBook = (Button) findViewById(R.id.BookButton2);
		//btnSelectTest = (Button) findViewById(R.id.SelectButton2);
		editTxtAge = (EditText)findViewById(R.id.etPAge2);
		editTxtTestName=(EditText)findViewById(R.id.etEnterTest2);
		editTxtPatientName = (EditText)findViewById(R.id.etPatientName2);
		editTxtRefDocName = (EditText)findViewById(R.id.etRefDoctor2);
		imgbtnClear = (ImageButton)findViewById(R.id.clear2);
		imgbtnClear2 = (ImageButton)findViewById(R.id.clear3);
		imgbtnClear3 = (ImageButton)findViewById(R.id.clear4);
		imgbtnClear4 = (ImageButton)findViewById(R.id.clear5);
		lblgender = (TextView)findViewById(R.id.txtGender2);
		txtlname = (TextView)findViewById(R.id.txtlabname2);
		rb_male = (RadioButton) findViewById(R.id.radioMale2);
		rb_female = (RadioButton) findViewById(R.id.radioFemale2);
		btnLabchange = (ImageButton) findViewById(R.id.changelabbtn2);

		ReferddoctorLayout =(LinearLayout)findViewById(R.id.ll_doctorname) ;



		editTxtPatientName.setTypeface(custom_font);
		editTxtAge.setTypeface(custom_font);
		editTxtTestName.setTypeface(custom_font);
		editTxtRefDocName.setTypeface(custom_font);
		lblgender.setTypeface(custom_font);
		txtlname.setTypeface(custom_font);
		rb_male.setTypeface(custom_font);
		rb_female.setTypeface(custom_font);
		btnQuickBook.setTypeface(custom_bold_font);



		allCollectionCenterNameSet = new HashSet<String>();
		allTestID = new HashSet<String>();
		allTestPrice = new HashSet<String>();
		allTestDiscountPrice = new HashSet<String>();
		popularTestName = new HashSet<String>();
		popularTestID = new HashSet<String>();
		popularTestPrice = new HashSet<String>();
		popularTestDiscountPrice = new HashSet<String>();
		profileNameSet = new HashSet<String>();
		profileIDSet = new HashSet<String>();
		allaffiliationNameSet = new HashSet<String>();
		categoryIDSet = new HashSet<String>();




		if(selected_labnamefrompref_other != null || !(selected_labnamefrompref_other.equalsIgnoreCase("")))
		{
			txtlname.setText(selected_labnamefrompref_other);
		}
		
		btnLabchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor1 = sharedpreferences.edit();

				editor1.remove("CheckedValue");

				editor1.commit();

				Intent i = new Intent(RegisterPatient.this,LabSelectionForDoctor.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});

		Set<String> setallCollectionCenterNameSet = new HashSet<String>();
		Set<String> setallaffiliationNameSet = new HashSet<String>();



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


		if(
				role.equalsIgnoreCase("Doctor")
				)
		{
			ReferddoctorLayout.setVisibility(View.GONE);
		}
		else
		{
			ReferddoctorLayout.setVisibility(View.VISIBLE);
		}

		new GetCollectionCenterList().execute();

		new GetAffiliationList().execute();

	//	new LoginCheck().execute(new String[] { url });


		btnQuickBook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				patient_name = editTxtPatientName.getText().toString();
				patient_age = editTxtAge.getText().toString();	
				test_name = editTxtTestName.getText().toString();
				refDocName = editTxtRefDocName.getText().toString();
				
				if (isInternetOn(getApplicationContext())) 
				{
					if(patient_name.equals(""))
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter patient name     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
					}
					else if(patient_age.equals(""))
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter age     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
					}
					else
					{
						// Call Quick Book Service
						new NewQuickBook().execute(new String[] { new_quickbook_url });
					}
				}
				else 
				{
					alertBox("Internet Connection is not available..!");
				}
			}
		});
		
		/*btnSelectTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				if (isInternetOn(getApplicationContext())) 
				{
					Intent i1 = new Intent(RegisterPatient.this, SelectTest_RegisterPatient.class);
					i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(i1);
					finish();
				}
				else 
				{
					alertBox("Internet Connection is not available..!");
				}
			}
		});*/
		
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
		
		imgbtnClear3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editTxtTestName.setText("");
			}
		});
		
		imgbtnClear4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editTxtRefDocName.setText("");
			}
		});
	}
	
	private class NewQuickBook extends AsyncTask<String, Void, String> 
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(RegisterPatient.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) 
		{
			return QuickBookPOST(params[0]);
		}

		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);

			//Toast.makeText(getApplicationContext(), role, Toast.LENGTH_LONG).show();
			
			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}

			if(result.equals("true"))
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Test registered successfully     ");
			    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			    text.setTypeface(typeface);
			    text.setTextColor(Color.WHITE);
			    text.setTextSize(18);
			    Toast toast = new Toast(getApplicationContext());
			    toast.setDuration(Toast.LENGTH_LONG);
			    toast.setView(layout);

			    toast.show();
			    
			    editTxtPatientName.setText("");
			    editTxtAge.setText("");
			    editTxtTestName.setText("");
			    editTxtRefDocName.setText("");
			    rb_male.setChecked(true);
			}
			else
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Could not register test. Please try again.    ");
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
	
	public static String QuickBookPOST(String url) 
	{
		InputStream inputStream = null;
		String result = "";
		
		String PatientnameVal = editTxtPatientName.getText().toString();
		String AgeVal = editTxtAge.getText().toString();
		String TestNamesVal = editTxtTestName.getText().toString();
		String DoctornameVal = editTxtRefDocName.getText().toString();
		
		if(rb_male.isChecked())
		{
			genderval="0";
		}
		else
		{
			genderval="1";
		}

		try 
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"PatientName\"", "\"" + PatientnameVal + "\""));  
			nameValuePairs.add(new BasicNameValuePair("\"Age\"", "" + AgeVal + ""));
			nameValuePairs.add(new BasicNameValuePair("\"Gender\"", ""+ genderval + ""));
			nameValuePairs.add(new BasicNameValuePair("\"RefDoctor\"", "\""+ DoctornameVal + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"EnteredTest\"", "\""+ TestNamesVal + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"LabFID\"", "\""+ selected_labidfrompref_other + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"GlobalUserFID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"RoleName\"", "\"" + role + "\""));
			
			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			finalString = "{" + finalString + "}";
			String tempString = "\"objQuickBook\"" + ":";
			String strToServer = tempString + finalString;
			String strToServer1 = "{" + strToServer + "}";
			StringEntity se = new StringEntity(strToServer1);
			se.setContentType("application/json;charset=UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
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
					status = jsonObj.get("d").toString();
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

	private static String convertInputStreamToString(InputStream inputStream)throws IOException 
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";

		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
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
	public void onBackPressed() 
	{
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



					if(ServiceLenghAfilSize == setAfilSizee){



					}else{


						affiliation_nameId_all.clear();
						affiliation_all.clear();
						allaffiliationNameSet.clear();

						// looping through All Contacts
						for (int i = 0; i < affiliationnamee.length(); i++) {
							// String listd = new String();
							JSONObject c = affiliationnamee.getJSONObject(i);


							String AffiliationName = c.getString("AffiliationCompanyName");
							String  Affiliation_id = c.getString("AffiliationID");

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

			Affiliation_myurlll =Affiliation_url;

			//	Intent iintent = new Intent(GetLabListReport.this,ViewReportLabUser.class);
			//  Intent iintent = new Intent(MassagList.this, AllTestDataPhlebo.class);
			//startActivity(iintent);
			//	finish();

		}
	}
////////////////////////////////////////  Authentication  /////////////////////////////////

	private class LoginCheck extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

	/*		// Showing progress dialog
			pDialog = new ProgressDialog(SigninActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();*/
		}

		@Override
		protected String doInBackground(String... params)
		{
			return LoginPOST(params[0]);
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

		/*	// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();*/

			if (result.equals("Success"))
			{
                System.out.println("Verified");
				Toast.makeText(RegisterPatient.this, "Verified", Toast.LENGTH_SHORT).show();
			}
			else
			{
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog,null);

				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("    Authentication Error !! \n  Please login again     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);

				toast.show();

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.remove("loginchkbox");
				editor.remove("checkboxchecked");
				editor.remove("allTestNames");
				editor.remove("popularTestData");
				editor.remove("setprofilename");
				editor.remove("setcategoryname");

				editor.remove("SelectedLabId");
				editor.remove("SelectedLabName");

				editor.remove("SelectedLabId");
				editor.remove("LabIdFromLogin");
				editor.remove("SelectedLabName");
				editor.remove("Role");
				editor.remove("usertype");

				editor.commit();

				i1 = new Intent(RegisterPatient.this, SigninActivity.class);
				i1.putExtra("fromLogout","yes");
				i1.putExtra("fromchange", "no");
				//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
				//finish();

			}
		}
	}

	public static String LoginPOST(String url)
	{
		InputStream inputStream = null;
		String result = "";
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"3\""));
			nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\"" + num_valueAutho + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Password\"", "\"" + password_valueAutho + "\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			finalString = "{" + finalString + "}";
			String tempString = "\"objSP\"" + ":";
			String strToServer = tempString + finalString;
			String strToServer1 = "{" + strToServer + "}";

			StringEntity se = new StringEntity(strToServer1);
			se.setContentType("application/json;charset=UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
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
					JSONObject resultObj = (JSONObject) jsonObj.get("d");

					statusAutho = resultObj.getString("Result");
					//Code = resultObj.getString("Data");

				}
				catch (JSONException e)
				{

					statusAutho = "Did not work!";
					e.printStackTrace();
				}
			}
			else
				statusAutho = "Did not work!";
		}
		catch (Exception e)
		{
			// Log.i("InputStream", e.getLocalizedMessage());
			statusAutho = "Did not work!";
		}

		return statusAutho;
	}

}
