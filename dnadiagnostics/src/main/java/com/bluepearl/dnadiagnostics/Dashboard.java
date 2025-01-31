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
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Dashboard extends BaseActivityAdmin {

    BarChart barChart;
    LineChart Linechart;
    BarData bardata;
    ArrayList<BarDataSet> dataSets;
    ArrayList<String> labels;

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

    // contacts JSONArray
    static JSONArray patient = null;
    //  static JSONArray msglistt = null;
    static JSONObject msglistt =null;


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

    JSONArray GetDashbordArry = null;
    JSONArray Testprofile = null;
    JSONArray Centername = null;

    private static ArrayList<ChildItemColectionCenter> listDataChild;
    private static ArrayList<GroupItemColectonCenter> listDataHeader;


    ArrayList<String> collectioncentrList = new ArrayList<String>();


    ProgressBar myProgressRegis, myProgressTotalAmount,myProgressBlnc,myProgressPendinTest,mypiaGraph,myBarGraph;

    TextView TVLabDashboard ,TVRegistration,TVTotalAmount,TVBalnceAmount,TVPenditTest;

    ExpandableListAdapter listAdapter;
    ExpandableListView ColectionCenterList;

    private static  String  getDashboardSummary_myurl = "";
    private static final String  getDashboardSummary_url= "https://www.elabassist.com/Services/HtmlData.svc/getDashboardSummary?FromDate=";

    private static String  getCCDashboardSummary_myurl= "https://www.elabassist.com/Services/HtmlData.svc/getCCDashboardSummary?FromDate=";
    private static final String getCCDashboardSummary_url = "https://www.elabassist.com/Services/HtmlData.svc/getCCDashboardSummary?FromDate=";

    private static  String getDashboardTestStatusSummary_myurl = "https://www.elabassist.com/Services/HtmlData.svc/getDashboardTestStatusSummary?FromDate=";
    private static final String  getDashboardTestStatusSummary_url= "https://www.elabassist.com/Services/HtmlData.svc/getDashboardTestStatusSummary?FromDate=";

    private static  String getTATDashboard_myurl= "https://www.elabassist.com/Services/HtmlData.svc/getTATDashboard?FromDate=";
    private static final String getTATDashboard_url = "https://www.elabassist.com/Services/HtmlData.svc/getTATDashboard?FromDate=";


    LinearLayout LLPopularTest ,LLUploadPrecription, LLSelectTest;

    Button BtnPopularTest ,BtnUploadPrecription, BtnSelectTest ,  RegisterPatiebtBtn;

    ArrayList<Entry> yVals1 = new ArrayList<Entry>();
    ArrayList<String> xVals = new ArrayList<String>();

    PieChart mChart;



    public static  final int[] MY_COLORS = {
            Color.rgb(255, 77, 170),
            Color.rgb(117, 206, 56),
            Color.rgb(147, 90, 205),
            Color.rgb(0, 221, 255),
            Color.rgb(255, 230, 40),
            Color.rgb(255, 102, 125),
            Color.rgb(255, 140, 25),
            Color.rgb(120, 230, 0),

    };

    public static  final int[] MY_COLORSLine = {
            Color.rgb(255, 140, 20),
            Color.rgb(255, 102, 125),
            Color.rgb(0, 221, 255),
            Color.rgb(120, 230, 0),
            Color.rgb(0, 0, 255),
            Color.rgb(150, 100, 55),
            Color.rgb(255, 50, 50),

    };

    static String statusAutho = "";
    static String num_valueAutho = null;
    static String password_valueAutho = null;
    private String LoginUrl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";


/////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //   setContentView(R.layout.activity_send_message);


        //getSupportActionBar().setTitle("ydtshhd");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
        // selected_labidfrompref ="84135443-7a22-4f96-b7ac-9fa211b750c5";
        pid = sharedpreferences.getString("patientId", "");
        UserRole= sharedpreferences.getString("Role", "");
        num_valueAutho = sharedpreferences.getString("phno","");
        password_valueAutho = sharedpreferences.getString("password","");

        getLayoutInflater().inflate(R.layout.activity_dashboard, frameLayout);
        mDrawerList.setItemChecked(position, true);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        LLPopularTest = (LinearLayout) findViewById(R.id.LL_PopularTest_id);
        LLUploadPrecription = (LinearLayout) findViewById(R.id.LL_UploadPrescription_id);
        LLSelectTest = (LinearLayout) findViewById(R.id.LL_SelectTest_id);
        BtnPopularTest = (Button) findViewById(R.id.buttonPopularTest_id);
        BtnUploadPrecription = (Button) findViewById(R.id.buttonUploadPrescription_id);
        BtnSelectTest = (Button) findViewById(R.id.buttonSelectTest_id);


        etTodate = (EditText) findViewById(R.id.etToDate);
        etFromdate = (EditText) findViewById(R.id.etFromDate);
        search = (ImageButton) findViewById(R.id.btnSearch);
       // newmsg = (ImageButton) findViewById(R.id.NewMessagee);
        TVLabDashboard = (TextView) findViewById(R.id.TVLabDashboard_id);

        TVRegistration = (TextView) findViewById(R.id.TVRegistration_id);
        TVTotalAmount = (TextView) findViewById(R.id.TVTotalAmount_id);
        TVBalnceAmount = (TextView) findViewById(R.id.TVBalnceAmount_id);
        TVPenditTest = (TextView) findViewById(R.id.TVPenditTest_id);
        myProgressRegis = (ProgressBar) findViewById(R.id.myProgressRegis_id);
        myProgressTotalAmount = (ProgressBar) findViewById(R.id.myProgressTotalAmount_id);
        myProgressBlnc = (ProgressBar) findViewById(R.id.myProgressBlnc_id);
        myProgressPendinTest = (ProgressBar) findViewById(R.id.myProgressPendinTest_id);
        mypiaGraph   = (ProgressBar) findViewById(R.id.mypiaGraph_id);
        myBarGraph   = (ProgressBar) findViewById(R.id.myBarGraph_id);
        ColectionCenterList = (ExpandableListView)findViewById(R.id.coolectioncenterlistview);

        Linechart = (LineChart) findViewById(R.id.Linechart_id);
        Linechart.setDescription(" ");


      //  listDataChild = new ArrayList<ChildItemColectionCenter>();
        listDataHeader = new ArrayList<GroupItemColectonCenter>();

        int colorCodeDark = Color.parseColor("#ff8b0e");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myProgressRegis.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
            myProgressTotalAmount.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
            myProgressBlnc.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
            myProgressPendinTest.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
            mypiaGraph.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
            myBarGraph.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
        }

    //    myProgressRegis.getProgressDrawable().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.SRC_IN);


        //  TVLabDashboard.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

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
        ColectionCenterList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                //Toast.makeText(getApplicationContext(), "GroupClick", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        // Listview Group expanded listener
        ColectionCenterList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(), "expnd", Toast.LENGTH_SHORT).show();
                ColectionCenterList.setDividerHeight(10);
              //  ColectionCenterList.setDivider(Color.parseColor("#ffffff"));
                ColectionCenterList.setBackgroundColor(Color.parseColor("#ffffff"));

            }
        });

        // Listview Group collasped listener
        ColectionCenterList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), "colaps", Toast.LENGTH_SHORT).show();
                ColectionCenterList.setDividerHeight(10);

            }
        });


        // Listview on child click listener
        ColectionCenterList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                //Toast.makeText(getApplicationContext(), "child click", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

   /*     newmsg.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(DashboardTwo.this, "Go To Graph View", Toast.LENGTH_SHORT).show();
                    *//*
                        Intent iintent = new Intent(Dashboard.this,SendMessage.class);
                        startActivity(iintent);
                        finish();*//*
                    }
                }
        );*/

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

                   // adapter.notifyDataSetChanged();

                   // ColectionCenterList.setAdapter((CollectionCenterAdapterTwo)null);
                   // listDataHeader.clear();
                   // ExpandableListAdapter adapter = new ExpandableListAdapter();
                   // ColectionCenterList.setAdapter((CollectionCenterAdapterTwo)null);//or what ever adapter you use/created
                  //  ColectionCenterList.setAdapter(adapter);

                    new getDashboardSummary().execute();
                    new getCCDashboardSummary().execute();
                    new getTesatLevelSummary().execute();
                    new getTATDashboard().execute();


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



        if (isInternetOn(getApplicationContext()))
        {

            new LoginCheck().execute(new String[]{LoginUrl});
            new getDashboardSummary().execute();
            new getCCDashboardSummary().execute();
           // new getTATDashboard().execute();

        }
        else
        {
            //  showAlert("Internet Connection is not available..!", "Alert");
        }

