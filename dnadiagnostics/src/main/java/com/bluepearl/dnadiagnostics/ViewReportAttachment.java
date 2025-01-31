package com.bluepearl.dnadiagnostics;

import static com.bluepearl.dnadiagnostics.ViewReportAdmin.MyPREFERENCES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewReportAttachment extends AppCompatActivity {

    private final int PERMISSION_REQUEST_CODE = 205;
    private final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    Activity context;
    TextView attachment1, attachment2, attachment3,extraattachment,testdetails;
    CheckBox showheader;
    CheckBox showfooter;
    CheckBox attach1check;
    CheckBox attach2check;
    CheckBox attach3check;
    Button viewattachment1,viewattachment2,viewattachment3,btndownload;

    ImageView shareicon, whatsappicon;

    ImageButton btnclose;


    //To get test details
    private String gettestdetails = "https://www.elabassist.com/Services/TestService.svc/GetTestTransDetailsByTestRegnIDMobApi?TestRegnID=";
    SharedPreferences sharedpreferences;
    String testRegistrationID = "";
    String patient = "";
    String affiliation = "";
    String collecetioncenter = "";
    String doctor = "";
    String userRole = null;
    String selected_labidfrompref = null;
    String  userfid = null;
    private ProgressDialog pDialog;
    ArrayAdapter<String> arrayAdapter;

    // Create the JSON array for RegnTestPrint
    JSONArray regnTestPrintArray = new JSONArray();
    List<String> displayNames = new ArrayList<>();
    String attachmentone = "N/A";
    String attachmenttwo = "N/A";
     String attachmentthree = "N/A";

    String attachmentOne = "";
    String attachmentTwo = "";
    String attachmentThree = "";
    ListView listViewTestList;

    //To download test report
    private String getPdfUrl = "https://www.elabassist.com/Services/Test_RegnService.svc/MobReleaseRegistrationReport";
    URL downloadUrl = null;
    String pdfFileName = "";
    boolean ShowHeader = true;
    boolean ShowFooter = true;

    boolean isShare = false;

    boolean isWhatsappShare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_attachment);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("eLAB Assist");
        toolbar.setTitleTextColor(getResources().getColor(R.color.WITHE));
        Intent intent = getIntent();
        testRegistrationID = intent.getStringExtra("testRegistrationID");
        patient = intent.getStringExtra("PMobile");
        affiliation = intent.getStringExtra("AMobile");
        collecetioncenter = intent.getStringExtra("CCMobile");
        doctor = intent.getStringExtra("DMobile");

        Log.d("testRegistrationID",""+testRegistrationID);

        userfid = sharedpreferences.getString("patientId", "");
        selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
        userRole= sharedpreferences.getString("usertype", "");

        listViewTestList = findViewById(R.id.listview);
        attachment1 = findViewById(R.id.attachment1);
        attachment2 = findViewById(R.id.attachment2);
        attachment3 = findViewById(R.id.attachment3);
        testdetails = findViewById(R.id.testdetails);
        extraattachment = findViewById(R.id.textView12);
        showheader = findViewById(R.id.checkheader);
        showfooter = findViewById(R.id.checkfooter);
        attach1check = findViewById(R.id.attachone);
        attach2check = findViewById(R.id.attachtwocheck);
        attach3check = findViewById(R.id.attachthreecheck);
        viewattachment1 = findViewById(R.id.button1);
        viewattachment2 = findViewById(R.id.button2);
        viewattachment3 = findViewById(R.id.button3);
        shareicon = findViewById(R.id.shareicon);
        whatsappicon = findViewById(R.id.whatsappicon);

        btndownload = findViewById(R.id.btndownload);
        btnclose = findViewById(R.id.btnclose);

        viewattachment1.setTypeface(custom_font);
        viewattachment2.setTypeface(custom_font);
        viewattachment3.setTypeface(custom_font);
        testdetails.setTypeface(custom_font);
        extraattachment.setTypeface(custom_font);
        showheader.setTypeface(custom_font);
        showfooter.setTypeface(custom_font);
        btndownload.setTypeface(custom_font);
        attachment1.setTypeface(custom_font);
        attachment2.setTypeface(custom_font);
        attachment3.setTypeface(custom_font);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,displayNames);
        listViewTestList.setAdapter(arrayAdapter);
        attachment1.setText(attachmentone);
        attachment2.setText(attachmenttwo);
        attachment3.setText(attachmentthree);
        attach1check.setEnabled(false);
        attach2check.setEnabled(false);
        attach3check.setEnabled(false);
        Log.d("usertype",""+userRole);
        if(userRole.equals("Patient")  || userRole.equals("Collection Boy")){
            whatsappicon.setVisibility(View.GONE);
            showheader.setVisibility(View.GONE);
            showfooter.setVisibility(View.GONE);
        }
        new GetTestDetails().execute(new String[]{gettestdetails});

        if (!checkPermission()) {
            requestPermission();
        }

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attachmentone = "N/A";
                attachmenttwo = "N/A";
                attachmentthree = "N/A";
                pdfFileName = "";
                finish();
            }
        });

        //downlaod attachment1
        viewattachment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(attachmentone.equalsIgnoreCase("N/A") && !(attachmentthree.equalsIgnoreCase("")))){
                    //new DownloadFile(ViewReportAttachment.this,"https://www.elabassist.com/"+attachmentone).execute();
                    pDialog = new ProgressDialog(ViewReportAttachment.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    DownloadReport downloadReport = new DownloadReport();
                    downloadReport.downloadPdf(ViewReportAttachment.this,1,"https://www.elabassist.com/"+attachmentone,"","","","");
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }
        });
        //downlaod attachment2
        viewattachment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(attachmenttwo.equalsIgnoreCase("N/A") && !(attachmentthree.equalsIgnoreCase("")))){
                    //new DownloadFile(ViewReportAttachment.this,"https://www.elabassist.com/"+attachmenttwo).execute();
                    pDialog = new ProgressDialog(ViewReportAttachment.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    DownloadReport downloadReport = new DownloadReport();
                    downloadReport.downloadPdf(ViewReportAttachment.this,1,"https://www.elabassist.com/"+attachmenttwo,"","","","");
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }
        });
        //downlaod attachment3
        viewattachment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(attachmentthree.equalsIgnoreCase("N/A") && !(attachmentthree.equalsIgnoreCase("")))){
                    //new DownloadFile(ViewReportAttachment.this,"https://www.elabassist.com/"+attachmentthree).execute()
                    pDialog = new ProgressDialog(ViewReportAttachment.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    DownloadReport downloadReport = new DownloadReport();
                    downloadReport.downloadPdf(ViewReportAttachment.this,1,"https://www.elabassist.com/"+attachmentthree,"","","","");
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }
        });

        //download report
        btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfFileName = "";
                attachmentOne = "";
                attachmentTwo = "";
                attachmentThree = "";
                if(showheader.isChecked()){
                    ShowHeader = true;
                }else{
                    ShowHeader = false;
                }

                if(showfooter.isChecked()){
                    ShowFooter = true;
                }else{
                    ShowFooter = false;
                }

                if(userRole.equals("Patient") || userRole.equals("Collection Boy")){
                    ShowHeader = true;
                    ShowFooter = true;
                }

                if(attach1check.isChecked()){
                    attachmentOne = attachmentone;
                }

                if(attach2check.isChecked()){
                    attachmentTwo = attachmenttwo;
                }

                if(attach3check.isChecked()){
                    attachmentThree = attachmentthree;
                }

                Log.d("ShowHeader",""+ShowHeader);
                new GetPatientReport().execute(new String[]{getPdfUrl});
            }
        });

        shareicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShare = true;
                pdfFileName = "";
                attachmentOne = "";
                attachmentTwo = "";
                attachmentThree = "";
                if(showheader.isChecked()){
                    ShowHeader = true;
                }else{
                    ShowHeader = false;
                }

                if(showfooter.isChecked()){
                    ShowFooter = true;
                }else{
                    ShowFooter = false;
                }

                if(userRole.equals("Patient") || userRole.equals("Collection Boy")){
                    ShowHeader = true;
                    ShowFooter = true;
                }

                if(attach1check.isChecked()){
                    attachmentOne = attachmentone;
                }

                if(attach2check.isChecked()){
                    attachmentTwo = attachmenttwo;
                }

                if(attach3check.isChecked()){
                    attachmentThree = attachmentthree;
                }

                Log.d("ShowHeader",""+ShowHeader);
                new GetPatientReport().execute(new String[]{getPdfUrl});
            }
        });

        whatsappicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWhatsappShare = true;
                pdfFileName = "";
                attachmentOne = "";
                attachmentTwo = "";
                attachmentThree = "";
                if(showheader.isChecked()){
                    ShowHeader = true;
                }else{
                    ShowHeader = false;
                }

                if(showfooter.isChecked()){
                    ShowFooter = true;
                }else{
                    ShowFooter = false;
                }

                if(attach1check.isChecked()){
                    attachmentOne = attachmentone;
                }

                if(attach2check.isChecked()){
                    attachmentTwo = attachmenttwo;
                }

                if(attach3check.isChecked()){
                    attachmentThree = attachmentthree;
                }

                Log.d("ShowHeader",""+ShowHeader);
                new GetPatientReport().execute(new String[]{getPdfUrl});
            }
        });

    }

    //Get Report Details
    private class GetTestDetails extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog
            pDialog = new ProgressDialog(ViewReportAttachment.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return PatientPOST(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            attachment1.setText(attachmentone);
            attachment2.setText(attachmenttwo);
            attachment3.setText(attachmentthree);

            if(!(attachmentone.equalsIgnoreCase("")) && !(attachmentone.equalsIgnoreCase("N/A"))){
                attach1check.setEnabled(true);
                attach1check.setChecked(true);
            }

            if(!(attachmenttwo.equalsIgnoreCase("")) && !(attachmenttwo.equalsIgnoreCase("N/A"))){
                attach2check.setEnabled(true);
                attach2check.setChecked(true);
            }
            Log.d("attachmentthree",""+attachmentthree);
            if(!(attachmentthree.equalsIgnoreCase("")) && !(attachmentthree.equalsIgnoreCase("N/A"))){
                attach3check.setEnabled(true);
                attach3check.setChecked(true);
            }

            arrayAdapter.notifyDataSetChanged();
        }
    }

    //Get test report details
    public String PatientPOST(String url) {
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();
        String getPdfUrl = url+testRegistrationID+"&LabId="+selected_labidfrompref;

        Log.d("getPdfUrl",""+getPdfUrl);
        String jsonStr = sh.makeServiceCall(getPdfUrl, ServiceHandler.GET);

        Log.d("jsonStr",""+jsonStr);
        if (jsonStr != null && jsonStr.length() > 0) {
            Log.d("jsonStr",""+jsonStr);
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                Log.d("jsonArray",""+jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("jsonObject",""+jsonObject);
                    String displayName = jsonObject.getString("DisplayName");
                    displayNames.add(displayName);
                    Log.d("displayName",""+displayName);
                    int workflowCurrentState = jsonObject.getInt("WorkFlowCurrentState");
                    Log.d("WorkFlowCurrentState",""+workflowCurrentState);
                    if (workflowCurrentState >= 6) {
                        JSONObject objParam = new JSONObject();

                        objParam.put("TestRegnTransId", jsonObject.get("TestRegnTransID"));
                        objParam.put("TestID", jsonObject.get("TestID"));
                        objParam.put("IsPrint", true);
                        objParam.put("TestRegnID", jsonObject.get("TestRegnID"));
                        objParam.put("ReportDocID", jsonObject.get("DocID"));
                        objParam.put("PreviousStatus", jsonObject.get("WorkFlowCurrentState"));
                        objParam.put("CurrentStatus", jsonObject.get("WorkFlowCurrentState"));
                        objParam.put("ShowHeaderOnReport", true);
                        objParam.put("ShowFooterOnReport", true);

                        regnTestPrintArray.put(objParam);
                    }
                }
                Log.d("regnTestPrintArray",""+regnTestPrintArray);
                Log.d("displayNames",""+displayNames);

                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                if(!(jsonObject1.getString("PDFFileName1").equalsIgnoreCase(""))){
                    attachmentone = jsonObject1.getString("PDFFileName1").replace("~/", "");
                }
                if(!(jsonObject1.getString("PDFFileName2").equalsIgnoreCase(""))){
                    attachmenttwo = jsonObject1.getString("PDFFileName2").replace("~/", "");
                }
                if(!(jsonObject1.getString("PDFFileName3").equalsIgnoreCase(""))){
                    attachmentthree = jsonObject1.getString("PDFFileName3").replace("~/", "");
                }
                Log.d("attachmentone",""+attachmentone+attachmenttwo+attachmentthree);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return null;
    }

    private class GetPatientReport extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            // Showing progress dialog
            pDialog = new ProgressDialog(ViewReportAttachment.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return ReportPost(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if(pdfFileName != null && !(pdfFileName.equalsIgnoreCase("Null")) && !(pdfFileName.equalsIgnoreCase(""))){
                //new DownloadFile(ViewReportAttachment.this,"https://www.elabassist.com/"+pdfFileName).execute();
                pDialog = new ProgressDialog(ViewReportAttachment.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

                if(isShare){
                    isShare = false;
                    DownloadReport downloadReport = new DownloadReport();
                    downloadReport.downloadPdf(ViewReportAttachment.this,2,"https://www.elabassist.com/"+pdfFileName,"","","","");
                    if (pDialog != null && pDialog.isShowing())
                    {
                        pDialog.dismiss();
                    }
                }else if(isWhatsappShare){
                    isWhatsappShare = false;
                    DownloadReport downloadReport = new DownloadReport();
                    downloadReport.downloadPdf(ViewReportAttachment.this,3,"https://www.elabassist.com/"+pdfFileName,"","","","");
                    if (pDialog != null && pDialog.isShowing())
                    {
                        pDialog.dismiss();
                    }
                }else{
                    DownloadReport downloadReport = new DownloadReport();
                    downloadReport.downloadPdf(ViewReportAttachment.this,1,"https://www.elabassist.com/"+pdfFileName,"","","","");
                    if (pDialog != null && pDialog.isShowing())
                    {
                        pDialog.dismiss();
                    }
                }
            }else{
                showAlert1("Report is not generated yet. Please contact the laboratory for further assistance.");
            }

        }
    }

    //Get pdf report url
    public String ReportPost(String pdfUrl) {
        InputStream inputStream = null;
        String result = "";
        pdfFileName = "";

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(pdfUrl);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("\"EmailIDs_EmailSend\"", "\"\""));
            nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"PDFFileName1\"", "\"" + attachmentOne + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"PDFFileName2\"", "\"" + attachmentTwo + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"MobileNumbers_SMSSend\"", "\"\""));
            nameValuePairs.add(new BasicNameValuePair("\"PrintOnSamePage\"", "false"));
            nameValuePairs.add(new BasicNameValuePair("\"PDFFileName3\"", "\"" + attachmentThree + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"PrintRequestTask\"", "2"));
            nameValuePairs.add(new BasicNameValuePair("\"RegnTestPrint\"", ""+regnTestPrintArray));
            nameValuePairs.add(new BasicNameValuePair("\"ShowHeader\"", ""+ShowHeader));
            nameValuePairs.add(new BasicNameValuePair("\"ShowFooter\"", ""+ShowFooter));
            nameValuePairs.add(new BasicNameValuePair("\"Task\"", "0"));
            nameValuePairs.add(new BasicNameValuePair("\"UserFID\"", "\"" + userfid + "\""));

            String finalString = nameValuePairs.toString();
            finalString = finalString.replace('=', ':');
            finalString = finalString.substring(1, finalString.length() - 1);
            String strToServe = " \"objReleaseTestRegn\": {" + finalString + "}";
            String strToServer = "{" + strToServe + "}";
            Log.d("strToServer", "strToServer: "+strToServer);

            StringEntity se = new StringEntity(strToServer);
            se.setContentType("application/json;charset=UTF-8");
            httpPost.setEntity(se);

            // Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Log.d("inputStream",""+inputStream);
            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                Log.d("result",""+result);
                String jsonformattString = result.replaceAll("\\\\", "");
                try {
                    JSONObject jsonObj = new JSONObject(jsonformattString);
                    Log.d("jsonObj",""+jsonObj);
                    JSONObject pdfName = jsonObj.getJSONObject("d");

                    pdfFileName = pdfName.getString("PdfName");
                    Log.d("pdfFileName",""+pdfFileName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else{

            }
        } catch (Exception e) {
             //Log.i("InputStream", e.getLocalizedMessage());
        }
        return null;
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
    public void showAlert1(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView Mytitle = new TextView(this);
        Mytitle.setText("Alert");
        Mytitle.setTextSize(20);
        Mytitle.setPadding(5, 15, 5, 5);
        Mytitle.setGravity(Gravity.CENTER);
        Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
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
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        textView.setTypeface(custom_font);
        yesButton.setTypeface(custom_bold_font);
    }

    private boolean checkPermission() {
        int writePermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return writePermissionResult == PackageManager.PERMISSION_GRANTED && readPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writePermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readPermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writePermissionGranted && readPermissionGranted) {
                        // Permissions granted
                        // new GetPatientCheck().execute(new String[]{url});
                    } else {
                        // Permissions denied
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            boolean shouldShowWriteRationale = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            boolean shouldShowReadRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);

                            if (shouldShowWriteRationale || shouldShowReadRationale) {
                                // Show rationale and request permission again
                                showMessageOKCancel("Save PDFs Offline: Store downloaded PDF reports on your device for offline access.\n" +
                                                "Display PDFs: Show the PDF reports you download within the app for easy viewing.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ActivityCompat.requestPermissions(
                                                        ViewReportAttachment.this,
                                                        PERMISSIONS,
                                                        PERMISSION_REQUEST_CODE
                                                );
                                            }
                                        },
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // User clicked Cancel, handle accordingly
                                            }
                                        });
                            } else {
                                // User denied without showing rationale
                                Toast.makeText(this, "You need to allow permissions in phone settings", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(ViewReportAttachment.this)
                .setTitle("Permission Required")
                .setMessage(message)
                .setPositiveButton("Allow", okListener)
                .setNegativeButton("Cancel", cancelListener)
                .create()
                .show();
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
            attachmentone = "N/A";
            attachmenttwo = "N/A";
            attachmentthree = "N/A";
            pdfFileName = "";
            finish();
    }

}