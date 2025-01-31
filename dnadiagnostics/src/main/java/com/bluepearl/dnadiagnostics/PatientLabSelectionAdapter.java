package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PatientLabSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private Activity activity;
    private List<PatientLabSelectionDetailes> contacts;
    private List<PatientLabSelectionDetailes> contactListFiltered;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //  private List<PatientLabSelectionDetailes> contact;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    SharedPreferences sharedpreferences;
    static String labIdForProfileUpdate = "";
    static String selected_labidfrompref = null;
    static String pid = null;
    AlertDialog alertbox;
    static String status = "";
    private String preferdurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/UpdateProfile";

    // URL to get Test Name
    private static final String testurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?TestName=&LabID=";
    private static String testmyurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchTest?TestName=&LabID=";

    // URL to get Test Profile
    private static final String profileurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchProfile?LabID=";
    private static String profilemyurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/SearchProfile?LabID=";

    // URL to get all categories
    private static final String category_url = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetTestCategories?LabID=";
    private static String category_myurl = "http://devglobal.elabassist.com/Services/GlobalUserService.svc/GetTestCategories?LabID=";

    int setTestSizee,setCategorySizee,setProfileSizee,setTestPopulorSizee;

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



    public PatientLabSelectionAdapter(RecyclerView recyclerView, ArrayList<PatientLabSelectionDetailes> contacts, Activity activity) {
        this.contacts = contacts;
        this.activity = activity;
        this.contactListFiltered = contacts;

        sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        pid = sharedpreferences.getString("patientId", "");
        selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
        Log.d("pid", "PatientLabSelectionAdapter: "+pid);



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
        Set<String> setallCollectionCenterNameSet = new HashSet<String>();
        Set<String> setallaffiliationNameSet = new HashSet<String>();

        Set<String> selectedpopularsetinmain = new HashSet<String>();
        Set<String> selectedtestnamesetinmain = new HashSet<String>();
        Set<String> selectedtestcategorysetinmain = new HashSet<String>();
        Set<String> selectedtestprofilesetinmain = new HashSet<String>();

//        selectedpopularsetinmain = sharedpreferences.getStringSet("popularTestData", null);
//        selectedtestnamesetinmain = sharedpreferences.getStringSet("allTestNames", null);
//        selectedtestprofilesetinmain = sharedpreferences.getStringSet("setprofilename", null);
//        selectedtestcategorysetinmain = sharedpreferences.getStringSet("setcategoryname", null);

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

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        // return contacts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        return contacts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_patientlab_recycler_view_row, parent, false);
            return new UserViewHolder(view);
        } /*else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }*/
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        UserViewHolder userViewHolder = (UserViewHolder) holder;
        final PatientLabSelectionDetailes  contactttt = contactListFiltered.get(position);
        if (holder instanceof UserViewHolder) {
            // PatientLabSelectionDetailes contact = contacts.get(position);




            if(contactttt.getpreferred().equalsIgnoreCase("true"))
            {
                userViewHolder.Labname.setText(contactttt.getLabName() + " (Preferred Lab)");
            }
            else
            {
                userViewHolder.Labname.setText(contactttt.getLabName());
            }


            if(contactttt.gethomeflg().equalsIgnoreCase("false"))
            {
                userViewHolder.txtHomeCollection.setVisibility(View.GONE);
            }
            else
            {
                userViewHolder.txtHomeCollection.setVisibility(View.VISIBLE);
            }


            userViewHolder.LabAdress.setText(contactttt.getLabAddress());
            //   userViewHolder.LabDistance.setText(contactttt.getLabDistance()+" km");

            // holder.txtDistance.setText(userlist.get(position).getLabDistance() +" km");

        } /*else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }*/

        userViewHolder.TVReport.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(activity, "report"+contactttt.getLabId(), Toast.LENGTH_SHORT).show();





                        String disid = contactttt.getLabId();
                        String lab_name = contactttt.getLabName();
                        String mnumber = contactttt.getLabMobileNumber();
                        String emailid = contactttt.getLabEmailId();
                        labIdForProfileUpdate = disid;

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        //editor.putString("LabId", disid);
                        editor.putString("SelectedLabId", disid);
                        editor.putString("SelectedLabName", lab_name);
                        editor.putString("SelectedLabEmail", emailid);
                        editor.putString("SelectedLabMobile", mnumber);
                        editor.commit();
                        Log.d("pid", "PatientLabSelectionAdapter: "+disid+lab_name);

                        testmyurl = testurl;
                        profilemyurl = profileurl;
                        category_myurl = category_url;

                        testmyurl = testmyurl + labIdForProfileUpdate;
                        profilemyurl = profilemyurl + labIdForProfileUpdate;
                        category_myurl = category_myurl + labIdForProfileUpdate;


                        new GetTestName().execute();
                        new GetTestProfile().execute();
                        new GetTestCategory().execute();

                        Intent i1 = new Intent(activity,PatientList.class);
                        //   i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i1);
                    }
                }
        );

        userViewHolder.TVSelectLab.setOnClickListener(


                new View.OnClickListener() {



                    String disid = contactttt.getLabId();
                    String lab_name = contactttt.getLabName();
                    String mnumber = contactttt.getLabMobileNumber();
                    String emailid = contactttt.getLabEmailId();

                    //  String homecollection = contactttt.gethomeflg();

                    String homecollection = "true";


                    @Override
                    public void onClick(View view) {
                        //   Toast.makeText(activity, contactttt.getLabName(), Toast.LENGTH_SHORT).show();



                        //  Object position_clicked = v.getTag();
                        labIdForProfileUpdate = disid;

                        testmyurl = testurl;
                        profilemyurl = profileurl;
                        category_myurl = category_url;

                        testmyurl = testmyurl + labIdForProfileUpdate;
                        profilemyurl = profilemyurl + labIdForProfileUpdate;
                        category_myurl = category_myurl + labIdForProfileUpdate;

                        new GetTestName().execute();
                        new GetTestProfile().execute();
                        new GetTestCategory().execute();

                        if(disid.equals(selected_labidfrompref))
                        {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("homecollectioncheck", homecollection);
                            editor.commit();

                            Intent i1 = new Intent(activity,AddressSelection.class);
                            //  i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(i1);
                        }
                        else
                        {
                            alertbox = new AlertDialog.Builder(activity)
                                    .setMessage("Do you want to set this lab as your preferred lab?")
                                    .setTitle("Set Preferred Lab")
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {

                                                // do something when the button is clicked
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                                    editor.putString("LabId", disid);
                                                    editor.putString("SelectedLabId", disid);
                                                    editor.putString("SelectedLabName", lab_name);
                                                    editor.putString("homecollectioncheck", homecollection);
                                                    editor.putString("SelectedLabEmail", emailid);
                                                    editor.putString("SelectedLabMobile", mnumber);

                                                    editor.remove("allTestNames");
                                                    editor.remove("popularTestData");
                                                    editor.remove("setprofilename");
                                                    editor.remove("setcategoryname");

                                                    editor.commit();

                                                    new ProfileCheck().execute(new String[] { preferdurl });
                                                    Intent i1 = new Intent(activity,AddressSelection.class);
                                                    //  i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    activity.startActivity(i1);
                                                }
                                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            SharedPreferences.Editor editor = sharedpreferences.edit();

                                            editor.putString("SelectedLabId", disid);
                                            editor.putString("SelectedLabName", lab_name);
                                            editor.putString("homecollectioncheck", homecollection);
                                            editor.putString("SelectedLabEmail", emailid);
                                            editor.putString("SelectedLabMobile", mnumber);
                                            // editor.remove("LabId");

                                            editor.commit();

                                            Intent i1 = new Intent(activity,AddressSelection.class);
                                            i1.putExtra("homecollectioncheck", homecollection);
                                            //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            activity.startActivity(i1);
                                        }
                                    }).show();

                            TextView textView = (TextView) alertbox.findViewById(android.R.id.message);
                            Button yesButton = alertbox.getButton(DialogInterface.BUTTON_POSITIVE);
                            Button noButton = alertbox.getButton(DialogInterface.BUTTON_NEGATIVE);
                            Typeface custom_font = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Regular.ttf");
                            Typeface custom_bold_font = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Bold.ttf");
                            textView.setTypeface(custom_font);
                            yesButton.setTypeface(custom_bold_font);
                            noButton.setTypeface(custom_bold_font);
                        }



                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return contactListFiltered == null ? 0 : contactListFiltered.size();
