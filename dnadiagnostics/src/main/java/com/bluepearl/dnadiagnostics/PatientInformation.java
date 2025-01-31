package com.bluepearl.dnadiagnostics;

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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientInformation extends BaseActivity
{
	Button bookbtn,btnSelect;
	EditText eAge,eGender,newPname;
	EditText pname;
	private RadioGroup radioGroup;
	TextView txtCurAddress,lblgender;
	private RadioButton radioButton,rb_male,rb_female;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	String Patientid;
	ListView familyList;
	boolean flg = false;
	boolean name_flg = false;
	int my_flg = -1;
	AlertDialog alertbox;
	static String pid = null;
	static String pnameval = null;
	static String patientnameval = null;
	//static String name = null;
	static String text = null;
	static String gender = "";
	static String genderval = "";
	ImageButton btnclear;
	static String patientname_val = "";
	static String pname1;
	static String age = "";
	static String ageval = "";
	static String familymemberid = "";
	static int pat_id;
	static String selectedDocId = "";
	private static String filePath = null;
	static String selected_labnamefromintent = null;
	static String date_val_temp = null;
	static String activity_name = null;
	static String pin = null;
	static String selected_labidfrompref = null;
	String ActivityName;
	FamilyMemberAdapter adapter;
	static String patient_name;
	static String patient_id = "";
	static String patient_age;
	static String patient_gender;
	static String status = "";
	static String finalResult = "";
	static String popular_ids = null;
	static String test_value;
	private ProgressDialog pDialog;
	static String strDate = "";
	static String register_patientgender = null;
	static String selectedCenterId = null;
	static String updated_val = null;
	static String name;
	//String age;
	static String newpname;
	String valueFromPopulatTest = "";
	String valueFromUploadPrec = "";
	String selectedIdFromPopularTable = "";
	String selectedImageFromTab="";
	String valueFromSelectTab = "";
	String selectedtestIdFromTab = "";
	String freetextval = "";
	static String homecollection = "";
	static ArrayList<FamilyMemberListDetails> adapterList = new ArrayList<FamilyMemberListDetails>();

	static JSONArray Patientname = null;

	// JSON Node names
	private static final String TAG_PATIENTNAME = "ShortName";
	private static final String TAG_PATIENTID = "PatientID";
	private static final String TAG_AGE = "Age";
	private static final String TAG_GENDER = "Gender";

	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetFamilyMembers";

	private String appointment_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/PatientAppointMent_Global";

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		tid = sharedpreferences.getString("testid", "");
		testpid = sharedpreferences.getString("testprofileid", "");
		pid = sharedpreferences.getString("patientId", "");
		phone = sharedpreferences.getString("phno", "");
		date_val = sharedpreferences.getString("DateTime", "");
		date_val_temp = date_val;

		filePath = sharedpreferences.getString("filePath", "");
		residencial_address = sharedpreferences.getString("res_add", "");
		official_address = sharedpreferences.getString("office_add", "");
		entered_test_name = sharedpreferences.getString("test_name", "");
		register_patientgender = sharedpreferences.getString("self_patientgender", "");
		addval = sharedpreferences.getString("address", "");
		pin = sharedpreferences.getString("pincode", "");
		dnameval = sharedpreferences.getString("Doctor", "");
		selectedDocId = sharedpreferences.getString("doc_id", "");
		popular_ids = sharedpreferences.getString("popularids", "");
		selectedCenterId = sharedpreferences.getString("centerid", "");
		updated_val = sharedpreferences.getString("updated", "");

		filePath = sharedpreferences.getString("filePath", "");
		homecollection = sharedpreferences.getString("home_collection", "");

		getLayoutInflater().inflate(R.layout.activity_patient_registration, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		bookbtn = (Button)findViewById(R.id.BookButton);
		btnSelect = (Button)findViewById(R.id.SelectButton);
		pname = (EditText)findViewById(R.id.etPatientName);
		eAge = (EditText)findViewById(R.id.etPAge);
		radioGroup = (RadioGroup)findViewById(R.id.radioGender);
		int selectedrb = radioGroup.getCheckedRadioButtonId();
		radioButton = (RadioButton) findViewById(selectedrb);
		rb_male = (RadioButton) findViewById(R.id.radioMale);
		rb_female = (RadioButton) findViewById(R.id.radioFemale);
		btnclear = (ImageButton)findViewById(R.id.clear);
		familyList = (ListView)findViewById(R.id.listviewFamilyMembers);
		lblgender = (TextView)findViewById(R.id.txtGender);

		pname.setTypeface(custom_font);
		eAge.setTypeface(custom_font);
		lblgender.setTypeface(custom_font);
		rb_male.setTypeface(custom_font);
		rb_female.setTypeface(custom_font);
		bookbtn.setTypeface(custom_btnfont);
		btnSelect.setTypeface(custom_btnfont);

		newpname = pname.getText().toString();

		if (tid.equals("")) {
			tid = "";
		}
		if (testpid.equals("")) {
			testpid = "";
		}
		if (filePath.equals("")) {
			filePath = "";
		}
		if (entered_test_name.equals("")) {
			entered_test_name = "";
		}
		if (residencial_address.equalsIgnoreCase("null") || residencial_address.equalsIgnoreCase(""))
		{
			residencial_address = "";
		}
		else if (addval != null && !(addval.equalsIgnoreCase("")))
		{
			txtCurAddress.setText(addval);
		}
		else
		{
			txtCurAddress.setText(residencial_address);
		}

		new ChangeSearchCheck().execute(new String[] { url });

		Intent testIdIntent = getIntent();
		if(testIdIntent != null)
		{
			valueFromPopulatTest = testIdIntent.getStringExtra("isFromPopularTest");
			if(valueFromPopulatTest !=null && valueFromPopulatTest.equalsIgnoreCase("Yes"))
			{
				selectedIdFromPopularTable = testIdIntent.getStringExtra("PopularTestIdInTable");
				freetextval = testIdIntent.getStringExtra("freetext");
			}

			valueFromUploadPrec  = testIdIntent.getStringExtra("isFromUploadTab");
			if(valueFromUploadPrec !=null && valueFromUploadPrec.equalsIgnoreCase("Yes"))
			{
				selectedImageFromTab = testIdIntent.getStringExtra("ImageFomTab");
			}

			valueFromSelectTab  = testIdIntent.getStringExtra("isFromSelectTest");
			if(valueFromSelectTab !=null && valueFromSelectTab.equalsIgnoreCase("Yes"))
			{
				selectedtestIdFromTab = testIdIntent.getStringExtra("testIdsFromIntent");
			}
		}

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"dd-MMM-yyyy HH:mm");
		strDate = sdf.format(c.getTime());

		bookbtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				patientname_val = pname.getText().toString();
				ageval = eAge.getText().toString();
				if(rb_male.isChecked())
				{
					genderval="0";
				}
				else
				{
					genderval="1";
				}
				String p_age = eAge.getText().toString();

				int selectedrb = radioGroup.getCheckedRadioButtonId();
				radioButton = (RadioButton) findViewById(selectedrb);
				String rb = radioButton.getText().toString();

				if (isInternetOn(getApplicationContext()))
				{
					if(pname.getText().toString().equals(""))
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
						//Toast.makeText(getApplicationContext(), "Please enter patient name", Toast.LENGTH_LONG).show();
					}
					else
					{
						SharedPreferences.Editor editor = sharedpreferences.edit();

						editor.putString("SelectedPatientName", patient_name);
						editor.putString("SelectedPatient", patient_id);
						editor.putString("SelectedPatientAge", patient_age);
						editor.putString("SelectedPatientGender", patient_gender);
						editor.commit();

						new uploadAllData().execute(new String[] { appointment_url });
					}
				}
				else
				{
					alertBox();
				}
			}
		});

		familyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("self", "no");
				editor.commit();

				name = ((FamilyMemberListDetails) arg0.getItemAtPosition(arg2)).getPatientName();
				age = ((FamilyMemberListDetails) arg0.getItemAtPosition(arg2)).getAge();
				gender = ((FamilyMemberListDetails) arg0.getItemAtPosition(arg2)).getGender();
				familymemberid = ((FamilyMemberListDetails) arg0.getItemAtPosition(arg2)).getPatientId();

				pname.setText(name);

				if(age.equals("-1"))
				{
					eAge.setText("");
				}
				else
				{
					eAge.setText(age);
				}

				if(gender.equals("0"))
				{
					rb_female.setChecked(false);
					rb_male.setChecked(true);
				}
				else if(gender.equals("1"))
				{
					rb_female.setChecked(true);
					rb_male.setChecked(false);
				}
				else
				{
					rb_female.setChecked(false);
					rb_male.setChecked(false);
				}
			}
		});

		btnclear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pname.setText("");
				eAge.setText("");
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("self", "no");
				editor.commit();
				rb_male.setChecked(true);
			}
		});

		btnSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				patientnameval = pname.getText().toString();
				ageval = eAge.getText().toString();
				if(rb_male.isChecked())
				{
					genderval="0";
				}
				else
				{
					genderval="1";
				}

				String idOfPopularTest = "";
				if(valueFromPopulatTest!= null && valueFromPopulatTest.equalsIgnoreCase("Yes"))
				{
					idOfPopularTest = selectedIdFromPopularTable;
				}

				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("SelectedPatient", familymemberid);
				editor.putString("SelectedPatientName", patientnameval);
				if(patientnameval != null && !(patientnameval.equalsIgnoreCase("")))
				{
					editor.putString("SelectedPatientName", patientnameval);
				}
				else
				{
					editor.putString("SelectedPatientName",  sharedpreferences.getString("MainPatientName",""));
				}

				if(familymemberid.equalsIgnoreCase("null") || familymemberid.equalsIgnoreCase(""))
				{
					editor.putString("SelectedPatientAge", ageval);
				}
				else
				{
					editor.putString("SelectedPatientAge", age);
				}

				if(familymemberid.equalsIgnoreCase("null") || familymemberid.equalsIgnoreCase(""))
				{
					editor.putString("SelectedPatientGender", genderval);
				}
				else
				{
					editor.putString("SelectedPatientGender", gender);
				}

				editor.putString("self", "no");
				editor.putString("populartestidfromintent", idOfPopularTest);
				editor.putString("Enteredtestidfromintent", freetextval);
				editor.putString("imageFromPatientInfo", selectedImageFromTab);
				editor.putString("testIdFromPatientInfo", selectedtestIdFromTab);
				editor.commit();

				Intent i = new Intent(PatientInformation.this, BookAppointment.class);
				startActivity(i);
				//finish();
			}
		});
	}

	public static ArrayList<FamilyMemberListDetails> SearchPOST(String url)
	{
		InputStream inputStream = null;
		String result = "";

		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"PatientName\"", "\"\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			//finalString = "{" + finalString + "}";
			//	String tempString = "\"objSP\"" + ":";
			//String strToServer = tempString + finalString;
			String strToServer1 = "{" + finalString + "}";

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
				try
				{
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					Patientname = jsonObj.getJSONArray("d");

					FamilyMemberListDetails listd1 = new FamilyMemberListDetails();
					listd1.setPatientName("SELF");
					listd1.setPatientId(pid);
					listd1.setAge(age);
					listd1.setGender(gender);
					adapterList.add(listd1);

					if(Patientname.length() > 0)
					{
						// looping through All Contacts
						for (int i = 0; i < Patientname.length(); i++)
						{
							FamilyMemberListDetails listd = new FamilyMemberListDetails();
							JSONObject c = Patientname.getJSONObject(i);

							patient_name = c.getString(TAG_PATIENTNAME);
							patient_id = c.getString(TAG_PATIENTID);
							patient_age = c.getString(TAG_AGE);
							patient_gender = c.getString(TAG_GENDER);

							listd.setPatientName(patient_name);
							listd.setPatientId(patient_id);
							listd.setAge(patient_age);
							listd.setGender(patient_gender);

							// adding contact to contact list
							adapterList.add(listd);
						}
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
		return adapterList;
	}

	private class ChangeSearchCheck extends AsyncTask<String, Void, ArrayList<FamilyMemberListDetails>>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			adapterList.clear();
		}

		@Override
		protected ArrayList<FamilyMemberListDetails> doInBackground(String... params)
		{
			return SearchPOST(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<FamilyMemberListDetails> result)
		{
			super.onPostExecute(result);

			adapter = new FamilyMemberAdapter(PatientInformation.this, adapterList);

			// Set The Adapter
			familyList.setAdapter(adapter);
		}
	}

	private class uploadAllData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog pDialog = new
			pDialog = new ProgressDialog(PatientInformation.this);
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
			nameValuePairs.add(new BasicNameValuePair("\"SelectedTest\"", "\"" + tid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedProfile\"","\"" + testpid + "\""));
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
			if(patientname_val.equalsIgnoreCase("self"))
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
				if(familymemberid.equalsIgnoreCase("null") || familymemberid.equalsIgnoreCase(""))
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
							"\"" + patientname_val + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"true\""));

					if(ageval.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"Age\"",
								"\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + ageval + "\""));
					}
					if(genderval.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + genderval + "\""));
					}

				}
				else
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\""
							+ familymemberid + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
							"\"\""));
					nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"true\""));

					if(age.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"Age\"",
								"\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + age + "\""));
					}
					if(gender.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + gender + "\""));
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
			nameValuePairs.add(new BasicNameValuePair("\"patientaddress\"", "\""+ addval + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"emailid\"", "\"\""));

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

	private void alertBox()
	{
		Builder alert = new Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText("Alert");
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		alert.setCustomTitle(Mytitle);

		alert.setIcon(R.drawable.sign_logoelab);
		alert.setMessage("Internet Connection is not available..!");
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

				Intent i = new Intent(PatientInformation.this,AddressSelection.class);
			//	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

				Intent i = new Intent(PatientInformation.this,
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
		editor.remove("address");
		editor.remove("Doctor");
		editor.remove("DateTime");
		editor.remove("datetime");
		editor.remove("doc_id");
		editor.remove("ActivityName");
		editor.remove("popularids");
		editor.remove("updated");
		editor.remove("pincode");
		editor.remove("SelectedPatient");
		editor.remove("SelectedPatientName");
		editor.remove("SelectedPatientAge");
		editor.remove("SelectedPatientGender");
		editor.remove("homecollection");
		editor.remove("centerid");

		selectedDocId = "";
		name = "";

		editor.commit();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(PatientInformation.this,BookAppointment.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		//finish();
	}
}