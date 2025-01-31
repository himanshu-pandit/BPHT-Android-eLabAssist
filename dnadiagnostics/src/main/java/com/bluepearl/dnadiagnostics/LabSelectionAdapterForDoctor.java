package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jsonparsing.webservice.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LabSelectionAdapterForDoctor extends BaseAdapter{
	
	Activity context;
	String names[];
	static String role = "";
	int totalLabcount =0;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	
	private ArrayList<LabSelectionDoctorDetails> arraylist =  new ArrayList<LabSelectionDoctorDetails>();
	private List<LabSelectionDoctorDetails> userlist = null;


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


	private static final String collection_center_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";
	private static String collection_center_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";

	private static final String Affiliation_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";
	private static String Affiliation_myurlll = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";

	public LabSelectionAdapterForDoctor(Activity context, String[] name) {
		super();
		this.context = context;
		this.names = name;
	}

	public LabSelectionAdapterForDoctor(Activity context, List<LabSelectionDoctorDetails> userlist) {
		this.context = context;
		this.userlist = userlist;
		this.arraylist = new ArrayList<LabSelectionDoctorDetails>();
		this.arraylist.addAll(userlist);

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return userlist.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userlist.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class ViewHolder {
		TextView txtName, txtAddress, txtlabid, txtReport, txtSelect,txtDashboard;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater =  context.getLayoutInflater();

		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

		totalLabcount =userlist.size();

		allaffiliationNameSet = new HashSet<String>();
		allCollectionCenterNameSet = new HashSet<String>();


		Log.d("totalLabcount1", "totalLabcount1: "+totalLabcount);

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.activity_lab_doctor_details, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtname);
			holder.txtAddress = (TextView) convertView.findViewById(R.id.txtaddress);
			holder.txtlabid = (TextView) convertView.findViewById(R.id.txtid);
			holder.txtReport = (TextView) convertView.findViewById(R.id.textView1);
			holder.txtSelect = (TextView) convertView.findViewById(R.id.textView2);
			holder.txtDashboard = (TextView) convertView.findViewById(R.id.textView3);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtName.setText(userlist.get(position).getLabName());
		holder.txtAddress.setText(userlist.get(position).getLabAddress());
		holder.txtlabid.setText(userlist.get(position).getLabId());

		holder.txtName.setTypeface(custom_bold_font);
		holder.txtAddress.setTypeface(custom_font);
		//holder.txtReport.setTypeface(custom_bold_font);
		//holder.txtSelect.setTypeface(custom_bold_font);

		if((userlist.get(position).getLabRole()).equals("Admin"))
		{
			holder.txtDashboard.setVisibility(View.VISIBLE);
		}
		else {
			holder.txtDashboard.setVisibility(View.GONE);
		}

		String disid = userlist.get(position).getLabId();
		String lab_name = userlist.get(position).getLabName();
		String lab_role = userlist.get(position).getLabRole();

		if(	totalLabcount ==1)
	{
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.putString("SelectedLabId", disid);
		editor.putString("LabIdFromLogin",disid);
		editor.putString("SelectedLabName", lab_name);
		editor.putString("Role",lab_role);
		editor.putString("usertype", lab_role);

		editor.remove("allTestNames");
		editor.remove("popularTestData");
		editor.remove("setprofilename");
		editor.remove("setcategoryname");



		editor.commit();

		 if(lab_role.equals("Admin")){

			 Intent i1 = new Intent(context,Dashboard.class);
			 //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 context.startActivity(i1);

		 }
		else if(lab_role.equals("Collection Boy"))
		{
			Intent i1 = new Intent(context,PhlebotomyTabOn.class);
			//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i1);
		}
		 else if(lab_role.equals("Patient"))
		 {
			 Intent i1 = new Intent(context,AddressSelection.class);
			 //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 context.startActivity(i1);
		 }
		 else {
			 Intent i1 = new Intent(context, RegisterPatientNew.class);
		//	 i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 context.startActivity(i1);
		 }


	}
	else if(	totalLabcount ==0)
		{
			Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
		}

		if((userlist.get(position).getLabRole()).equals("Collection Boy"))
		{
			holder.txtReport.setVisibility(View.GONE);
		}
		else {
			holder.txtReport.setVisibility(View.VISIBLE);
		}


		holder.txtDashboard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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


				// TODO Auto-generated method stub
				String disid = userlist.get(position).getLabId();
				String lab_name = userlist.get(position).getLabName();
				String lab_role = userlist.get(position).getLabRole();

				collection_center_myurl =collection_center_url;
				Affiliation_myurlll =Affiliation_url;
				collection_center_myurl = collection_center_myurl + disid;
				Affiliation_myurlll = Affiliation_myurlll + disid;

				new GetCollectionCenterList().execute();
				new GetAffiliationList().execute();

				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("SelectedLabId", disid);
				editor.putString("LabIdFromLogin",disid);
				editor.putString("SelectedLabName", lab_name);
				editor.putString("Role",lab_role);
				editor.putString("usertype", lab_role);

				editor.remove("allTestNames");
				editor.remove("popularTestData");
				editor.remove("setprofilename");
				editor.remove("setcategoryname");

				editor.commit();




				if(lab_role.equals("Admin"))
				{
					Intent i1 = new Intent(context,Dashboard.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}
				else if(lab_role.equals("Collection Boy"))
				{
					Intent i1 = new Intent(context,PhlebotomyTabOn.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}
				else if(lab_role.equals("Patient"))
				{
					Intent i1 = new Intent(context,AddressSelection.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}

				else {
					Intent i1 = new Intent(context, RegisterPatientNew.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}
			}
		});

		holder.txtSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

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


				String disid = userlist.get(position).getLabId();
				String lab_name = userlist.get(position).getLabName();
				String lab_role = userlist.get(position).getLabRole();

				collection_center_myurl =collection_center_url;
				Affiliation_myurlll =Affiliation_url;
				collection_center_myurl = collection_center_myurl + disid;
				Affiliation_myurlll = Affiliation_myurlll + disid;

				new GetCollectionCenterList().execute();
				new GetAffiliationList().execute();

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("SelectedLabId", disid);
				editor.putString("LabIdFromLogin",disid);
				editor.putString("SelectedLabName", lab_name);
				editor.putString("Role",lab_role);
				editor.putString("usertype", lab_role);

				editor.remove("allTestNames");
				editor.remove("popularTestData");
				editor.remove("setprofilename");
				editor.remove("setcategoryname");

				editor.commit();

				//Toast.makeText(context, lab_role, Toast.LENGTH_LONG).show();

				if(lab_role.equals("Collection Boy"))
				{
					Intent i1 = new Intent(context,PhlebotomyTabOn.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}

				else if(lab_role.equals("Admin"))
				{
					Intent i1 = new Intent(context,RegisterPatientNewAdmin.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}

				else if(lab_role.equals("Patient"))
				{
					Intent i1 = new Intent(context,AddressSelection.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}
				else {
					Intent i1 = new Intent(context, RegisterPatientNew.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}
			}
		});




		holder.txtReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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


				String disid = userlist.get(position).getLabId();
				String lab_name = userlist.get(position).getLabName();
				String lab_role = userlist.get(position).getLabRole();

				collection_center_myurl =collection_center_url;
				Affiliation_myurlll =Affiliation_url;
				collection_center_myurl = collection_center_myurl + disid;
				Affiliation_myurlll = Affiliation_myurlll + disid;

				new GetCollectionCenterList().execute();
				new GetAffiliationList().execute();

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("SelectedLabId", disid);
				editor.putString("LabIdFromLogin",disid);
				editor.putString("SelectedLabName", lab_name);
				editor.putString("usertype", lab_role);
				editor.putString("Role",lab_role);
				editor.commit();

				//Toast.makeText(context,"aaaa"+ lab_role, Toast.LENGTH_LONG).show();

				if(lab_role.equals("Doctor") || lab_role.equals("AffiliationUser")|| lab_role.equals("Collection Center Lab User") || lab_role.equals("Collection Center User") || lab_role.equals("Collection Center Admin"))
				{
					//Toast.makeText(context,"aaa"+ lab_role, Toast.LENGTH_LONG).show();
					System.out.println("aaab"+ lab_role);
					Intent i1 = new Intent(context,ViewReportDoctor.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);

				}
				//|| lab_role.equals("Collection Center User")
				else if(lab_role.equals("Admin"))
				{
					//Toast.makeText(context,"aaaa"+ lab_role, Toast.LENGTH_LONG).show();
					System.out.println("aaaab"+ lab_role);
					Intent i1 = new Intent(context,ViewReportAdmin.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}
				else if(lab_role.equals("Patient"))
				{
					Intent i1 = new Intent(context,PatientList.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}

				else
				{
					//Toast.makeText(context,"aaaaa"+ lab_role, Toast.LENGTH_LONG).show();
					System.out.println("aaaab"+ lab_role);
					Intent i1 = new Intent(context,ViewReportLabUser.class);
					//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i1);
				}
			}
		});

		return convertView;
	}

	public void filter(String charText)
	{
		charText = charText.toLowerCase(Locale.getDefault());
		if(charText.equalsIgnoreCase("All"))
			charText = "";
		userlist.clear();
		if (charText.length() == 0) {
			userlist.addAll(arraylist);
		}
		else
		{
			for (LabSelectionDoctorDetails udet : arraylist)
			{
				if (udet.getLabName().toLowerCase().contains(charText)) 
				{
					userlist.add(udet);
				}
			}
		}
		notifyDataSetChanged();
	}








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

			// Dismiss the progress dialog
			//if (pDialog.isShowing())
			//	pDialog.dismiss();

			Affiliation_myurlll =Affiliation_url;

			//	Intent iintent = new Intent(GetLabListReport.this,ViewReportLabUser.class);
			//  Intent iintent = new Intent(MassagList.this, AllTestDataPhlebo.class);
			//startActivity(iintent);
			//	finish();

		}
	}










}