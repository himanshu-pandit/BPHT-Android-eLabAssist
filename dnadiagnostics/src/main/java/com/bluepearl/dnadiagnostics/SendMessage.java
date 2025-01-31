package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
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
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import android.view.View.OnClickListener;
import android.view.WindowManager;

import android.widget.EditText;

import org.json.JSONArray;
import android.util.Log;
public class SendMessage extends BaseActivityDL {

    Button btnsendMessage;
     EditText editTxtMessage;
    Spinner spiner;


    static String pid = "";
    static String selected_labidfrompref_other = "";
    static String strmessage = "";
    static String strmessageT = "";

    static String status = "";
    static String finalResult = "";
    static String tid = null;
    static String testpid = null;
    static String spinnerval = "";
    ArrayList<String> worldlist =    worldlist = new ArrayList<String>();
    ArrayList<GetMessageFromLab> world;


    private ArrayAdapter<String> adapterr;
    JSONArray Testnamebycategory = null;
    AlertDialog alertbox;
    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    static SharedPreferences sharedpreferences;

   private static String SendMessage_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/Message";
///////////////////////////////////////////

      private static String getMessage_myurlLab = "";

    private static String myurl = "https://www.elabassist.com/Services/Userservice.svc/GetMessageList?LabId=";


/////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

     //   setContentView(R.layout.activity_send_message);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

        selected_labidfrompref_other = sharedpreferences.getString("SelectedLabId", "");
        tid = sharedpreferences.getString("testid", "");
        testpid = sharedpreferences.getString("testprofileid", "");
        pid = sharedpreferences.getString("patientId", "");

       getLayoutInflater().inflate(R.layout.activity_send_message, frameLayout);
     mDrawerList.setItemChecked(position, true);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        /*

        * */

        btnsendMessage=(Button)findViewById(R.id.btnSend_id);
        editTxtMessage =(EditText)findViewById(R.id.etSendMessage_id);
        spiner = (Spinner) findViewById(R.id.spinner_msg_id);

        btnsendMessage.setTypeface(custom_font);
        editTxtMessage.setTypeface(custom_font);


      new getMessagee().execute();

        btnsendMessage.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strmessage = editTxtMessage.getText().toString();
                       // strmessageT=strmessage.trim();
                        strmessageT = strmessage.replace("\n","\\n" );
                       // Toast.makeText(SendMessage.this, "", Toast.LENGTH_SHORT).show();

                        if (isInternetOn(getApplicationContext()))
                        {
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
                        else
                        {
                            alertBox("Internet Connection is not available..!");
                        }
                    }
                }
        );


    }

private class getMessagee extends AsyncTask<Void, Void, Void>{


    @Override
    protected Void doInBackground(Void... params) {

        // Locate the WorldPopulation Class
        world = new ArrayList<GetMessageFromLab>();
        // Create an array to populate the spinner
        worldlist = new ArrayList<String>();

        ServiceHandler sh = new ServiceHandler();

/////////////////////////////////////////////////////////////////////////////////////////////////

        getMessage_myurlLab = myurl+selected_labidfrompref_other;
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
        spiner.setAdapter(new ArrayAdapter<String>(SendMessage.this,
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
            pDialog = new ProgressDialog(SendMessage.this);
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

           if(selected_labidfrompref_other != null && !(selected_labidfrompref_other.equalsIgnoreCase(""))) {
                nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref_other + "\""));
           }
            nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + pid + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"Message\"","\"" + strmessageT + "\""));


            String finalString = nameValuePairs.toString();

            finalString = finalString.replace('=', ':');
            finalString = finalString.substring(1, finalString.length() - 1);
            String strToServer = "{" + finalString + "}";
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


}