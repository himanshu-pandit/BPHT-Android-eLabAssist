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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestEnterTab extends Activity {
	Button btnok,btnOther;
	Typeface custom_font;
	EditText test,patientname;
	String ispopular,price,discount_price;
	TextView lblpopular,lblOR;
	TableLayout myTestTable;
	static String ids = "";
	String testname;
	static String outputDate = "";
	Bundle data;
	private static String filePath = null;
	private ProgressDialog pDialog;
	static String finalResult = "";
	GPSTracker gps;
	public static final String MyPREFERENCES = "MyPrefs";
	static SharedPreferences sharedpreferences;
	static String tid = null;
	static String testpid = null;
	static String pid = null;
	static String phone = null;
	static String add_val = null;
	static String pval = null;
	static String date_val = null;
	static String date_val_temp = null;
	static String dname_val = null;
	static String selectedDocId = "";
	static String selectedPatientId = "";
	static String residencial_address = "";
	static String official_address = "";
	static String status = "";
	static String text = "";
	static String entered_test_name = null;
	static String addval = null;
	static String pin = null;
	static String dnameval = null;
	static String family_pid = null;
	static String family_pname = null;
	static String family_page = null;
	static String self_id = null;
	static String register_patientgender = null;
	static String family_pgender = "0";
	static String pnameval = null;
	static String selected_labidfrompref = null;
	static String selected_patient_name = null;
	static String selectedCenterId = null;
	static String updated_val = null;
	static String pname_fromprofile = "";
	static String test_value;
	int my_flg = -1;
	static String selected_labnamefromintent = null;
	static String strDate = "";
	static String homecollection = "";
	String selectedPopularIdFromPatientInfo = "";
	static String EnteredFromPatientInfo = "";

	Set<String> popularTestDataFromPrefs;

	boolean isOtherSelected = false;
	EditText otherAddress;
	InputMethodManager inputMethodManager;

	private String appointment_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/PatientAppointMent_Global";

	ArrayList<String> test_name = new ArrayList<String>();
	ArrayList<String> testid = new ArrayList<String>();
	ArrayList<String> populartestid = new ArrayList<String>();
	ArrayList<String> test_price = new ArrayList<String>();
	ArrayList<String> test_discountprice = new ArrayList<String>();

	JSONArray Testname = null;
	String my_test_name = "";
	String my_testid = "";
	String my_test_price = "";
	String my_test_discountprice = "";

	String my_test_Short = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.fragment_enter_test);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

		tid = sharedpreferences.getString("testid", "");
		addval = sharedpreferences.getString("address", "");
		pin = sharedpreferences.getString("pincode", "");
		testpid = sharedpreferences.getString("testprofileid", "");
		pid = sharedpreferences.getString("patientId", "");
		phone = sharedpreferences.getString("phno", "");
		date_val = sharedpreferences.getString("DateTime", "");
		date_val_temp = date_val;
		updated_val = sharedpreferences.getString("updated", "");
		filePath = sharedpreferences.getString("filePath", "");
		selectedPatientId = sharedpreferences.getString("SelectedPatient", "");
		register_patientgender = sharedpreferences.getString("self_patientgender", "");
		residencial_address = sharedpreferences.getString("res_add", "");
		official_address = sharedpreferences.getString("office_add", "");
		entered_test_name = sharedpreferences.getString("test_name", "");
		selected_patient_name = sharedpreferences.getString("SelectedPatientName", "");
		selectedDocId = sharedpreferences.getString("doc_id", "");
		selectedCenterId = sharedpreferences.getString("centerid", "");
		addval = sharedpreferences.getString("address", "");
		dnameval = sharedpreferences.getString("Doctor", "");
		family_pid = sharedpreferences.getString("SelectedPatient", "");
		family_pname = sharedpreferences.getString("SelectedPatientName", "");

		family_page = sharedpreferences.getString("SelectedPatientAge", "");
		family_pgender = sharedpreferences.getString("SelectedPatientGender", "0");

		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");

		EnteredFromPatientInfo = sharedpreferences.getString("Enteredtestidfromintent", "");
		pname_fromprofile = sharedpreferences.getString("patient_name", "");

		selectedPopularIdFromPatientInfo = sharedpreferences.getString("populartestidfromintent", "");
		self_id = sharedpreferences.getString("userid", "");
		homecollection = sharedpreferences.getString("home_collection", "");

		btnok = (Button) findViewById(R.id.BookButton);
		btnOther = (Button) findViewById(R.id.MemberButton);
		test = (EditText) findViewById(R.id.etTest);
		patientname = (EditText) findViewById(R.id.etPname);
		myTestTable = (TableLayout) findViewById(R.id.TestNameTable);
		lblpopular = (TextView) findViewById(R.id.textview);
		lblOR = (TextView) findViewById(R.id.ORlabel);

		test.setTypeface(custom_font);
		patientname.setTypeface(custom_font);
		lblpopular.setTypeface(custom_btnfont);
		btnok.setTypeface(custom_btnfont);
		btnOther.setTypeface(custom_btnfont);
		lblOR.setTypeface(custom_btnfont);

		// changes

		//Set<String> popularTestDataFromPrefs = new HashSet<String>();
		 popularTestDataFromPrefs = new HashSet<String>();

		popularTestDataFromPrefs = sharedpreferences.getStringSet("popularTestData", null);

		if(EnteredFromPatientInfo != null || !(EnteredFromPatientInfo.equalsIgnoreCase("")))
		{
			test.setText(EnteredFromPatientInfo);
		}

		if(popularTestDataFromPrefs != null)
		{
			Iterator<String> i1 = popularTestDataFromPrefs.iterator();

			while(i1.hasNext())
			{
				String my_test_data = i1.next();
				String[] popularTestsData = my_test_data.split("#");

				my_test_name = popularTestsData[0];
				my_testid = popularTestsData[1];
				my_test_price = popularTestsData[2];
				my_test_discountprice = popularTestsData[3];

				my_test_Short= popularTestsData[4];

				//test_name.add(my_test_name+", "+my_test_Short);

				//populateTable(my_test_name, my_testid, my_test_price, my_test_discountprice);
				populateTable(my_test_name+"( "+my_test_Short+" )", my_testid, my_test_price, my_test_discountprice);
			}
		}
		else
		{
			lblpopular.setVisibility(View.GONE);
			lblOR.setVisibility(View.GONE);
		}
		// changes ends here


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
		if (official_address.equalsIgnoreCase("null")) {
			official_address = "";
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

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"dd-MMM-yyyy HH:mm");
		strDate = sdf.format(c.getTime());


		btnok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				test_value = test.getText().toString();
				pval = patientname.getText().toString();

				for(int j=0; j< myTestTable.getChildCount(); j++)
				{
					TableRow tr = (TableRow)myTestTable.getChildAt(j);
					CheckBox chk = (CheckBox)tr.getChildAt(0);
					if(chk.isChecked())
					{
						if(ids.equals(""))
						{
							ids = String.valueOf(chk.getId());
						}
						else
						{
							ids = ids + "," + String.valueOf(chk.getId());
						}
					}

				}

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("popularids", "Upload");

				editor.commit();

				if (isInternetOn(getApplicationContext())) {
					new uploadAllData().execute(new String[] { appointment_url });
				} else {
					alertBox("Internet Connection is not available..!");
				}
			}
		});

		btnOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String edittextval = test.getText().toString();

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("ActivityName", "Enter");

				editor.commit();

				Intent i = new Intent(TestEnterTab.this,PatientInformation.class);
				i.putExtra("isFromPopularTest", "Yes");
				String selectedpopularids = "";
				for(int j=0; j< myTestTable.getChildCount(); j++)
				{
					TableRow tr = (TableRow)myTestTable.getChildAt(j);
					CheckBox chk = (CheckBox)tr.getChildAt(0);
					if(chk.isChecked())
					{
						if(selectedpopularids.equals(""))
						{
							selectedpopularids = String.valueOf(chk.getId());
						}
						else
						{
							selectedpopularids = selectedpopularids + "," + String.valueOf(chk.getId());
						}
					}
				}
				i.putExtra("PopularTestIdInTable", selectedpopularids);
				i.putExtra("freetext", edittextval);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});
	}

	private class uploadAllData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog pDialog = new
			pDialog = new ProgressDialog(TestEnterTab.this);
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
			nameValuePairs.add(new BasicNameValuePair("\"SelectedTest\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedProfile\"","\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedPopularTest\"","\"" + ids + "\""));
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
			if (str.equals("yes")) {
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

			System.out.println("strToServer " +strToServer);

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
				//TableRow.LayoutParams.WRAP_CONTENT,
				0,
				TableRow.LayoutParams.WRAP_CONTENT,1f));
		chkbox.setText(tname);
		chkbox.setTypeface(custom_font);
		chkbox.setId(Integer.parseInt(popularid));
		chkbox.setBackgroundColor(Color.TRANSPARENT);
		chkbox.setTextSize(12);

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

				Intent i = new Intent(TestEnterTab.this,
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

				Intent i = new Intent(TestEnterTab.this,
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
		editor.remove("populartestidfromintent");
		editor.remove("Enteredtestidfromintent");
		editor.remove("homecollection");
		editor.remove("centerid");
		//editor.remove("popularTestData");

		selectedDocId = "";
		ids = "";

		editor.commit();
	}

}
