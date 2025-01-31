package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientDetailsShowOn extends BaseActivityPhlebotomy {

    Activity context;
  //  DataBaseHelper myDbHelper;
   Integer  TestRegistraionId;

    static String strDate = "";
    static String status = "";

    static String usertype = "";
    static String pid = null;
    static String selected_labidfrompref = null;

    private ProgressDialog pDialog;

    public static final String MyPREFERENCES = "MyPrefs";
    static SharedPreferences sharedpreferences;

    private String url = "https://www.elabassist.com/Services/Test_RegnService.svc/UpdateTestRegnState";


    private ImageButton backButn,Mapbtn,callBtn;

    TextView patientname,mobilenumber,patientadresa,collectioncentername,testname,testcurrentstatus,testnextstatus,
                TotalAmount,AmountPaid,PenddingAmount;
    LinearLayout LLtestNextState;
    String originalpatientname, originalmobilenumber, originalpatientadresa, originalcollectioncentername,
            originaltestname, originaltestcurrentstatus;
    static String RegistrationId = "";
    static Integer currentState = 0;
    String  patientMbile , patientAdrs , SelectedTest , CollectionCenterName , TestRegistraionIdString ,
            PatientName , DoctorName , LabCode , PendingBlnc , totalAmount ,  amountPaid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // setContentView(R.layout.activity_show_patient_details);
        getLayoutInflater().inflate(R.layout.activity_show_patient_details_on, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

       // myDbHelper = new DataBaseHelper(this.getApplicationContext());
//        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
        pid = sharedpreferences.getString("patientId", "");

        usertype = sharedpreferences.getString("usertype","");


       backButn = (ImageButton) findViewById(R.id.id_backBtn);
        Mapbtn = (ImageButton) findViewById(R.id.imgMapLoction);
        callBtn = (ImageButton) findViewById(R.id.imgCallbtnTwo);

        patientname = (TextView) findViewById(R.id.id_patientName);
        mobilenumber = (TextView) findViewById(R.id.id_patientNameMobileNumber);
        patientadresa = (TextView) findViewById(R.id.id_patientAddrs);
        collectioncentername = (TextView) findViewById(R.id.id_collectionCentername);
        testname = (TextView) findViewById(R.id.id_TestName);
        TotalAmount = (TextView) findViewById(R.id.id_TotalAmount);
        AmountPaid = (TextView) findViewById(R.id.id_AmountPaid);
        PenddingAmount = (TextView) findViewById(R.id.id_penddingAmount);
        testcurrentstatus = (TextView) findViewById(R.id.id_TestCurrentState);
        testnextstatus = (TextView) findViewById(R.id.id_TestNextState);
        LLtestNextState  = (LinearLayout) findViewById(R.id.ll_TestNextState);

        // feboname = sharedpreferences.getString("FeboPatientNameTopass", "");

       Bundle b = getIntent().getExtras();

        RegistrationId = b.getString("testRegistrationID", "");
           PatientName = b.getString("PatientName", "");
          DoctorName = b.getString("DoctorName", "");
          LabCode = b.getString("LabCode", "");
          PendingBlnc = b.getString("PendingBalnc", "");
         originaltestcurrentstatus = b.getString("State", "");
        currentState= Integer.valueOf(b.getString("State", ""));
          patientMbile = b.getString("patientMbile", "");
          patientAdrs = b.getString("patientAdrs", "");
          SelectedTest = b.getString("SelectedTest", "");
         CollectionCenterName = b.getString("CollectionCenterName", "");

        totalAmount = b.getString("TotalAmount", "");
        amountPaid = b.getString("AmountPaid", "");

        TestRegistraionId = Integer.valueOf(RegistrationId);
        patientname.setText(PatientName);
        mobilenumber.setText(patientMbile);
        patientadresa.setText(patientAdrs);
        collectioncentername.setText(CollectionCenterName);
        testname.setText(SelectedTest);

        TotalAmount.setText(totalAmount);
        AmountPaid.setText(amountPaid);
        PenddingAmount.setText(PendingBlnc);


        LLtestNextState.setVisibility(View.GONE);
        if( ( originaltestcurrentstatus.equalsIgnoreCase("1") ) ||
                originaltestcurrentstatus.equalsIgnoreCase("2"))
        {
            LLtestNextState.setVisibility(View.VISIBLE);
            testcurrentstatus.setText("Registration");
            testnextstatus.setText("Sample Collection");
            testcurrentstatus.setTextColor(Color.parseColor("#ff4d4d"));
        }
        else if(originaltestcurrentstatus.equalsIgnoreCase("3"))
        {
            LLtestNextState.setVisibility(View.VISIBLE);
            testcurrentstatus.setText("Sample Collected");
            testnextstatus.setText("Sample Deliver");
            testcurrentstatus.setTextColor(Color.parseColor("#ff4d4d"));
        }
        else if(originaltestcurrentstatus.equalsIgnoreCase("4"))
        {
            testcurrentstatus.setText("Sample Delivered");
            testcurrentstatus.setTextColor(Color.parseColor("#ff4d4d"));
        }
        else if ((originaltestcurrentstatus.equalsIgnoreCase("5")) ||
                (originaltestcurrentstatus.equalsIgnoreCase("6")))
        {
            testcurrentstatus.setText("Accession");
            testcurrentstatus.setTextColor(Color.parseColor("#ff4d4d"));
        }
        else if (originaltestcurrentstatus.equalsIgnoreCase("7"))
        {
            testcurrentstatus.setText("Result");
            testcurrentstatus.setTextColor(Color.parseColor("#4d94ff"));
        }
        else if( originaltestcurrentstatus.equalsIgnoreCase("8"))
        {
            testcurrentstatus.setText("Technician Approved ");
            testcurrentstatus.setTextColor(Color.parseColor("#4d94ff"));
        }
        else if( originaltestcurrentstatus.equalsIgnoreCase("9"))
        {
            testcurrentstatus.setText("Pathology Approved");
            testcurrentstatus.setTextColor(Color.parseColor("#4d94ff"));
        }
        else if (originaltestcurrentstatus.equalsIgnoreCase("10") ||
                originaltestcurrentstatus.equalsIgnoreCase("11"))
        {
            testcurrentstatus.setText("Release");
            testcurrentstatus.setTextColor(Color.parseColor("#6cb13c"));
        }
        else {
            testcurrentstatus.setText("New");
            testcurrentstatus.setTextColor(Color.parseColor("#ff4d4d"));
        }
        testcurrentstatus.setTypeface(null, Typeface.BOLD_ITALIC);


        if( ( originaltestcurrentstatus.equalsIgnoreCase("1") ) ||
                originaltestcurrentstatus.equalsIgnoreCase("2")  ||
                originaltestcurrentstatus.equalsIgnoreCase("3"))
        {
            LLtestNextState.setVisibility(View.VISIBLE);
           // testcurrentstatus.setText("Registration");
            testnextstatus.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // Toast.makeText(PatientDetailsShowOff.this, originaltestcurrentstatus, Toast.LENGTH_SHORT).show();

                            new StatusCheck().execute(new String[] { url });

/*
                            Boolean CurrentStateUpdate = myDbHelper.UpdateTestState(TestRegistraionId);
                           if(CurrentStateUpdate == true){

                               Calendar c = Calendar.getInstance();
                               SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                               strDate = sdf.format(c.getTime());
                               if( ( originaltestcurrentstatus.equalsIgnoreCase("1") ) ||
                                       originaltestcurrentstatus.equalsIgnoreCase("2"))
                               {
                                   showAlert( "Sample Collected From!\n\n \" "+originalpatientname+ " \"\n\nat " + strDate);
                               }
                               else {
                                   //  Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
                                   showAlert("Sample Delivered To!\n\n \" " +originalcollectioncentername+ " \"\n\nat " + strDate);
                               }
                           }
                            else
                           {
                               showAlert("Failed to Load the Data ");
                           }
*/
                        }
                    }
            );

        }



        backButn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent iintent = new Intent(PatientDetailsShowOn.this, PhlebotomyTabOn.class);
                        // Intent iintent = new Intent(MassagList.this, AllTestDataPhlebo.class);
                        startActivity(iintent);
                        //finish();
                    }
                }
        );


        callBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppConfig.call_number));
                        //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppConfig.call_number));
                        //startActivity(intent);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                       callIntent.setData(Uri.parse("tel:"+patientMbile));
                        startActivity(callIntent);

                    }
                }
        );


        Mapbtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//  18.528845, 73.874412
                        String uri = "http://maps.google.com/maps?daddr=" + 18.528846 + "," + 73.874418 + " (" + originalpatientname + ")";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        try
                        {
                            startActivity(intent);
                        }
                        catch(ActivityNotFoundException ex)
                        {
                            try
                            {
                                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(unrestrictedIntent);
                            }
                            catch(ActivityNotFoundException innerEx)
                            {
                                Toast.makeText(PatientDetailsShowOn.this, "Please install a maps application", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }
        );
    }

    private class StatusCheck extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialog = new ProgressDialog(PatientDetailsShowOn.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
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

            if (pDialog != null && pDialog.isShowing())
            {
                pDialog.dismiss();
            }

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            strDate = sdf.format(c.getTime());
            if (result.equals("true")) {

                  if (currentState == 1 || currentState == 2) {
                      showAlert("Sample Collected From!\n\n \" " +PatientName+ " \"\n\nat " + strDate);
                }
                else
                {
                    showAlert("Sample Delivered To!\n\n \" " +CollectionCenterName+ " \"\n\nat " + strDate);
                }



            } else {
                showAlert("Failed Please try again ");


            }
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
            nameValuePairs.add(new BasicNameValuePair("\"PreviousActionFID\"", "" + currentState + ""));
            nameValuePairs.add(new BasicNameValuePair("\"ActionBy\"", "\"" + pid + "\""));

            if (currentState == 1 || currentState == 2) {
                nameValuePairs.add(new BasicNameValuePair("\"ActionFID\"", "3"));
            }
            else
            {
                nameValuePairs.add(new BasicNameValuePair("\"ActionFID\"", "4"));
            }
         //    nameValuePairs.add(new BasicNameValuePair("\"ActionFID\"", "3"));


            nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));

            String finalString = nameValuePairs.toString();
            finalString = finalString.replace('=', ':');
            finalString = finalString.substring(1, finalString.length() - 1);
            finalString = "{" + finalString + "}";
            String tempString = "\"objUSLC\"" + ":";
            String strToServer = tempString + finalString;
            String strToServer1 = "{" + strToServer + "}";


            Log.d("strToServer1", strToServer1);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PatientDetailsShowOn.this, PhlebotomyTabOn.class);
        startActivity(i);
        //finish();
    }

    public void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView Mytitle = new TextView(this);
        Mytitle.setText(Html.fromHtml("<font color='#33cc33'>!!Test Status!!</font>"));
        Mytitle.setTextSize(20);
        Mytitle.setPadding(5, 15, 5, 5);
        Mytitle.setGravity(Gravity.CENTER);
        Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf"));
        builder.setCustomTitle(Mytitle);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        Intent i = new Intent(PatientDetailsShowOn.this,PhlebotomyTabOn.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        //finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        TextView textView = (TextView) alert.findViewById(android.R.id.message);
        Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button noButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        textView.setTypeface(custom_font);
        yesButton.setTypeface(custom_bold_font);
        noButton.setTypeface(custom_bold_font);
    }
}
