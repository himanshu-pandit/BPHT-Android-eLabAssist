package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddressSelection extends BaseActivity implements OnItemClickListener{

	Button btnProceed,btnDocSelection,btnQuickBook;
	Typeface custom_font;
	Dialog dialog;
	TextView txtCurAddress,txtlname;
	private ProgressDialog pDialog;
	RadioGroup rg1,rg2;
	ImageButton clear,btnLabchange;
	RadioButton radio_Residence, radio_Lab, radio_Other,radio_Home,radio_office;
	LinearLayout linear_et, linear_tv, ll7,ll_rbmain,ll4,ll_pincode;
	ListView list1;
	boolean isOtherSelected = false;
	boolean isResidenceSelected = false;
	static EditText otherAddress;
	static EditText pincode;
	EditText cur_pincode;
	EditText dname;
	EditText address;
	Bundle data;
	static String finalResult = "";
	final int maxResult = 5;
	String addressList[] = new String[maxResult];
	static String pin_val = null;
	static String pin_val_quick = null;
	String home_val = null;
	String lab_val = null;
	static String selected_labnamefrompref = null;
	static String tid = null;
	//static String testpid = null;
	static String pid = null;
	static String phone = null;
	static String add_val = null;
	static String pval = null;
	static String date_val = null;
	static String date_val_temp = null;
	static String dname_val = null;
	static String selectedDocId = "";
	static String selectedPatientId = "";
	static String residencial_address = "";
	static String official_address = "";
	static String status = "";
	static String text = "";
	static String entered_test_name = null;
	static String addval = null;
	static String pin = null;
	static String dnameval = null;
	static String family_pid = null;
	static String family_pname = null;
	static String family_page = null;
	static String self_id = null;
	static String register_patientgender = null;
	static String family_pgender = "0";
	static String pnameval = null;
	static String selected_labidfrompref = null;
	static String selected_patient_name = null;
	static String selectedCenterId = null;
	static String updated_val = null;
	static String pname_fromprofile = "";
	static String test_value;
	int my_flg = -1;
	static String selected_labnamefromintent = null;
	static String strDate = "";
	static String homecollection = "";
	String selectedPopularIdFromPatientInfo = "";
	static String EnteredFromPatientInfo = "";
	static String str = "home";
	String labstr = "lab";
	static String rb_str =null;
	Builder alertbox;
	boolean btnflg = true;
	static String res_add = "";
	static String res_pin = "";
	static String off_add = "";
	static String off_pin = "";
	public static final String MyPREFERENCES = "MyPrefs" ;
	static SharedPreferences sharedpreferences;
	String id;
	String center_id,collectioncenter_id;
	boolean isValidPincode = true;
	String selected_labid = "";
	String homecollectionfrompref = "";
	private static String filePath = null;
	JSONArray Centername = null;
	JSONArray Pincode = null;
	JSONArray Address = null;
	JSONArray Doctorname = null;
	public View row;
	String doc_id2;

	DoctorNameAdapter adapter;

	private static final String TAG_CENTERNAME = "Name";
	private static final String TAG_PINCODE = "pincode";
	private static final String TAG_DOCTORNAME = "Shortname";

	ArrayList<String> lab_List = new ArrayList<String>();
	ArrayList<CollectionCenterDetails> collectiocenterlist = new ArrayList<CollectionCenterDetails>();
	ArrayList<String> collectiocenteridlist = new ArrayList<String>();
	ArrayList<String> addresslist = new ArrayList<String>();
	ArrayList<String> pincodelist = new ArrayList<String>();
	ArrayList<DoctorNameDetails> dnameList = new ArrayList<DoctorNameDetails>();
	ArrayList<DoctorNameDetails> originaldnameList = new ArrayList<DoctorNameDetails>();
	ArrayList<String> d_idList = new ArrayList<String>();
	CollectionCenterAdapter labAdapter;

	Set<String> DoctorNames;
	Set<String> DoctorIDs;

    ArrayList<String> test_name_all = new ArrayList<String>();
    ArrayList<String> testid_all = new ArrayList<String>();
    ArrayList<String> test_name = new ArrayList<String>();
    ArrayList<String> testid = new ArrayList<String>();
    ArrayList<String> test_price = new ArrayList<String>();
    ArrayList<String> test_discountprice = new ArrayList<String>();
    ArrayList<String> popular_test_name = new ArrayList<String>();

	ArrayList<String> popular_test_nameTemp = new ArrayList<String>();

    ArrayList<String> testcategory_Listview = new ArrayList<String>();
    ArrayList<String> testbycategory_name = new ArrayList<String>();
    ArrayList<String> testidbycategory = new ArrayList<String>();

    ArrayList<String> test_profile_name = new ArrayList<String>();
    ArrayList<String> testpid = new ArrayList<String>();

    Set<String> allTestName;
    Set<String> allTestID;
    Set<String> allTestPrice;
    Set<String> allTestDiscountPrice;
    Set<String> popularTestName;
    Set<String> popularTestID;
    Set<String> popularTestPrice;
    Set<String> popularTestDiscountPrice;
    Set<String> profileNameSet;
    Set<String> profileIDSet;
    Set<String> categoryNameSet;
    Set<String> categoryIDSet;

    // contacts JSONArray
    JSONArray Testname = null;
    JSONArray Testprofile = null;

    String Test = null;
    String Testprofileid = null;

    JSONArray Testnamebycategory = null;
    JSONArray Testcategory = null;

    private static final String TAG_TESTNAME = "TestName";
    private static final String TAG_TESTID = "TestID";
    private static final String TAG_TESTPROFILE = "TestProfileName";
    private static final String TAG_TESTCATEGORY = "TestCategoryName";
    private static final String TAG_TESTPROFILEID = "TestProfileId";


	private String appointment_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/PatientAppointMent_Global";

	private static final String dname_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetDoctorsList?labid=";
	private static String dname_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetDoctorsList?labid=";

	private static final String address_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAddress?UserID=";
	private static String address_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetAddress?UserID=";

	private static final String collection_center_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";
	private static String collection_center_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetCollectionCenterList?labid=";

	private static final String pincode_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/PincodeAvailability?labid=";
	private static String pincode_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/PincodeAvailability?labid=";


    // URL to get Test Name
    private static final String url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?TestName=&LabID=";
    private static String myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?TestName=&LabID=";


    // URL to get Test Profile
    private static final String profileurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchProfile?LabID=";
    private static String profilemyurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchProfile?LabID=";

    // URL to get all categories
    private static final String category_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetTestCategories?LabID=";
    private static String category_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetTestCategories?LabID=";

	int setTestSizee,setCategorySizee,setProfileSizee,setTestPopulorSizee;

	Boolean isFirstRun;

	static String statusAutho = "";
	static String num_valueAutho = null;
	static String password_valueAutho = null;
	private String LoginUrl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UserRegistration";

	static String multiusertype = "null";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		getLayoutInflater().inflate(R.layout.activity_select_address,frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		DoctorNames = new HashSet<String>();
		DoctorIDs = new HashSet<String>();

		allTestName = new HashSet<String>();
		allTestID = new HashSet<String>();
		allTestPrice = new HashSet<String>();
		allTestDiscountPrice = new HashSet<String>();
		popularTestName = new HashSet<String>();
		popularTestID = new HashSet<String>();
		popularTestPrice = new HashSet<String>();
		popularTestDiscountPrice = new HashSet<String>();
		profileNameSet = new HashSet<String>();
		profileIDSet = new HashSet<String>();
		categoryNameSet = new HashSet<String>();
		categoryIDSet = new HashSet<String>();

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		pid = sharedpreferences.getString("patientId", "");
		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		selected_labnamefrompref = sharedpreferences.getString("SelectedLabName", "");
		homecollectionfrompref = sharedpreferences.getString("homecollectioncheck", "");
		addval = sharedpreferences.getString("address", "");
		pin = sharedpreferences.getString("pincode", "");
		phone = sharedpreferences.getString("phno", "");
		date_val = sharedpreferences.getString("DateTime", "");
		date_val_temp = date_val;
		updated_val = sharedpreferences.getString("updated", "");
		filePath = sharedpreferences.getString("filePath", "");
		selectedPatientId = sharedpreferences.getString("SelectedPatient", "");
		register_patientgender = sharedpreferences.getString("self_patientgender", "");
		residencial_address = sharedpreferences.getString("res_add", "");
		official_address = sharedpreferences.getString("office_add", "");
		entered_test_name = sharedpreferences.getString("test_name", "");
		selected_patient_name = sharedpreferences.getString("SelectedPatientName", "");
		selectedDocId = sharedpreferences.getString("doc_id", "");
		selectedCenterId = sharedpreferences.getString("centerid", "");
		addval = sharedpreferences.getString("address", "");
		dnameval = sharedpreferences.getString("Doctor", "");

		family_pid = sharedpreferences.getString("SelectedPatient", "");
		family_pname = sharedpreferences.getString("SelectedPatientName", "");
		family_page = sharedpreferences.getString("SelectedPatientAge", "");
		family_pgender = sharedpreferences.getString("SelectedPatientGender", "0");

		EnteredFromPatientInfo = sharedpreferences.getString("Enteredtestidfromintent", "");
		pname_fromprofile = sharedpreferences.getString("patient_name", "");
		selectedPopularIdFromPatientInfo = sharedpreferences.getString("populartestidfromintent", "");
		self_id = sharedpreferences.getString("userid", "");
		homecollection = sharedpreferences.getString("home_collection", "");

		num_valueAutho = sharedpreferences.getString("phno","");
		password_valueAutho = sharedpreferences.getString("password","");


		multiusertype = sharedpreferences.getString("multiusertype","null");
        //loginchk = sharedpreferences.getString("loginCheckData", "");

		rb_str = "residence";

		Set<String> setallCollectionCenterNameSet = new HashSet<String>();
		Set<String> setallaffiliationNameSet = new HashSet<String>();

		Set<String> selectedpopularsetinmain = new HashSet<String>();
		Set<String> selectedtestnamesetinmain = new HashSet<String>();
		Set<String> selectedtestcategorysetinmain = new HashSet<String>();
		Set<String> selectedtestprofilesetinmain = new HashSet<String>();



		isFirstRun = sharedpreferences.getBoolean("FIRSTRUN", true);

		if (isFirstRun)
		{

			System.out.println("FIRSTRUN ho raha hai....hehe" );


			SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putBoolean("FIRSTRUN", false);
			editor.remove("popularTestData");
			editor.remove("allTestNames");
			editor.remove("setprofilename");
			editor.remove("setcategoryname");
			editor.commit();

		}
		selectedpopularsetinmain = sharedpreferences.getStringSet("popularTestData", null);
		selectedtestnamesetinmain = sharedpreferences.getStringSet("allTestNames", null);
		selectedtestprofilesetinmain = sharedpreferences.getStringSet("setprofilename", null);
		selectedtestcategorysetinmain = sharedpreferences.getStringSet("setcategoryname", null);

		if(selectedtestnamesetinmain != null )
		{
			setTestSizee =selectedtestnamesetinmain.size();
		}else{
			setTestSizee = 0;
		}
		if(selectedtestcategorysetinmain != null )
		{
			setCategorySizee =selectedtestcategorysetinmain.size();
		}else{
			setCategorySizee = 0;
		}
		if(selectedtestprofilesetinmain != null )
		{
			setProfileSizee =selectedtestprofilesetinmain.size();
		}else{
			setProfileSizee = 0;
		}
		if(selectedpopularsetinmain != null )
		{
			setTestPopulorSizee =selectedpopularsetinmain.size();
		}else{
			setTestPopulorSizee = 0;
		}

		rg1 = (RadioGroup) findViewById(R.id.radioCollectionFrom);
		rg2 = (RadioGroup) findViewById(R.id.radioCollection);

		radio_Home = (RadioButton) findViewById(R.id.radiobuttonHome);
		radio_Lab = (RadioButton) findViewById(R.id.radiobuttonLab);
		radio_Residence = (RadioButton) findViewById(R.id.radioResidence);
		radio_office = (RadioButton) findViewById(R.id.radioOffice);
		radio_Other = (RadioButton) findViewById(R.id.radioOther);

		clear = (ImageButton) findViewById(R.id.clear);
		btnLabchange = (ImageButton) findViewById(R.id.changelabbtn);

		linear_et = (LinearLayout) findViewById(R.id.ll9);
		ll_pincode = (LinearLayout) findViewById(R.id.ll10);
		ll7 = (LinearLayout) findViewById(R.id.ll7);
		ll4 = (LinearLayout) findViewById(R.id.ll4);
		ll_rbmain = (LinearLayout) findViewById(R.id.llrb);

		btnProceed = (Button) findViewById(R.id.btnproceed);
		btnDocSelection = (Button) findViewById(R.id.btnSelectDoc);
		btnQuickBook = (Button) findViewById(R.id.QuickBookButton);

		list1 = (ListView) findViewById(R.id.listView1);

		otherAddress = (EditText) findViewById(R.id.etaddress);
		pincode = (EditText) findViewById(R.id.etpincode);
		dname = (EditText) findViewById(R.id.etRefDoc);
		txtlname = (TextView) findViewById(R.id.txtlabname);
		//dnumber = (EditText) findViewById(R.id.etRefDocNum);
		//dEmail = (EditText) findViewById(R.id.etRefDocEmail);

		txtCurAddress = (TextView) findViewById(R.id.txtaddress);

		txtlname.setTypeface(custom_font);
		radio_Home.setTypeface(custom_btnfont);
		radio_Lab.setTypeface(custom_btnfont);
		radio_Residence.setTypeface(custom_font);
		radio_office.setTypeface(custom_font);
		radio_Other.setTypeface(custom_font);
		otherAddress.setTypeface(custom_font);
		pincode.setTypeface(custom_font);
		dname.setTypeface(custom_font);
		/*dnumber.setTypeface(custom_font);
		dEmail.setTypeface(custom_font);*/
		btnDocSelection.setTypeface(custom_btnfont);
		btnQuickBook.setTypeface(custom_btnfont);
		btnProceed.setTypeface(custom_btnfont);

		dname_myurl = dname_myurl + selected_labidfrompref;
		new GetDoctorName().execute();

		address_myurl = address_myurl + pid + "&" + "LabID=" + selected_labidfrompref + "&" + "UserType=" + "2";
		new GetAddress().execute();

		Intent myIntent = getIntent();
		if(myIntent != null)
		{
			String name = myIntent.getStringExtra("doctorname");
			id = myIntent.getStringExtra("doctorid");
			dname.setText(name);
			txtlname.setText(selected_labnamefrompref);
		}

		if(homecollectionfrompref != null && !(homecollectionfrompref.equalsIgnoreCase("")))
		{
			if(homecollectionfrompref.equalsIgnoreCase("true"))
			{
				//str = "home";
				//radio_Home.setChecked(true);

				rg2.setVisibility(View.VISIBLE);
				linear_et.setVisibility(View.VISIBLE);
				ll_pincode.setVisibility(View.VISIBLE);
				list1.setVisibility(View.GONE);

			}
			else
			{
				ll7.setVisibility(View.VISIBLE);
				radio_Lab.setChecked(true);
				list1.setVisibility(View.VISIBLE);

				collection_center_myurl = collection_center_myurl + selected_labidfrompref;
				new GetCollectionCenterList().execute();

				str = "lab";

				rg2.setVisibility(View.GONE);
				linear_et.setVisibility(View.GONE);
				ll_pincode.setVisibility(View.GONE);
			}
		}



		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"dd-MMM-yyyy HH:mm");
		strDate = sdf.format(c.getTime());


		if (isInternetOn(getApplicationContext())) {

			new LoginCheck().execute(new String[]{LoginUrl});
		}else {
			Toast.makeText(AddressSelection.this, "Internet Connection is not available..!", Toast.LENGTH_SHORT).show();
		}

		myurl = url;
		profilemyurl = profileurl;
		category_myurl = category_url;

            myurl = myurl + selected_labidfrompref;
            profilemyurl = profilemyurl + selected_labidfrompref;
            category_myurl = category_myurl + selected_labidfrompref;

        /*    new GetTestName().execute();
            new GetTestProfile().execute();
            new GetTestCategory().execute();
            */


		rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radiobuttonHome:
					if(homecollectionfrompref != null && !(homecollectionfrompref.equalsIgnoreCase("")))
					{
						if(homecollectionfrompref.equalsIgnoreCase("true"))
						{
							str = "home";
							ll4.setVisibility(View.VISIBLE);
							ll7.setVisibility(View.GONE);
							list1.setVisibility(View.GONE);
							linear_et.setVisibility(View.VISIBLE);
							ll_pincode.setVisibility(View.VISIBLE);
						}
						else
						{
							alertBox("Home collection facility is not available in this lab");
							radio_Lab.setChecked(true);
						}
					}
					break;

				case R.id.radiobuttonLab:

					labstr = "lab";
					ll7.setVisibility(View.VISIBLE);
					list1.setVisibility(View.VISIBLE);

					collection_center_myurl = collection_center_myurl + selected_labidfrompref;
					new GetCollectionCenterList().execute();

					str = "lab";
					ll4.setVisibility(View.GONE);
					//txtCurAddress.setText("");
					linear_et.setVisibility(View.GONE);
					ll_pincode.setVisibility(View.GONE);
					//linear_tv.setVisibility(View.GONE);
					break;
				}
			}
		});

		rg2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				switch (checkedId) {

				case R.id.radioResidence:
					rb_str = "residence";

					otherAddress.setText("");
					pincode.setText("");

					linear_et.setVisibility(View.VISIBLE);
					ll_pincode.setVisibility(View.VISIBLE);
					ll7.setVisibility(View.GONE);
					isOtherSelected = false;

					if(str.equalsIgnoreCase("home"))
					{
						if(rb_str.equalsIgnoreCase("residence"))
						{

							if(res_add != null && !(res_add.isEmpty()) && !(res_add.equals("null")))
							{
								otherAddress.setText(res_add);
							}
							else
							{
								otherAddress.setText("");
							}
						}
					}


					if(str.equalsIgnoreCase("home"))
					{
						if(rb_str.equalsIgnoreCase("residence"))
						{
							if(res_pin != null && !(res_pin.isEmpty()) && !(res_pin.equals("null")))
							{
								pincode.setText(res_pin);
							}
							else
							{
								pincode.setText("");
							}
						}
					}

					break;

				case R.id.radioOffice:
					rb_str = "office";

					otherAddress.setText("");
					pincode.setText("");

					ll7.setVisibility(View.GONE);
					linear_et.setVisibility(View.VISIBLE);
					ll_pincode.setVisibility(View.VISIBLE);
					isOtherSelected = false;

					if(str.equalsIgnoreCase("home"))
					{
						if(rb_str.equalsIgnoreCase("office"))
						{
							if(off_add != null && !(off_add.isEmpty()) && !(off_add.equals("null")))
							{
								otherAddress.setText(off_add);
							}
							else
							{
								otherAddress.setText("");
							}
						}
					}


					if(str.equalsIgnoreCase("home"))
					{
						if(rb_str.equalsIgnoreCase("office"))
						{
							if(off_pin != null && !(off_pin.isEmpty()) && !(off_pin.equals("null")))
							{
								pincode.setText(off_pin);
							}
							else
							{
								pincode.setText("");
							}
						}
					}

					break;
				case R.id.radioOther:
					rb_str = "other";

					otherAddress.setText("");
					pincode.setText("");

					linear_et.setVisibility(View.VISIBLE);
					ll_pincode.setVisibility(View.VISIBLE);
					ll7.setVisibility(View.GONE);
					isOtherSelected = true;
					break;

				default:
					break;
				}
			}
		});

		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				for(int i=0;i<collectiocenterlist.size();i++)
				{
					list1.getChildAt(i).setBackgroundColor(Color.WHITE);
				}
				list1.getChildAt(arg2).setBackgroundColor(Color.GRAY);

				String selectedname = ((CollectionCenterDetails) arg0.getItemAtPosition(arg2)).getName();
				collectioncenter_id = ((CollectionCenterDetails) arg0.getItemAtPosition(arg2)).getId();
			}
		});

		btnProceed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(btnflg == true)
				{
					//btnProceed.setClickable(false);
					//btnProceed.setFocusable(false);
				}
				else
				{
					btnProceed.setClickable(true);
					btnProceed.setFocusable(true);
				}

				pin_val = pincode.getText().toString();
				dname_val = dname.getText().toString();
				home_val = radio_Home.getText().toString();
				lab_val = radio_Lab.getText().toString();
				add_val = otherAddress.getText().toString();

				/*SharedPreferences.Editor editor1 = sharedpreferences.edit();

				editor1.remove("popularTestData");

				editor1.commit();
*/
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("SelectedPatientName", sharedpreferences.getString("MainPatientName",""));

				editor.commit();

				if(selected_labidfrompref != null && !(selected_labidfrompref.equalsIgnoreCase("")))
				{
					selected_labid = selected_labidfrompref;
				}

				Boolean flg = false;

				if (isInternetOn(getApplicationContext()))
				{
					/*if (str.equalsIgnoreCase("home") && add_val.equals(""))
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter the Address     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);

					    toast.show();
						//btnflg = false;
					}*/
					/*else
					{*/
						btnflg = true;
						pincode_myurl = pincode_myurl + selected_labidfrompref + "&" + "pincode=" + pin_val;
					//	new GetPincode().execute();
						proceed();
					//}
				}
				else
				{
					alertBox("Internet Connection is not available..!");
				//	btnflg = false;
				}
			}
		});

		btnDocSelection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(dnameList.size() != originaldnameList.size())
				{
					dnameList.clear();
					dnameList.addAll(originaldnameList);
				}
				showdialog();
			}
		});

		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dname.setText("");
			}
		});

		btnLabchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor1 = sharedpreferences.edit();
				editor1.remove("CheckedValue");
				editor1.commit();

			if(multiusertype.equals("Patient"))
			{
				Intent i1 = new Intent(AddressSelection.this, LabSelection.class);
				startActivity(i1);
			}else
			{
				Intent i = new Intent(AddressSelection.this, LabSelectionForDoctor.class);
				startActivity(i);
			}

			}
		});

		btnQuickBook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	btnQuickBook.setEnabled(false);

				pin_val_quick = pincode.getText().toString();
				dname_val = dname.getText().toString();
				home_val = radio_Home.getText().toString();
				lab_val = radio_Lab.getText().toString();
				add_val = otherAddress.getText().toString();

				if (isInternetOn(getApplicationContext()))
				{
					if (str.equalsIgnoreCase("home") && add_val.equals(""))
					{
						LayoutInflater inflater = getLayoutInflater();
					    View layout = inflater.inflate(R.layout.my_dialog,null);

					    TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
					    text.setText("     Please enter the Address     ");
					    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
					    text.setTypeface(typeface);
					    text.setTextColor(Color.WHITE);
					    text.setTextSize(18);
					    Toast toast = new Toast(getApplicationContext());
					    toast.setDuration(Toast.LENGTH_LONG);
					    toast.setView(layout);
					    btnQuickBook.setEnabled(true);
					    toast.show();
					}
					else
					{
						pincode_myurl = pincode_myurl + selected_labidfrompref + "&" + "pincode=" + pin_val_quick;
						//new GetPincodeforquickbook().execute();
						new uploadAllData().execute(new String[] { appointment_url });

					}
				}
				else
				{
					btnQuickBook.setEnabled(true);
					alertBox("Internet Connection is not available..!");
				}
			}
		});
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

		alert.setIcon(R.drawable.sign_logoelab);
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


	/*** Async task class to get json by making HTTP call **/
	private class GetCollectionCenterList extends
	AsyncTask<String, Void, ArrayList<CollectionCenterDetails>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			collectiocenterlist.clear();

			// Showing progress dialog
			pDialog = new ProgressDialog(AddressSelection.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
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

						String listd = c.getString(TAG_CENTERNAME);
						center_id = c.getString("CollectionCenterID");

						// adding contact to contact list
						det.setName(listd);
						det.setId(center_id);

						collectiocenterlist.add(det);
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

			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			labAdapter = new CollectionCenterAdapter(AddressSelection.this, collectiocenterlist);

			list1.setAdapter(labAdapter);

			setListViewHeightBasedOnChildren(list1);

			collection_center_myurl = collection_center_url;
		}
	}


	/*** Async task class to get json by making HTTP call **/
	private class GetPincode extends
	AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pincodelist.clear();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String response = "";

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(pincode_myurl, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					Boolean abc = jsonObj.getBoolean("d");
					response = abc.toString();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {


			if(result.equalsIgnoreCase("false"))
			{
				if(str.equals("home"))
				{
					//btnflg = false;
					isValidPincode = false;
					alertBox("Service is not available in your area");
				}
				else
				{
					isValidPincode = true;
					proceed();
				}
			}
			else
			{
				isValidPincode = true;
				proceed();
			}

			pincode_myurl = pincode_url;
		}
	}

	/*** Async task class to get json by making HTTP call **/
	private class GetAddress extends
	AsyncTask<String, Void, ArrayList<String>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			addresslist.clear();
		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(address_myurl, ServiceHandler.GET);

			if (jsonStr != null)
			{
				try {

					JSONObject jsonObj = new JSONObject(jsonStr);

					JSONObject address_object = jsonObj.getJSONObject("d");

					res_add = address_object.getString("ResidenceAddress");
					res_pin = address_object.getString("ResidencePincode");
					off_add = address_object.getString("OfficeAddress");
					off_pin = address_object.getString("OfficePincode");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return addresslist;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);

			if(str.equalsIgnoreCase("home"))
			{
				if(rb_str.equalsIgnoreCase("residence"))
				{
					if(res_add != null && !(res_add.isEmpty()) && !(res_add.equals("null")))
					{
						otherAddress.setText(res_add);
					}
					else
					{
						otherAddress.setText("");
					}
				}
			}


			if(str.equalsIgnoreCase("home"))
			{
				if(rb_str.equalsIgnoreCase("residence"))
				{
					if(res_pin != null && !(res_pin.isEmpty()) && !(res_pin.equals("null")))
					{
						pincode.setText(res_pin);
					}
					else
					{
						pincode.setText("");
					}
				}
			}

			if(str.equalsIgnoreCase("home"))
			{
				if(rb_str.equalsIgnoreCase("office"))
				{
					if(off_add != null && !(off_add.isEmpty()) && !(off_add.equals("null")))
					{
						otherAddress.setText(off_add);
					}
					else
					{
						otherAddress.setText("");
					}
				}
			}


			if(str.equalsIgnoreCase("home"))
			{
				if(rb_str.equalsIgnoreCase("office"))
				{
					if(off_pin != null && !(off_pin.isEmpty()) && !(off_pin.equals("null")))
					{
						pincode.setText(off_pin);
					}
					else
					{
						pincode.setText("");
					}
				}
			}

			address_myurl = address_url;
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

				SharedPreferences.Editor editor = sharedpreferences.edit();
				//editor.remove("allTestNames");
				//editor.remove("popularTestData");
				//editor.remove("setprofilename");
				//editor.remove("setcategoryname");

				editor.remove("ResidenceAddressOnDocSelection");
				editor.remove("ResidencePincodeOnDocSelection");

				editor.remove("OfficeAddressOnDocSelection");
				editor.remove("OfficePincodeOnDocSelection");

				editor.remove("CurrentAddressOnDocSelection");
				editor.remove("CurrentPincodeOnDocSelection");

				editor.remove("OtherAddressOnDocSelection");
				editor.remove("OtherPincodeOnDocSelection");

				editor.remove("CheckedValue");
				editor.remove("FromDoctor");

				editor.commit();

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
		});

		AlertDialog alert1 = alertbox.create();
		alert1.show();

		TextView textView = (TextView) alert1.findViewById(android.R.id.message);
		Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
		Button noButton = alert1.getButton(DialogInterface.BUTTON_NEGATIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font);
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
	}

	public void proceed()
	{
		if(isValidPincode)
		{
			if(str.equals("home"))
			{
				if(rb_str.equals("residence"))
				{
					if(res_add.equals(add_val) || res_pin.equals(pin_val))
					{
						String isupdated = "0";
						String ishome = "true";

					/*	SharedPreferences.Editor edt = sharedpreferences.edit();

						edt.remove("popularTestData");

						edt.commit();*/

						SharedPreferences.Editor editor = sharedpreferences.edit();

						editor.putString("Doctor", dname_val);
						editor.putString("doc_id", doc_id2);
						editor.putString("address", add_val);
						editor.putString("pincode", pin_val);
						editor.putString("updated", isupdated);
						editor.putString("home_collection", ishome);

						editor.commit();


						Intent i = new Intent(AddressSelection.this,DateTimeSelection.class);
						i.putExtra("doctorid", doc_id2);
						/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
						startActivity(i);
						//finish();
					}
					else
					{
						String isupdated = "1";
						String ishome = "true";

						/*SharedPreferences.Editor edt = sharedpreferences.edit();

						edt.remove("popularTestData");

						edt.commit();*/

						SharedPreferences.Editor editor = sharedpreferences.edit();

						editor.putString("Doctor", dname_val);
						editor.putString("doc_id", doc_id2);
						editor.putString("address", add_val);
						editor.putString("pincode", pin_val);
						editor.putString("updated", isupdated);
						editor.putString("home_collection", ishome);

						editor.commit();

						Intent i = new Intent(AddressSelection.this,DateTimeSelection.class);
						i.putExtra("doctorid", doc_id2);
						/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
						startActivity(i);
						//finish();
					}

				}
				else if(rb_str.equals("office"))
				{
					if(off_add.equals(add_val) || off_pin.equals(pin_val))
					{
						String isupdated = "0";
						String ishome = "true";

						/*SharedPreferences.Editor edt = sharedpreferences.edit();

						edt.remove("popularTestData");

						edt.commit();*/

						SharedPreferences.Editor editor = sharedpreferences.edit();

						editor.putString("Doctor", dname_val);
						editor.putString("doc_id", doc_id2);
						editor.putString("address", add_val);
						editor.putString("pincode", pin_val);
						editor.putString("updated", isupdated);
						editor.putString("home_collection", ishome);

						editor.commit();

						Intent i = new Intent(AddressSelection.this,DateTimeSelection.class);
						i.putExtra("doctorid", doc_id2);
					/*	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
						startActivity(i);
						//finish();
					}
					else
					{
						String isupdated = "2";
						String ishome = "true";

					/*	SharedPreferences.Editor edt = sharedpreferences.edit();

						edt.remove("popularTestData");

						edt.commit();*/

						SharedPreferences.Editor editor = sharedpreferences.edit();

						editor.putString("Doctor", dname_val);
						editor.putString("doc_id", doc_id2);
						editor.putString("address", add_val);
						editor.putString("pincode", pin_val);
						editor.putString("updated", isupdated);
						editor.putString("home_collection", ishome);

						editor.commit();

						Intent i = new Intent(AddressSelection.this,DateTimeSelection.class);
						i.putExtra("doctorid", doc_id2);
					/*	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
						startActivity(i);
						//finish();
					}
				}
				else if(rb_str.equals("other"))
				{

					String isupdated = "0";
					String ishome = "true";

				/*	SharedPreferences.Editor edt = sharedpreferences.edit();

					edt.remove("popularTestData");

					edt.commit();
*/
					SharedPreferences.Editor editor = sharedpreferences.edit();

					editor.putString("Doctor", dname_val);
					editor.putString("doc_id", doc_id2);
					editor.putString("address", add_val);
					editor.putString("pincode", pin_val);
					editor.putString("updated", isupdated);
					editor.putString("home_collection", ishome);

					editor.commit();

					Intent i = new Intent(AddressSelection.this,DateTimeSelection.class);
					i.putExtra("doctorid", doc_id2);
					/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
					startActivity(i);
					//finish();
				}
				else
				{

					String isupdated = "0";
					String ishome = "true";
					
					/*SharedPreferences.Editor edt = sharedpreferences.edit();

					edt.remove("popularTestData");
					
					edt.commit();*/
					
					SharedPreferences.Editor editor = sharedpreferences.edit();

					editor.putString("Doctor", dname_val);
					editor.putString("doc_id", doc_id2);
					editor.putString("address", add_val);
					editor.putString("pincode", pin_val);
					editor.putString("updated", isupdated);
					editor.putString("home_collection", ishome);

					editor.commit();

					Intent i = new Intent(AddressSelection.this,DateTimeSelection.class);
					i.putExtra("doctorid", doc_id2);
					/*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
					startActivity(i);
					//finish();
				}
			}
			else 
			{
				String isupdated = "0";
				String ishome = "false";
				
			/*	SharedPreferences.Editor edt = sharedpreferences.edit();

				edt.remove("popularTestData");
				
				edt.commit();*/
				
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("Doctor", dname_val);
				editor.putString("doc_id", doc_id2);
				editor.putString("centerid", collectioncenter_id);
				editor.putString("updated", isupdated);
				editor.putString("home_collection", ishome);
				editor.commit();

				//Intent i = new Intent(AddressSelection.this,BookAppointment.class);
				Intent i = new Intent(AddressSelection.this,DateTimeSelection.class);
				i.putExtra("doctorid", doc_id2);
				i.putExtra("labidval", selected_labid);
				startActivity(i);
				//finish();
			}
		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);

			if(listItem != null){
				// This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
				listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
				listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
				totalHeight += listItem.getMeasuredHeight();

			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}


	/*** Async task class to get json by making HTTP call **/
	private class GetDoctorName extends
	AsyncTask<String, Void, ArrayList<DoctorNameDetails>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dnameList.clear();

			// Showing progress dialog
			pDialog = new ProgressDialog(AddressSelection.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<DoctorNameDetails> doInBackground(String... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh
					.makeServiceCall(dname_myurl, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					Doctorname = jsonObj.getJSONArray("d");

					// looping through All Contacts
					for (int i = 0; i < Doctorname.length(); i++) {

						DoctorNameDetails doc = new DoctorNameDetails();
						JSONObject c = Doctorname.getJSONObject(i);

						String listd = c.getString(TAG_DOCTORNAME);
						String id = c.getString("DoctorID");

						doc.setUserName(listd);
						doc.setUserId(id);

						dnameList.add(doc);
						originaldnameList.add(doc);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return dnameList;
		}

		@Override
		protected void onPostExecute(ArrayList<DoctorNameDetails> result) {
			super.onPostExecute(result);
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			dname_myurl = dname_url;
		}
	}


	private class uploadAllData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog pDialog = new
			pDialog = new ProgressDialog(AddressSelection.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return POST(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			if (result.equals("Appointment_Booked")) {
				btnQuickBook.setEnabled(true);
				showAlert("Your Appointment booked successfully at "+selected_labnamefrompref+". Please make yourself available on time at your booking location.");

				clearPreferences();

			} else {
				btnQuickBook.setEnabled(true);
				showAlert1("Appointment not booked");
				date_val = date_val_temp;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static String POST(String url) {
		InputStream inputStream = null;
		String result = "";
		String encodedImage = "";

		pin_val_quick = pincode.getText().toString();
		add_val = otherAddress.getText().toString();

		if (!(filePath.equals("")) ) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
			/*
			 * MultipartEntity entity1 = new MultipartEntity(
			 * HttpMultipartMode.BROWSER_COMPATIBLE);
			 */
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
			byte[] data = bos.toByteArray();

			encodedImage = Base64.encodeToString(data, Base64.NO_WRAP);
		} else {
			encodedImage = "";
		}

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(selected_labidfrompref != null && !(selected_labidfrompref.equalsIgnoreCase("")))
			{
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"PatientID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Username\"", "\"" + phone + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedTest\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedProfile\"","\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedPopularTest\"","\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"EnteredTest\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"Prescription\"", "\"\""));

			nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"","\"" + strDate + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"AppointmentAddress\"","\"\""));

			if (dname_val.equalsIgnoreCase("") || dname_val.equalsIgnoreCase("null")) {

				nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\"" + 0 + "\""));
				nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"",
						"\"SELF\""));


			} else {
				if(selectedDocId.equalsIgnoreCase("null") || selectedDocId.equalsIgnoreCase(""))
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\"" + 0 + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"","\"" + dname_val + "\""));
				}
				else
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\""
							+ selectedDocId + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"",
							"\"\""));
				}
			}

			nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));

			if(pname_fromprofile.equalsIgnoreCase("null") || pname_fromprofile.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
						"\"SELF\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
						"\"" + pname_fromprofile + "\""));
			}

			nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"false\""));

			nameValuePairs.add(new BasicNameValuePair("\"Age\"",
					"\"" + 0 + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
					"\"" + 0 + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"Pincode\"","\"" + pin_val_quick + "\""));

			if(selectedCenterId.equalsIgnoreCase("null") || selectedCenterId.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"CollectionCenterID\"","\"" + 0 + "\""));
			}
			else
			{

				nameValuePairs.add(new BasicNameValuePair("\"CollectionCenterID\"","\"" + selectedCenterId + "\""));
			}

			if(homecollection.equalsIgnoreCase("true"))
			{
				nameValuePairs.add(new BasicNameValuePair("\"IsHomeCollection\"","\"true\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"IsHomeCollection\"","\"false\""));
			}

			if(str.equals("home"))
			{
				if(rb_str.equals("residence"))
				{
					if(res_add.equals(add_val) && res_pin.equals(pin_val_quick))
					{
						nameValuePairs.add(new BasicNameValuePair("\"IsUpdated\"","\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"IsUpdated\"","\"" + 1 + "\""));
					}
				}
				
				if(rb_str.equals("office"))
				{
					if(off_add.equals(add_val) && off_pin.equals(pin_val_quick))
					{
						nameValuePairs.add(new BasicNameValuePair("\"IsUpdated\"","\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"IsUpdated\"","\"" + 2 + "\""));
					}
				}
			}

			nameValuePairs.add(new BasicNameValuePair("\"DeviceType\"", "\"" + 1 + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"patientaddress\"","\""+add_val+"\""));

			nameValuePairs.add(new BasicNameValuePair("\"emailid\"","\"\""));

			String finalString = nameValuePairs.toString();
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			String strToServer = "{" + finalString + "}";
			Log.d("strToServer", "POST: "+strToServer);
			StringEntity se = new StringEntity(strToServer);
			se.setContentType("application/json;charset=UTF-8");
			httpPost.setEntity(se);

			// Execute POST request to the given URL
			HttpResponse httpResponse = httpClient.execute(httpPost);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				String jsonformattString = result.replaceAll("\\\\", "");
				try {
					JSONObject jsonObj = new JSONObject(jsonformattString);
					JSONObject resultObj = (JSONObject) jsonObj.get("d");
					finalResult = resultObj.getString("Result");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				finalResult = "Did not work!";
		} catch (Exception e) {
			// Log.i("InputStream", e.getLocalizedMessage());
		}
		return finalResult;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";

		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

	public void showAlert(String message) {
		Builder builder = new Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText(Html.fromHtml("<font color='#33cc33'>!!Success!!</font>"));
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				clearPreferences();
				
				Intent i = new Intent(AddressSelection.this,
						AddressSelection.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
	}

	public void showAlert1(String message) {
		Builder builder = new Builder(this);

		TextView Mytitle = new TextView(this);
		Mytitle.setText(Html.fromHtml("<font color='#ff0000'>Failed</font>"));
		Mytitle.setTextSize(20);
		Mytitle.setPadding(5, 15, 5, 5);
		Mytitle.setGravity(Gravity.CENTER);
		Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
		builder.setCustomTitle(Mytitle);

		builder.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				Intent i = new Intent(AddressSelection.this,
						AddressSelection.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		Button yesButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font); 
		yesButton.setTypeface(custom_bold_font);
	}

	public void clearPreferences() {
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.remove("testid");
		editor.remove("testprofileid");
		editor.remove("test_name");
		editor.remove("filePath");
		editor.remove("Doctor");
		editor.remove("DateTime");
		editor.remove("datetime");
		editor.remove("doc_id");
		editor.remove("ActivityName");
		editor.remove("updated");
		editor.remove("popularids");
		editor.remove("SelectedPatient");
		editor.remove("SelectedPatientAge");
		editor.remove("SelectedPatientGender");
		editor.remove("address");
		editor.remove("pincode");
		editor.remove("populartestidfromintent");
		editor.remove("Enteredtestidfromintent");
		editor.remove("homecollection");
		editor.remove("centerid");
		editor.remove("ResidenceAddressOnDocSelection");
		editor.remove("ResidencePincodeOnDocSelection");
		editor.remove("OfficeAddressOnDocSelection");
		editor.remove("OfficePincodeOnDocSelection");
		editor.remove("CurrentAddressOnDocSelection");
		editor.remove("CurrentPincodeOnDocSelection");
		editor.remove("OtherAddressOnDocSelection");
		editor.remove("OtherPincodeOnDocSelection");
		editor.remove("CheckedValue");
		editor.remove("FromDoctor");

		selectedDocId = "";

		editor.commit();
	}


	/*** Async task class to get json by making HTTP call **/
	private class GetPincodeforquickbook extends
	AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pincodelist.clear();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String response = "";

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(pincode_myurl, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					Boolean abc = jsonObj.getBoolean("d");
					response = abc.toString();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(result.equalsIgnoreCase("false"))
			{
				if(str.equals("home"))
				{
					btnQuickBook.setEnabled(true);
					//btnflg = false;
					isValidPincode = false;
					alertBox("Service is not available in your area");
				}
				else
				{
					isValidPincode = true;
					new uploadAllData().execute(new String[] { appointment_url });
				}
			}
			else
			{
				isValidPincode = true;
				new uploadAllData().execute(new String[] { appointment_url });
			}

			pincode_myurl = pincode_url;
		}
	}

	private void showdialog()
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_description, null);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		Builder builder = new Builder(this);

		builder.setView(layout);

		final EditText search = (EditText) layout.findViewById(R.id.inputSearch);
		final ListView modeList = (ListView) layout.findViewById(R.id.listViewDoctors);
		search.setTypeface(custom_font);

		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		modeList.setOnItemClickListener(this);

		adapter = new DoctorNameAdapter(this, dnameList);
		modeList.setAdapter(adapter);

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				String text = search.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);
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

		});

		dialog = builder.create();

		dialog.show();

		Button noButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
		noButton.setTypeface(custom_bold_font);
		noButton.setTextSize(20);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String name = ((DoctorNameDetails) arg0.getItemAtPosition(arg2)).getUserName();
		doc_id2 = ((DoctorNameDetails) arg0.getItemAtPosition(arg2)).getUserId();
		
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.putString("Doctor", name);
		editor.putString("doc_id", doc_id2);
		editor.commit();

		dialog.dismiss();
		dname.setText(name);
		selectedDocId = doc_id2;
	}



    /*** Async task class to get json by making HTTP call **/
    private class GetTestName extends
            AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            test_name.clear();
            testid.clear();
        }

        @Override
        protected ArrayList<String> doInBackground(String... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(myurl, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					int tempPopuloSize=0;
					// Getting JSON Array node
					Testname = jsonObj.getJSONArray("d");

					int ServiceLenghTestSize = Testname.length();

					popular_test_nameTemp.clear();
					for (int i = 0; i < Testname.length(); i++) {
						JSONObject c = Testname.getJSONObject(i);
						String testname = c.getString(TAG_TESTNAME);
						String tid = c.getString(TAG_TESTID);
						String ispopular = c.getString("IsPopular");
						String price = c.getString("TestPrice");
						String discount_price = c.getString("TestDiscountPrice");

						if (ispopular.equalsIgnoreCase("true")) {
							String strPopularTestData = testname + "#" + tid + "#" + price + "#" + discount_price;
							popular_test_nameTemp.add(strPopularTestData);
						}
						 tempPopuloSize =popular_test_nameTemp.size();
					}


					if((ServiceLenghTestSize == setTestSizee )&& (tempPopuloSize ==setTestPopulorSizee) ){
					}else{

						popular_test_name.clear();
						test_name_all.clear();
						allTestName.clear();
						popularTestName.clear();

					// looping through All Contacts
					for (int i = 0; i < Testname.length(); i++) {
						// String listd = new String();
						JSONObject c = Testname.getJSONObject(i);

						String testname = c.getString(TAG_TESTNAME);
						String tid = c.getString(TAG_TESTID);
						String ispopular = c.getString("IsPopular");
						String price = c.getString("TestPrice");
						String discount_price = c.getString("TestDiscountPrice");
						String Short_Name = c.getString("ShortName");


						double discount_priceRate = Double.parseDouble(price) - Double.parseDouble(discount_price);

						String discount_priceRateStrg = String.valueOf(discount_priceRate);

						String strAllTestData = testname + "#" + tid + "#" + price + "#" + discount_priceRateStrg + "# " + Short_Name ;
						test_name_all.add(strAllTestData);
						testid_all.add(tid);
						test_price.add(price);
						test_discountprice.add(discount_price);

						if (ispopular.equalsIgnoreCase("true")) {
							String strPopularTestData = testname + "#" + tid + "#" + price + "#" + discount_priceRateStrg +  "# " + Short_Name;
							popular_test_name.add(strPopularTestData);
						}
					}

					allTestName.addAll(test_name_all);
					popularTestName.addAll(popular_test_name);
					SharedPreferences.Editor edt = sharedpreferences.edit();
					edt.putStringSet("allTestNames", allTestName);
					edt.putStringSet("popularTestData", popularTestName);
					edt.commit();

				}

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return test_name;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);

            myurl = url;
        }
    }




    //*** Async task class to get json by making HTTP call **/
    private class GetTestProfile extends
            AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            test_profile_name.clear();
            testpid.clear();
        }

        @Override
        protected ArrayList<String> doInBackground(String... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(profilemyurl,
                    ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    Testprofile = jsonObj.getJSONArray("d");
					Log.d("Testprofile2166",""+Testprofile);
					int ServiceLenghTestProfileSize = Testprofile.length();
					if(ServiceLenghTestProfileSize == setProfileSizee){
					}else {

						test_profile_name.clear();
						profileNameSet.clear();

						// looping through All Contacts
						for (int i = 0; i < Testprofile.length(); i++) {
							// String listd = new String();
							JSONObject c = Testprofile.getJSONObject(i);

							String listd = c.getString(TAG_TESTPROFILE);
							String test_pid = c.getString(TAG_TESTPROFILEID);
							String price = c.getString("TestPrice");
							String discount_price = c.getString("TestDiscountPrice");
							String strProfileData = listd + "#" + test_pid + "#" + price + "#" + discount_price;
							test_profile_name.add(strProfileData);
						}

						  profileNameSet.addAll(test_profile_name);
						SharedPreferences.Editor edt = sharedpreferences.edit();
						edt.putStringSet("setprofilename", profileNameSet);
						edt.commit();

					}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return test_profile_name;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);

            profilemyurl = profileurl;
        }
    }

    //*** Async task class to get json by making HTTP call **//
    private class GetTestCategory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            for (int i = 0; i < testcategory_Listview.size(); i++) {
                testcategory_Listview.remove(i);

            }

        /*    // Showing progress dialog
            pDialog = new ProgressDialog(BookAppointment.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(category_myurl,
                    ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    Testcategory = jsonObj.getJSONArray("d");

					int ServiceLenghTestCatSize = Testcategory.length();
					if(ServiceLenghTestCatSize == setCategorySizee){
					}else {

						testcategory_Listview.clear();
						categoryNameSet.clear();

						// looping through All Contacts
						for (int i = 0; i < Testcategory.length(); i++) {
							// String listd = new String();
							JSONObject c = Testcategory.getJSONObject(i);

							String listd = c.getString(TAG_TESTCATEGORY);

							// adding contact to contact list
							testcategory_Listview.add(listd);
						}
						categoryNameSet.addAll(testcategory_Listview);
						SharedPreferences.Editor edt = sharedpreferences.edit();
						edt.putStringSet("setcategoryname", categoryNameSet);
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

            // Dismiss the progress dialog
         /*   if (pDialog.isShowing())
                pDialog.dismiss();*/

           // paintTabs();

            category_myurl = category_url;
        }
    }


	////////////////////////////////////////  Authentication  /////////////////////////////////

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
				//Toast.makeText(AddressSelection.this, "Verified", Toast.LENGTH_SHORT).show();



				new GetTestName().execute();
				new GetTestProfile().execute();
				new GetTestCategory().execute();

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

				editor.remove("testid");
				editor.remove("testprofileid");
				editor.remove("test_name");
				editor.remove("filePath");
				editor.remove("address");
				editor.remove("doctor");
				editor.remove("DateTime");
				editor.remove("datetime");
				editor.remove("loginchkbox");
				editor.remove("checkboxchecked");
				editor.remove("allTestNames");
				editor.remove("popularTestData");
				editor.remove("setprofilename");
				editor.remove("setcategoryname");
				editor.remove("CheckedValue");
				//editor.remove("patient_name");

				editor.remove("SelectedLabId");
				editor.remove("SelectedLabName");

				editor.commit();

				Intent i1 = new Intent(AddressSelection.this, SigninActivity.class);
				i1.putExtra("fromLogout","yes");
				i1.putExtra("fromchange", "no");
				//i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i1);
				//finish();

			}
		}
	}

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


/////////////////////////////////////////////////////////////////////////

}
