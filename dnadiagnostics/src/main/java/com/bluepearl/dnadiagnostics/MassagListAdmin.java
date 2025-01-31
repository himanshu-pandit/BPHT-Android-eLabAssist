package com.bluepearl.dnadiagnostics;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MassagListAdmin extends BaseActivityAdmin {


    // static String FlebotomiaString = "";

    Spinner msgSpinnr;

    private LinearLayout ll1;
    private ImageButton search;
    private ImageButton newmsg;

    static int MsgStateTOServer = -1;
    //static int MsgStateTOServer = Integer.parseInt("-1");

    ListView doctorliListView;
    EditText etpname, etlabcode, etTodate, etFromdate;
    private int mYear, mMonth, mDay;
    static final int FROM_DATE = 0;
    static final int TO_DATE = 1;
    ListView flebotomiaListview;
    MessageAdaptor flebotomiaAdapter;
    static String FromDate;
    static String ToDate;
    boolean flg = false;
    static String etpname_val = "";
    static String etlabcode_val = "";
    AutoCompleteTextView textView;
    String pdfFileName = "";


    String FullMessage = "";
    String PatientAge = "";
    String PatientMobileNumber = "";
    String PatientAdress  = "";

    String txtfromdate, txttodate;
    static String doc_id = null;
    static String phone = null;
    static String usertype = "";
    static int isSearch = 0;
    int temp =0;
    static String selected_labidfrompref = null;
    static String UserRole = null;
    static String curState = "";
    static String ReleseDoctorState = "";
    static String RelesePatientState = "";

    // private String url = "https://www.elabassist.com/Services/UserService.svc/GetMesssageTransactionList";
    private String url = "https://www.elabassist.com/Services/UserService.svc/GetMesssageTransactionList";


    // contacts JSONArray
    static JSONArray patient = null;
    //  static JSONArray msglistt = null;
    static JSONObject msglistt =null;

    static ArrayList<MessageListDetails> flebotomiaList = new ArrayList<MessageListDetails>();
    static ArrayList<MessageListDetails> originalflebotomiaList = new ArrayList<MessageListDetails>();
    String[] patientNameArr, doctorNameArr, patientContactArr;
    static List<String> doctor_Listview = new ArrayList<String>();


    Button btnsendMessage;
    EditText editTxtMessage;
    Spinner spiner;


    static String pid = "";
    //static String selected_labidfrompref_other = "";
    static String strmessage = "";
    static String strmessageT = "";

    static String status = "";
    static String finalResult = "";
    static String tid = null;
    static String testpid = null;
    static String spinnerval = "";
    ArrayList<String> worldlist = new ArrayList<String>();
    private ArrayAdapter<String> adapterr;
    JSONArray Testnamebycategory = null;
    AlertDialog alertbox;
    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    static SharedPreferences sharedpreferences;

/////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //   setContentView(R.layout.activity_send_message);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
        // selected_labidfrompref ="84135443-7a22-4f96-b7ac-9fa211b750c5";
        pid = sharedpreferences.getString("patientId", "");
        UserRole= sharedpreferences.getString("Role", "");
        getLayoutInflater().inflate(R.layout.activity_message_list, frameLayout);

        mDrawerList.setItemChecked(position, true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);




        // System.out.println(" flebString " + FlebotomiaString);

        ll1 = (LinearLayout) findViewById(R.id.ll3);
        search = (ImageButton) findViewById(R.id.btnSearch);
        newmsg = (ImageButton) findViewById(R.id.NewMessagee);
        etpname = (AutoCompleteTextView) findViewById(R.id.etPatientName);
        etlabcode = (EditText) findViewById(R.id.etLabCode);
        etTodate = (EditText) findViewById(R.id.etToDate);
        etFromdate = (EditText) findViewById(R.id.etFromDate);
        flebotomiaListview = (ListView) findViewById(R.id.doctorlistview);
        textView = (AutoCompleteTextView) findViewById(R.id.etPatientName);

        msgSpinnr= (Spinner) findViewById(R.id.msg_spinr_id);



        //    etpname.setTypeface(custom_font);
        //  textView.setTypeface(custom_font);
        //  etlabcode.setTypeface(custom_font);
        // etTodate.setTypeface(custom_font);
        //  etFromdate.setTypeface(custom_font);
        msgSpinnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println(" position " + position);

                        MsgStateTOServer =  position -1;

                        // if(MsgStateTOServer == 1){
                        //     MsgStateTOServer = 2;
                        // }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        System.out.println(" position " + parent);
                    }
                }
        );

        String[] items = {"All","New","Replied", "Closed" };
        // String[] items = {"All","New","Closed" };
        ArrayAdapter obj = new ArrayAdapter(this,android.R.layout.simple_spinner_item,items);
        obj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msgSpinnr.setAdapter(obj);


        Calendar from = Calendar.getInstance();
        Calendar fromone = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        final String strDatefrom = sdf.format(from.getTime());
        final String strDateto = sdf.format(to.getTime());

        etFromdate.setText(strDatefrom);
        etTodate.setText(strDateto);

        FromDate = etFromdate.getText().toString() ;
        //FromDate = FromDate + " 00:00:00";
        FromDate = FromDate;

        ToDate = etTodate.getText().toString() ;
        // ToDate = ToDate + " 23:59:59";
        ToDate = ToDate ;

        FromDate = changeDateFormat(FromDate);
        ToDate = changeDateFormat(ToDate);


        if (isInternetOn(getApplicationContext()))
        {
            isSearch = 0;
            new GetPatientCheck().execute(new String[] { url });
        }
        else
        {
            //  showAlert("Internet Connection is not available..!", "Alert");
        }

        newmsg.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent iintent = new Intent(MassagListAdmin.this,SendMessageAdmin.class);

                        startActivity(iintent);
                        //finish();
                    }
                }
        );

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isInternetOn(getApplicationContext())) {
                    isSearch = 1;
//                    etpname_val = etpname.getText().toString() ;
//                   etlabcode_val = etlabcode.getText().toString() ;

                    FromDate = etFromdate.getText().toString() ;
                    //FromDate = FromDate + " 00:00:00";

                    ToDate = etTodate.getText().toString() ;
                    // ToDate = ToDate + " 23:59:59";

                    FromDate = changeDateFormat(FromDate);
                    ToDate = changeDateFormat(ToDate);

                    new GetPatientCheck().execute(new String[] { url });
                    flg = true;

                } else {
                    // showAlert("Internet Connection is not available..!","Alert");
                }
            }
        });

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        etFromdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(FROM_DATE);
            }
        });

        etTodate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(TO_DATE);
            }
        });
        // new GetPatient().execute();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, doctor_Listview);
