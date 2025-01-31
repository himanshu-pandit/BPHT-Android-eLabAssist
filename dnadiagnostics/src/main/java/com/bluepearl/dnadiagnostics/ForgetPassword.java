package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ForgetPassword extends Activity
{
	Button btnsend;
	EditText number;
	TextView lblLab;
	static String status = "";
	private ProgressDialog pDialog;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String number_value = null;
	static String pid = null;

	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";


	JSONArray phno = null;

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		setContentView(R.layout.activity_forget_password);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");

		btnsend = (Button)findViewById(R.id.SendButton);
		number = (EditText)findViewById(R.id.etmob);
		lblLab = (TextView)findViewById(R.id.txtviewlab);

		number.setTypeface(custom_font);
		btnsend.setTypeface(custom_btnfont);
		lblLab.setTypeface(custom_btnfont);

		btnsend.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub

				number_value = number.getText().toString();

				if (isInternetOn(getApplicationContext())) 
				{
					if((number_value).equalsIgnoreCase(""))
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter the mobile number     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
					}
					else 
					{
						new ForgetPasswordCheck().execute(new String[] { url });
					}
				}
				else
				{
					alertBox();
				}
			}
		});
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

	public static String ForgetPOST(String url) 
	{
		InputStream inputStream = null;
		String result = "";
		try 
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"4\""));
			nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\"" + number_value + "\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			finalString = "{" + finalString + "}";
			String tempString = "\"objSP\"" + ":";
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
					JSONObject resultObj = (JSONObject) jsonObj.get("d");
					
					status = resultObj.getString("Result");
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

		return status;
	}

	private class ForgetPasswordCheck extends AsyncTask<String, Void, String> 
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(ForgetPassword.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) 
		{
			return ForgetPOST(params[0]);
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

			if (result.equals("Password Resend on Mobile Number")) 
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Password is sent to your mobile     ");
			    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			    text.setTypeface(typeface);
			    text.setTextColor(Color.WHITE);
			    text.setTextSize(18);
			    Toast toast = new Toast(getApplicationContext());
			    toast.setDuration(Toast.LENGTH_LONG);
			    toast.setView(layout);

			    toast.show();
				
				Intent i = new Intent(ForgetPassword.this,SigninActivity.class);
				i.putExtra("fromLogout","no");
				i.putExtra("fromchange", "yes");
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
			else if (result.equals("Password Resend on EmailID"))
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Password is sent to your email id     ");
			    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			    text.setTypeface(typeface);
			    text.setTextColor(Color.WHITE);
			    text.setTextSize(18);
			    Toast toast = new Toast(getApplicationContext());
			    toast.setDuration(Toast.LENGTH_LONG);
			    toast.setView(layout);

			    toast.show();

				Intent i = new Intent(ForgetPassword.this,SigninActivity.class);
				i.putExtra("fromLogout","no");
				i.putExtra("fromchange", "yes");
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
			else if (result.equals("Contact Support Center"))
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Please Contact Support Center     ");
			    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			    text.setTypeface(typeface);
			    text.setTextColor(Color.WHITE);
			    text.setTextSize(18);
			    Toast toast = new Toast(getApplicationContext());
			    toast.setDuration(Toast.LENGTH_LONG);
			    toast.setView(layout);

			    toast.show();

				Intent i = new Intent(ForgetPassword.this,SigninActivity.class);
				i.putExtra("fromLogout","no");
				i.putExtra("fromchange", "yes");
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
			else if (result.equals("User Not Exist"))
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     User not Exist     ");
			    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			    text.setTypeface(typeface);
			    text.setTextColor(Color.WHITE);
			    text.setTextSize(18);
			    Toast toast = new Toast(getApplicationContext());
			    toast.setDuration(Toast.LENGTH_LONG);
			    toast.setView(layout);

			    toast.show();

				Intent i = new Intent(ForgetPassword.this,SigninActivity.class);
				i.putExtra("fromLogout","no");
				i.putExtra("fromchange", "yes");
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
			else
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Password is not sent     ");
			    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			    text.setTypeface(typeface);
			    text.setTextColor(Color.WHITE);
			    text.setTextSize(18);
			    Toast toast = new Toast(getApplicationContext());
			    toast.setDuration(Toast.LENGTH_LONG);
			    toast.setView(layout);

			    toast.show();
			}
		}
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

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();

		Intent i = new Intent(ForgetPassword.this,SigninActivity.class);
		i.putExtra("fromLogout","yes");
		i.putExtra("fromchange", "no");
		/*i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
		startActivity(i);
		//finish();
	}
}