//        int totalItems = contactListFiltered == null ? 0 : contactListFiltered.size();

        // Return either the total number of items or 100, whichever is smaller
//        return Math.min(totalItems, 500);
    }



    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contacts;
                } else {
                    List<PatientLabSelectionDetailes> filteredList = new ArrayList<>();
                    for (PatientLabSelectionDetailes row : contacts) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        //   if (row.getLabName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                        if (row.getLabName().toLowerCase().contains(charString.toLowerCase()) ) {
                            Log.d("performFiltering", "performFiltering: ");
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<PatientLabSelectionDetailes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView LabAdress;
        private TextView Labname,TVReport,TVSelectLab,txtHomeCollection;

        private UserViewHolder(View view) {
            super(view);
            LabAdress = (TextView) view.findViewById(R.id.LabAdress_id);
            Labname = (TextView) view.findViewById(R.id.Labname_id);

            TVReport = (TextView) view.findViewById(R.id.Report_id);

            TVSelectLab = (TextView) view.findViewById(R.id.SelectLab_id);

            txtHomeCollection = (TextView) view.findViewById(R.id.tvhome);



        }
    }




    private class ProfileCheck extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
            return profilePOST(params[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    public static String profilePOST(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + pid + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"PrefferedLabID\"", "\"" + labIdForProfileUpdate + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"Task\"", "\"2\""));

            String finalString = nameValuePairs.toString();
            finalString = finalString.replace('=', ':');
            finalString = finalString.substring(1, finalString.length() - 1);
            finalString = "{" + finalString + "}";
            String tempString = "\"UserProfile\"" + ":";
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
/////////////////////////////////////////////////////////////////////////////////////////////////



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
            String jsonStr = sh.makeServiceCall(testmyurl, ServiceHandler.GET);

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

            testmyurl = testurl;
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

                            String strProfileData = listd + "#" + test_pid;
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

            if (jsonStr != null && !jsonStr.isEmpty()) {
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


}