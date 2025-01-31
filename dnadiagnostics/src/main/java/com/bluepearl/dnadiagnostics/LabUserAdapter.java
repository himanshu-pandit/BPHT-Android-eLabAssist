package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.jsonparsing.webservice.ServiceHandler;

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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LabUserAdapter extends BaseAdapter
{
	private ProgressDialog pDialog;
	static String usertype = "";
	static JSONArray patient = null;
	String pdfFileName = "" ;

	Activity context; 
	String pnames[];
	String dnames[];
	String labcode[];
	String date[];
	String amnt[];
	int layoutResourceId;    
	private ArrayList<LabListDetails> arraylist =  new ArrayList<LabListDetails>();
	private List<LabListDetails> labuserList = null;

	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	TestListAdapter adapter, recent_adapter;

	Dialog dialog;

	ArrayList<TsetListDetails> vicinitynamesList = new ArrayList<TsetListDetails>();

	static String status = "";
	String testRegistrationID = "";
	static String lab_user_id = null;
	static String pid = null;
	static String selected_labidfrompref = null;
	static String RegistrationId = "";
	static String CurrentState = "";
	static String TestDetaile = "";
	String pdfReportUrl = "";
	URL downloadUrl = null;

	private String url = "https://www.elabassist.com/Services/Test_RegnService.svc/UpdateTestRegnState";
	private static String getPDFurl = "https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";
	private static final String originalPDF_URL ="https://www.elabassist.com/Services/Test_RegnService.svc/GetReleaseTestReport_Global?TestRegnID=";
	private static final String getbillreciept="https://www.elabassist.com/Services/Billing_Service.svc/ViewBillReceipt";
	public LabUserAdapter(Activity context, List<LabListDetails> patientList)
	{
		this.context = context;
		this.labuserList = patientList;
		this.arraylist = new ArrayList<LabListDetails>();
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
		usertype = sharedpreferences.getString("usertype","");

		pid = sharedpreferences.getString("patientId", "");
		
		if(row == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.labuser_listview_item_row, null);

			holder = new LabUserHolder();
			holder.imgIcon = (ImageView)row.findViewById(R.id.imgTestInfo);
			holder.shareIcon = (ImageView)row.findViewById(R.id.imgSharePdf_id);
			holder.whatsappshare = (ImageView)row.findViewById(R.id.whatsappsharepdf);

			holder.txtPname = (TextView)row.findViewById(R.id.patientName);
			holder.txtDname = (TextView)row.findViewById(R.id.doctorName);
			holder.txtlabcode = (TextView)row.findViewById(R.id.labcode);
			holder.txtdate = (TextView)row.findViewById(R.id.Date);
			holder.txtamt = (TextView)row.findViewById(R.id.Amount);
			holder.lbltxtlabcode = (TextView)row.findViewById(R.id.txtlabcode);
			holder.lbltxtamt = (TextView)row.findViewById(R.id.txtAmt);
			holder.lbldr = (TextView)row.findViewById(R.id.txtDoctorName);
			holder.txtApprove = (TextView)row.findViewById(R.id.textApprove);
			holder.doctorLayout = (LinearLayout)row.findViewById(R.id.DoctorLayout_id);
			holder.txtState = (TextView)row.findViewById(R.id.State_id);
			holder.SeekBarrr = (SeekBar) row.findViewById(R.id.SeekBarrr_id);
			holder.ProgressBarrr = (ProgressBar) row.findViewById(R.id.mf_progress_bar);
			holder.ProgressPer = (TextView)row.findViewById(R.id.ProgressPercentage);
			holder.textViewaprovtest = (TextView)row.findViewById(R.id.textViewaprovtest);
			holder.txtTestName = (TextView) row.findViewById(R.id.SlectTest_id);




			holder.ProgressBarrr.setProgress(10);
			holder.ProgressPer.setText("10%");


			//holder.txtamt.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtamt.setTextColor(	Color.RED);
			//holder.txtPname.setTextColor(Color.parseColor("#4d4dff"));
			holder.txtPname.setTextColor(Color.parseColor("#000000"));
			holder.txtPname.setTypeface(null, Typeface.BOLD_ITALIC);
			holder.txtPname.setTextSize(18);


			//holder.ProgressBarrr.getIndeterminateDrawable().setColorFilter(Color.parseColor("#6cb13c"), android.graphics.PorterDuff.Mode.SRC_ATOP);
			//holder.ProgressBarrr.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

			//holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#6cb13c"), PorterDuff.Mode.SRC_IN);




			//holder.SeekBarrr.isClickable(false);
		/*	holder.SeekBarrr.setOnTouchListener(
					new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							return false;
						}
					}
			);
*/


			row.setTag(holder);
		}
		else
		{
			holder = (LabUserHolder)row.getTag();
		}

		//holder.txtPname.setText(labuserList.get(position).getPatientName());
		holder.txtPname.setText(labuserList.get(position).getPatientName()+"("+labuserList.get(position).getLabCode()+")");
		holder.textViewaprovtest.setText("("+ labuserList.get(position).getApproveTest()+"/"+labuserList.get(position).getTotalTest()+")");

		holder.txtDname.setText(labuserList.get(position).getDoctorName());
		holder.txtlabcode.setText(labuserList.get(position).getLabCode());
		holder.txtdate.setText(labuserList.get(position).getDate());
		holder.txtamt.setText(labuserList.get(position).getBal_Amt());
		holder.txtState.setText(labuserList.get(position).getState());
		holder.txtTestName.setText(labuserList.get(position).getSelectTest());

		holder.DoctorMobile=labuserList.get(position).getDMobile();
		holder.PatientMobile=labuserList.get(position).getPMobile();
		holder.AffiliationMobile=labuserList.get(position).getAMobile();
		holder.CollectionCenterMobile=labuserList.get(position).getCCMobile();
		Log.e("patientmobilenumber=","k"+labuserList.get(position).getPMobile());
		if(
				labuserList.get(position).getDoctorName().equalsIgnoreCase("SELF") ||
						labuserList.get(position).getDoctorName().equalsIgnoreCase(null) ||
						labuserList.get(position).getDoctorName().equalsIgnoreCase("") ||
						labuserList.get(position).getDoctorName().equalsIgnoreCase(" ")
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


		if( (    (labuserList.get(position).getState().equalsIgnoreCase("1")) ||
				(labuserList.get(position).getState().equalsIgnoreCase("2")))
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

		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("3")))
				)
		{
			holder.txtState.setVisibility(View.VISIBLE);
			holder.txtState.setText("Sample Collection");
			holder.txtState.setTextColor(Color.parseColor("#ff4d4d"));
			holder.txtState.setTypeface(null, Typeface.BOLD);
			holder.ProgressBarrr.setProgress(30);
			holder.ProgressPer.setText("30%");
			holder.ProgressBarrr.getProgressDrawable().setColorFilter(Color.parseColor("#ff4d4d"), PorterDuff.Mode.SRC_IN);
		}

		else if( (    (labuserList.get(position).getState().equalsIgnoreCase("4")) ||
				(labuserList.get(position).getState().equalsIgnoreCase("5")) ||
				(labuserList.get(position).getState().equalsIgnoreCase("6")))
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

		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("7")))
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
		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("8")))
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

		else if( ( (labuserList.get(position).getState().equalsIgnoreCase("9")))
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

		else if( (    (labuserList.get(position).getState().equalsIgnoreCase("10")) ||
				(labuserList.get(position).getState().equalsIgnoreCase("11")))
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



		if(labuserList.get(position).getState().equalsIgnoreCase("8"))
		{
			holder.txtApprove.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.txtApprove.setVisibility(View.GONE);
			//holder.txtApprove.setVisibility(View.VISIBLE);
		}

		holder.imgIcon.setImageResource(R.drawable.info);

		holder.txtamt.setTypeface(custom_font);
		holder.txtlabcode.setTypeface(custom_font);
		//holder.txtPname.setTypeface(custom_font);
		holder.txtDname.setTypeface(custom_font);
		holder.txtdate.setTypeface(custom_font);
		holder.lbltxtlabcode.setTypeface(custom_font);
		holder.lbltxtamt.setTypeface(custom_font);
		holder.lbldr.setTypeface(custom_font);
		//holder.txtApprove.setTypeface(custom_font);

		if (position % 2 == 1)
		{
			row.setBackgroundColor(Color.parseColor("#1Affffff"));
		}
		else if (position % 2 == 0)
		{
			row.setBackgroundColor(Color.parseColor("#f2f2f2"));
		}
		holder.imgIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, "clck", Toast.LENGTH_SHORT).show();

				vicinitynamesList.clear();

				TestDetaile = (labuserList.get(position).getTestListDetails());

				if(TestDetaile.equals("")){
					Toast.makeText(context, "Test Details Not Available", Toast.LENGTH_SHORT).show();
				}
				else{
					ArrayList aList = new ArrayList(Arrays.asList(TestDetaile.split("#")));
					for (int i = 0; i < aList.size(); i++) {
						System.out.println(" -->" + aList.get(i));

						String my_test_data = (String) aList.get(i);

						String[] allTestsData = my_test_data.split(":");


						String my_test_name = allTestsData[0];
						String my_teststatus = allTestsData[1];
						String totalcount = String.valueOf(i + 1);

						TsetListDetails det = new TsetListDetails(totalcount + ") " + my_test_name, my_teststatus);
						vicinitynamesList.add(det);
					}



	/*	TsetListDetails det = new TsetListDetails("1) Complete Blood Count(CBC)","Approved","djksf","gn","gdn","gdfj","gnudfh");
		TsetListDetails dett = new TsetListDetails("2) Lipid Profile(LIPID)","UnAuthorize","djksf","gn","gdn","gdfj","gnudfh");
		TsetListDetails dettt = new TsetListDetails("3) Blood Sugar Fasting / Post Prandial ( BSL-F/PP)","UnAuthorize","djksf","gn","gdn","gdfj","gnudfh");
*/
			/*	TsetListDetails det = new TsetListDetails("1) Complete Blood Count(CBC)","Approved");
				TsetListDetails dett = new TsetListDetails("2) Lipid Profile(LIPID)","UnAuthorize");
				TsetListDetails dettt = new TsetListDetails("3) Blood Sugar Fasting / Post Prandial ( BSL-F/PP)","UnAuthorize");


				// adding contact to contact list
				vicinitynamesList.add(det);
				vicinitynamesList.add(dett);
				vicinitynamesList.add(dettt);*/

					adapter = new TestListAdapter(context, vicinitynamesList);
					showdialog();

				}
			}
		});


		holder.shareIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


			//	Toast.makeText(context, "Share pdf", Toast.LENGTH_SHORT).show();
				RegistrationId = (labuserList.get(position).getTestRegId());
				testRegistrationID = (labuserList.get(position).getTestRegId());
				new GetPDFReport().execute();
			}
		});

		holder.whatsappshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				testRegistrationID = (labuserList.get(position).getTestRegId());
				new GetPDFReport().execute();

			}
		});

		holder.txtApprove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegistrationId = (labuserList.get(position).getTestRegId());

				CurrentState = (labuserList.get(position).getState());

				new StatusCheck().execute(new String[] { url });
				//alertBox();
			}
		});
		return row;
	}

	static class LabUserHolder
	{
		ImageView imgIcon,shareIcon,whatsappshare;
		TextView txtPname,txtDname,txtlabcode,txtdate,txtamt,lbltxtlabcode,
				lbltxtamt,lbldr,txtApprove,txtState,txtTestName,textViewaprovtest,ProgressPer;
		LinearLayout doctorLayout;
		SeekBar SeekBarrr;
		ProgressBar ProgressBarrr;
		public String PatientMobile;
		public String DoctorMobile;
		public String AffiliationMobile;
		public String CollectionCenterMobile;

	}


	private class GetPDFReport extends AsyncTask<Void, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(Void... arg0)
		{
			InputStream inputStream = null;
			String result = "";

			try
			{
				Log.d("try","try block called");
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(getbillreciept);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("\"TestRegnID\"", "\"" + testRegistrationID + "\""));
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

			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}

			if(result != null && !(result.equalsIgnoreCase("")))
			{
				new DownloadFile(context,"https://www.elabassist.com/"+result).execute();
			}
			else
			{
				showAlert1("Bill Receipt is not generated yet, Please contact to laboratory.");
			}
		}
	}


	private class DownloadFile extends AsyncTask<String, Void, String>
	{
		private Context mContext;
		private String pdfReceiptUrl;

		public DownloadFile (Context context,String pdfReceiptUrl){
			mContext = context;
			this.pdfReceiptUrl = pdfReceiptUrl;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... strings)
		{
			try {
				downloadUrl = new URL(pdfReceiptUrl);
				Log.d("MalformedURLException",""+downloadUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Log.d("MalformedURLException",""+e);
			}
			String filepath = downloadUrl.getPath();
			filepath = filepath.substring(filepath.lastIndexOf("/")+1);

			Log.d("filepath",""+filepath);

			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(downloadUrl)));
			request.setTitle(filepath);
			request.setMimeType("application/pdf");

			//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filepath);

			DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
			dm.enqueue(request);
			pdfFileName = "";
			return filepath;
		}

		@Override
		protected void onPostExecute(String filepath)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(filepath);
			final Handler handler = new Handler(Looper.getMainLooper());
			final String filePath = filepath;
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filepath);

			Log.d("file exist",""+file.exists());
			final Uri uri = FileProvider.getUriForFile(mContext, "com.bluepearl.dnadiagnostics" + ".provider", file);

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					//Do something after 5000s
					if (pDialog != null && pDialog.isShowing()){
						pDialog.dismiss();
					}
					try{
						Intent intent = new Intent();
						intent.setDataAndType(uri,"application/pdf");
						intent.setAction(Intent.ACTION_VIEW);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
						mContext.startActivity(intent);
					}
					catch(ActivityNotFoundException e){
						Toast.makeText(context, "  No Application available to view PDF ", Toast.LENGTH_SHORT).show();
					}
				}
			}, 4000);
			getPDFurl = originalPDF_URL;
		}
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

			if(result.equals("false")){
				//Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
				alertBox("Failed");


			}else {

				alertBox("Report is successfully approved");

				//Toast.makeText(context, "Report is successfully approved", Toast.LENGTH_SHORT).show();
			}


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
	
	private void alertBox(String Msg)
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
	//	alert.setMessage("Report is successfully approved.");
		alert.setMessage(Msg);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent i = new Intent(context,ViewReportLabUser.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
				//context./finish();
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
	private void showdialog()
	{
		//adapter.notifyDataSetChanged();

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.testdialog, null);

		//Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		//Typeface custom_bold_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

		Builder builder = new Builder(context);
		builder.setView(layout);

		final TextView search = (TextView) layout.findViewById(R.id.inputSearch);
		final ListView modeList = (ListView) layout.findViewById(R.id.listviewNearby);
		//search.setTypeface(custom_font);

		//modeList.setOnItemClickListener(this);

		/*ProgressDialog pDialogg = new ProgressDialog(LabSelection.this);
		pDialogg.setMessage("Please wait...");
		pDialogg.setCancelable(false);
		pDialogg.show();*/


/*		vicinitynamesList.clear();
	*//*	TsetListDetails det = new TsetListDetails("1) Complete Blood Count(CBC)","Approved","djksf","gn","gdn","gdfj","gnudfh");
		TsetListDetails dett = new TsetListDetails("2) Lipid Profile(LIPID)","UnAuthorize","djksf","gn","gdn","gdfj","gnudfh");
		TsetListDetails dettt = new TsetListDetails("3) Blood Sugar Fasting / Post Prandial ( BSL-F/PP)","UnAuthorize","djksf","gn","gdn","gdfj","gnudfh");
*//*
        TsetListDetails det = new TsetListDetails("1) Complete Blood Count(CBC)","Approved");
        TsetListDetails dett = new TsetListDetails("2) Lipid Profile(LIPID)","UnAuthorize");
        TsetListDetails dettt = new TsetListDetails("3) Blood Sugar Fasting / Post Prandial ( BSL-F/PP)","UnAuthorize");


        // adding contact to contact list
		vicinitynamesList.add(det);
		vicinitynamesList.add(dett);
		vicinitynamesList.add(dettt);

		adapter = new TestListAdapter(context, vicinitynamesList);*/
		modeList.setAdapter(adapter);



		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

	/*	search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				String text = search.getText().toString().toLowerCase(Locale.getDefault());
				//adapter.filter(text);
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
		});*/

		dialog = builder.create();

		dialog.show();

		Button noButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
		//noButton.setTypeface(custom_bold_font);
		noButton.setTextSize(20);
	}


	public void showAlert1(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		TextView Mytitle = new TextView(context);
		Mytitle.setText("Alert");
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});

		AlertDialog alert = builder.create();
		alert.show();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);

	}


}

