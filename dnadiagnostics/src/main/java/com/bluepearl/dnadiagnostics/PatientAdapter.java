package com.bluepearl.dnadiagnostics;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.FileProvider;

import com.jsonparsing.webservice.ServiceHandler;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends BaseAdapter {
	Activity context;
	URL url = null;
	private ProgressDialog pDialog;
	String dnames[];
	String date[];
	String pnames[];
	int layoutResourceId;
	private ArrayList<PatientListDetails> arraylist =  new ArrayList<PatientListDetails>();
	private List<PatientListDetails> patientList = null;

	static String status = "";
	String testRegistrationID = "";
	public static String testID = "";
	static String lab_user_id = null;
	static String pid = null;
	static String selected_labidfrompref = null;
	static String RegistrationId = "";
	static String CurrentState = "";
	static String TestDetaile = "";

	static String pdfReportFileName = "" ;

	static String pdfFileName = "" ;
	static JSONArray patient = null;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String selected_labemailfrompref = null;

	static String selected_labmobilefrompref = null;

	private static String getPDFurl = "https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";
	private static final String originalPDF_URL ="https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";
	private static String getbillreciept="https://www.elabassist.com/Services/Billing_Service.svc/ViewBillReceipt";


	public PatientAdapter(Activity context, List<PatientListDetails> patientList)
	{
		this.context = context;
		this.patientList = patientList;
		this.arraylist = new ArrayList<PatientListDetails>();
		this.arraylist.addAll(patientList);
	}

	public int getCount()
	{
		// TODO Auto-generated method stub
		return patientList.size();
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
		PatientHolder holder = null;

		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");

		if(row == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.patient_listview_item_row, null);

			sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

			selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
			selected_labemailfrompref = sharedpreferences.getString("SelectedLabEmail", "");
			selected_labmobilefrompref = sharedpreferences.getString("SelectedLabMobile", "");
			//usertype = sharedpreferences.getString("usertype","");

			pid = sharedpreferences.getString("patientId", "");



			holder = new PatientHolder();
			holder.imgIcon = (ImageView)row.findViewById(R.id.list_image);
			holder.txtDname = (TextView)row.findViewById(R.id.doctorName);
			holder.txtPname = (TextView)row.findViewById(R.id.patientName);
			holder.txtdate = (TextView)row.findViewById(R.id.Date);
			holder.txtamt = (TextView)row.findViewById(R.id.Amount);
			holder.lbldr = (TextView)row.findViewById(R.id.txtDoctorName);
			holder.ProgressPer = (TextView)row.findViewById(R.id.ProgressPercentage);
//			holder.billrecieptview=(Button)row.findViewById(R.id.billrecieptbutton);

			holder.doctorLayout = (LinearLayout)row.findViewById(R.id.DoctorLayout_id);
			holder.centerCard = row.findViewById(R.id.ll2);
			holder.txtState = (TextView)row.findViewById(R.id.State_id);

			holder.shareIcon = (ImageView)row.findViewById(R.id.imgSharePdf_id);

			holder.ProgressBarrr = (ProgressBar) row.findViewById(R.id.mf_progress_bar);
			holder.ProgressBarrr.setProgress(10);
			holder.ProgressPer.setText("10%");

			holder.txtamt.setTextColor(	Color.RED);
			//holder.txtPname.setTextColor(Color.parseColor("#4d4dff"));
			holder.txtPname.setTextColor(Color.parseColor("#000000"));
			holder.txtPname.setTypeface(null, Typeface.BOLD_ITALIC);
			holder.txtPname.setTextSize(18);

			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#6cb13c"), PorterDuff.Mode.SRC_IN);



			row.setTag(holder);
		}
		else
		{
			holder = (PatientHolder)row.getTag();
		}

		//holder.txtPname.setText(patientList.get(position).getPatientName());
		holder.txtPname.setText(patientList.get(position).getPatientName()+"("+patientList.get(position).getLabCode()+")");
		holder.txtDname.setText(patientList.get(position).getDoctorName());
		holder.txtdate.setText(patientList.get(position).getDate());
		holder.txtamt.setText(patientList.get(position).getBal_Amt());
		holder.txtState.setText(patientList.get(position).getTestState());
		if(
				patientList.get(position).getDoctorName().equalsIgnoreCase("SELF") ||
						patientList.get(position).getDoctorName().equalsIgnoreCase(null) ||
						patientList.get(position).getDoctorName().equalsIgnoreCase("") ||
						patientList.get(position).getDoctorName().equalsIgnoreCase(" ")
			//  patientList.get(position).getDoctorName().equals("") ||
			//	patientList.get(position).getDoctorName().equals(" ")
		)
		{
			holder.doctorLayout.setVisibility(View.GONE);
		}
		else
		{
			holder.doctorLayout.setVisibility(View.VISIBLE);
		}
		if( (    (patientList.get(position).getTestState().equalsIgnoreCase("1")) ||
				(patientList.get(position).getTestState().equalsIgnoreCase("2")))
		)
		{
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText(" Registration");
			holder.txtState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtState.setTypeface(null, Typeface.BOLD);
			holder.ProgressBarrr.setProgress(20);
			holder.ProgressPer.setText("20%");

			//int colorCodeDark = Color.parseColor("#ff4d4d");
			//	holder.ProgressBarrr.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
			//	holder.ProgressBarrr.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ff4d4d"), android.graphics.PorterDuff.Mode.SRC_ATOP);

			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#ff4d4d"), PorterDuff.Mode.SRC_IN);


		}

		else if( ( (patientList.get(position).getTestState().equalsIgnoreCase("3")))
		)
		{
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText("Sample Collection");
			holder.txtState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtState.setTypeface(null, Typeface.BOLD);
			holder.ProgressBarrr.setProgress(30);
			holder.ProgressPer.setText("30%");
			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#ff4d4d"), PorterDuff.Mode.SRC_IN);
		}

		else if( (    (patientList.get(position).getTestState().equalsIgnoreCase("4")) ||
				(patientList.get(position).getTestState().equalsIgnoreCase("5")) ||
				(patientList.get(position).getTestState().equalsIgnoreCase("6")))
		)
		{
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText(" Accession");
			holder.txtState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtState.setTypeface(null, Typeface.BOLD);
			holder.ProgressBarrr.setProgress(50);
			holder.ProgressPer.setText("50%");
			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#ff4d4d"), PorterDuff.Mode.SRC_IN);
		}

		else if( ( (patientList.get(position).getTestState().equalsIgnoreCase("7")))
		)
		{
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText(" Result");
			holder.txtState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtState.setTypeface(null, Typeface.BOLD);
			holder.ProgressBarrr.setProgress(70);
			holder.ProgressPer.setText("70%");
			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#4d94ff"), PorterDuff.Mode.SRC_IN);
		}


		//ffcc00
		else if( ( (patientList.get(position).getTestState().equalsIgnoreCase("8")))
		)
		{
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText("Technician Approve");
			//holder.txtState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtState.setTextColor(Color.parseColor("#e6b800"));
			holder.txtState.setTypeface(null, Typeface.BOLD);
			holder.ProgressBarrr.setProgress(80);
			holder.ProgressPer.setText("80%");
			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#e6b800"), PorterDuff.Mode.SRC_IN);
		}

		else if( ( (patientList.get(position).getTestState().equalsIgnoreCase("9")))
		)
		{
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText("Pathology Approve");
			holder.txtState.setTextColor(Color.parseColor("#4d94ff"));
			holder.txtState.setTypeface(null, Typeface.BOLD);
			holder.ProgressBarrr.setProgress(90);
			holder.ProgressPer.setText("90%");
			//holder.ProgressBarrr.getIndeterminateDrawable().setColorFilter(Color.parseColor("#4d94ff"), android.graphics.PorterDuff.Mode.SRC_ATOP);
			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#4d94ff"), PorterDuff.Mode.SRC_IN);


		}

		else if( (    (patientList.get(position).getTestState().equalsIgnoreCase("10")) ||
				(patientList.get(position).getTestState().equalsIgnoreCase("11")))
		)
		{
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText(" Release");
			holder.txtState.setTextColor(Color.parseColor("#6cb13c"));
			holder.txtState.setTypeface(null, Typeface.BOLD);
			holder.ProgressBarrr.setProgress(100);
			holder.ProgressPer.setText("100%");
			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#6cb13c"), PorterDuff.Mode.SRC_IN);
		}
		else
		{
			holder.txtState.setVisibility(View.GONE);
		}



		holder.txtamt.setTypeface(custom_font);
		holder.txtDname.setTypeface(custom_font);
		//holder.txtPname.setTypeface(custom_font);
		holder.txtdate.setTypeface(custom_font);
		holder.lbldr.setTypeface(custom_font);

		holder.imgIcon.setImageResource(R.drawable.report);

		if (position % 2 == 1)
		{
			row.setBackgroundColor(Color.parseColor("#1Affffff"));
		}
		else if (position % 2 == 0)
		{
			row.setBackgroundColor(Color.parseColor("#f2f2f2"));
		}

//		holder.billrecieptview.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
		holder.centerCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					String balanceAmt = patientList.get(position).getBal_Amt();
					Log.d("balanceAmt",balanceAmt);
					if(balanceAmt.equalsIgnoreCase("0.0")){
							Intent intent = new Intent(context.getApplicationContext(),ViewReportAttachment.class);
							intent.putExtra("testRegistrationID",(patientList.get(position).getTestRegId()));
							intent.putExtra("PMobile","");
							intent.putExtra("AMobile","");
							intent.putExtra("CCMobile","");
							intent.putExtra("DMobile","");
							context.startActivity(intent);
					}else{
					showAlert1("To download the report, please pay the bill amount.");
				}
				}catch(NumberFormatException e){
					showAlert1("Something went wrong!");
				}
			}
		});

		holder.shareIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				RegistrationId = (patientList.get(position).getTestRegId());
				testRegistrationID = (patientList.get(position).getTestRegId());
				testID = (patientList.get(position).getTestRegId());
				Log.d("onClick","GetBillReceiptDetails");
				new GetBillrecieptdetail().execute();
			}
		});




		return row;
	}



	static class PatientHolder
	{
		ImageView imgIcon,shareIcon;
		TextView txtDname,txtdate,txtPname,lbldr,txtamt,txtState,ProgressPer;
		LinearLayout doctorLayout,centerCard;
		ProgressBar ProgressBarrr;
		Button billrecieptview;
	}


	//bill receipt
	private class GetBillrecieptdetail extends AsyncTask<String,Void,String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();


			// patientList.clear();

			// Showing progress dialog
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(false);
			//Toast.makeText(context, "onPreExecute", Toast.LENGTH_SHORT).show();
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			Log.d("statusPOST","testID"+testID+"labID"+selected_labidfrompref);
			InputStream inputStream = null;
			String result = "";

			try
			{
				Log.d("try","try block called");
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(getbillreciept);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("\"TestRegnID\"", "\"" + testID + "\""));
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));



				String finalString = nameValuePairs.toString();
				finalString = finalString.replace('=', ':');
				finalString = finalString.substring(1, finalString.length() - 1);
				String strToServer1 = "{" + finalString + "}";

				Log.d("strToServer++",strToServer1);



				StringEntity se = new StringEntity(strToServer1);
				se.setContentType("application/json;charset=UTF-8");
				//se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setEntity(se);

				// Execute POST request to the given URL
				HttpResponse httpResponse = httpClient.execute(httpPost);

				Log.d("httpResponse",""+httpResponse);

				// receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();
				Log.d("httpResponse",""+inputStream);


				// convert inputstream to string
				if (inputStream != null)
				{
					result = convertInputStreamToString(inputStream);

					Log.d("inputtostring result",""+result);
					//String jsonformattString = result.replaceAll("\\\\", "");
					try
					{
						JSONObject jsonObj = new JSONObject(result);

						String abc = jsonObj.getString("d");
						result = abc.replace("~/","");
						Log.d("BillReceipt","BillReceipt "+result);


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
				Log.d("BillReceipt",""+e);
				// Log.i("InputStream", e.getLocalizedMessage());
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

			//Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}

			if(result != null && !(result.equals(""))){
				new DownloadReceiptFile(context,"https://www.elabassist.com/"+result).execute();

			}
			else
			{
				showAlertReportNotGenerated("Your Lab has not released a report online. Please press 'yes' to contact Lab "+selected_labmobilefrompref+ " "+selected_labemailfrompref );

//				getPDFurl = originalPDF_URL;
			}

		}
	}


	//for bill-receipt and testreport -> view
	private class DownloadReceiptFile extends AsyncTask<String, Void, String>
	{
		private Context mContext;
		private String pdfReceiptUrl;

		public DownloadReceiptFile (Context context,String pdfReceiptUrl){
			mContext = context;
			this.pdfReceiptUrl = pdfReceiptUrl;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			Log.d("DownloadReceiptFile","onPreexecute");
			// Showing progress dialog
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... strings){
			Log.d("DownloadReceiptFile","doInBackground");

			try {
				url = new URL(pdfReceiptUrl);
				Log.d("MalformedURLException",""+url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Log.d("MalformedURLException",""+e);
			}
			String filepath = url.getPath();
			filepath = filepath.substring(filepath.lastIndexOf("/")+1);

			Log.d("filepath",""+filepath);

			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(url)));
			request.setTitle(filepath);
			request.setMimeType("application/pdf");

			//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filepath);

			DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
			dm.enqueue(request);
			pdfReportFileName = "";
			return filepath;
		}

		@Override
		protected void onPostExecute(String filepath) {

			super.onPostExecute(filepath);

			Log.d("DownloadReceiptFile", "onPostExecute");
			final Handler handler = new Handler(Looper.getMainLooper());
			final String filePath = filepath;
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filePath);
			final Uri uri = FileProvider.getUriForFile(mContext, "com.bluepearl.dnadiagnostics" + ".provider", file);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					//Do something after 3000s
					if (pDialog != null && pDialog.isShowing()){
						pDialog.dismiss();
					}
					try{
						Intent intent = new Intent();
						intent.setDataAndType(uri,"application/pdf");
						intent.setAction(Intent.ACTION_VIEW);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
						mContext.startActivity(intent);
					}catch(Exception e){
						Log.e("error", String.valueOf(e));
						Toast.makeText(mContext, "Download failed due to poor internet connection issues.", Toast.LENGTH_SHORT).show();
					}
				}
			}, 4000);

			getPDFurl = originalPDF_URL;
		}
	}




	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		Log.d("Inputdream called","inputstream called");
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";

		while ((line = bufferedReader.readLine()) != null) {
			Log.d("line",""+line);
			result += line;
		}

		Log.d("inputstream",""+result);

		inputStream.close();
		Log.d("inputstream",""+result);
		return result;
	}


	public void showAlert1(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		TextView Mytitle = new TextView(context);
		Mytitle.setText("Alert");
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		builder.setCustomTitle(Mytitle);

		// Create the message TextView and set its text and appearance
		TextView messageTextView = new TextView(context);
		messageTextView.setText(message);
		messageTextView.setTextSize(16); // Adjust the text size
		messageTextView.setTypeface(null, Typeface.BOLD); // Set the text to bold
		messageTextView.setPadding(15, 5, 15, 15); // Adjust padding

		builder.setView(messageTextView);
		builder.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});

		AlertDialog alert = builder.create();
		alert.show();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);

	}

	public void showAlertReportNotGenerated(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		TextView Mytitle = new TextView(context);
		Mytitle.setText("Alert");
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		builder.setCustomTitle(Mytitle);


		// Create the message TextView and set its text and appearance
		TextView messageTextView = new TextView(context);
		messageTextView.setText(message);
		messageTextView.setTextSize(16); // Adjust the text size
		messageTextView.setTypeface(null, Typeface.BOLD); // Set the text to bold
		messageTextView.setPadding(5, 5, 5, 15); // Adjust padding

		builder.setView(messageTextView)
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						dialog.dismiss();
					}
				})
				.setNegativeButton("More", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//  Action for 'NO' Button
						Intent i = new Intent(context,MoreActivity.class);
						context.startActivity(i);
					}
				});
		//Creating dialog box
		AlertDialog alert = builder.create();
		//Setting the title manually
		alert.setTitle("AlertDialogExample");
		alert.show();

	}

}
