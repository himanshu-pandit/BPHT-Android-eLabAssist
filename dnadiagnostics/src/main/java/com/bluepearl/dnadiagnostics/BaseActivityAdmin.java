package com.bluepearl.dnadiagnostics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.List;

public class BaseActivityAdmin extends AppCompatActivity {
	Intent i1;
	protected FrameLayout frameLayout;
	protected ListView mDrawerList;
	protected static int position;
	private static boolean isLaunch = true;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;
	static String user_type = "";

	FontAdapter adapter;
	List<FontDetails> FontDetailsList = new ArrayList<FontDetails>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_drawer_base_layout_dl);

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		user_type = sharedpreferences.getString("usertype", "");

		frameLayout = (FrameLayout) findViewById(R.id.content_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		String[] array = getResources().getStringArray(R.array.order_options_menu_admin);

		for (String s : array) {


			FontDetails det = new FontDetails(s);
			// adding contact to contact list
			FontDetailsList.add(det);
		}

		// set up the drawer's list view with items and click listener
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

				i1 = new Intent(BaseActivityAdmin.this, Dashboard.class);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
				//finish();
				break;

			case 1:
				i1 = new Intent(BaseActivityAdmin.this, LabSelectionForDoctor.class);
				/*i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
				startActivity(i1);
				//finish();
				break;



			case 2:
				i1 = new Intent(BaseActivityAdmin.this, RegisterPatientNewAdmin.class);
				//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
				//finish();
				break;
			case 3:

					i1 = new Intent(BaseActivityAdmin.this, ViewReportAdmin.class);
					startActivity(i1);
					//finish();

				break;


			case 4:

				i1 = new Intent(BaseActivityAdmin.this, MassagListAdmin.class);
				//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
				//finish();
				break;
			case 5:
				SharedPreferences.Editor editor1 = sharedpreferences.edit();

				editor1.putString("kuthunaala","gharun");

				editor1.commit();

				i1 = new Intent(BaseActivityAdmin.this, AdminProfile.class);
				/*i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				i1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
				startActivity(i1);
				//finish();
				break;
			case 6:
				i1 = new Intent(BaseActivityAdmin.this, ChangePasswordForAdmin.class);
				//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

				editor.remove("loginchkbox");
				editor.remove("checkboxchecked");
				editor.remove("loginCheckData");
				editor.remove("SelectedLabId");
				editor.remove("SelectedLabName");

				editor.remove("SelectedLabId");
				editor.remove("LabIdFromLogin");
				editor.remove("SelectedLabName");
				editor.remove("Role");
				editor.remove("usertype");

				editor.commit();

				i1 = new Intent(BaseActivityAdmin.this, SigninActivity.class);
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
}
