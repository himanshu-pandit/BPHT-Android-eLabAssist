package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.jsonparsing.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class LabSelection extends Activity implements OnItemClickListener {

	Dialog dialog;
	String lname, registerLabAddress, recentLabAddress, nearbyLabAddress;
	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;
	static String pid = null;
	double latitude, longitude;
	//	ListView listView, listView1;
	AlertDialog alertbox;
	LinearLayout ll_recent, ll_register;

	TextView home, empty, lblLab, tvrecent, lblLogout;
	Button booktest;
	LabSelectionAdapter adapter, recent_adapter, adapterLab;
	LabSelectionAdapterNew recent_adapternew;
	String LabName;
	private ProgressDialog pDialog;

	JSONArray Labname = null;
	JSONArray recentLab;

	Boolean LLVIsibilty = true;

	AutoCompleteTextView textViewlab;

	static ArrayList<LabSelectionDetails> labList = new ArrayList<LabSelectionDetails>();

	ArrayList<Integer> labid_list = new ArrayList<Integer>();
	ArrayList<LabSelectionDetails> labnamesList = new ArrayList<LabSelectionDetails>();
	ArrayList<LabSelectionDetails> recentlabnamesList = new ArrayList<LabSelectionDetails>();
	ArrayList<LabSelectionDetails> vicinitynamesList = new ArrayList<LabSelectionDetails>();
	ArrayList<LabSelectionDetails> originalvicinitynamesList = new ArrayList<LabSelectionDetails>();


	private static final String lablist_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/LabsInVicinityAndRecentLabs?patientid=";
	private static String lablist_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/LabsInVicinityAndRecentLabs?patientid=";

	boolean loadingMore = false;
	//private MyScrollView scrollView;
	private LinearLayout container;
	private ProgressBar progressBar;
	int maxItem = 20;
	private View lastItemView;
	boolean alreadyExecutingRequest = false;


	private ArrayList<PatientLabSelectionDetailes> contacts;
	private ArrayList<PatientLabSelectionDetailes> contactsTwo;
	private ArrayList<PatientLabSelectionDetailes> contactsThree;

	private ArrayList<PatientLabSelectionDetailes> contacts500;

	private int duplicateIndex;

	//private List<Contact> contacts;
	public PatientLabSelectionAdapter contactAdapter;
	public PatientLabSelectionAdapter contactAdapterRecent;
	//	private ContactAdapter contactAdapter;

	RecyclerView recyclerView,recyclerViewRecent;
	NestedScrollView nestedScrollView;


	// The current offset index of data you have loaded
	private int currentPage = 0;
	// The total number of items in the dataset after the last load
	private int previousTotalItemCount = 0;
	// True if we are still waiting for the last set of data to load.
	private boolean loading = true;
	// Sets the starting page index
	private int startingPageIndex = 0;
	// The minimum amount of pixels to have below your current scroll position
	// before loading more.
	private int visibleThresholdDistance = 300;

	//RecyclerView.LayoutManager mLayoutManager;

	boolean isLoaded = false;
	LinearLayout ll1vdcv2;

	ProgressBar myProgressRegis;
	SearchView searchView;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lab_selection);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");


		Set<String> selectedpopularsetinmain = new HashSet<String>();
		Set<String> selectedtestnamesetinmain = new HashSet<String>();
		Set<String> selectedtestcategorysetinmain = new HashSet<String>();
		Set<String> selectedtestprofilesetinmain = new HashSet<String>();

		selectedpopularsetinmain = sharedpreferences.getStringSet("popularTestData", null);
		selectedtestnamesetinmain = sharedpreferences.getStringSet("allTestNames", null);
		selectedtestcategorysetinmain = sharedpreferences.getStringSet("setprofilename", null);
		selectedtestprofilesetinmain = sharedpreferences.getStringSet("setcategoryname", null);

		// Getting a reference to listview of main.xml layout file
		//	listView = (ListView) findViewById(R.id.listviewRecent);
		//	listView1 = (ListView) findViewById(R.id.listviewNearby);
		ll_recent = (LinearLayout) findViewById(R.id.ll1);

		//empty = (TextView) findViewById(R.id.empty);
		lblLab = (TextView) findViewById(R.id.txtviewlab);
		lblLogout = (TextView) findViewById(R.id.txtviewlogout);
		tvrecent = (TextView) findViewById(R.id.txtview);

		textViewlab = (AutoCompleteTextView) findViewById(R.id.etPatientName);
		//scrollView = (MyScrollView) findViewById(R.id.scrollViewId);
		//scrollView.setOnScrollListener(scrollListener);
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerViewRecent = (RecyclerView) findViewById(R.id.recyclerrecent_view);

		nestedScrollView = (NestedScrollView) findViewById(R.id.NestedScrollView_id);
		myProgressRegis = (ProgressBar) findViewById(R.id.myProgressRegis_id);
		searchView =(SearchView)findViewById(R.id.searchView_id);
		searchView.setQueryHint("Search Laboratory.");

		searchView.setIconified(false);

		recyclerView.setNestedScrollingEnabled(false);
		recyclerViewRecent.setNestedScrollingEnabled(false);


		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerViewRecent.setLayoutManager(new LinearLayoutManager(this));
		// listening to search query text change
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// filter recycler view when query submitted
				//contactAdapter.getFilter().filter(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String query) {
				// filter recycler view when text is changed

				if(query.length()>0){
					ll_recent.setVisibility(View.GONE);
					recyclerViewRecent.setVisibility(View.GONE);
					contactAdapter.getFilter().filter(query);
				}else{
					ll_recent.setVisibility(View.VISIBLE);
					recyclerViewRecent.setVisibility(View.VISIBLE);
				}



				return false;
			}
		});





		myProgressRegis.setVisibility(View.GONE);

		int colorCodeDark = Color.parseColor("#000066");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			myProgressRegis.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));

		}





		ll_recent.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (LLVIsibilty) {

							recyclerViewRecent.setVisibility(View.GONE);
							LLVIsibilty = false;

						} else {
							recyclerViewRecent.setVisibility(View.VISIBLE);
							LLVIsibilty = true;
						}
					}
				}
		);

		lblLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.remove("testid");
				editor.remove("testprofileid");
				editor.remove("test_name");
				editor.remove("filePath");
				editor.remove("address");
				editor.remove("doctor");
				editor.remove("DateTime");
				editor.remove("datetime");
				editor.remove("loginchkbox");
				editor.remove("checkboxchecked");
				editor.remove("allTestNames");
				editor.remove("popularTestData");
				editor.remove("setprofilename");
				editor.remove("setcategoryname");
				editor.remove("CheckedValue");
				//editor.remove("patient_name");

				editor.remove("SelectedLabId");
				editor.remove("LabIdFromLogin");
				editor.remove("SelectedLabName");
				editor.remove("Role");
				editor.remove("usertype");

				editor.commit();

				Intent i1 = new Intent(LabSelection.this, SigninActivity.class);
				i1.putExtra("fromLogout", "yes");
				i1.putExtra("fromchange", "no");
				//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
				//finish();
			}
		});







		if (isInternetOn(getApplicationContext())) {
			lablist_myurl = lablist_myurl + pid + "&" + "latitude=" + latitude + "&" + "longitude=" + longitude + "&" + "Task=" + "4";
			new GetLabList().execute();
		} else {
			alertBox();
		}
		contacts = new ArrayList<>();
		contactsTwo = new ArrayList<>();
		contactsThree = new ArrayList<>();


