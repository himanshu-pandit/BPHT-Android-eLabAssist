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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SendMessageAdmin extends BaseActivityAdmin {

    Button btnsendMessage;
    EditText editTxtMessage;
    Spinner spiner,msgToSpinnr;
    TextView CollectionCenterName;

    LinearLayout LLLablist;
    JSONArray Centername = null;
    JSONArray Affiliationname = null;

    static int MsgStateTOPosition = 1;
    static String pid = "";
    static String selected_labidfrompref = "";
    static String strmessage = "";
    static String strmessageT = "";
    static String usertype = "";
    String center_id, collectioncenter_id;
    static String status = "";
    static String finalResult = "";
    static String tid = null;
    // static String testpid = null;
    static String spinnerval = "";

    ArrayList<String> worldlist = new ArrayList<String>();
    ArrayList<GetMessageFromLab> world;

    CollectionCenterAdapter labAdapter;

    AutoCompleteTextView SearchCentertextView;
    ImageButton imgbtnClear2;

    String my_test_name = "";
    String my_testid = "";

    private ArrayAdapter<String> testnameadapter;

    ArrayList<CollectionCenterDetails> collectiocenterlist = new ArrayList<CollectionCenterDetails>();
    static List<String> collectiocenterlist_Listview = new ArrayList<String>();
    ArrayList<String> collectiocenterlist_id = new ArrayList<String>();

    ArrayList<AffiliationDetails> Affiliationlist = new ArrayList<AffiliationDetails>();
    static List<String> Affiliationlist_Listview = new ArrayList<String>();
    ArrayList<String> Affiliationlist_id = new ArrayList<String>();


    Set<String> allCollectionCenterNameSet = new HashSet<String>();
    Set<String> allaffiliationNameSet = new HashSet<String>();

    ArrayList<String> test_name = new ArrayList<String>();

    static String collectiocenter_id;
    static String affiliation_id;

    private ArrayAdapter<String> adapterr;
    JSONArray Testnamebycategory = null;
    AlertDialog alertbox;
    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    static SharedPreferences sharedpreferences;

    //  private static String SendMessage_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/Message";

    private static String SendMessage_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/Message";
///////////////////////////////////////////

    // private static String getMessage_myurl = "https://www.elabassist.com/Services/Userservice.svc/GetMessageList?LabId=84135443-7a22-4f96-b7ac-9fa211b750c5";
////////////////////////////////////          http://test3.elabassist.com/Services/Userservice.svc/GetMessageList?LabId=84135443-7a22-4f96-b7ac-9fa211b750c5
    private static String getMessage_myurlLab = "";

    private static String myurl = "https://www.elabassist.com/Services/Userservice.svc/GetMessageList?LabId=";


    private static final String collection_center_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";
    private static String collection_center_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";

    private static final String Affiliation_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";
    private static String Affiliation_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAffiliationList?labid=";

    //  http://localhost:50577/Services/GlobalUserService.svc/GetAffiliationList?labid=84135443-7a22-4f96-b7ac-9fa211b750c5
/////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

        //   setContentView(R.layout.activity_send_message);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
        // selected_labidfrompref ="84135443-7a22-4f96-b7ac-9fa211b750c5";
        pid = sharedpreferences.getString("patientId", "");
        usertype = sharedpreferences.getString("usertype","");

        getLayoutInflater().inflate(R.layout.activity_send_message, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);




        /* * */
        LLLablist =(LinearLayout)findViewById(R.id.ll_Msg_listSpinnern) ;
        btnsendMessage=(Button)findViewById(R.id.btnSend_id);
        editTxtMessage =(EditText)findViewById(R.id.etSendMessage_id);
        spiner = (Spinner) findViewById(R.id.spinner_msg_id);
        msgToSpinnr= (Spinner) findViewById(R.id.msgTo_spinr_id);
        SearchCentertextView = (AutoCompleteTextView)findViewById(R.id.SearchCenter);
        CollectionCenterName =(TextView)findViewById(R.id.CollectionCenterNameTv_id);
        imgbtnClear2 = (ImageButton)findViewById(R.id.clear2_id);

        btnsendMessage.setTypeface(custom_font);
        editTxtMessage.setTypeface(custom_font);
        SearchCentertextView.setTypeface(custom_font);

        collection_center_myurl = collection_center_myurl + selected_labidfrompref;
        Affiliation_myurl = Affiliation_myurl + selected_labidfrompref;

        new getMessagee().execute();
        // new GetCollectionCenterList().execute();
        //new GetAffiliationList().execute();

        // Set<String> allCollectionCenterNameSet = new HashSet<String>();
        //  Set<String> allaffiliationNameSet = new HashSet<String>();

        allCollectionCenterNameSet = sharedpreferences.getStringSet("setallCollectionCenterNameSet", null);
        allaffiliationNameSet = sharedpreferences.getStringSet("setallaffiliationNameSet", null);


        // collectiocenterlist.clear();
        collectiocenterlist_Listview.clear();
        collectiocenterlist_id.clear();

        Affiliationlist_Listview.clear();
        Affiliationlist_id.clear();



        if(allCollectionCenterNameSet != null)
        {
            Iterator<String> i1 = allCollectionCenterNameSet.iterator();
            while(i1.hasNext())
            {
                String my_test_data = i1.next();

                //String[] allTestsData = my_test_data.split(",");
                String[] allTestsData = my_test_data.split("#");


                my_test_name = allTestsData[0];
                my_testid = allTestsData[1];


                collectiocenterlist_Listview.add(my_test_name);
                collectiocenterlist_id.add(my_testid);

            }
            // testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
            //  testname.setAdapter(testnameadapter);
        }

        if(allaffiliationNameSet != null)
        {
            Iterator<String> i1 = allaffiliationNameSet.iterator();
            while(i1.hasNext())
            {
                String my_test_data = i1.next();

                //String[] allTestsData = my_test_data.split(",");
                String[] allTestsData = my_test_data.split("#");


                my_test_name = allTestsData[0];
                my_testid = allTestsData[1];

                Affiliationlist_Listview.add(my_test_name);
                Affiliationlist_id.add(my_testid);

            }
            // testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
            //  testname.setAdapter(testnameadapter);
        }



        if(     user_type.equals("AffiliationUser")||
                user_type.equals("Collection Center Admin") ||
                user_type.equals("Collection Center User") ||
                usertype.equals("Collection Center Lab User"))
        {
            String[] items = {"Laboratory" };
            ArrayAdapter obj = new ArrayAdapter(this,android.R.layout.simple_spinner_item,items);
            obj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            msgToSpinnr.setAdapter(obj);

            LLLablist.setVisibility(View.GONE);

        }
        else
        {

            SearchCentertextView.setCursorVisible(true);
            String[] items = {"Laboratory","Collection Center","Affiliation" };
            ArrayAdapter obj = new ArrayAdapter(this,android.R.layout.simple_spinner_item,items);
            obj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            msgToSpinnr.setAdapter(obj);

            LLLablist.setVisibility(View.VISIBLE);

  /*          if(allCollectionCenterNameSet != null)
            {
                Iterator<String> i1 = allCollectionCenterNameSet.iterator();
                while(i1.hasNext())
                {
                    String my_test_data = i1.next();

                    //String[] allTestsData = my_test_data.split(",");
                    String[] allTestsData = my_test_data.split("#");


                    my_center_name = allTestsData[0];
                    my_center_id = allTestsData[1];


                    collectiocenterlist_Listview.add(my_center_name);
                    collectiocenterlist_id.add(my_center_id);

                }
                // testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
                //  testname.setAdapter(testnameadapter);
            }

            if(allaffiliationNameSet != null)
            {
                Iterator<String> i1 = allaffiliationNameSet.iterator();
                while(i1.hasNext())
                {
                    String my_test_data = i1.next();

                    //String[] allTestsData = my_test_data.split(",");
                    String[] allTestsData = my_test_data.split("#");


                    my_center_name = allTestsData[0];
                    my_center_id = allTestsData[1];

                    Affiliationlist_Listview.add(my_center_name);
                    Affiliationlist_id.add(my_center_id);

                }
                // testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
                //  testname.setAdapter(testnameadapter);
            }

*/

            // collection_center_myurl = collection_center_myurl + selected_labidfrompref;
            //  new GetCollectionCenterList().execute();
        }

        msgToSpinnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println(" position " + position);

                        MsgStateTOPosition =  position;
                        //  Toast.makeText(SendMessage.this, "position "+ MsgStateTOPosition, Toast.LENGTH_SHORT).show();
                        if(MsgStateTOPosition == 0){
                            LLLablist.setVisibility(View.GONE);
                            CollectionCenterName.setText("");
                        }
                        else if(MsgStateTOPosition == 1){


                           /*
                            if(allCollectionCenterNameSet != null)
                            {
                                Iterator<String> i1 = allCollectionCenterNameSet.iterator();
                                while(i1.hasNext())
                                {
                                    String my_test_data = i1.next();

                                    //String[] allTestsData = my_test_data.split(",");
                                    String[] allTestsData = my_test_data.split("#");


                                    my_center_name = allTestsData[0];
                                    my_center_id = allTestsData[1];


                                    collectiocenterlist_Listview.add(my_center_name);
                                    collectiocenterlist_id.add(my_center_id);

                                }
                                // testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
                                //  testname.setAdapter(testnameadapter);
                            }

                            */

                            SearchCentertextView.setCursorVisible(true);
                            SearchCentertextView.setText("");
                            CollectionCenterName.setText("");
                            LLLablist.setVisibility(View.VISIBLE);
                            SearchCentertextView.setHint("Search Collection Center Here");
                            if(collectiocenterlist_Listview.size() > 0) {
                                // CollectionCenterName.setText(collectiocenterlist_Listview.get(0));
                            }
                            MsgStateTOPositioncollection();
                        }
                        else if(MsgStateTOPosition == 2){
/*

                            if(allaffiliationNameSet != null)
                            {
                                Iterator<String> i1 = allaffiliationNameSet.iterator();
                                while(i1.hasNext())
                                {
                                    String my_test_data = i1.next();

                                    //String[] allTestsData = my_test_data.split(",");
                                    String[] allTestsData = my_test_data.split("#");


                                    my_center_name = allTestsData[0];
                                    my_center_id = allTestsData[1];

                                    Affiliationlist_Listview.add(my_center_name);
                                    Affiliationlist_id.add(my_center_id);

                                }
                                // testnameadapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_dropdown_item_1line,test_name);
                                //  testname.setAdapter(testnameadapter);
                            }

                            */

                            SearchCentertextView.setCursorVisible(true);
                            SearchCentertextView.setText("");
                            CollectionCenterName.setText("");
                            LLLablist.setVisibility(View.VISIBLE);

                            SearchCentertextView.setHint("Search Affiliation Here");
                            if(Affiliationlist_Listview.size() > 0) {
                                // CollectionCenterName.setText(Affiliationlist_Listview.get(0));
                            }
                            MsgStateTOPositionaffiliation();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        System.out.println(" position " + parent);
                    }
                }
        );


        //  if(MsgStateTOPosition == 1){
        //      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, collectiocenterlist_Listview);
        //      SearchCentertextView.setAdapter(adapter);
        //  }
        //  if(MsgStateTOPosition == 2){
        //     ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Affiliationlist_Listview);
        //     SearchCentertextView.setAdapter(adapter);
        // }

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, collectiocenterlist_Listview);
        //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Affiliationlist_Listview);
        //textView.setAdapter(adapter);
        // SearchCentertextView.setAdapter(adapter);
        //textView.setOnItemClickListener();
        SearchCentertextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {

                String selection = (String)parent.getItemAtPosition(position);
                // Toast.makeText(SendMessage.this, "aa" +position, Toast.LENGTH_SHORT).show();
                CollectionCenterName.setText(selection);
                // SearchCentertextView.setText("");
                //TODO Do something with the selected text
                SearchCentertextView.setCursorVisible(false);

            }
        });
        // textView.setText("");
        imgbtnClear2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SearchCentertextView.setText("");
                SearchCentertextView.setCursorVisible(true);
                CollectionCenterName.setText("");
            }
        });
        btnsendMessage.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isInternetOn(getApplicationContext()))
                        {

                            if(MsgStateTOPosition == 0) {

                                strmessage = editTxtMessage.getText().toString();
                                // strmessageT=strmessage.trim();
                                strmessageT = strmessage.replace("\n","\\n" );
                                // Toast.makeText(SendMessage.this, "", Toast.LENGTH_SHORT).show();
                                if(strmessageT.equals(""))
                                {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.my_dialog,null);
                                    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                                    text.setText("     Please Enter Your Message     ");
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
                                    // Call Quick Book Service
                                    new SendMessagee().execute(new String[] { SendMessage_url });
                                }

                            }
                            if(MsgStateTOPosition == 1) {

                                strmessage = editTxtMessage.getText().toString();
                                // strmessageT=strmessage.trim();
                                strmessageT = strmessage.replace("\n","\\n" );
                                // Toast.makeText(SendMessage.this, "", Toast.LENGTH_SHORT).show();

                                if ((CollectionCenterName.getText().toString()).equals("") || (CollectionCenterName.getText().toString()) == null)
                                {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.my_dialog, null);

                                    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                                    text.setText("     Please Select any Collection Center    ");
                                    Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                                    text.setTypeface(typeface);
                                    text.setTextColor(Color.WHITE);
                                    text.setTextSize(18);
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);

                                    toast.show();
                                }

                                else if (strmessageT.equals(""))
                                {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.my_dialog,null);
                                    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                                    text.setText("     Please Enter Your Message     ");
                                    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
                                    text.setTypeface(typeface);
                                    text.setTextColor(Color.WHITE);
                                    text.setTextSize(18);
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                }
                                else {
                                    String CollectionCenterNameString = CollectionCenterName.getText().toString();

                                    int index = -1;
                                    for (int i = 0; i < collectiocenterlist_Listview.size(); i++) {
                                        if (CollectionCenterNameString.equals(collectiocenterlist_Listview.get(i))) {
                                            index = i;
                                            break;
                                        }
                                    }

                                    collectiocenter_id = collectiocenterlist_id.get(index);


                                    if(CollectionCenterNameString.equals(collectiocenterlist_Listview.get(index))){

                                        new SendMessagee().execute(new String[]{SendMessage_url});

                                    } else{
                                        Toast.makeText(SendMessageAdmin.this, "this collection center is dasnt exixt  please select other collection center", Toast.LENGTH_SHORT).show();
                                    }

                                    // new SendMessagee().execute(new String[] { SendMessage_url });

                                }
                            }

                            if(MsgStateTOPosition == 2)
                            {

                                strmessage = editTxtMessage.getText().toString();
                                // strmessageT=strmessage.trim();
                                strmessageT = strmessage.replace("\n","\\n" );
                                // Toast.makeText(SendMessage.this, "", Toast.LENGTH_SHORT).show();

                                if ((CollectionCenterName.getText().toString()).equals("") || (CollectionCenterName.getText().toString()) == null)
                                {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.my_dialog, null);

                                    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                                    text.setText("     Please Select any Affiliation    ");
                                    Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                                    text.setTypeface(typeface);
                                    text.setTextColor(Color.WHITE);
                                    text.setTextSize(18);
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);

                                    toast.show();
                                }

                                else if(strmessageT.equals(""))
                                {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.my_dialog,null);
                                    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                                    text.setText("     Please Enter Your Message     ");
                                    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
                                    text.setTypeface(typeface);
                                    text.setTextColor(Color.WHITE);
                                    text.setTextSize(18);
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                }
                                else {
                                    String AffiliationNameString = CollectionCenterName.getText().toString();

                                    int index = -1;
                                    for(int i=0;i<Affiliationlist_Listview.size();i++)
                                    {
                                        if(AffiliationNameString.equals(Affiliationlist_Listview.get(i)))
                                        {
                                            index = i;
                                            break;
                                        }
                                    }

                                    collectiocenter_id = Affiliationlist_id.get(index);

                                    //   if(AffiliationNameString.equals(Affiliationlist_Listview.get(index))) {

                                    new SendMessagee().execute(new String[]{SendMessage_url});
                                    // } else{
                                    //     Toast.makeText(SendMessage.this, "this collection center is dasnt exixt  please select other collection center", Toast.LENGTH_SHORT).show();
                                    // }
                                }
                            }
                        }
                        else
                        {
                            alertBox("Internet Connection is not available..!");
                        }
                    }
                }
        );


    }

    public void MsgStateTOPositioncollection() {
        ///  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, collectiocenterlist_Listview);
        //  SearchCentertextView.setAdapter(adapter);

        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, R.layout.listauto_item, R.id.item, collectiocenterlist_Listview);

        //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, collectiocenterlist_Listview);
        SearchCentertextView.setAdapter(adapter);
    }
    public void MsgStateTOPositionaffiliation() {

        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, R.layout.listauto_item, R.id.item, Affiliationlist_Listview);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, Affiliationlist_Listview);
        SearchCentertextView.setAdapter(adapter);

    }
    /*** Async task class to get json by making HTTP call **/
    private class GetCollectionCenterList extends
            AsyncTask<String, Void, ArrayList<CollectionCenterDetails>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            collectiocenterlist.clear();
            collectiocenterlist_Listview.clear();
            collectiocenterlist_id.clear();

            // Showing progress dialog
            //  pDialog = new ProgressDialog(SendMessage.this);
            //  pDialog.setMessage("Please wait...");
            //  pDialog.setCancelable(false);
            //  pDialog.show();
        }

        @Override
        protected ArrayList<CollectionCenterDetails> doInBackground(String... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(collection_center_myurl, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    Centername = jsonObj.getJSONArray("d");

                    // looping through All Contacts
                    for (int i = 0; i < Centername.length(); i++) {

                        CollectionCenterDetails det = new CollectionCenterDetails();
                        JSONObject c = Centername.getJSONObject(i);

                        String collectionCebterName = c.getString("Name");
                        center_id = c.getString("CollectionCenterID");

                        // adding contact to contact list
                        det.setName(collectionCebterName);
                        det.setId(center_id);

                        collectiocenterlist.add(det);
                        collectiocenterlist_Listview.add(collectionCebterName);
                        collectiocenterlist_id.add(center_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return collectiocenterlist;
        }

        @Override
        protected void onPostExecute(ArrayList<CollectionCenterDetails> result) {
            super.onPostExecute(result);

            // if (pDialog.isShowing())
            //    pDialog.dismiss();

            labAdapter = new CollectionCenterAdapter(SendMessageAdmin.this, collectiocenterlist);

            //   list1.setAdapter(labAdapter);
            //   setListViewHeightBasedOnChildren(list1);

            collection_center_myurl = collection_center_url;
        }
    }


    /*** Async task class to get json by making HTTP call **/
    private class GetAffiliationList extends
            AsyncTask<String, Void, ArrayList<AffiliationDetails>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Affiliationlist.clear();
            Affiliationlist_Listview.clear();
            Affiliationlist_id.clear();

            // Showing progress dialog
            pDialog = new ProgressDialog(SendMessageAdmin.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<AffiliationDetails> doInBackground(String... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Affiliation_myurl, ServiceHandler.GET);
            // String jsonStr = affiliationString;

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    Affiliationname = jsonObj.getJSONArray("d");

                    // looping through All Contacts
                    for (int i = 0; i < Affiliationname.length(); i++) {

                        AffiliationDetails det = new AffiliationDetails();
                        JSONObject c = Affiliationname.getJSONObject(i);

                        String AffiliationName = c.getString("AffiliationCompanyName");
                        String Affiliation_id = c.getString("AffiliationID");

                        // adding contact to contact list
                        // det.setName(AffiliationName);
                        // det.setId(Affiliation_id);

                        // Affiliationlist.add(det);
                        Affiliationlist_Listview.add(AffiliationName);
                        Affiliationlist_id.add(Affiliation_id);
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
        protected void onPostExecute(ArrayList<AffiliationDetails> result) {
            super.onPostExecute(result);

            if (pDialog != null && pDialog.isShowing())
            {
                pDialog.dismiss();
            }

            //   labAdapter = new AffiliationAdapter(SendMessage.this, Affiliationlist);

            //   list1.setAdapter(labAdapter);
            //   setListViewHeightBasedOnChildren(list1);

            Affiliation_myurl = Affiliation_myurl;
        }
    }

    private class getMessagee extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            // Locate the WorldPopulation Class
            world = new ArrayList<GetMessageFromLab>();
            // Create an array to populate the spinner
            worldlist = new ArrayList<String>();

            ServiceHandler sh = new ServiceHandler();

/////////////////////////////////////////////////////////////////////////////////////////////////

            getMessage_myurlLab = myurl+selected_labidfrompref;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(getMessage_myurlLab, ServiceHandler.GET);
////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonarray = jsonObj.getJSONArray("d");
                    // JSONObject msgObj = jsonObj.getJSONObject("d");
                    // JSONArray msgArrey = msgObj.getJSONArray("d");

                    // Getting values from register lab array
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject c = jsonarray.getJSONObject(i);

                        GetMessageFromLab worldpop = new GetMessageFromLab();

                        worldpop.setMessage(c.optString("Message"));

                        world.add(worldpop);

                        worldlist.add(c.optString("Message"));

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
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml


            // Spinner adapter
            spiner.setAdapter(new ArrayAdapter<String>(SendMessageAdmin.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    worldlist));

            // Spinner on item click listener
            spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {

                    editTxtMessage.setText( world.get(position).getMessage());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


    }

    /////////////////////////////////////////////////////////////////////
    private class SendMessagee extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            // Showing progress dialog
            pDialog = new ProgressDialog(SendMessageAdmin.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            return SendMessagPOST(params[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            SearchCentertextView.setCursorVisible(true);
            SearchCentertextView.setText("");
            CollectionCenterName.setText("");

            // Dismiss the progress dialog
            if (pDialog != null && pDialog.isShowing())
            {
                pDialog.dismiss();
            }

            if(result.equals("Success"))

            //{"d":"Success"}
            {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.my_dialog,null);

                TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                text.setText(" Message Sent Successfully ");
                Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
                text.setTypeface(typeface);
                text.setTextColor(Color.WHITE);
                text.setTextSize(18);
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);

                toast.show();
                editTxtMessage.setText("");
                Intent iintent = new Intent(SendMessageAdmin.this, MassagListAdmin.class);

                startActivity(iintent);
                //finish();

            }
            else
            {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.my_dialog,null);

                TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                text.setText(" Message Not Sent ");
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


    public static String SendMessagPOST(String url)
    {
        InputStream inputStream = null;
        String result = "";

        //   String MessageVal = editTxtMessage.getText().toString();

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            if(selected_labidfrompref != null && !(selected_labidfrompref.equalsIgnoreCase(""))) {
                nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
            }
            nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + pid + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"Message\"","\"" + strmessageT + "\""));

            if(MsgStateTOPosition == 1)
            {
                nameValuePairs.add(new BasicNameValuePair("\"EntityId\"","\"" + collectiocenter_id + "\""));
                nameValuePairs.add(new BasicNameValuePair("\"EntityTypeId\"", "\"3\""));

            }
            else if(MsgStateTOPosition == 2)
            {
                nameValuePairs.add(new BasicNameValuePair("\"EntityId\"","\"" + collectiocenter_id + "\""));
                nameValuePairs.add(new BasicNameValuePair("\"EntityTypeId\"", "\"4\""));

            }
            else
            {
                nameValuePairs.add(new BasicNameValuePair("\"EntityId\"", "\"0\""));
                nameValuePairs.add(new BasicNameValuePair("\"EntityTypeId\"", "\"5\""));

            }

            String finalString = nameValuePairs.toString();

            finalString = finalString.replace('=', ':');
            finalString = finalString.substring(1, finalString.length() - 1);
            String strToServer = "{" + finalString + "}";

            System.out.println("strToServer" + strToServer);

            StringEntity se = new StringEntity(strToServer);
            se.setContentType("application/json;charset=UTF-8");
            httpPost.setEntity(se);

            // Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
            {
                result = convertInputStreamToString(inputStream);
                String jsonformattString = result.replaceAll("\\\\", "");
                //  String jsonformattString = result.replaceAll("\\\\", "");
                try
                {

                    JSONObject jsonObj = new JSONObject(jsonformattString);
                    status = jsonObj.get("d").toString();
                    // JSONObject jsonObj = new JSONObject(jsonformattString);
                    // JSONObject resultObj = (JSONObject) jsonObj.get("d");
                    // finalResult = resultObj.getString("Result");

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


    public class GetMessageFromLab {

        // private String LabId;
        private String Message;


        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

    }
    private static String convertInputStreamToString(InputStream inputStream)throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null)
            //  sb.append(line).append('\n');
            result += line;

        inputStream.close();
        return result;
        //   return sb.toString();
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);

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

       // alert.setIcon(R.drawable.loginbtnnew250);
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

        Intent i = new Intent(SendMessageAdmin.this,MassagListAdmin.class);
        //	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        //finish();
    }


}