/////////////////////////////////////////////////////////////////////////

        mChart = (PieChart) findViewById(R.id.chart1);

        //   mChart.setUsePercentValues(true);
        mChart.setDescription("");

        mChart.setRotationEnabled(true);
        //   mChart.setDrawHoleEnabled(false);
        mChart.setDrawHoleEnabled(true);
        mChart.setTransparentCircleRadius(2f);
        mChart.setHoleRadius(2f);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                double answer =  e.getVal();
                DecimalFormat df = new DecimalFormat("###.#");
               String dhhdi = df.format(answer);

             int itd =e.getXIndex();
                  Toast.makeText(Dashboard.this," Total " +xVals.get(e.getXIndex()) + " is " + dhhdi + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });


        LLPopularTest.setVisibility(View.VISIBLE);
        LLUploadPrecription.setVisibility(View.GONE);
        LLSelectTest.setVisibility(View.GONE);

        BtnPopularTest.setBackgroundColor(Color.parseColor("#ffffff"));
        BtnUploadPrecription.setBackgroundResource(R.drawable.borderbtn);
        BtnSelectTest.setBackgroundResource(R.drawable.borderbtn);

        BtnSelectTest.setTextColor(Color.parseColor("#ffffff"));
        BtnPopularTest.setTextColor(Color.parseColor("#000000"));
        BtnUploadPrecription.setTextColor(Color.parseColor("#ffffff"));

        BtnPopularTest.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LLPopularTest.setVisibility(View.VISIBLE);
                        LLUploadPrecription.setVisibility(View.GONE);
                        LLSelectTest.setVisibility(View.GONE);

                        BtnPopularTest.setBackgroundColor(Color.parseColor("#ffffff"));
                        BtnUploadPrecription.setBackgroundResource(R.drawable.borderbtn);
                        BtnSelectTest.setBackgroundResource(R.drawable.borderbtn);

                        BtnSelectTest.setTextColor(Color.parseColor("#ffffff"));
                        BtnUploadPrecription.setTextColor(Color.parseColor("#ffffff"));
                        BtnPopularTest.setTextColor(Color.parseColor("#000000"));

                        new getDashboardSummary().execute();
                        new getCCDashboardSummary().execute();

                    }
                }
        );


        BtnUploadPrecription.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LLPopularTest.setVisibility(View.GONE);
                        LLUploadPrecription.setVisibility(View.VISIBLE);
                        LLSelectTest.setVisibility(View.GONE);

                        BtnUploadPrecription.setBackgroundColor(Color.parseColor("#ffffff"));
                        BtnPopularTest.setBackgroundResource(R.drawable.borderbtn);
                        BtnSelectTest.setBackgroundResource(R.drawable.borderbtn);

                        BtnSelectTest.setTextColor(Color.parseColor("#ffffff"));
                        BtnUploadPrecription.setTextColor(Color.parseColor("#000000"));
                        BtnPopularTest.setTextColor(Color.parseColor("#ffffff"));

                        new getTesatLevelSummary().execute();


                    }
                }
        );


        BtnSelectTest.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LLPopularTest.setVisibility(View.GONE);
                        LLUploadPrecription.setVisibility(View.GONE);
                        LLSelectTest.setVisibility(View.VISIBLE);

                        BtnSelectTest.setBackgroundColor(Color.parseColor("#ffffff"));
                        BtnUploadPrecription.setBackgroundResource(R.drawable.borderbtn);
                        BtnPopularTest.setBackgroundResource(R.drawable.borderbtn);

                        BtnSelectTest.setTextColor(Color.parseColor("#000000"));
                        BtnUploadPrecription.setTextColor(Color.parseColor("#ffffff"));
                        BtnPopularTest.setTextColor(Color.parseColor("#ffffff"));

                       new getTATDashboard().execute();

                    }
                }
        );
