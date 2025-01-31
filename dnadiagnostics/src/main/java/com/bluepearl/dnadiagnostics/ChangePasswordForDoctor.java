package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChangePasswordForDoctor extends BaseActivityDL
{
	EditText old_pass,new_pass,re_pass;
	Button btnok;

	static String status = "";
	private ProgressDialog pDialog;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	static String oldpass_value = null;
	static String newpass_value = null;
	static String repass_value = null;
	static String pid = null;
	static String user_type_frmpref = null;

	private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");
		user_type_frmpref = sharedpreferences.getString("usertype", "");
		
		getLayoutInflater().inflate(R.layout.activity_change_password, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));     
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		old_pass = (EditText)findViewById(R.id.oldpassword);
		new_pass = (EditText)findViewById(R.id.newpassword);
		re_pass = (EditText)findViewById(R.id.confirmpassword);
		btnok = (Button)findViewById(R.id.OkButton);

		old_pass.setTypeface(custom_font);
		new_pass.setTypeface(custom_font);
		re_pass.setTypeface(custom_font);
		btnok.setTypeface(custom_btnfont);

		btnok.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				oldpass_value = old_pass.getText().toString();
				newpass_value = new_pass.getText().toString();
				repass_value = re_pass.getText().toString();

				if (isInternetOn(getApplicationContext())) 
				{
					if((oldpass_value).equalsIgnoreCase(""))
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter the new password     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
					}
					else if((newpass_value).equalsIgnoreCase(""))
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter the new password     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
					}
					else if((repass_value).equalsIgnoreCase(""))
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please re-enter your new password     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
					}
					else if(newpass_value.equals(repass_value))
					{
						new ChangePasswordCheck().execute(new String[] { url });
					}
					else
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     The password does not match     ");
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
				else
				{
					alertBox();
				}
			}
		});
	}

	public static String ChangePOST(String url) 
	{
		InputStream inputStream = null;
		String result = "";

		try 
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"5\""));  
			nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"OldPassword\"", "\""+ oldpass_value + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Password\"", "\""+ newpass_value + "\""));

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

	private class ChangePasswordCheck extends AsyncTask<String, Void, String> 
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();

			// Showing progress dialog
			pDialog = new ProgressDialog(ChangePasswordForDoctor.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) 
		{
			return ChangePOST(params[0]);
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
			if (result.equals("Success")) 
			{
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("checkboxchecked", "no");
				editor.commit();

				
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Password updated successfully     ");
			    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			    text.setTypeface(typeface);
			    text.setTextColor(Color.WHITE);
			    text.setTextSize(18);
			    Toast toast = new Toast(getApplicationContext());
			    toast.setDuration(Toast.LENGTH_LONG);
			    toast.setView(layout);

			    toast.show();
				
				Intent i = new Intent(ChangePasswordForDoctor.this,SigninActivity.class);
				i.putExtra("fromLogout","no");
				i.putExtra("fromchange", "yes");
				/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
				startActivity(i);
				//finish();
			} 
			else if (result.equals("Old Password Not Matched")) 
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Old Password Not Matched     ");
			    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			    text.setTypeface(typeface);
			    text.setTextColor(Color.WHITE);
			    text.setTextSize(18);
			    Toast toast = new Toast(getApplicationContext());
			    toast.setDuration(Toast.LENGTH_LONG);
			    toast.setView(layout);

			    toast.show();
			    
				Intent i = new Intent(ChangePasswordForDoctor.this,ChangePasswordForDoctor.class);
				i.putExtra("fromLogout","no");
				/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
				startActivity(i);
				//finish();
			}
			else 
			{
				LayoutInflater inflater = getLayoutInflater();
			    View layout = inflater.inflate(R.layout.my_dialog,null);

			    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			    text.setText("     Password is not updated     ");
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

	private static String convertInputStreamToString(InputStream inputStream)throws IOException 
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

	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();

		Intent i = new Intent(ChangePasswordForDoctor.this,LabSelectionForDoctor.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		//finish();
	}
}
