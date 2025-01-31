package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestListAdapter extends BaseAdapter{
	Activity context;
	private ArrayList<TsetListDetails> arraylist =  new ArrayList<TsetListDetails>();
	private List<TsetListDetails> userlist = null;
	AlertDialog alertbox;
	static String status = "";
	static String pid = null;
	static String labid = null;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String labnamefromadapter;
	static String selected_labidfrompref = null;
	Integer[] addlabid;
	private ViewHolder holder;
	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UpdateProfile";
	static String labIdForProfileUpdate = "";

	public TestListAdapter(Activity context, List<TsetListDetails> userlist) {
		this.context = context;
		this.userlist = userlist;
		this.arraylist = new ArrayList<TsetListDetails>();
		this.arraylist.addAll(userlist);

		addlabid = new Integer[this.userlist.size()];
		for (int i = 0; i < userlist.size(); i++) {
			addlabid[i] = 0;
		}

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
		TextView txtName,txtAddress,txtDistance,txtHomeCollection,txtlabid,txtlbl,txtbook,txtreports;
		ImageView imgLocation;
		Button img,btnview;
		TsetListDetails distributor;
		//,txtmore
	}

	public View getView(final int position, final View convertView, ViewGroup parent) {
		View view = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.activity_test_listview, null);

		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");

		sharedpreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");

		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
	//	selected_labidfrompref ="b3bcf625-f93a-4c97-a39e-0a6563dc72a5";


		holder = new ViewHolder();
		holder.distributor = userlist.get(position);

		holder.txtName = (TextView) view.findViewById(R.id.txtname);
		holder.txtAddress = (TextView) view.findViewById(R.id.txtaddress);
		holder.txtDistance = (TextView) view.findViewById(R.id.txtdistance);
		holder.imgLocation = (ImageView) view.findViewById(R.id.imgloc);
		/*holder.img = (Button) view.findViewById(R.id.btnBook);
		holder.btnview = (Button) view.findViewById(R.id.btnReport);*/
		holder.txtHomeCollection = (TextView) view.findViewById(R.id.tvhome);
		holder.txtlabid = (TextView) view.findViewById(R.id.txtid);
		holder.txtreports = (TextView) view.findViewById(R.id.textView1);
		holder.txtbook = (TextView) view.findViewById(R.id.textView2);
		//holder.txtmore = (TextView) view.findViewById(R.id.textViewmore);

		//holder.txtName.setTypeface(custom_font);
		//holder.txtAddress.setTypeface(custom_font);
		holder.txtDistance.setTypeface(custom_font);
		holder.txtHomeCollection.setTypeface(custom_btnfont);
	//	holder.txtbook.setTypeface(custom_btnfont);
		holder.txtreports.setTypeface(custom_btnfont);
		//holder.txtmore.setTypeface(custom_btnfont);

		holder.txtName.setTextColor(Color.parseColor("#000000"));
		holder.txtName.setTypeface(null, Typeface.BOLD);

		holder.txtbook.setTag(position);
	//holder.txtbook.setTag(position);
		holder.txtbook.setText(userlist.get(position).getTestStatus());
		holder.txtbook.setTypeface(null, Typeface.BOLD);

		//holder.txtbook.setBackgroundResource(R.drawable.approvebuttonstyle);
		//holder.txtbook.setTextColor(Color.parseColor("#ff3300"));


		//(labuserList.get(position).getState().equalsIgnoreCase("3"))

		if((userlist.get(position).getTestStatus().trim()).equalsIgnoreCase("Approved"))
		{
			//holder.txtbook.setBackgroundResource(R.drawable.approvebuttonstyle);
			holder.txtbook.setTextColor(Color.parseColor("#248f24"));
		}

		/*else if(userlist.get(position).getTestStatus().equals("UnAuthorize"))
		{
			//holder.txtbook.setBackgroundResource(R.drawable.approvebuttonstyle);
			holder.txtbook.setTextColor(Color.parseColor("#ff3300"));
		}*/
		else{
			holder.txtbook.setTextColor(Color.parseColor("#ff3300"));
		}




		//view.setTag(holder);
		
		holder.txtName.setText(userlist.get(position).gettestName());
		holder.txtAddress.setText(userlist.get(position).getTestStatus());
		//holder.txtDistance.setText(userlist.get(position).getLabDistance() +" km");
		//holder.txtlabid.setText(userlist.get(position).getLabId());
	/*
		if(userlist.get(position).getpreferred().equalsIgnoreCase("true"))
		{
			holder.txtName.setText(userlist.get(position).gettestName() + " (Preferred Lab)");
		}
		else 
		{
			holder.txtName.setText(userlist.get(position).gettestName());
		}
*/
		/*if(userlist.get(position).getregister().equalsIgnoreCase("false"))
	 	{
			holder.txtbook.setVisibility(View.GONE);
			holder.txtreports.setVisibility(View.GONE);
		}
		else
		{
			holder.txtbook.setVisibility(View.VISIBLE);
			holder.txtreports.setVisibility(View.GONE);
		}*/

		/*if(userlist.get(position).gethomeflg().equalsIgnoreCase("false"))
		{
			holder.txtHomeCollection.setVisibility(View.GONE);
		}
		else
		{
			holder.txtHomeCollection.setVisibility(View.VISIBLE);
		}*/
		
		/*holder.txtmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String address = userlist.get(position).getTestStatus();
				
				Intent i1 = new Intent(context,LabDetails.class);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i1.putExtra("LabAddress", address);
				context.startActivity(i1);
			}
		});*/

/*		holder.txtreports.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String disid = userlist.get(position).getLabId();
				String lab_name = userlist.get(position).gettestName();
				
				SharedPreferences.Editor editor = sharedpreferences.edit();

				//editor.putString("LabId", disid);
				//editor.putString("SelectedLabId", disid);
				//editor.putString("SelectedLabName", lab_name);

				editor.commit();

				Intent i1 = new Intent(context,PatientList.class);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i1);
			}
		});*/

/*
		holder.txtbook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//holder.txtbook.setText("TESTING");
				if(userlist.get(position).getTestStatus().equals("Authorize"))
				{

					holder.txtbook.setText("Approved");
					holder.txtbook.setTextColor(Color.parseColor("#248f24"));

					Toast.makeText(context, "Test Approved Successfully", Toast.LENGTH_SHORT).show();

				*//*	notifyDataSetChanged();
					convertView.setTag(holder);
					v.setTag(holder);*//*
				}
			}
		});*/
/*
		holder.txtbook.setOnClickListener(new OnClickListener() {

			String disid = holder.distributor.getLabId();
			String lab_name = holder.distributor.gettestName();
			String homecollection = holder.distributor.gethomeflg();

			@Override
			public void onClick(View v) {

				if(userlist.get(position).getTestStatus().equals("Authorize"))
				{
					Toast.makeText(context, "Test Approved Successfully", Toast.LENGTH_SHORT).show();
					holder.txtbook.setText("Approved");
					holder.txtbook.setTextColor(Color.parseColor("#248f24"));

					notifyDataSetChanged();
				}

				
				*//*if(isInternetOn(context))
				{
					Object position_clicked = v.getTag();
					labIdForProfileUpdate = disid;

					if(disid.equals(selected_labidfrompref))
					{
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putString("homecollectioncheck", homecollection);
						editor.commit();

						Intent i1 = new Intent(context,AddressSelection.class);
						i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(i1);
					}
					else
					{
						alertbox = new Builder(context)
						.setMessage("Do you want to set this lab as your preferred lab?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

							// do something when the button is clicked
							public void onClick(DialogInterface arg0, int arg1) {

								SharedPreferences.Editor editor = sharedpreferences.edit();

								//editor.putString("LabId", disid);
								//editor.putString("SelectedLabId", disid);
								//editor.putString("SelectedLabName", lab_name);
								editor.putString("homecollectioncheck", homecollection);

								editor.remove("allTestNames");
								editor.remove("popularTestData");
								editor.remove("setprofilename");
								editor.remove("setcategoryname");

								editor.commit();

								new ProfileCheck().execute(new String[] { url });
								Intent i1 = new Intent(context,AddressSelection.class);
								i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								context.startActivity(i1);
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {

								SharedPreferences.Editor editor = sharedpreferences.edit();

								//editor.putString("SelectedLabId", disid);
								//editor.putString("SelectedLabName", lab_name);
								editor.putString("homecollectioncheck", homecollection);

								//editor.remove("LabId");

								editor.commit();

								Intent i1 = new Intent(context,AddressSelection.class);
								i1.putExtra("homecollectioncheck", homecollection);
								i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								context.startActivity(i1);
							}
						}).show();

						TextView textView = (TextView) alertbox.findViewById(android.R.id.message);
						Button yesButton = alertbox.getButton(DialogInterface.BUTTON_POSITIVE);
						Button noButton = alertbox.getButton(DialogInterface.BUTTON_NEGATIVE);
						Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
						Typeface custom_bold_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
						textView.setTypeface(custom_font);
						yesButton.setTypeface(custom_bold_font);
						noButton.setTypeface(custom_bold_font);
						Log.d("elab", "Pref lab : " + disid);
						Log.d("elab", "Pref lab name : " + lab_name);
					}
				}
				else
				{
					alertBox();
				}*//*
			}
		});*/
		return view;
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
			for (TsetListDetails udet : arraylist)
			{
				if (udet.gettestName().toLowerCase().contains(charText))
				{
					userlist.add(udet);
				}
			}
		}
		notifyDataSetChanged();
	}

	private class ProfileCheck extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
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
			nameValuePairs.add(new BasicNameValuePair("\"PrefferedLabID\"", "\"" + labIdForProfileUpdate + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"2\""));

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
		Builder alert = new Builder(context);
		
		TextView Mytitle = new TextView(context);
		Mytitle.setText("Alert"); 
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
		alert.setCustomTitle(Mytitle);
		
		alert.setIcon(R.drawable.sign_logo);
		alert.setMessage("Internet Connection is not available..!");
		alert.setPositiveButton("OK", null);
		
		AlertDialog alert1 = alert.create();
		alert1.show();

		TextView textView = (TextView) alert1.findViewById(android.R.id.message);
		Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
	}
}
