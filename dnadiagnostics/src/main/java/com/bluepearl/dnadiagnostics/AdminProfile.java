package com.bluepearl.dnadiagnostics;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdminProfile extends BaseActivityAdmin implements OnItemClickListener
{
	Button submitbtn;
	Dialog dialog;
	ListView modeList;
	EditText newPname,eDob,labname;
	TextView lblgender,lblpreferredlab;
	private int mYear, mMonth, mDay;
	static final int DATE_OF_BIRTH = 0;
	private RadioGroup radioGroup;
	private RadioButton radioButton,rb_male,rb_female;
	private ProgressDialog pDialog;
	static String pid = null;
	static String genderval= null;
	static String name = null;
	static String emailid = null;
	static String resadd = null;
	static String offadd = null;
	static String resPin = null;
	static String offPin = null;
	static String p_dob = null;
	static String rb = null;
	ImageButton btnclear;
	static String labnameval = "";
	String pre_labname = "";
	static String patientname = "";
	static String dateofbirth = "";
	static String gender_val = "";
	static String emailID = "";
	static String officialaddress = "";
	static String residecialaddress = "";
	static String status = "";
	static String selected_labidfrompref = null;
	static String preferredlab_val = "";
	static String preferredlab_val_from_get = "";
	static String patientid_from_get = "";
	String lname;

	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String patient_name = "";
	public static final String age = "";
	public static final String birth_date = "";
	public static final String contact = "";
	public static final String email = "";
	public static final String gender = "";
	PreferredLabAdapter adapter;

	ArrayList<PreferredLabNameDetails> labnameList = new ArrayList<PreferredLabNameDetails>();
	ArrayList<PreferredLabNameDetails> originallabnameList = new ArrayList<PreferredLabNameDetails>();
	ArrayList<String> lab_idList = new ArrayList<String>();

	static SharedPreferences sharedpreferences;

	private String url = "https://devglobal.elabassist.com/Services/GlobalUserService.svc/UpdateProfile";

	private String get_patient_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetUserProfile";

	private static final String lablist_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/LabsInVicinityAndRecentLabs?patientid=";
	private static String lablist_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/LabsInVicinityAndRecentLabs?patientid=";

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");

		getLayoutInflater().inflate(R.layout.activity_user_profile, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);


		submitbtn = (Button)findViewById(R.id.SubmitButton);
		newPname = (EditText)findViewById(R.id.name);
		eDob = (EditText)findViewById(R.id.dateofbirth);
		labname = (EditText)findViewById(R.id.txtlab);
		radioGroup = (RadioGroup)findViewById(R.id.radioGender);
		rb_male = (RadioButton)findViewById(R.id.radioMale);
		rb_female = (RadioButton)findViewById(R.id.radioFemale);
		btnclear = (ImageButton)findViewById(R.id.clear);
		lblgender = (TextView)findViewById(R.id.txtGender);
		lblpreferredlab = (TextView)findViewById(R.id.txtlablabel);

		newPname.setTypeface(custom_font);
		eDob.setTypeface(custom_font);
		lblgender.setTypeface(custom_btnfont);
		rb_male.setTypeface(custom_font);
		rb_female.setTypeface(custom_font);
		lblpreferredlab.setTypeface(custom_btnfont);
		labname.setTypeface(custom_font);
		submitbtn.setTypeface(custom_btnfont);

		if (isInternetOn(getApplicationContext()))
		{
			new GetProfileCheck().execute(new String[] { get_patient_url });
		}
		else
		{
			alertBox();
		}

		lablist_myurl = lablist_myurl + pid +  "&" + "Task=" + "3";
		//new GetLabList().execute();

		Calendar c= Calendar.getInstance();
		mYear=c.get(Calendar.YEAR);
		mMonth=c.get(Calendar.MONTH);
		mDay=c.get(Calendar.DAY_OF_MONTH);

		labname.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(labnameList.size() != originallabnameList.size())
				{
					labnameList.clear();
					labnameList.addAll(originallabnameList);
				}
				showdialog();
			}
		});

		eDob.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				showDialog(DATE_OF_BIRTH);
			}
		});

		submitbtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				name = newPname.getText().toString();
				p_dob = eDob.getText().toString();
				p_dob =changeDateFormat(p_dob);
				labnameval = labname.getText().toString();
				int selectedrb = radioGroup.getCheckedRadioButtonId();
				radioButton = (RadioButton) findViewById(selectedrb);
				rb = radioButton.getText().toString();

				if(rb.equalsIgnoreCase("Male"))
				{
					genderval = "0";
				}
				else if(rb.equalsIgnoreCase("Female"))
				{
					genderval = "1";
				}

				if (isInternetOn(getApplicationContext()))
				{
					if(newPname.getText().toString().equals(""))
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

					else
					{
						SharedPreferences.Editor editor = sharedpreferences.edit();

						editor.putString("patient_name", name);
						editor.putString("birth_date", p_dob);
						editor.putString("userid", patientid_from_get);

						if(rb != null && rb.equalsIgnoreCase("Male"))
						{
							editor.putInt("gender", 0);
						}
						else if(rb != null && rb.equalsIgnoreCase("Female"))
						{
							editor.putInt("gender", 1);
						}
						editor.commit();

						new ProfileCheck().execute(new String[] { url });
					}
				}
				else
				{
					alertBox();
				}
			}
		});

		btnclear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selected_labidfrompref = "";
				labname.setText("");
			}
		});
	}

	private class ProfileCheck extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(AdminProfile.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			return profilePOST(params[0]);
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

			if (result.equals("Profile Updated."))
			{
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("SelectedPatientName", newPname.getText().toString());
				editor.putString("patient_name", newPname.getText().toString());
				editor.putString("MainPatientName", newPname.getText().toString());
				editor.putString("SelectedLabName", labnameval);

				editor.commit();

				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog,null);

				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Profile updated sucsessfully     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);

				toast.show();

				Intent i = new Intent(AdminProfile.this,Dashboard.class);
				/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
				startActivity(i);
				//finish();
			}
			else
			{
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog,null);

				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Profile is not updated     ");
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

	public static String GetprofilePOST(String url)
	{
		InputStream inputStream = null;
		String result = "";
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + pid + "\""));

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

					JSONObject resultObj = (JSONObject) jsonObj.get("d");
					patientname = resultObj.getString("ShortName");
					dateofbirth = resultObj.getString("DOB");
					dateofbirth = changeDateFormatforGetMethod(dateofbirth);
					gender_val = resultObj.getString("Gender");
					preferredlab_val = resultObj.getString("PrefferedLabName");
					preferredlab_val_from_get =  resultObj.getString("PrefferedLabID");
					patientid_from_get =  resultObj.getString("UserID");
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
		}
		return status;
	}

	private class GetProfileCheck extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(AdminProfile.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			return GetprofilePOST(params[0]);
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

			SharedPreferences.Editor editor = sharedpreferences.edit();

			editor.putString("userid", patientid_from_get);

			editor.commit();

			if(patientname.equalsIgnoreCase("null"))
			{
				newPname.setText("");
			}
			else
			{
				newPname.setText(patientname);
			}
			if(dateofbirth.equalsIgnoreCase("null"))
			{
				eDob.setText("");
			}
			else
			{
				eDob.setText(dateofbirth);
			}
			if(gender_val.equalsIgnoreCase("0"))
			{
				rb_male.setChecked(true);
			}
			else if(gender_val.equalsIgnoreCase("1"))
			{
				rb_female.setChecked(true);
			}
			if(preferredlab_val.equalsIgnoreCase("null") || preferredlab_val.equalsIgnoreCase(""))
			{
				labname.setText("");
			}
			else
			{
				labname.setText(preferredlab_val);
			}

			String str = sharedpreferences.getString("kuthunaala", "");
			if(str.equals("baherun"))
			{
				labname.setText(lname);
			}
		}
	}

	public static String profilePOST(String url)
	{
		InputStream inputStream = null;
		String result = "";
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + pid + "\""));
			Log.d("update-pid",patientid_from_get);
			if(labnameval.equalsIgnoreCase("null") || labnameval.equalsIgnoreCase(""))
			{
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("SelectedLabId", "00000000-0000-0000-0000-000000000000");

				editor.commit();

				nameValuePairs.add(new BasicNameValuePair("\"PrefferedLabID\"", "\"  00000000-0000-0000-0000-000000000000 \""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrefferedLabID\"", "\"" + sharedpreferences.getString("SelectedLabId", "") + "\""));
			}
			Log.d("update-selectedlabid",sharedpreferences.getString("SelectedLabId",""));
			nameValuePairs.add(new BasicNameValuePair("\"ShortName\"", "\"" + name + "\""));
			Log.d("update-shortname",name);
			nameValuePairs.add(new BasicNameValuePair("\"DOB\"", "\"" + p_dob + "\""));
			Log.d("update-DOB",p_dob);
			nameValuePairs.add(new BasicNameValuePair("\"Gender\"", "" + genderval + ""));
			Log.d("update-gender",genderval);
			nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"1\""));
			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			finalString = "{" + finalString + "}";
			String tempString = "\"UserProfile\"" + ":";
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

					String abc = jsonObj.getString("d");
					result = abc.toString();

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
		return result;
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

	@SuppressLint("NewApi") protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case DATE_OF_BIRTH:
			DatePickerDialog dialog_from= new DatePickerDialog(this,mDateSetListener_DOB,mYear, mMonth, mDay);
			Calendar currentDate_from = Calendar.getInstance();
			dialog_from.getDatePicker().setMaxDate(currentDate_from.getTimeInMillis());
			return dialog_from;
		}
		return null;
	}

	//DatePickerDialog from date
	private DatePickerDialog.OnDateSetListener mDateSetListener_DOB = new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
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
				eDob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
			else
			{
				eDob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}

			if(month.length() < 2)
			{
				month = "0" + month;
				eDob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
			else
			{
				eDob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(mYear));
			}
		}
	};

	public String changeDateFormat(String dateToFormat)
	{
		String outputDate = null;

		if(dateToFormat != null && !(dateToFormat.equalsIgnoreCase("")) )
		{
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");
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
			SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
			outputDate = sdfDestination.format(date);
		}
		return outputDate;
	}

	public static String changeDateFormatforGetMethod(String dateToFormat)
	{
		String outputDate = null;

		if(dateToFormat != null && !(dateToFormat.equalsIgnoreCase("")) )
		{
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");
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
			SimpleDateFormat sdfDestination = new SimpleDateFormat("dd-MM-yyyy");
			outputDate = sdfDestination.format(date);
		}
		return outputDate;
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();

		Intent i = new Intent(AdminProfile.this,Dashboard.class);
		/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
		startActivity(i);
		//finish();
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


		alert.setMessage("Internet Connection is not available..!");
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

	/*** Async task class to get json by making HTTP call **/
	private class GetLabList extends AsyncTask<String, Void, ArrayList<PreferredLabNameDetails>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<PreferredLabNameDetails> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(lablist_myurl, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					JSONObject labObj = jsonObj.getJSONObject("d");

					JSONArray registerLab = labObj.getJSONArray("RegisteredLabs");

					// Getting values from register lab array
					for (int i = 0; i < registerLab.length(); i++) {
						PreferredLabNameDetails lab = new PreferredLabNameDetails();
						JSONObject c = registerLab.getJSONObject(i);

						String LabName = c.getString("Labname");
						String ID = c.getString("LabID");

						lab.setName(LabName);
						lab.setId(ID);

						labnameList.add(lab);
						originallabnameList.add(lab);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return labnameList;
		}

		@Override
		protected void onPostExecute(ArrayList<PreferredLabNameDetails> result) {
			super.onPostExecute(result);

			lablist_myurl = lablist_url;
		}
	}

	private void showdialog()
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.view, null);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		Builder builder = new Builder(this);

		builder.setView(layout);

		final EditText search = (EditText) layout.findViewById(R.id.inputSearch);
		final ListView modeList = (ListView) layout.findViewById(R.id.listViewPreferredLabs);
		search.setTypeface(custom_font);

		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		modeList.setOnItemClickListener(this);

		adapter = new PreferredLabAdapter(this, labnameList);
		modeList.setAdapter(adapter);

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				String text = search.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);
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

		dialog = builder.create();

		dialog.show();

		Button noButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
		noButton.setTypeface(custom_bold_font);
		noButton.setTextSize(20);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		lname = ((PreferredLabNameDetails) arg0.getItemAtPosition(arg2)).getName();
		String preferredid = ((PreferredLabNameDetails) arg0.getItemAtPosition(arg2)).getId();

		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.putString("SelectedLabId", preferredid);
		editor.putString("kuthunaala","baherun");

		editor.commit();

		dialog.dismiss();
		labname.setText(lname);
	}
}
