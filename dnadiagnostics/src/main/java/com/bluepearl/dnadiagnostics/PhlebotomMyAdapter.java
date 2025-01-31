package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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


public class PhlebotomMyAdapter extends BaseAdapter
{
	Activity context;
	String pnames[];
	String dnames[];
	String labcode[];
	String date[];
	String amnt[];
	int layoutResourceId;
	private ArrayList<PhlebotomyMyListDetails> arraylist =  new ArrayList<PhlebotomyMyListDetails>();
	private List<PhlebotomyMyListDetails> labuserList = null;

	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;

	static String status = "";
	String testRegistrationID = "";
	static String lab_user_id = null;
	static String pid = null;
	static String selected_labidfrompref = null;
	static String RegistrationId = "";
	static String CurrentState = "";

	private String url = "https://www.elabassist.com/Services/Test_RegnService.svc/UpdateTestRegnState";

	public PhlebotomMyAdapter(Activity context, List<PhlebotomyMyListDetails> patientList)
	{
		this.context = context;
		this.labuserList = patientList;
		this.arraylist = new ArrayList<PhlebotomyMyListDetails>();
		this.arraylist.addAll(patientList);
	}

	public int getCount() 
	{
		// TODO Auto-generated method stub
		return labuserList.size();
	}

	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		LabUserHolder holder = null;
		
		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		
		sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		pid = sharedpreferences.getString("patientId", "");
		
		if(row == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.phlebotomy_listview_item_row_online, null);

			holder = new LabUserHolder();
			holder.imgIcon = (ImageView)row.findViewById(R.id.list_image);
			holder.txtPname = (TextView)row.findViewById(R.id.PatientName_id);
			holder.txtDname = (TextView)row.findViewById(R.id.doctorName);
			holder.txtlabcode = (TextView)row.findViewById(R.id.labcode);
			holder.txtdate = (TextView)row.findViewById(R.id.Date);
			holder.txtamt = (TextView)row.findViewById(R.id.Amount);
			holder.lbltxtlabcode = (TextView)row.findViewById(R.id.txtlabcode);
			holder.lbltxtamt = (TextView)row.findViewById(R.id.txtAmt);
			holder.lbldr = (TextView)row.findViewById(R.id.txtDoctorName);
			holder.txtApprove = (TextView)row.findViewById(R.id.textApprove);

			holder.txtCoolectionCenterName = (TextView)row.findViewById(R.id.CollectionCenterName_id);
			holder.txtCurrentState = (TextView)row.findViewById(R.id.CurntState_id);
			
			row.setTag(holder);
		}
		else
		{
			holder = (LabUserHolder)row.getTag();
		}
		
		holder.txtPname.setText(labuserList.get(position).getPatientName());
		holder.txtDname.setText(labuserList.get(position).getDoctorName());
		holder.txtlabcode.setText(labuserList.get(position).getLabCode());
		holder.txtdate.setText(labuserList.get(position).getDate());
		holder.txtamt.setText(labuserList.get(position).getBal_Amt());

		holder.txtCoolectionCenterName.setText(labuserList.get(position).getCollectionCenterName());


		//holder.txtCurrentState.setText(labuserList.get(position).getState());

		if( (    (labuserList.get(position).getState().equalsIgnoreCase("1")) ||
				(labuserList.get(position).getState().equalsIgnoreCase("2")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText(" Registration");
			holder.txtCurrentState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);

		}

		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("3")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("Sample Collection");
			holder.txtCurrentState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}
		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("4")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("Sample Deliver");
			holder.txtCurrentState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if ((labuserList.get(position).getState().equalsIgnoreCase("5")) ||
				(labuserList.get(position).getState().equalsIgnoreCase("6")))

		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText(" Accession");
			holder.txtCurrentState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("7")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText(" Result");
			holder.txtCurrentState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("8")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("Technician Approve");
			holder.txtCurrentState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("9")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("Pathology Approve");
			holder.txtCurrentState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}

		else if( (    (labuserList.get(position).getState().equalsIgnoreCase("10")) ||
				(labuserList.get(position).getState().equalsIgnoreCase("11")))
				)
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText(" Release");
			holder.txtCurrentState.setTextColor(Color.parseColor("#6cb13c"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}
		else
		{
			holder.txtCurrentState.setVisibility(View.VISIBLE);
			holder.txtCurrentState.setText("No Status");
			holder.txtCurrentState.setTextColor(Color.parseColor("#6cb13c"));
			holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);
		}
		//holder.txtCurrentState.setTypeface(null, Typeface.BOLD_ITALIC);

		if(labuserList.get(position).getState().equalsIgnoreCase("8"))
		{
		//	holder.txtApprove.setVisibility(View.VISIBLE);
		}
		else
		{
			//holder.txtApprove.setVisibility(View.GONE);
		}
		
		holder.imgIcon.setImageResource(R.drawable.report);
		
		//holder.txtamt.setTypeface(custom_font);
		//holder.txtlabcode.setTypeface(custom_font);
		//holder.txtPname.setTypeface(custom_font);
		//holder.txtDname.setTypeface(custom_font);
		//holder.txtdate.setTypeface(custom_font);
		//holder.lbltxtlabcode.setTypeface(custom_font);
		//holder.lbltxtamt.setTypeface(custom_font);
		//holder.lbldr.setTypeface(custom_font);
		//holder.txtApprove.setTypeface(custom_font);

		if (position % 2 == 0) 
		{
			row.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		else if (position % 2 == 1) 
		{
			row.setBackgroundColor(Color.parseColor("#dde3ee"));
		}
		
		holder.txtApprove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegistrationId = (labuserList.get(position).getTestRegId());
				CurrentState = (labuserList.get(position).getState());
				
				new StatusCheck().execute(new String[] { url });
				alertBox();
			}
		});
		return row;
	}

	static class LabUserHolder
	{
		ImageView imgIcon;
		TextView txtPname,txtDname,txtlabcode,txtdate,txtamt,lbltxtlabcode,lbltxtamt,lbldr,txtApprove
				,txtCoolectionCenterName,txtCurrentState;
	}
	
	private class StatusCheck extends AsyncTask<String, Void, String> 
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) 
		{
			return StatusPOST(params[0]);
		}

		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
		}
	}

	public static String StatusPOST(String url) 
	{
		InputStream inputStream = null;
		String result = "";
		try 
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("\"TestRegnFID\"", "" + RegistrationId + ""));
			nameValuePairs.add(new BasicNameValuePair("\"LabCode\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"PreviousActionFID\"", "" + CurrentState + ""));
			nameValuePairs.add(new BasicNameValuePair("\"ActionBy\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"ActionFID\"", "9"));
			nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			finalString = "{" + finalString + "}";
			String tempString = "\"objUSLC\"" + ":";
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
	
	private void alertBox() 
	{
		Builder alert = new Builder(context);
		
		TextView Mytitle = new TextView(context);
		Mytitle.setText("Alert"); 
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf"));
		alert.setCustomTitle(Mytitle);
		
		alert.setIcon(R.drawable.sign_logoelab);
		alert.setMessage("Report is successfully approved.");
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent i = new Intent(context,PhlebotomyMyListTabOn.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
				//context.finish();
			}
		});
		
		AlertDialog alert1 = alert.create();
		alert1.show();

		TextView textView = (TextView) alert1.findViewById(android.R.id.message);
		Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
	}
}
