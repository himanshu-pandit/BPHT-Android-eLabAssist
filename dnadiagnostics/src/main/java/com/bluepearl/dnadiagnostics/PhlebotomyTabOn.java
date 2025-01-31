package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class PhlebotomyTabOn extends BaseActivityPhlebotomy {
	TabHost tabHost;
	LocalActivityManager mLocalActivityManager;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String activity_name = null;
	static String selected_labidfrompref = null;
	private ProgressDialog pDialog;
	String selectedPopularIdFromIntent = "";

   Builder alertbox;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		getLayoutInflater().inflate(R.layout.activity_phlebotomy_tab,frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		activity_name = sharedpreferences.getString("ActivityName", "");

		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		 // selected_labidfrompref ="802de9b1-8ab9-4701-8bde-50bc99de1bae";


		// create the TabHost that will contain the Tabs
		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				//Toast.makeText(PhlebotomyTabOn.this, "Tabchanged", Toast.LENGTH_SHORT).show();
				setTabColor(tabHost);
			}
		});
		     
		
		mLocalActivityManager = new LocalActivityManager(this, false);
		mLocalActivityManager.dispatchCreate(savedInstanceState);
		tabHost.setup(mLocalActivityManager);


			paintTabs();

	}


	public void paintTabs()
	{
		/*setupTabHost();

		setupTab(new TextView(this), "Popular Test");
		setupTab(new TextView(this), "Upload Photo");
		setupTab(new TextView(this), "Select Test");*/
	//	TabSpec tab1 = tabHost.newTabSpec("Popular Test");
		TabSpec tab2 = tabHost.newTabSpec("My List");
		TabSpec tab3 = tabHost.newTabSpec("All List");

		// Set the Tab name and Activity
		// that will be opened when particular Tab will be selected
	//	tab1.setIndicator("Popular Test");
	//	tab1.setContent(new Intent(this, TestEnterTab.class)
	//	.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


		tab2.setIndicator("My List");
				//setBackgroundColor(Color.parseColor("#ffffff"));
		tab2.setContent(new Intent(this, PhlebotomyAreaListTabOn.class)
		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

		tab3.setIndicator("All List");

		tab3.setContent(new Intent(this, PhlebotomyMyListTabOn.class)
		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

		// Add the tabs to the TabHost to display. 
		//tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);

		//tabHost.getTabWidget().getChildAt(0).getLayoutParams().width = 25;
		tabHost.getTabWidget().getChildAt(0).getLayoutParams().width = 25;
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().width = 25;
		//tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.backgroundlowmidltopone);
		//tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.backgroundlowmidltoptwo);
		//tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.backgroundlowmidltopthree);

		
//		if(!(activity_name.equalsIgnoreCase("")) && activity_name.equals("Enter") && !(activity_name.equalsIgnoreCase("null")))
//		{
//			tabHost.setCurrentTab(0);
//			//tabHost.setBackground(@Dra);
//			//tabHost.getTabWidget().setBackgroundResource(R.drawable.tabhost_bg);
//		}
//		else
			if(!(activity_name.equalsIgnoreCase("")) && activity_name.equals("My List") && !(activity_name.equalsIgnoreCase("null")))
		{
			tabHost.setCurrentTab(0);
		}
		else if(!(activity_name.equalsIgnoreCase("")) && activity_name.equals("Area List") && !(activity_name.equalsIgnoreCase("null")))
		{
			tabHost.setCurrentTab(1);
		}
		else
		{
			tabHost.setCurrentTab(0);
		}
	}

    @Override
    public void onBackPressed() {
        alertbox = new Builder(this)
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {


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
                });

        AlertDialog alert1 = alertbox.create();
        alert1.show();

        TextView textView = (TextView) alert1.findViewById(android.R.id.message);
        Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
        Button noButton = alert1.getButton(DialogInterface.BUTTON_NEGATIVE);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        textView.setTypeface(custom_font);
        yesButton.setTypeface(custom_bold_font);
        noButton.setTypeface(custom_bold_font);
    }

	public static void setTabColor(TabHost tabhost) {

		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
			tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#bcc8dc")); // unselected
			TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#000000"));

		}
		tabhost.getTabWidget().setCurrentTab(0);
		tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())
				.setBackgroundColor(Color.parseColor("#ffffff")); // selected
        TextView tv = (TextView) tabhost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#000000"));

	}
}
