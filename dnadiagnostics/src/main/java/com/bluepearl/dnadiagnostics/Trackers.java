package com.bluepearl.dnadiagnostics;

import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.jsonparsing.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Trackers extends BaseActivity {

LinearLayout diabetesCare;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		getLayoutInflater().inflate(R.layout.activity_trackers,
				frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		diabetesCare = (LinearLayout)findViewById(R.id.diabetesCare_ll);
		diabetesCare.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent i1 = new Intent(Trackers.this, DiabetesCare.class);
						startActivity(i1);

					}
				}
		);

	}}