package com.bluepearl.dnadiagnostics;

import android.app.ActionBar;
import android.app.AlertDialog;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

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
import java.util.List;

public class MyAppointments extends BaseActivity {
	ActionBar actionBar;
	private ProgressDialog pDialog;
	static String finalResult = "";
	
	// URL to get contacts JSON
	private static final String urlHistory = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAppointmentHistory";
	
	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;
	static String pid = null;
	
	// contacts JSONArray
	static JSONArray patient = null;
	static JSONArray seletedTest = null;
	static JSONArray seletedpProfile = null;
	static String selected_labidfrompref = null;

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	private static ArrayList<GroupItem> listDataHeader;
	private static ArrayList<ChildItem> listDataChild;
	private static final String TAG_SELECTED_TEST = "SelectedTest";
	private static final String TAG_SELECTED_PROFILE = "SelectedProfile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getLayoutInflater().inflate(R.layout.activ_history_appointment,
				frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				getApplicationContext().MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");

		if (isInternetOn(getApplicationContext())) {
			new GetPatientData().execute(new String[] { urlHistory });
		} else {
			showAlert("Internet Connection is not available..!","Alert");
		}

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {
				
			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				return false;
			}
		});
	}

	private class GetPatientData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyAppointments.this);
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
			
			listAdapter = new PatientHistoryAdapter(MyAppointments.this,
					listDataHeader);
			expListView.setAdapter(listAdapter);
			
			if (listAdapter.getGroupCount() == 0) {
				showAlert("No History Available","");
			}
		}
	}

	public static String POST(String url) {
		InputStream inputStream = null;
		String result = "";
		try {
			listDataHeader = new ArrayList<GroupItem>();

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			String strToServer = "{" + finalString + "}";

			StringEntity se = new StringEntity(strToServer);
			se.setContentType("application/json;charset=UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
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
					if (jsonformattString != null) {
						// gp_list = new ArrayList<GroupItem>();
						JSONObject jsonObj = new JSONObject(jsonformattString);
						patient = jsonObj.getJSONArray("d");

						for (int i = 0; i < patient.length(); i++) {
							JSONObject jObj = patient.getJSONObject(i);
							GroupItem groupItem = new GroupItem();
							if (jObj != null) {
								String temp = jObj
										.getString("PatientName");
								String temp1 = jObj
										.getString("AppointmentDate");
								groupItem.setAppointmentAddress(temp);
								groupItem.setAppointmentDate(temp1);
							}
							listDataChild = new ArrayList<ChildItem>();
							seletedTest = jObj.getJSONArray(TAG_SELECTED_TEST);
							if (seletedTest != null) {
								if (seletedTest.toString().equals("[]")) {
								} else {
									for (int j = 0; j < seletedTest.length(); j++) {
										if (jObj != null) {
											ChildItem childItem = new ChildItem();
											String temp3 = seletedTest
													.getString(j);
											childItem.setSelectedTest(temp3);
											listDataChild.add(childItem);
										}
									}
								}
							}
							seletedpProfile = jObj
									.getJSONArray(TAG_SELECTED_PROFILE);
							if (seletedpProfile != null) {
								if (seletedpProfile.toString().equals("[]")) {
								} else {
									for (int j = 0; j < seletedpProfile
											.length(); j++) {
										if (jObj != null) {
											ChildItem childItem = new ChildItem();
											String temp3 = seletedpProfile
													.getString(j);
											childItem.setSelectedTest(temp3);
											listDataChild.add(childItem);
										}
									}
								}
							}
							groupItem.setChildItems(listDataChild);
							listDataHeader.add(groupItem);
						}
					} else {
						finalResult = "";
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				finalResult = "Did not work!";
		} catch (Exception e) {
			Log.i("InputStream", e.getLocalizedMessage());
		}
		return finalResult;
	}

	public static String convertInputStreamToString(InputStream inputStream)
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
				Intent i = new Intent(MyAppointments.this,AddressSelection.class);
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



	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(MyAppointments.this, AddressSelection.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		//finish();
	}
}
