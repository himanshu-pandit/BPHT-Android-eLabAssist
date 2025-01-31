package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.util.Locale;
import java.util.Set;

import static com.bluepearl.dnadiagnostics.SignupActivity.TAG;

public class LabSelectionForDoctor extends Activity{

	int totalLabcount =0;

	GPSTracker gps;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String pid = null;
	double latitude,longitude;
	ListView nearbylabs_listView;
	Builder alertbox;
	EditText search;
	TextView lblLab,lblLogout;
	Button cancel;
	LabSelectionAdapterForDoctor adapter;
	String isHomeCollection,isRegister,isPreffered;
	String id;
// iI lL
	static ArrayList<LabSelectionDoctorDetails> labList = new ArrayList<LabSelectionDoctorDetails>();

	JSONArray Labname = null;

	ArrayList<Integer> labid_list = new ArrayList<Integer>();
	ArrayList<LabSelectionDoctorDetails> labnamesList = new ArrayList<LabSelectionDoctorDetails>();

	private static final String lablist_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/LabsInVicinityAndRecentLabs?patientid=";
	    private static String lablist_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/LabsInVicinityAndRecentLabs?patientid=";
	private static final String collection_center_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";
	private static String collection_center_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";

	private static final String Affiliation_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";
	private static String Affiliation_myurlll = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";

	static String statusAutho = "";
	static String num_valueAutho = null;
	static String password_valueAutho = null;
	private String LoginUrl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";



	JSONArray Centername = null;

	int setCCSizee,setAfilSizee;
	Set<String> setallCollectionCenterNameSet = new HashSet<String>();
	Set<String> setallaffiliationNameSet = new HashSet<String>();
	ArrayList<String> center_nameId_all = new ArrayList<String>();
	ArrayList<String> centerid_all = new ArrayList<String>();

	ArrayList<String> affiliation_nameId_all = new ArrayList<String>();
	ArrayList<String> affiliation_all = new ArrayList<String>();

