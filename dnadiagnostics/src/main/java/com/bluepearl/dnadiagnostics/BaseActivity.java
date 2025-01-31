package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseActivity extends AppCompatActivity {
	Intent i1;

	protected FrameLayout frameLayout;
	protected ListView mDrawerList;
	protected static int position;
	private static boolean isLaunch = true;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;
	static String tid = null;
	static String testpid = null;
	static String pid = null;
	static String phone = null;
	static String add_val = null;
	static String date_val = null;
	static String dname_val = null;
	static String residencial_address = "";
	static String official_address = "";
	static String status = "";
	static String text = "";
	static String entered_test_name = null;
	static String addval = null;
	static String dnameval = null;
	private static String filePath = null;
	
	String menu_name = "";
	FontAdapter adapter;
	List<FontDetails> FontDetailsList = new ArrayList<FontDetails>();
	static String multiusertype = "null";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_drawer_base_layout);

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		tid = sharedpreferences.getString("testid", "");
		testpid = sharedpreferences.getString("testprofileid", "");
		pid = sharedpreferences.getString("patientId", "");
		phone = sharedpreferences.getString("phno", "");
		date_val = sharedpreferences.getString("DateTime", "");

		filePath = sharedpreferences.getString("filePath", "");

		residencial_address = sharedpreferences.getString("res_add", "");

		official_address = sharedpreferences.getString("office_add", "");
		entered_test_name = sharedpreferences.getString("test_name", "");

		addval = sharedpreferences.getString("address", "");
		dnameval = sharedpreferences.getString("doctor", "");

		multiusertype = sharedpreferences.getString("multiusertype","null");

		Set<String> selectedpopularsetinmain = new HashSet<String>();
		Set<String> selectedtestnamesetinmain = new HashSet<String>();
		Set<String> selectedtestcategorysetinmain = new HashSet<String>();
		Set<String> selectedtestprofilesetinmain = new HashSet<String>();

		selectedpopularsetinmain = sharedpreferences.getStringSet("popularTestData", null);
		selectedtestnamesetinmain = sharedpreferences.getStringSet("allTestNames", null);
		selectedtestcategorysetinmain = sharedpreferences.getStringSet("setprofilename", null);
		selectedtestprofilesetinmain = sharedpreferences.getStringSet("setcategoryname", null);

		frameLayout = (FrameLayout) findViewById(R.id.content_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		String[] array = getResources().getStringArray(R.array.order_options_menu);

		for (String s : array) {
			FontDetails det = new FontDetails(s);
			// adding contact to contact list
			FontDetailsList.add(det);
		}

		adapter = new FontAdapter(FontDetailsList, this);
		
		mDrawerList.setAdapter(adapter);
		
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				openActivity(position);
			}
		});
		initViews();
	}

	private void initViews() {
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
		mTitle.setTypeface(custom_font);
		
		toolbar.setTitleTextColor(getResources()
				.getColor(android.R.color.white));
		setSupportActionBar(toolbar);

		actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				toolbar, R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	protected void openActivity(int position) {
		mDrawerLayout.closeDrawer(mDrawerList);
		BaseActivity.position = position;

		switch (position) {
		case 0:
			
			i1 = new Intent(BaseActivity.this, AddressSelection.class);
		/*	i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
			startActivity(i1);
			//finish();
			break;
		case 1:
			if (isInternetOn(getApplicationContext())) 
			{


				if(multiusertype.equals("Patient")) {

					i1 = new Intent(BaseActivity.this, LabSelection.class);
					startActivity(i1);

				}else
				{

					i1 = new Intent(BaseActivity.this, LabSelection.class);
					startActivity(i1);
				}







			}
			else
			{
				alertBox();
			}
			break;
		case 2:
			
			i1 = new Intent(BaseActivity.this, MyAppointments.class);
			/*i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
			startActivity(i1);
			//finish();
			break;
		case 3:
			
			i1 = new Intent(BaseActivity.this, PatientList.class);
			startActivity(i1);
			//finish();
			break;
			case 4:

				i1 = new Intent(BaseActivity.this, Trackers.class);
				startActivity(i1);
				//finish();
				break;
		case 5:

			SharedPreferences.Editor editor1 = sharedpreferences.edit();

			editor1.putString("kuthunaala","gharun");

			editor1.commit();

			i1 = new Intent(BaseActivity.this, PatientProfile.class);
			/*i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
			startActivity(i1);
			//finish();
			break;
		case 6:
			
			i1 = new Intent(BaseActivity.this, ChangePassword.class);
			/*i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
			startActivity(i1);
			//finish();
			break;
		case 7:
			
			Intent testIntent = new Intent(Intent.ACTION_VIEW);
			Uri data = Uri.parse("mailto:?subject=" + "eLAB Assist Feedback" + "&body="
					+ "Dear...," + "&to=" + "android@bluepearlinfotech.com");
			testIntent.setData(data);
			startActivity(testIntent);
			break;
		case 8:
			
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
			editor.remove("SelectedLabId");
			editor.remove("SelectedLabName");

			//editor.remove("patient_name");



			editor.commit();

			i1 = new Intent(BaseActivity.this, SigninActivity.class);
			i1.putExtra("fromLogout","yes");
			i1.putExtra("fromchange", "no");
			//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i1);
			//finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
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
		
		AlertDialog alert1 = alert.create();
		alert1.show();

		TextView textView = (TextView) alert1.findViewById(android.R.id.message);
		Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
	}
}