/*

//pagination for scrolloing
		nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {


			@Override
			public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

				isLoaded = true;

				if (view.getChildAt(view.getChildCount() - 1) != null) {
					if ((scrollY >= (view.getChildAt(view.getChildCount() - 1).getMeasuredHeight() - view.getMeasuredHeight())) && scrollY > oldScrollY) {


						if (isLoaded) {
							if (contactsTwo.size() <= labnamesList.size()) {
								//nestedScrollView.setEnabled(false);


								//new LoadLabDetailsNew().execute();
								nestedScrollView.setEnabled(true);


							}
						} else {
						//	Toast.makeText(LabSelection.this, "Loading data completed", Toast.LENGTH_SHORT).show();
						}

					}


				}
			}
		});
*/



	}

	/*** Async task class to get json by making HTTP call **/
	private class GetLabList extends AsyncTask<String, Void, ArrayList<LabSelectionDetails>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(LabSelection.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<LabSelectionDetails> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			Log.e("lablist_myurl",lablist_myurl);
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(lablist_myurl, ServiceHandler.GET);

			if (jsonStr != null && !jsonStr.isEmpty()) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					JSONObject labObj = jsonObj.getJSONObject("d");

					JSONArray registerLab = labObj.getJSONArray("RegisteredLabs");
					//JSONArray labinvicinity = labObj.getJSONArray("LabsInVicinity");
					recentLab = labObj.getJSONArray("RecentLabs");
					Log.e("registerLab",registerLab.toString());

					if (recentLab == null) {
						ll_recent.setVisibility(View.GONE);
						recyclerViewRecent.setVisibility(View.GONE);
					} else {
						ll_recent.setVisibility(View.VISIBLE);
						recyclerViewRecent.setVisibility(View.VISIBLE);
					}

					// Getting values from register lab array
					for (int i = 0; i < registerLab.length(); i++) {
						//	for (int i = 0; i < 10; i++) {

						JSONObject c = registerLab.getJSONObject(i);

						String distance = c.getString("DistanceString");
						String registerLabAddress = c.getString("LabAddress");
						String LabName = c.getString("Labname");
						String Labid = c.getString("LabID");
						String isHomeCollection = c.getString("IsHomeCollectionAvailable");
						String isRegister = c.getString("IsRegisteredLab");
						String isPreffered = c.getString("IsPrefferedLab");
						//Lab Mobile Number and Email
						String LAbMobile = c.getString("MobileNumber");
						String LabEmail = c.getString("EmailID");
						Log.d("commingm+",c.getString("MobileNumber"));


						PatientLabSelectionDetailes contact = new PatientLabSelectionDetailes(LabName, registerLabAddress, distance, Labid, isRegister, isPreffered, isHomeCollection,LAbMobile,LabEmail);
						contactsTwo.add(contact);



					}

					// Getting values from recent lab array
					for (int i = 0; i < recentLab.length(); i++) {

						JSONObject c = recentLab.getJSONObject(i);

						String distance = c.getString("DistanceString");
						String recentLabAddress = c.getString("LabAddress");
						String LabName = c.getString("Labname");
						String Labid = c.getString("LabID");
						String isHomeCollection = c.getString("IsHomeCollectionAvailable");
						String isRegister = c.getString("IsRegisteredLab");
						String isPreffered = c.getString("IsPrefferedLab");


						//Lab Mobile Number and Email
						String LAbMobile = c.getString("MobileNumber");
						String LabEmail = c.getString("EmailID");
						//LabSelectionDetails det = new LabSelectionDetails(LabName, recentLabAddress, distance, Labid, isRegister, isPreffered, isHomeCollection);
						//	recentlabnamesList.add(det);


						PatientLabSelectionDetailes contactthree = new PatientLabSelectionDetailes(LabName, recentLabAddress, distance, Labid, isRegister, isPreffered, isHomeCollection,LAbMobile,LabEmail);
						contactsThree.add(contactthree);


					}

					/*// Getting values from labs in vicinity array
					for (int i = 0; i < labinvicinity.length(); i++) {

						JSONObject c = labinvicinity.getJSONObject(i);

						String distance = c.getString("DistanceString");
						nearbyLabAddress = c.getString("LabAddress");
						String LabName = c.getString("Labname");
						String Labid = c.getString("LabID");
						String isHomeCollection = c.getString("IsHomeCollectionAvailable");
						String isRegister = c.getString("IsRegisteredLab");
						String isPreffered = c.getString("IsPrefferedLab");

						LabSelectionDetails det = new LabSelectionDetails(LabName,nearbyLabAddress,distance,Labid,isRegister,isPreffered,isHomeCollection);
						// adding contact to contact list
						vicinitynamesList.add(det);
					//	originalvicinitynamesList.add(det);
					}
*/


				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return labnamesList;
		}

		@Override
		protected void onPostExecute(ArrayList<LabSelectionDetails> result) {
			super.onPostExecute(result);

			contactAdapterRecent = new PatientLabSelectionAdapter(recyclerViewRecent, contactsThree, LabSelection.this);
			recyclerViewRecent.setAdapter(contactAdapterRecent);

			contactAdapter = new PatientLabSelectionAdapter(recyclerView, contactsTwo, LabSelection.this);
			recyclerView.setAdapter(contactAdapter);

			recyclerViewRecent.invalidate();
			recyclerView.invalidate();

			loadingMore = true;
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			lablist_myurl = lablist_url;
		}
	}



	public static boolean isInternetOn(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

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

	private void alertBox() {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
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

			if (listItem != null) {
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

	private void showdialog() {
		adapter.notifyDataSetChanged();

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.nearbylabs_view, null);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		Builder builder = new Builder(this);
		builder.setView(layout);

		final EditText search = (EditText) layout.findViewById(R.id.inputSearch);
		//final ListView modeList = (ListView) layout.findViewById(R.id.listviewNearby);
		search.setTypeface(custom_font);

		//modeList.setOnItemClickListener(this);

		/*ProgressDialog pDialogg = new ProgressDialog(LabSelection.this);
		pDialogg.setMessage("Please wait...");
		pDialogg.setCancelable(false);
		pDialogg.show();*/


		adapter = new LabSelectionAdapter(this, vicinitynamesList);
		//modeList.setAdapter(adapter);


		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

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
	}




	private class LoadLabDetailsNew extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		/*	pDialog = new ProgressDialog(LabSelection.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();*/

			//ll1vdcv2.addFooterView(footerView);
			//	ll1vdcv2.addView(footerView);
			//Toast.makeText(LabSelection.this, "Please wait while loading", Toast.LENGTH_SHORT).show();
			myProgressRegis.setVisibility(View.VISIBLE);
			Log.d("myProgressRegis", "myProgressRegis. VISIBLE");

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			int index = contactsTwo.size();
			int end = index + 10;
			try {
				for (int i = index; i < end; i++) {

					PatientLabSelectionDetailes contacttwo = new PatientLabSelectionDetailes(
							labnamesList.get(i).getLabName(),
							labnamesList.get(i).getLabAddress(),
							labnamesList.get(i).getLabDistance(),
							labnamesList.get(i).getLabId(),
							labnamesList.get(i).getregister(),
							labnamesList.get(i).getpreferred(),
							labnamesList.get(i).gethomeflg());

					contactsTwo.add(contacttwo);

				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(LabSelection.this, "No More Labs", Toast.LENGTH_SHORT).show();
			}

			isLoaded = false;

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog

			myProgressRegis.setVisibility(View.GONE);
			Log.d("myProgressRegis", "myProgressRegis. GON");

			contactAdapter.notifyDataSetChanged();
			contactAdapter = new PatientLabSelectionAdapter(recyclerView, contactsTwo, LabSelection.this);
			contactAdapter.notifyDataSetChanged();
			recyclerView.setAdapter(contactAdapter);
			//ll1vdcv2.removeView(footerView);
			/*if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}*/


		/*	nestedScrollView.post( new Runnable() {
				@Override
				public void run() {
					//   myScrollerViewOne.smoothScrollTo( 0, myScrollerViewOne.getChildAt( 0 ).getBottom() );
					// myScrollerViewOne.smoothScrollTo( 0,10 );
					nestedScrollView.smoothScrollBy(0,200);
				}
			});*/
			//	nestedScrollView.setEnabled(true);


		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			nestedScrollView.setEnabled(true);

		}

	}




}
