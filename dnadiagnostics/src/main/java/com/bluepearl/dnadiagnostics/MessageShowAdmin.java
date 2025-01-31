package com.bluepearl.dnadiagnostics;

import android.app.Activity;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jsonparsing.webservice.ServiceHandler;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MessageShowAdmin extends BaseActivityAdmin {

    Activity context;
    public static final String MyPREFERENCES = "MyPrefs";
    static SharedPreferences sharedpreferences;

    private ProgressDialog pDialog;
    private ImageButton newmsg;
    TextView fullmsg,msgstate,createddate,MessageFrom,MessageTo,RplyMessg,ReplyMesgBy,RplyMesgDate,ClosedBy;
    Button btnColose;
    ImageButton sendRplyImgBtn;
    EditText editTesxtSendRply;
    String result;

    static String status = "";

    static String StaicTransMsgId = "";
    static String StaticMessgaStatus = "";
    static String StaticCloseBy = "";
    static String StatciLabId = "";

    private static String MessageStatusChange_url = "https://www.elabassist.com/Services/UserService.svc/updatestatus";

    private static String SendReplyMessage_url = "https://www.elabassist.com/Services/UserService.svc/SaveReplyToMessage";

    private static final String ststusurl = "https://www.elabassist.com/Services/UserService.svc/updatestatus?TransMsgId=";
    private static String ststusmyurl = "https://www.elabassist.com/Services/UserService.svc/updatestatus?TransMsgId=";

    static String ststusmyurlOrignal = "";
    // http://localhost:53380/Services/UserService.svc/updatestatus?TransMsgId=12&Status=2&CloseBy=hfagjf&Labid=84135443-7a22-4f96-b7ac-9fa211b750c5




    static String selected_labidfrompref = "";
    static String pid = "";
    static String strReplyMessageId = "";
    static String strReplyMessage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // setContentView(R.layout.activity_show_message);
        getLayoutInflater().inflate(R.layout.activity_show_message, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);




        StaticCloseBy = sharedpreferences.getString("patientId", "");
        StatciLabId = sharedpreferences.getString("SelectedLabId", "");


        //  newmsg = (ImageButton) findViewById(R.id.NewMessagee);
        fullmsg = (TextView) findViewById(R.id.Message_id);
        msgstate = (TextView) findViewById(R.id.MessageState_id);
        createddate = (TextView) findViewById(R.id.getCreatedDate_id);
        MessageFrom = (TextView) findViewById(R.id.MessageFrom_id);
        MessageTo = (TextView) findViewById(R.id.MessageTo_id);

        RplyMessg = (TextView) findViewById(R.id.eRplyMesg_id);
        ReplyMesgBy = (TextView) findViewById(R.id.RplyBy_id);
        RplyMesgDate = (TextView) findViewById(R.id.RpluMsgDate_id);
        ClosedBy = (TextView) findViewById(R.id.ClosedBy_id);

        btnColose   = (Button) findViewById(R.id.closeBtn_id);

        sendRplyImgBtn =(ImageButton) findViewById(R.id.sendRply_img_id);
        editTesxtSendRply=(EditText) findViewById(R.id.editTesxtSendRply_id);




        // feboname = sharedpreferences.getString("FeboPatientNameTopass", "");
        Bundle b = getIntent().getExtras();
        String message = b.getString("FullMessage", "");
        String MssgeState = b.getString("MessageState", "");
        String CreatedDate = b.getString("CreatedDate", "");
        String messageFrom = b.getString("MessageFrom", "");
        String messageTo = b.getString("MessageTo", "");

        String rplyMessg = b.getString("ReplyMessage", "");

        rplyMessg = rplyMessg.replace(",", ",\n");

        String replyMesgBy = b.getString("ReplyBy", "");
        String rplyMesgDate = b.getString("ModifiedDate", "");
        String messsageTansactionId = b.getString("MesssageTansactionId", "");
        String closedBy = b.getString("CloseByB", "");

        StaicTransMsgId = messsageTansactionId;

        fullmsg.setText(message);
        createddate.setText(CreatedDate);
        MessageFrom.setText(messageFrom);
        MessageTo.setText(messageTo);

        RplyMessg.setText(rplyMessg);
        ReplyMesgBy.setText(replyMesgBy);
        RplyMesgDate.setText(rplyMesgDate);
        ClosedBy.setText(closedBy);
        // msgstate.setText(MssgeState);

        // msgstate.setTypeface(custom_font);
        if (MssgeState.equalsIgnoreCase("0")) {
            msgstate.setText("New");
            msgstate.setTextColor(Color.parseColor("#4d94ff"));
            msgstate.setTypeface(null, Typeface.BOLD);
            btnColose.setVisibility(View.VISIBLE);
        } else if (MssgeState.equalsIgnoreCase("1")) {
            msgstate.setText("Replied");
            msgstate.setTextColor(Color.parseColor("#6cb13c"));
            msgstate.setTypeface(null, Typeface.BOLD);
            btnColose.setVisibility(View.VISIBLE);
        } else if (MssgeState.equalsIgnoreCase("2")) {
            msgstate.setText("Closed");
            msgstate.setTextColor(Color.parseColor("#ff4d4d"));
            msgstate.setTypeface(null, Typeface.BOLD);
            btnColose.setVisibility(View.INVISIBLE);
        } else {
            msgstate.setText("no Status");
            btnColose.setVisibility(View.INVISIBLE);
        }


        // fullmsg.setText(feboname);


//        testname.setTypeface(custom_font);
        //    testnamebycategory.setTypeface(custom_font);
        //     testprofile.setTypeface(custom_font);
        //     category_title.setTypeface(custom_btnfont);
//        fullmsg.setTypeface(custom_font);
//        btn1.setTypeface(custom_btnfont);
        //       btnOther.setTypeface(custom_btnfont);

        btnColose.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MessageShow.this, "click", Toast.LENGTH_SHORT).show();
                        // new MessageStatusChange().execute(new String[] { MessageStatusChange_url });
                        //  ststusmyurl = ststusmyurl + StaicTransMsgId;

                        ststusmyurlOrignal = ststusmyurl + StaicTransMsgId + "&Status=2&CloseBy=" + StaticCloseBy + "&" + "Labid=" +StatciLabId;
                        // http://localhost:53380/Services/UserService.svc/updatestatus?TransMsgId=12&Status=2&CloseBy=hfagjf&Labid=84135443-7a22-4f96-b7ac-9fa211b750c5

                        new setStstusUpdate().execute();

                    }
                }
        );
        sendRplyImgBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {





                        if (isInternetOn(getApplicationContext()))
                        {



                            // Toast.makeText(SendMessage.this, "", Toast.LENGTH_SHORT).show();
                            if(editTesxtSendRply.getText().toString().equals(""))
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

                                strReplyMessage = editTesxtSendRply.getText().toString();
                                new SendReplyMessagee().execute(new String[] { SendReplyMessage_url });
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


    private class SendReplyMessagee extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            // Showing progress dialog
            pDialog = new ProgressDialog(MessageShowAdmin.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            return SendReplyMessagPOST(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            // Dismiss the progress dialog
            if (pDialog != null && pDialog.isShowing())
            {
                pDialog.dismiss();
            }

            if (result.equals("1"))

            //{"d":"Success"}
            {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.my_dialog, null);

                TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                text.setText(" Reply Sent ");
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                text.setTypeface(typeface);
                text.setTextColor(Color.WHITE);
                text.setTextSize(18);
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);

                toast.show();
                editTesxtSendRply.setText("");

                Intent iintent = new Intent(MessageShowAdmin.this, MassagListAdmin.class);

                startActivity(iintent);
                //finish();

            } else {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.my_dialog, null);

                TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
                text.setText(" Message Not Sent ");
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



    public static String SendReplyMessagPOST(String url)
    {
        InputStream inputStream = null;
        String result = "";

        //   String MessageVal = editTxtMessage.getText().toString();

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


            nameValuePairs.add(new BasicNameValuePair("\"Labid\"", "\"" + StatciLabId + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"UserID\"", "\"" + StaticCloseBy + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"TransMsgId\"","\"" + StaicTransMsgId + "\""));
            nameValuePairs.add(new BasicNameValuePair("\"ReplyMessage\"","\"" + strReplyMessage + "\""));



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


    /*** Async task class to get json by making HTTP call **/
    private class setStstusUpdate extends AsyncTask<Object, Object, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Object... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(ststusmyurlOrignal, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    result = jsonObj.getString("d");

                    System.out.println(result);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result .equals("1")){
                Toast.makeText(MessageShowAdmin.this, "Message Closed Succesfully", Toast.LENGTH_SHORT).show();

                Intent i1 = new Intent(MessageShowAdmin.this, MassagListAdmin.class);
                startActivity(i1);
                //finish();

            }
            else{
                Toast.makeText(MessageShowAdmin.this, "Failed to Closed ", Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(MessageShowAdmin.this, MassagListAdmin.class);
                startActivity(i1);
                //finish();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        TextView Mytitle = new TextView(this);
        Mytitle.setText("Alert");
        Mytitle.setTextSize(20);
        Mytitle.setPadding(5, 15, 5, 5);
        Mytitle.setGravity(Gravity.CENTER);
        Mytitle.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf"));
        alert.setCustomTitle(Mytitle);

      //  alert.setIcon(R.drawable.signlogo);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", null);

        AlertDialog alert1 = alert.create();
        alert1.show();

        TextView textView = (TextView) alert1.findViewById(android.R.id.message);
        Button yesButton = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        textView.setTypeface(custom_font);
        yesButton.setTypeface(custom_bold_font);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MessageShowAdmin.this, MassagListAdmin.class);
        startActivity(i);
        //finish();
    }
}