	JSONArray affiliationnamee = null;
	Set<String> allaffiliationNameSet;
	Set<String> allCollectionCenterNameSet;



	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lab_selection_doctor);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");

		num_valueAutho = sharedpreferences.getString("phno","");
		password_valueAutho = sharedpreferences.getString("password","");

		nearbylabs_listView = ( ListView ) findViewById(R.id.listviewNearby);
		search = (EditText)findViewById(R.id.etSearch);
		lblLab = (TextView)findViewById(R.id.txtviewlab);
		lblLogout = (TextView)findViewById(R.id.txtviewlogout);

		search.setTypeface(custom_font);
		lblLab.setTypeface(custom_btnfont);
		lblLogout.setTypeface(custom_btnfont);

		allaffiliationNameSet = new HashSet<String>();
		allCollectionCenterNameSet = new HashSet<String>();

		lablist_myurl = lablist_url;
		lablist_myurl = lablist_myurl + pid + "&" + "Task=" + "2";

	//	new GetLabList().execute();


		if (isInternetOn(getApplicationContext())) {

			new LoginCheck().execute(new String[]{LoginUrl});
		}else {
			Toast.makeText(LabSelectionForDoctor.this, "Internet Connection is not available..!", Toast.LENGTH_SHORT).show();
		}

		lblLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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

				// TODO Auto-generated method stub
				Intent i1 = new Intent(LabSelectionForDoctor.this, SigninActivity.class);
				i1.putExtra("fromLogout","yes");
				i1.putExtra("fromchange", "no");
				//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
				//finish();
			}
		});

		/*nearbylabs_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				id = ((LabSelectionDoctorDetails) arg0.getItemAtPosition(arg2)).getLabId();

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("SelectedLabId", id);

				editor.commit();

				Intent i = new Intent(LabSelectionForDoctor.this,ViewReportDoctor.class);
				startActivity(i);

			}
		});
		 */

		/**
		 * Enabling Search Filter
		 * */
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				//DialogDescription.this.arrayAdapter.getFilter().filter(cs);
				String text = search.getText().toString().toLowerCase(Locale.getDefault());
				//DialogDescription.this.arrayAdapter.getFilter().filter(text);
				//arrayAdapter.filter(text);
 				//adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/*** Async task class to get json by making HTTP call **/
	private class GetLabList extends AsyncTask<String, Void, ArrayList<LabSelectionDoctorDetails>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<LabSelectionDoctorDetails> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(lablist_myurl, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);


					JSONObject labObj = jsonObj.getJSONObject("d");
					JSONArray labinvicinity = labObj.getJSONArray("UserRolesList");

					// Getting values from register lab array
					for (int i = 0; i < labinvicinity.length(); i++) {

						JSONObject c = labinvicinity.getJSONObject(i);

						String LabAddress = c.getString("PathologyLabAddress");
						String LabName = c.getString("PathologyLabName");
						String Labid = c.getString("PathologyLabFID");
						String LabRole = c.getString("UserRole");

						LabSelectionDoctorDetails det = new LabSelectionDoctorDetails(LabName,LabAddress,Labid,LabRole);
						// adding contact to contact list
						labnamesList.add(det);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return labnamesList;
		}

		@Override
		protected void onPostExecute(ArrayList<LabSelectionDoctorDetails> result) {
			super.onPostExecute(result);

			totalLabcount =labnamesList.size();
			Log.d(TAG, "totalLabcount: "+totalLabcount);
			if( totalLabcount == 1) {




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


				String disid = labnamesList.get(0).getLabId();

				collection_center_myurl =collection_center_url;
				Affiliation_myurlll =Affiliation_url;
				collection_center_myurl = collection_center_myurl + disid;
				Affiliation_myurlll = Affiliation_myurlll + disid;

				new GetCollectionCenterList().execute();
				new GetAffiliationList().execute();





				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("SelectedLabId", labnamesList.get(0).getLabId());
				editor.putString("LabIdFromLogin", labnamesList.get(0).getLabId());
				editor.putString("SelectedLabName", labnamesList.get(0).getLabName());
				editor.putString("Role", labnamesList.get(0).getLabRole());
				editor.putString("usertype", labnamesList.get(0).getLabRole());

				editor.remove("allTestNames");
				editor.remove("popularTestData");
				editor.remove("setprofilename");
				editor.remove("setcategoryname");

				Log.d(TAG, "totalLabcount: " + labnamesList.get(0).getLabId() + labnamesList.get(0).getLabName() + labnamesList.get(0).getLabRole());

				editor.commit();

				if (labnamesList.get(0).getLabRole().equals("Admin")) {

					Intent i1 = new Intent(LabSelectionForDoctor.this, Dashboard.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i1);

				} else if (labnamesList.get(0).getLabRole().equals("Collection Boy")) {
					Intent i1 = new Intent(LabSelectionForDoctor.this, PhlebotomyTabOn.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i1);
				}
				else if (labnamesList.get(0).getLabRole().equals("Patient")) {
					Intent i1 = new Intent(LabSelectionForDoctor.this, AddressSelection.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i1);
				} else {
					Intent i1 = new Intent(LabSelectionForDoctor.this, RegisterPatientNew.class);
					//	 i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i1);
				}

			}

			else
			{
				adapter = new LabSelectionAdapterForDoctor(LabSelectionForDoctor.this, labnamesList);
				nearbylabs_listView.setAdapter(adapter);
				setListViewHeightBasedOnChildren(nearbylabs_listView);
			}

			lablist_myurl = lablist_url;
		}
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
	public void onBackPressed() 
	{
		alertbox = new Builder(this)
		.setMessage("Do you want to exit?")
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {

				// Constant.SIGNIN_STATUS = false;
				Intent i1 = new Intent(Intent.ACTION_MAIN);
				i1.addCategory(Intent.CATEGORY_HOME);
				i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i1);
				finish();

			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
			}
		});

		AlertDialog alert1 = alertbox.create();
		alert1.show();

		TextView textView = (TextView) alert1.findViewById(android.R.id.message);
		Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
		Button noButton = alert1.getButton(DialogInterface.BUTTON_NEGATIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);

			if(listItem != null){
				// This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
				listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
				listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
				totalHeight += listItem.getMeasuredHeight();

			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
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
				//Toast.makeText(LabSelectionForDoctor.this, "Verified", Toast.LENGTH_SHORT).show();


				new GetLabList().execute();
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

				Intent i1 = new Intent(LabSelectionForDoctor.this, SigninActivity.class);
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

/////////////////////////////////////////////////////////////////////////













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


			Affiliation_myurlll =Affiliation_url;


		}
	}


















}