//        textView.setAdapter(adapter);
    }



    @SuppressLint("NewApi")
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case FROM_DATE:
                DatePickerDialog dialog_from = new DatePickerDialog(this,
                        mDateSetListener_from, mYear, mMonth, mDay);
                Calendar currentDate_from = Calendar.getInstance();
                dialog_from.getDatePicker().setMaxDate(
                        currentDate_from.getTimeInMillis());
                return dialog_from;

            case TO_DATE:
                DatePickerDialog dialog_to = new DatePickerDialog(this,
                        mDateSetListener_to, mYear, mMonth, mDay);
                Calendar currentDate_to = Calendar.getInstance();
                dialog_to.getDatePicker().setMaxDate(
                        currentDate_to.getTimeInMillis());
                return dialog_to;
        }
        return null;
    }

    // DatePickerDialog from date
    private DatePickerDialog.OnDateSetListener mDateSetListener_from = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            mMonth = mMonth + 1;

            String day = String.valueOf(mDay);
            String month = String.valueOf(mMonth);

            if (day.length() < 2) {
                day = "0" + day;
                etFromdate.setText(new StringBuilder().append(day).append("-")
                        .append(month).append("-").append(mYear));
            } else {
                etFromdate.setText(new StringBuilder().append(day).append("-")
                        .append(month).append("-").append(mYear));
            }

            if (month.length() < 2) {
                month = "0" + month;
                etFromdate.setText(new StringBuilder().append(day).append("-")
                        .append(month).append("-").append(mYear));
            } else {
                etFromdate.setText(new StringBuilder().append(day).append("-")
                        .append(month).append("-").append(mYear));
            }
        }
    };

    // DatePickerDialog to date
    private DatePickerDialog.OnDateSetListener mDateSetListener_to = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            mMonth = mMonth + 1;

            String day = String.valueOf(mDay);
            String month = String.valueOf(mMonth);

            if (day.length() < 2) {
                day = "0" + day;
                etTodate.setText(new StringBuilder().append(day).append("-")
                        .append(month).append("-").append(mYear));
            } else {
                etTodate.setText(new StringBuilder().append(day).append("-")
                        .append(month).append("-").append(mYear));
            }

            if (month.length() < 2) {
                month = "0" + month;
                etTodate.setText(new StringBuilder().append(day).append("-")
                        .append(month).append("-").append(mYear));
            } else {
                etTodate.setText(new StringBuilder().append(day).append("-")
                        .append(month).append("-").append(mYear));
            }
        }
    };
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    public String changeDateFormat(String dateToFormat) {
        String outputDate = null;

        if (dateToFormat != null && !(dateToFormat.equalsIgnoreCase(""))) {
            //  SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date date = null;

            try {
                date = sdfSource.parse(dateToFormat);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
            outputDate = sdfDestination.format(date);
        }
        return outputDate;
    }

    private class GetPatientCheck extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            flebotomiaList.clear();
            doctor_Listview.clear();

            // Showing progress dialog
            pDialog = new ProgressDialog(MassagListAdmin.this);
            pDialog.setMessage("Please wait...");
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
            if (pDialog != null && pDialog.isShowing())
            {
                pDialog.dismiss();
            }

            if(isSearch == 1)
            {
                isSearch = 0;
            }

            if (flebotomiaList.size() == 0) {

                flebotomiaAdapter = new MessageAdaptor(MassagListAdmin.this,flebotomiaList);
                flebotomiaListview.setAdapter(flebotomiaAdapter);
                //  showAlert1("No reports are available");

            } else {
                flebotomiaAdapter = new MessageAdaptor(MassagListAdmin.this,flebotomiaList);
                flebotomiaListview.setAdapter(flebotomiaAdapter);
            }

            flebotomiaListview
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            MessageListDetails myObj = (MessageListDetails) flebotomiaList.get(arg2);



                            FullMessage = myObj.getfullmessage();
                            String MessageState = myObj.getMessageState();
                            String CreatedDate = myObj.getCreatedDate();
                            String MessageFrom = myObj.getMessageFrom();
                            String MessageTo = myObj.getMessageTo();

                            String ReplyMessage = myObj.getReplyMessage();
                            String ReplyBy = myObj.getReplyBy();
                            String ModifiedDate = myObj.getModifiedDate();
                            String MesssageTansactionId = myObj.getMesssageTansactionId();
                            String CloseByBS = myObj.getCloseBy();


                            //  SharedPreferences.Editor editor = sharedpreferences.edit();
                            //  editor.putString("FullMessage", FullMessage);
                            //  editor.commit();

                            Intent iintent = new Intent(MassagListAdmin.this, MessageShowAdmin.class);

                            iintent.putExtra("FullMessage", FullMessage);
                            iintent.putExtra("MessageState", MessageState);
                            iintent.putExtra("CreatedDate", CreatedDate);
                            iintent.putExtra("MessageFrom", MessageFrom);
                            iintent.putExtra("MessageTo", MessageTo);

                            iintent.putExtra("ReplyMessage", ReplyMessage);
                            iintent.putExtra("ReplyBy", ReplyBy);
                            iintent.putExtra("ModifiedDate", ModifiedDate);
                            iintent.putExtra("MesssageTansactionId", MesssageTansactionId);
                            iintent.putExtra("CloseByB", CloseByBS);


                            startActivity(iintent);
                            //finish();

                        }
                    });
        }
    }


    public static String PatientPOST(String url) {
        InputStream inputStream = null;
        String result = "";


        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("\"currentPage\"", "\"1\""));
            nameValuePairs.add(new BasicNameValuePair("\"sortDirection\"", "\"true\""));
            nameValuePairs.add(new BasicNameValuePair("\"pageSize\"", "\"20\""));
            nameValuePairs.add(new BasicNameValuePair("\"sortCol\"", "\"CreatedDate\""));
            nameValuePairs.add(new BasicNameValuePair("\"FromDate\"", "\"" + FromDate + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"ToDate\"", "\"" + ToDate + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"EntityId\"", "\"0\""));
            nameValuePairs.add(new BasicNameValuePair("\"UserRole\"", "\"" + UserRole + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"UserFID\"", "\"" + pid + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"Labid\"", "\"" + selected_labidfrompref + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"EntityTypeId\"", "\"0\""));
            nameValuePairs.add(new BasicNameValuePair("\"MessageStatus\"", "\"" + MsgStateTOServer + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"2\""));

            String finalString = nameValuePairs.toString();
            finalString = finalString.replace('=', ':');
            finalString = finalString.substring(1, finalString.length() - 1);

            finalString = "{" + finalString + "}";

            String tempString = "\"objSearchOption\"" + ":";
            String strToServer = tempString + finalString;
            String strToServer1 = "{" + strToServer + "}";

            StringEntity se = new StringEntity(strToServer1);
            se.setContentType("application/json;charset=UTF-8");
            httpPost.setEntity(se);

            // Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                //result = rsultt;
                String jsonformattString = result.replaceAll("\\\\", "");

                // String jsonformattString = FlebotomiaString;

                System.out.println(" msglisttttt " + jsonformattString);

                try {
                    JSONObject jsonObj = new JSONObject(jsonformattString);

                    // Getting JSON Array node
                    msglistt = jsonObj.getJSONObject("d");

                    String msglisttt = String.valueOf(msglistt);
                    // patient = jsonObj.getJSONArray("d");
                    JSONObject jsonObjtwo = new JSONObject(msglisttt);

                    patient = jsonObjtwo.getJSONArray("MessageList");
                    // looping through All Contacts
                    for (int i = 0; i < patient.length(); i++) {
                        MessageListDetails listd = new MessageListDetails();
                        JSONObject c = patient.getJSONObject(i);


                        String Message_msg = c.getString("Message");
                        String msg_ststus = c.getString("Status");
                        String msg_createddate = c.getString("CreatedDatestr");
                        String msg_MessageFrom = c.getString("MessageFrom");
                        String msg_MessageTo = c.getString("MessageTo");

                        String msg_ReplyMessage = c.getString("ReplyMessage");
                        String msg_ReplyBy = c.getString("ReplyBy");
                        String msg_ModifiedDate = c.getString("ModifiedDatestring");
                        String msg_MesssageTansactionId = c.getString("MesssageTansactionId");
                        String msg_CloseBy = c.getString("CloseBy");





                        listd.setFullMessage(Message_msg);
                        listd.setMessageState(msg_ststus);
                        listd.setCreatedDate(msg_createddate);
                        listd.setMessageFrom(msg_MessageFrom);
                        listd.setMessageTo(msg_MessageTo);

                        listd.setReplyMessage(msg_ReplyMessage);
                        listd.setReplyBy(msg_ReplyBy);
                        listd.setModifiedDate(msg_ModifiedDate);
                        listd.setMesssageTansactionId(msg_MesssageTansactionId);
                        listd.setmsg_CloseBy(msg_CloseBy);

                        flebotomiaList.add(listd);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                status = "Did not work!";
        } catch (Exception e) {
            // Log.i("InputStream", e.getLocalizedMessage());
        }
        return status;
    }

    private static String convertInputStreamToString(InputStream inputStream)throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null)
            // sb.append(line).append('\n');
            result += line;

        inputStream.close();
        return result;
        // return sb.toString();
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


    private void alertBox(String msg) {
        Builder alert = new Builder(this);

        TextView Mytitle = new TextView(this);
        Mytitle.setText("Alert");
        Mytitle.setTextSize(20);
        Mytitle.setPadding(5, 15, 5, 5);
        Mytitle.setGravity(Gravity.CENTER);
        Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
        alert.setCustomTitle(Mytitle);

        //alert.setIcon(R.drawable.loginbutton);
        alert.setMessage(msg);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MassagListAdmin.this, Dashboard.class);
        startActivity(i);
        //finish();
    }
}
