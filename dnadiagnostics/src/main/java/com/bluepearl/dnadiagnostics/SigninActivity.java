package com.bluepearl.dnadiagnostics;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SigninActivity extends Activity {


    TextView forget_password, termscondition;
    Button btnlogin, signupbtn;
    String data = "";
    String changestr = null;
    EditText mobile, password;
    AlertDialog alertbox;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    private CheckBox saveLoginCheckBox;
    private ProgressDialog pDialog;
    //	GoogleCloudMessaging gcm;
    ShareExternalServer appUtil;
    static String regId;
    AsyncTask<Void, Void, String> shareRegidTask;
    Context context;
    static String num_value = null;
    static String password_value = null;
    static String FirebaseToken = null;
    static String preferred_id = null;
    static String preferreflabid = null;
    static String preferredlabname = null;
    static String resadd = "";
    static String offadd = "";
    static String status = "";
    static String Code = "";
    static String patient_id = "";
    static String doctor_id = "";
    static String labuser_id = "";
    static String mobile_number = "";
    static String user_type = "";
    static String user_type_frmpref = "";
    static String patientname = "";
    private Boolean saveLogin;
    String chkboxval = "";
    String phno = "";
    String pass = "";

    private String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";

    private static final String APP_VERSION = "appVersion";
    static final String TAG = "Register Activity";
    public static final String REG_ID = "regId";


    private static final String collection_center_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";
    private static String collection_center_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";

    private static final String Affiliation_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";
    private static String Affiliation_myurlll = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";


    JSONArray Centername = null;

    int setCCSizee, setAfilSizee;
    Set<String> setallCollectionCenterNameSet = new HashSet<String>();
    Set<String> setallaffiliationNameSet = new HashSet<String>();
    ArrayList<String> center_nameId_all = new ArrayList<String>();
    ArrayList<String> centerid_all = new ArrayList<String>();

    ArrayList<String> affiliation_nameId_all = new ArrayList<String>();
    ArrayList<String> affiliation_all = new ArrayList<String>();

    JSONArray affiliationnamee = null;
    Set<String> allaffiliationNameSet;
    Set<String> allCollectionCenterNameSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_signin);


        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        preferred_id = sharedpreferences.getString("SelectedLabId", "");
        chkboxval = sharedpreferences.getString("checkboxchecked", "");
        phno = sharedpreferences.getString("phno", "");
        pass = sharedpreferences.getString("password", "");
        user_type_frmpref = sharedpreferences.getString("usertype", "");
        FirebaseToken =sharedpreferences.getString("FirebaseToken", "");

        forget_password = (TextView) findViewById(R.id.txtForgetPassword);
        termscondition = (TextView) findViewById(R.id.termscondition);
        mobile = (EditText) findViewById(R.id.etmobilenumber);
        password = (EditText) findViewById(R.id.etpassword);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        btnlogin = (Button) findViewById(R.id.LoginButton);
        signupbtn = (Button) findViewById(R.id.SignUpButton);

        mobile.setTypeface(custom_font);
        password.setTypeface(custom_font);
        forget_password.setTypeface(custom_btnfont);
        termscondition.setTypeface(custom_btnfont);
        saveLoginCheckBox.setTypeface(custom_btnfont);
        btnlogin.setTypeface(custom_btnfont);
        signupbtn.setTypeface(custom_btnfont);

        termscondition.setClickable(true);
        termscondition.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://www.elabassist.com/Account/TermsAndConditions.aspx'> By Signing in you are agree to our Terms & Conditions</a>";
        termscondition.setText(Html.fromHtml(text));

        allaffiliationNameSet = new HashSet<String>();
        allCollectionCenterNameSet = new HashSet<String>();

        subscribeToFCMPushService();

        Intent myIntent = getIntent();
        if (myIntent != null) {
            data = myIntent.getStringExtra("fromLogout");
            changestr = myIntent.getStringExtra("fromchange");
        } else {
            data = "no";
            changestr = "yes";
        }
        if (data == (null) || data.equals("null")) {
            data = "no";
            changestr = "yes";
        }

        if (!(data.equals("yes"))) {
            if (chkboxval.equals("yes")) {
                if (phno != null && !(phno.equalsIgnoreCase(""))) {
                    mobile.setText(phno);
                }
                if (pass != null && !(pass.equalsIgnoreCase(""))) {
                    if (changestr.equals("yes")) {
                        password.setText("");
                    } else {
                        password.setText(pass);
                    }
                }
                saveLoginCheckBox.setChecked(true);

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putBoolean("loginchkbox", true);

                editor.commit();

                if (user_type_frmpref.equals("Patient")) {
                    if (preferred_id != null && !(preferred_id.equalsIgnoreCase("")) && !(preferred_id.equals("00000000-0000-0000-0000-000000000000"))) {
                        Intent i = new Intent(SigninActivity.this, AddressSelection.class);
                    /*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
                        startActivity(i);
                        //finish();
                    } else {
                        Intent i = new Intent(SigninActivity.this, LabSelection.class);
                    /*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
                        startActivity(i);
                        //finish();
                    }
                } else if (user_type_frmpref.equals("Doctor") ||
                        user_type_frmpref.equals("Lab User") ||
                        user_type_frmpref.equals("Sr. Lab User") ||
                        user_type_frmpref.equals("Jr. Lab User") ||
                        user_type_frmpref.equals("Admin") ||
                        user_type_frmpref.equals("Pathologist") ||
                        user_type_frmpref.equals("Pathologist_T2") ||
                        user_type_frmpref.equals("Pathologist_T3") ||
                        user_type_frmpref.equals("LabUser_T2") ||
                        user_type_frmpref.equals("AffiliationUser") ||
                        user_type_frmpref.equals("Collection Boy") ||
                        user_type_frmpref.equals("Collection Center Admin") ||
                        user_type_frmpref.equals("Collection Center User") ||
                        user_type_frmpref.equals("Collection Center Lab User")
                ) {
                    setallCollectionCenterNameSet = sharedpreferences.getStringSet("setallCollectionCenterNameSet", null);
                    setallaffiliationNameSet = sharedpreferences.getStringSet("setallaffiliationNameSet", null);


                    if (setallCollectionCenterNameSet != null) {
                        setCCSizee = setallCollectionCenterNameSet.size();
                    } else {
                        setCCSizee = 0;
                    }

                    if (setallaffiliationNameSet != null) {
                        setAfilSizee = setallaffiliationNameSet.size();
                    } else {
                        setAfilSizee = 0;
                    }


                    String disid = preferred_id;

                    collection_center_myurl = collection_center_url;
                    Affiliation_myurlll = Affiliation_url;
                    collection_center_myurl = collection_center_myurl + disid;
                    Affiliation_myurlll = Affiliation_myurlll + disid;

                    new GetCollectionCenterList().execute();
                    new GetAffiliationList().execute();


                    if (user_type_frmpref.equals("Admin")) {

                        Intent i1 = new Intent(this, Dashboard.class);
                        //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i1);

                    } else if (user_type_frmpref.equals("Collection Boy")) {
                        Intent i1 = new Intent(this, PhlebotomyTabOn.class);
                        //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i1);
                    } else {
                        Intent i1 = new Intent(this, RegisterPatientNew.class);
                        //	 i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i1);
                    }


                /*Intent i = new Intent(SigninActivity.this,LabSelectionForDoctor.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //finish();*/
                } else {
                    Intent i = new Intent(SigninActivity.this, SigninActivity.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    //finish();
                }
            } else {
                if (phno != null && !(phno.equalsIgnoreCase(""))) {
                    mobile.setText(phno);
                } else {
                    mobile.setText("");
                }
                if (pass != null && !(pass.equalsIgnoreCase(""))) {
                    if (changestr.equals("yes")) {
                        password.setText("");
                    } else {
                        password.setText(pass);
                    }
                } else {
                    password.setText("");
                }
                //saveLoginCheckBox.setChecked(false);
                saveLoginCheckBox.setChecked(true);
            }
        } else {
            if (phno != null && !(phno.equalsIgnoreCase(""))) {
                mobile.setText(phno);
            } else {
                mobile.setText("");
            }
            if (pass != null && !(pass.equalsIgnoreCase(""))) {
                if (changestr.equals("yes")) {
                    password.setText("");
                } else {
                    password.setText(pass);
                }
            } else {
                password.setText("");
            }
            //saveLoginCheckBox.setChecked(false);
            saveLoginCheckBox.setChecked(true);
        }

        btnlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isInternetOn(getApplicationContext())) {
                    num_value = mobile.getText().toString();
                    password_value = password.getText().toString();


                    if ((num_value).equalsIgnoreCase("")) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.my_dialog, null);

                        TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                        text.setText("     Please enter user name     ");
                        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                        text.setTypeface(typeface);
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(18);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);

                        toast.show();

                    } else if ((password_value).equalsIgnoreCase("")) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.my_dialog, null);

                        TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                        text.setText("     Please enter the Password     ");
                        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                        text.setTypeface(typeface);
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(18);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);

                        toast.show();
                    } else {
                        new LoginCheck().execute(new String[]{url});
                    }
                } else {
                    alertBox();
                }
            }
        });


        forget_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(SigninActivity.this, ForgetPassword.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //finish();
            }
        });

        signupbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(SigninActivity.this, SignupActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //finish();
            }
        });

    }

    private void alertBox() {
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
        //alert.show();

        AlertDialog alert1 = alert.create();
        alert1.show();

        TextView textView = (TextView) alert1.findViewById(android.R.id.message);
        Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        textView.setTypeface(custom_font);
        yesButton.setTypeface(custom_bold_font);
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    private class LoginCheck extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog
            pDialog = new ProgressDialog(SigninActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return LoginPOST(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (result.equals("Success")) {
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("patientId", patient_id);
                editor.putString("phno", num_value);
                editor.putString("password", password_value);

                if (saveLoginCheckBox.isChecked()) {
                    editor.putString("checkboxchecked", "yes");
                } else {
                    editor.putString("checkboxchecked", "no");
                }
                editor.putString("usertype", user_type);
                editor.putString("multiusertype", user_type);
                if (patientname != null && !(patientname.equalsIgnoreCase(""))) {

                } else {
                    patientname = "SELF";
                }
                editor.putString("SelectedPatientName", patientname);
                editor.putString("MainPatientName", patientname);
                editor.putString("LabIdFromLogin", preferreflabid);
                editor.putString("SelectedLabId", preferred_id);
                editor.putString("SelectedLabName", preferredlabname);

                editor.putString("self", "yes");
                editor.apply();

                if (user_type.equals("Patient")) {
                    if (preferred_id != null && !(preferred_id.equalsIgnoreCase("")) && !(preferred_id.equals("00000000-0000-0000-0000-000000000000"))) {
                        Intent i = new Intent(getApplicationContext(), AddressSelection.class);
                        Log.d("Patient","AddressSelection");
						/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
                        startActivity(i);
                        //finish();
                    } else {
                        Intent i = new Intent(SigninActivity.this, LabSelection.class);
                        Log.d("Patient","LabSelection");

						/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
                        startActivity(i);
                        //finish();
                    }
                } else if (user_type.equals("Doctor") ||
                        user_type.equals("AffiliationUser") ||
                        user_type.equals("Lab User") ||
                        user_type.equals("Sr. Lab User") ||
                        user_type.equals("Jr. Lab User") ||
                        user_type.equals("Pathologist") ||
                        user_type.equals("Pathologist_T2") ||
                        user_type.equals("Pathologist_T3") ||
                        user_type.equals("LabUser_T2") ||
                        user_type.equals("Collection Center User") ||
                        user_type.equals("Collection Center Lab User") ||
                        user_type.equals("Collection Center Admin") ||
                        user_type.equals("Collection Boy") ||
                        user_type.equals("Admin"))

                {

                    Toast.makeText(SigninActivity.this, "Please wait....", Toast.LENGTH_LONG).show();

                    Intent i1 = new Intent(SigninActivity.this, LabSelectionForDoctor.class);
                    //	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i1);
                    //	finish();
                } else {
                    Toast.makeText(SigninActivity.this, "unauthorised user....", Toast.LENGTH_LONG).show();
                }
				/*else if(user_type.equals("Lab User") || user_type.equals("LabUser_T2"))
				{
					Intent i = new Intent(SigninActivity.this,LabSelectionForDoctor.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}*/
				/*else if(user_type.equals("AffiliationUser")) 
				{
					Intent i = new Intent(SigninActivity.this,LabSelectionForDoctor.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_0TOP);
					startActivity(i);
					finish();
				}*/
            } else {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.my_dialog, null);

                TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                text.setText("     Login failed please enter correct username and password.     ");
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
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

    public static String LoginPOST(String url) {
        Log.e("LoginPOST","insideLoginPost");
        InputStream inputStream = null;
        String result = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            Log.e("httpPost","httpPost");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"3\""));
            nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\"" + num_value + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"Password\"", "\"" + password_value + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"MobileDeviceID\"", "\"" + FirebaseToken + "\""));

            String finalString = nameValuePairs.toString();
            finalString = finalString.replace('=', ':');
            finalString = finalString.substring(1, finalString.length() - 1);
            finalString = "{" + finalString + "}";
            String tempString = "\"objSP\"" + ":";
            String strToServer = tempString + finalString;
            String strToServer1 = "{" + strToServer + "}";

            StringEntity se = new StringEntity(strToServer1);
            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httpPost.setEntity(se);

            // Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.e("httpResponse","httpResponse");

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                String jsonformattString = result.replaceAll("\\\\", "");
                try {
                    JSONObject jsonObj = new JSONObject(jsonformattString);
                    JSONObject resultObj = (JSONObject) jsonObj.get("d");

                    status = resultObj.getString("Result");
                    //Code = resultObj.getString("Data");
                    patient_id = resultObj.getString("UserFID");
                    user_type = resultObj.getString("Role");
                    patientname = resultObj.getString("ShortName");
                    preferreflabid = resultObj.getString("PreferredLabID");
                    preferredlabname = resultObj.getString("PreferredLabName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                status = "Did not work!";
        } catch (Exception e) {
            Log.i("InputStream", e.getLocalizedMessage());
        }

        return status;
    }

    private boolean isValidMobile(String phone2) {
        boolean check;
        if (phone2.length() < 10 || phone2.length() > 10) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(SigninActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        // int appVersion = getAppVersion(context);
        // Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        // editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        alertbox = new Builder(this)
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {

                                // Constant.SIGNIN_STATUS = false;
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
                }).show();


        TextView textView = (TextView) alertbox.findViewById(android.R.id.message);
        Button yesButton = alertbox.getButton(DialogInterface.BUTTON_POSITIVE);
        Button noButton = alertbox.getButton(DialogInterface.BUTTON_NEGATIVE);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        textView.setTypeface(custom_font);
        yesButton.setTypeface(custom_bold_font);
        noButton.setTypeface(custom_bold_font);
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


                    if (ServiceLenghCcSize == setCCSizee) {


                    } else {


                        center_nameId_all.clear();
                        centerid_all.clear();
                        allCollectionCenterNameSet.clear();


                        // looping through All Contacts
                        for (int i = 0; i < Centername.length(); i++) {
                            // String listd = new String();
                            JSONObject c = Centername.getJSONObject(i);


                            String collectionCebterName = c.getString("Name");
                            String center_id = c.getString("CollectionCenterID");

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

            collection_center_myurl = collection_center_url;


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


                    if (ServiceLenghAfilSize == setAfilSizee) {


                    } else {


                        affiliation_nameId_all.clear();
                        affiliation_all.clear();
                        allaffiliationNameSet.clear();

                        // looping through All Contacts
                        for (int i = 0; i < affiliationnamee.length(); i++) {
                            // String listd = new String();
                            JSONObject c = affiliationnamee.getJSONObject(i);


                            String AffiliationName = c.getString("AffiliationCompanyName");
                            String Affiliation_id = c.getString("AffiliationID");

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


            Affiliation_myurlll = Affiliation_url;


        }
    }

    private void subscribeToFCMPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("Public");
        String token = FirebaseInstanceId.getInstance().getToken();
        //fcmtoken.setText(token);
        System.out.println("ststtt  tokn "+token);
        FirebaseMessaging.getInstance().subscribeToTopic("Public");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("FirebaseToken", token);;
        editor.commit();



    }


}