/////////////////////////////// test selection data ///////////////////////////////////

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


  /*      date1="2013-06-24";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = format.parse(date1);
        SimpleDateFormat your_format = new SimpleDateFormat("dd-MMM-yyyy");
        date2 = your_format.format(dt);*/

        if (dateToFormat != null && !(dateToFormat.equalsIgnoreCase(""))) {
            //  SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;

            try {
                date = sdfSource.parse(dateToFormat);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdfDestination = new SimpleDateFormat("MM-dd-yyyy");
            outputDate = sdfDestination.format(date);
        }
        return outputDate;
    }


    //*** Async task class to get json by making HTTP call **//
    private class getDashboardSummary extends AsyncTask<Void, Void, Void> {

        String totalReg ="0";
        String totalAmount ="0";
        String blncAmountt ="0";
        String PendinTestt ="0";




        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            TVRegistration.setText(totalReg);
            TVTotalAmount.setText(totalAmount);
            TVBalnceAmount.setText(blncAmountt);
            TVPenditTest.setText(PendinTestt);

            myProgressRegis.setVisibility(View.VISIBLE);
            myProgressTotalAmount.setVisibility(View.VISIBLE);
            myProgressBlnc.setVisibility(View.VISIBLE);
            myProgressPendinTest.setVisibility(View.VISIBLE);


//            getDashboardSummary_myurl = getDashboardSummary_url;

            FromDate = etFromdate.getText().toString() ;
            ToDate = etTodate.getText().toString() ;
            FromDate = changeDateFormat(FromDate);
            ToDate = changeDateFormat(ToDate);

            getDashboardSummary_myurl = getDashboardSummary_url + FromDate+"&ToDate="+ToDate+"&Lab="+selected_labidfrompref;


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            Log.d("url getDashboardSummary",getDashboardSummary_myurl);
            String jsonStr = sh.makeServiceCall(getDashboardSummary_myurl, ServiceHandler.GET);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray affiliationnamee = jsonObj.getJSONArray("Table1");
                    JSONObject feedObj = (JSONObject) affiliationnamee.get(0);

                    totalReg = (feedObj.getString("TotalRegns"));
                    totalAmount= (feedObj.getString("Net"));
                    blncAmountt= (feedObj.getString("BalanceAmt"));
                    PendinTestt = (feedObj.getString("PendingTestCount"));


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

            double valtotalAmount,valblncAmountt;

            if(totalAmount.equals("null")) {
                 valtotalAmount = Double.parseDouble("0.0");
            }else {
                 valtotalAmount = Double.parseDouble(totalAmount);
            }
                BigDecimal bigDecimaltotalAmoun = new BigDecimal(valtotalAmount);// form to BigDecimal
                String strtotalAmount = bigDecimaltotalAmoun.toString();

            if(blncAmountt.equals("null")) {
                 valblncAmountt  = Double.parseDouble("0.0");
            }else {
                 valblncAmountt  = Double.parseDouble(blncAmountt);
            }
                BigDecimal bigDecimalblncAmountt = new BigDecimal(valblncAmountt);// form to BigDecimal
                String strblncAmountt = bigDecimalblncAmountt.toString();

      /*   //   String TotalAmt =   str.replaceAll("[0]+$", "").replaceAll("(\\.)(?!.*?[1-9]+)", "");
            String blncAmt =   blncAmountt.replaceAll("[0]+$", "").replaceAll("(\\.)(?!.*?[1-9]+)", "");

            String TotalAmttt ="123456789123456789";

         String jsdbjibvg =   getIndianCurrencyFormat(TotalAmttt);
            String jsdbjibvg1 =   rupeeFormat(TotalAmttt);*/

                TVRegistration.setText(totalReg);
                TVTotalAmount.setText(getResources().getString(R.string.Rss) + rupeeFormat(strtotalAmount));
                TVBalnceAmount.setText(getResources().getString(R.string.Rss) + rupeeFormat(strblncAmountt));
                TVPenditTest.setText(PendinTestt);

                myProgressRegis.setVisibility(View.GONE);
                myProgressTotalAmount.setVisibility(View.GONE);
                myProgressBlnc.setVisibility(View.GONE);
                myProgressPendinTest.setVisibility(View.GONE);
            }

    }



    //*** Async task class to get json by making HTTP call **//
    private class getCCDashboardSummary extends AsyncTask<Void, Void, Void> {

        String totalReg ="0";
        String totalAmount ="0";
        String blncAmountt ="0";
        String PendinTestt ="0";




        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            if(alertbox != null && alertbox.isShowing()){
                //alert.show();
                alertbox.dismiss() ;
            }



         //   listDataHeader.clear();
          // listAdapter = new CollectionCenterAdapterTwo(DashboardTwo.this, listDataHeader);
           // listAdapter.notifyAll();
         //   ColectionCenterList.setAdapter(listAdapter);
          //  ColectionCenterList.setAdapter((CollectionCenterAdapterTwo)null);

          /*  TVRegistration.setText(totalReg);
            TVTotalAmount.setText(totalAmount);
            TVBalnceAmount.setText(blncAmountt);
            TVPenditTest.setText(PendinTestt);

            myProgressRegis.setVisibility(View.VISIBLE);
            myProgressTotalAmount.setVisibility(View.VISIBLE);

            myProgressPendinTest.setVisibility(View.VISIBLE);*/
           // myProgressCC.setVisibility(View.VISIBLE);

           // getDashboardSummary_myurl = getDashboardSummary_url;

            FromDate = etFromdate.getText().toString() ;
            ToDate = etTodate.getText().toString() ;
            FromDate = changeDateFormat(FromDate);
            ToDate = changeDateFormat(ToDate);

           // getDashboardSummary_myurl = getDashboardSummary_url + FromDate+"&ToDate="+ToDate+"&Lab="+selected_labidfrompref;
            getCCDashboardSummary_myurl = getCCDashboardSummary_url + FromDate+"&ToDate="+ToDate+"&Lab="+selected_labidfrompref;


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            Log.d("getCCDashboardSummary",getCCDashboardSummary_myurl);
            String jsonStr = sh.makeServiceCall(getCCDashboardSummary_myurl, ServiceHandler.GET);

            if (jsonStr != null) {
                try {





                   listDataHeader.clear();
                   // collectioncentrList.clear();

                    //listAdapter.
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray affiliationnamee = jsonObj.getJSONArray("Table1");

                    //GroupItemColectonCenter groupItem = new GroupItemColectonCenter();
                   // ChildItemColectionCenter childItem = new ChildItemColectionCenter();

                    for (int i = 0; i < affiliationnamee.length(); i++) {

                        //listDataChild.clear();

                        GroupItemColectonCenter groupItem = new GroupItemColectonCenter();

                        JSONObject c = affiliationnamee.getJSONObject(i);

                        String temp = c.getString("CollectionCenterName");
                        String temp1 = c.getString("CollectionCentreId");
                        String CenterCity = c.getString("RegCount");
                        String Name = c.getString("Net");
                        String CenterAddress = c.getString("AmountPaid");
                        String CenterNumber = c.getString("RegCount");
                        String CenterLongitude = c.getString("BalanceAmt");
                        String Centerlatitude = c.getString("CommissionAmount");
                        String CenterID = c.getString("RegCount");
                        String CenterAreaID = c.getString("RegCount");
                        String CenterDrName = c.getString("RegCount");


                        groupItem.setAppointmentAddress(temp);
                        groupItem.setAppointmentDate(temp1);

                        listDataChild = new ArrayList<ChildItemColectionCenter>();


                        ChildItemColectionCenter childItem = new ChildItemColectionCenter();
                        childItem.setSelectedTest(CenterNumber);
                        childItem.setSelectedCenmterName(Name);
                        childItem.setSelectedCenterAdress(CenterAddress);
                        childItem.setSelectedCenterLongitude(CenterLongitude);
                        childItem.setSelectedCenterLatitude(Centerlatitude);
                        childItem.setSelectedCenterID(CenterID);

                        listDataChild.add(childItem);
                        groupItem.setChildItems(listDataChild);
                        listDataHeader.add(groupItem);


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

            if(listDataHeader == null || listDataHeader.isEmpty() )
            {
              //  alertBox("Internet Connection is not available..!");
                Toast.makeText(Dashboard.this, "currently no data available..! please check your internet connection", Toast.LENGTH_SHORT).show();

            }else {
                listAdapter = new CollectionCenterAdapterTwo(Dashboard.this, listDataHeader);
                //listAdapter.notifyAll();
                ColectionCenterList.setAdapter(listAdapter);
                if (listAdapter.getGroupCount() == 0) {
                    //showAlert("No History Available","");
                    Toast.makeText(Dashboard.this, "no data ", Toast.LENGTH_SHORT).show();
                }
            }







        }
    }


    //*** Async task class to get json by making HTTP call **//
    private class getTesatLevelSummary extends AsyncTask<Void, Void, Void> {

        String totalReg ="0";
        String totalAmount ="0";
        String blncAmountt ="0";
        String PendinTestt ="0";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mypiaGraph.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
//            getDashboardTestStatusSummary_myurl = getDashboardTestStatusSummary_url;
            FromDate = etFromdate.getText().toString() ;
            ToDate = etTodate.getText().toString() ;
            FromDate = changeDateFormat(FromDate);
            ToDate = changeDateFormat(ToDate);
            getDashboardTestStatusSummary_myurl = getDashboardTestStatusSummary_url + FromDate+"&ToDate="+ToDate+"&Lab="+selected_labidfrompref;


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            Log.d("TestStatusSummary",getDashboardTestStatusSummary_myurl);
            String jsonStr = sh.makeServiceCall(getDashboardTestStatusSummary_myurl, ServiceHandler.GET);

            if (jsonStr != null) {
                try {



                    yVals1.clear();
                    xVals.clear();


                JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray affiliationnamee = jsonObj.getJSONArray("Table1");

                       for (int i = 0; i < affiliationnamee.length(); i++) {

                        JSONObject c = affiliationnamee.getJSONObject(i);
                        String StateName = c.getString("StateName");
                        String WorkFlowCurrentState = c.getString("WorkFlowCurrentState");
                        String RecCount = c.getString("RecCount");

                        int recCont = Integer.parseInt(RecCount.trim());

                           float newFloat = (float) recCont;


                           yVals1.add(new Entry(newFloat, i));

                           xVals.add(StateName);


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

            mypiaGraph.setVisibility(View.GONE);
            mChart.setVisibility(View.VISIBLE);

            PieDataSet dataSet = new PieDataSet(yVals1, "");
            dataSet.setSliceSpace(2);
            dataSet.setSelectionShift(10);
            // adding colors
            ArrayList<Integer> colors = new ArrayList<Integer>();
            // Added My Own colors
            for (int c : MY_COLORS) {
                colors.add(c);
            }

            dataSet.setColors(colors);
            //  create pie data object and set xValues and yValues and set it to the pieChart
            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextSize(21f);
            data.setValueTextColor(Color.WHITE);
            mChart.setData(data);
            mChart.setDrawSliceText(false);
            // undo all highlights
            mChart.highlightValues(null);
            mChart.getLegend().setWordWrapEnabled(true);
            // refresh/update pie chart
            mChart.invalidate();
            // animate piechart
            mChart.animateXY(1400, 1400);
            // Legends to show on bottom of the graph
            Legend l = mChart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            l.setXEntrySpace(7);
            l.setYEntrySpace(5);



        }
    }




    //*** Async task class to get json by making HTTP call **//
    private class getTATDashboard extends AsyncTask<Void, Void, Void> {


        String totalReg ="0";
        String totalAmount ="0";
        String blncAmountt ="0";
        String PendinTestt ="0";

        String testIdsString ="2634,2748,2760,4026,3885";
        String[] testStringname ={"Haemogram","Urine Analysis Report","Ultra Sensitive TSH","Blood Sugar Fasting","Thyroid Function Test1"};

        String testIdsArey[] =testIdsString.split(",");
        int testIdslenght =testIdsArey.length;

        JSONObject jsonObj =null;
        JSONArray affiliationnamee =null;

        ArrayList<String> TatDates = new ArrayList<String>();
        ArrayList<String> TestNameArry = new ArrayList<String>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Linechart.setVisibility(View.GONE);
            myBarGraph.setVisibility(View.VISIBLE);
//            getTATDashboard_myurl = getTATDashboard_url;

            FromDate = etFromdate.getText().toString() ;
            ToDate = etTodate.getText().toString() ;
            FromDate = changeDateFormat(FromDate);
            ToDate = changeDateFormat(ToDate);

            // getDashboardSummary_myurl = getDashboardSummary_url + FromDate+"&ToDate="+ToDate+"&Lab="+selected_labidfrompref;
           // getTATDashboard_myurl = getTATDashboard_url + FromDate+"&ToDate="+ToDate+"&TestIDs=2634,2748,2760&status=6&Lab="+selected_labidfrompref;
            getTATDashboard_myurl = getTATDashboard_url +FromDate+"&ToDate="+ToDate+"&TestIDs="+testIdsString+"&status=6&Lab="+selected_labidfrompref;

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            Log.d("getTATDashboard_myurl",getTATDashboard_myurl);
            String jsonStr = sh.makeServiceCall(getTATDashboard_myurl, ServiceHandler.GET);

            if (jsonStr != null) {
                try {






                    TatDates.clear();
                    TestNameArry.clear();

                     jsonObj = new JSONObject(jsonStr);

                     affiliationnamee = jsonObj.getJSONArray("Table1");


                    for (int i = 0; i < affiliationnamee.length(); i++) {

                        JSONObject c = affiliationnamee.getJSONObject(i);

                        String TestDate = c.getString("TestDate");
                        String Testid = c.getString("Testid");
                        String TestName = c.getString("TestName");
                        String AverageTime = c.getString("AverageTime");

                        String[] TestDateFull = TestDate.split("T");
                        String TestDateOne = TestDateFull[0];

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt = format.parse(TestDateOne);
                        SimpleDateFormat your_format = new SimpleDateFormat("dd-MMM-yyyy");
                        String date2 = your_format.format(dt);

                        if(TatDates.contains(date2)) {
                        }else
                        {
                            TatDates.add(date2);
                        }
                        TestNameArry.add(TatDates+"#"+TestName+"#"+AverageTime+"#"+Testid);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
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
            Linechart.setVisibility(View.GONE);
            myBarGraph.setVisibility(View.GONE);


            ArrayList<String> labels = new ArrayList<String>();
            for (int i = 0; i < TatDates.size(); i++) {
                String dateeee =TatDates.get(i).substring(0,6);
                labels.add(dateeee);
            }
            ArrayList<Entry>[] Entry = (ArrayList<Entry>[]) new ArrayList[testIdslenght];
            for (int i = 0; i < testIdslenght; i++) {
                Entry[i] = new ArrayList<Entry>();
                int index =0;
                for(int j = 0; j < TestNameArry.size(); j++) {
                    if (TestNameArry.get(j).contains(testIdsArey[i]) ){
                        String[] TestAvregTime = TestNameArry.get(j).split("#");
                        String TestAvregTimeValue = TestAvregTime[2];
                        int TestAvregTimeValueint = Integer.parseInt(TestAvregTimeValue.trim());
                        float TestAvregTimeValueintFloat = (float) TestAvregTimeValueint;
                        Entry[i].add(new Entry(TestAvregTimeValueintFloat, index));
                        index++;
                    } else {

                    }
                }
            }
            ArrayList<LineDataSet> dataSets = new ArrayList<>();
            LineData chartData = new LineData(labels);

            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int i = 0; i < testIdslenght; i++) {

               // BarDataSet barDataSet1 = new BarDataSet(Entry[i], testStringname[i]);
               // LineDataSet barDataSet1 = new LineDataSet(Entry[i], testStringname[i]);
                LineDataSet lDataSet1 = new LineDataSet(Entry[i], testStringname[i]);

                lDataSet1.setColor(MY_COLORSLine[i]);

               // LineDataSet lDataSet1 = new LineDataSet(dtatentries1, "CBC");

              /*  for (int c : MY_COLORS) {
                    colors.add(c);
                }
                barDataSet1.setColors(colors);
*/
              //  barDataSet1.setColor(Color.rgb(255, 100, 120));
                //dataSets.add(barDataSet1);
                chartData.addDataSet(lDataSet1);
            }


           // BarData data = new BarData(labels, dataSets);
          //  LineData data = new LineData(labels, dataSets);
            LineData data = new LineData(labels);

           // chartData.addDataSet(data);

            Linechart.setVisibility(View.VISIBLE);
           // Linechart.setData(data);

            Linechart.setData(chartData);
            Linechart.invalidate();
            Linechart.fitScreen();
            Linechart.getLegend().setWordWrapEnabled(true);
            Linechart.animateY(2000);
        }
    }

    public void drwagraph(){

        Linechart.setVisibility(View.GONE);
        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
        bargroup1.add(new BarEntry(8f, 0));
        bargroup1.add(new BarEntry(2f, 1));
       // bargroup1.add(new BarEntry(5f, 2));
       // bargroup1.add(new BarEntry(20f, 3));
        //bargroup1.add(new BarEntry(15f, 4));
       // bargroup1.add(new BarEntry(19f, 5));

// create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup2 = new ArrayList<>();
        bargroup2.add(new BarEntry(6f, 0));
        bargroup2.add(new BarEntry(10f, 1));
       // bargroup2.add(new BarEntry(5f, 2));
      //  bargroup2.add(new BarEntry(25f, 3));
       // bargroup2.add(new BarEntry(4f, 4));
       // bargroup2.add(new BarEntry(17f, 5));

        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup3 = new ArrayList<>();
        bargroup3.add(new BarEntry(8f, 0));
        bargroup3.add(new BarEntry(2f, 1));
      //  bargroup3.add(new BarEntry(5f, 2));
      //  bargroup3.add(new BarEntry(20f, 3));
       // bargroup3.add(new BarEntry(15f, 4));
      //  bargroup3.add(new BarEntry(19f, 5));


        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "CBC");
        barDataSet1.setColor(Color.rgb(255, 140, 20));

        BarDataSet barDataSet2 = new BarDataSet(bargroup2, "HBC");
        barDataSet2.setColor(Color.rgb(120, 230, 0));

        BarDataSet barDataSet3 = new BarDataSet(bargroup3, "LIPID");
       barDataSet3.setColor(Color.rgb(0, 222, 255));

       labels = new ArrayList<String>();
        labels.add("21 nov");
        labels.add("22 nov");
       // labels.add("23 nov");
       // labels.add("24 nov");
       // labels.add("25 nov");
        //labels.add("26 nov");

         dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
       // dataSets.add(barDataSet3);

// initialize the Bardata with argument labels and dataSet
        bardata = new BarData(labels, dataSets);
        Linechart.setVisibility(View.VISIBLE);
       // Linechart.setData(bardata);
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
        Builder alert = new Builder(Dashboard.this);

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

        if (!Dashboard.this.isFinishing()){
            alert1.show();
        }



        TextView textView = (TextView) alert1.findViewById(android.R.id.message);
        Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

    }
    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }

    @Override
    public void onBackPressed()
    {
        alertbox = new Builder(this)
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
/*
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.remove("allTestNames");
                                editor.remove("popularTestData");
                                editor.remove("setprofilename");
                                editor.remove("setcategoryname");

                                editor.commit();*/

                                // Constant.SIGNIN_STATUS = false;
                              //  Intent i1 = new Intent(Dashboard.this, KeshAyurHome.class);
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
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        textView.setTypeface(custom_font);
        yesButton.setTypeface(custom_bold_font);
        noButton.setTypeface(custom_bold_font);
    }

    private class LoginCheck extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

	/*		// Showing progress dialog
			pDialog = new ProgressDialog(SigninActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params)
        {
            return LoginPOST(params[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

		/*	// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();*/

            if (result.equals("Success"))
            {
                System.out.println("Verified");
                //Toast.makeText(RegisterPatient.this, "Verified", Toast.LENGTH_SHORT).show();


            }
            else
            {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.my_dialog,null);

                TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                text.setText("    Authentication Error !! \n  Please login again     ");
                Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
                text.setTypeface(typeface);
                text.setTextColor(Color.WHITE);
                text.setTextSize(18);
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);

                toast.show();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.remove("loginchkbox");
                editor.remove("checkboxchecked");
                editor.remove("allTestNames");
                editor.remove("popularTestData");
                editor.remove("setprofilename");
                editor.remove("setcategoryname");

                editor.remove("SelectedLabId");
                editor.remove("SelectedLabName");

                editor.commit();

                i1 = new Intent(Dashboard.this, SigninActivity.class);
                i1.putExtra("fromLogout","yes");
                i1.putExtra("fromchange", "no");
               // i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
                //finish();

            }
        }
    }
////////////////////////////////////////  Authentication  /////////////////////////////////


    public static String LoginPOST(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"3\""));
            nameValuePairs.add(new BasicNameValuePair("\"UserName\"", "\"" + num_valueAutho + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"Password\"", "\"" + password_valueAutho + "\""));

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

                    statusAutho = resultObj.getString("Result");
                    //Code = resultObj.getString("Data");

                }
                catch (JSONException e)
                {

                    statusAutho = "Did not work!";
                    e.printStackTrace();
                }
            }
            else
                statusAutho = "Did not work!";
        }
        catch (Exception e)
        {
            // Log.i("InputStream", e.getLocalizedMessage());
            statusAutho = "Did not work!";
        }

        return statusAutho;
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


    public static String rupeeFormat(String value){
        value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result+lastDigit);
    }


    public String getIndianCurrencyFormat(String amount) {
        StringBuilder stringBuilder = new StringBuilder();
        char amountArray[] = amount.toCharArray();
        int a = 0, b = 0;
        for (int i = amountArray.length - 1; i >= 0; i--) {
            if (a < 3) {
                stringBuilder.append(amountArray[i]);
                a++;
            } else if (b < 2) {
                if (b == 0) {
                    stringBuilder.append(",");
                    stringBuilder.append(amountArray[i]);
                    b++;
                } else {
                    stringBuilder.append(amountArray[i]);
                    b = 0;
                }
            }
        }
        return stringBuilder.reverse().toString();
    }

}
