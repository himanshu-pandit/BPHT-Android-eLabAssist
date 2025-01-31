package com.bluepearl.dnadiagnostics;

/**
 * Created by deepak.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import	android.app.ActivityManager;
import java.util.List;
import android.content.ComponentName;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //
        // Log.d(TAG, "From: " + remoteMessage.getFrom());
        System.out.println("ststtt  RemoteMessageRemoteMessage ");

        Boolean IsForground = CheckAppIsRunningForground(getApplicationContext());
        if (IsForground) {

            //App is FourGround

        } else {
           // sendNotification(remoteMessage.getNotification().getBody(),clickAction,title);

            System.out.println("ststtt  background "+remoteMessage.getNotification().getBody());

        }


        // play notification sound
         // NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        // notificationUtils.playNotificationSound();


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            System.out.println("ststtt  RemoteMessageDATA ");

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("ststtt  RemoteMessageNOTIFICATION ");

        }

        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.

        //message will contain the Push Message
      //  String message = remoteMessage.getData().get("body");
        //imageUri will contain URL of the image to be displayed with Notification
       // String imageUri = remoteMessage.getData().get("image");
        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
        String strTypeFCM = remoteMessage.getData().get("TypeFCM");
      //  strTypeFCM.trim();
        if(strTypeFCM.equals("ImageFCM"))

        {

            System.out.println("ststtt  in special RemoteMessageDATA ");
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();
            ShowNotificationImageOffer(notification, data);


        }
        else if(strTypeFCM.equals("TextFCM")){

            System.out.println("ststtt  in tip RemoteMessageDATA ");

            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();
            ShowNotificationTextFCM(notification, data);

        }
        else {

            System.out.println("ststtt  in tip RemoteMessageDATA ");

            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();
            ShowNotification(notification, data);

        }

        //To get a Bitmap image from the URL received
       // bitmap = getBitmapfromUrl(imageUri);

       // sendNotification(message, bitmap, TrueOrFlase);



    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */



        private void ShowNotification(RemoteMessage.Notification notification, Map<String, String> data) {
            Map<String, String> fcmdata =data;


            System.out.println("ststtt  in send notifictio ");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      //  intent.putExtra("TypeActivity", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.logo);

        NotificationCompat.BigPictureStyle notifystyle = new NotificationCompat.BigPictureStyle();
        notifystyle.bigPicture(icon);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
               // .setLargeIcon(image)/*Notification icon image*/

                .setSmallIcon(R.drawable.logo)
                //.setContentTitle(messageBody)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(fcmdata.get("title"))

                //  .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(icon))/*Notification with Image*/
               // .setStyle(notifystyle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        System.out.println("ststtt  after send notifictio ");
    }

    /*
    *To get a Bitmap image from the URL received
    */

    private void ShowNotificationImageOffer(RemoteMessage.Notification notification, Map<String, String> data) {

        Map<String, String> fcmdata =data;


        System.out.println("ststtt  in send notifictio ");

       // Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.one);


        Bitmap iconlogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("TypeActivity", fcmdata.get("ActivityTag"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/notification");


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
              //  .setContentTitle(getResources().getString(R.string.app_name))
                .setContentTitle(fcmdata.get("title"))
                .setContentText(fcmdata.get("body"))
                //.setContentText(data.get("body"))
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent)
              //  .setContentInfo("ANY")
                .setLargeIcon(iconlogo)
                //.setSmallIcon(iconlogo)

               .setColor(Color.RED)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

               // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(icon))/*Notification with Image*/
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapfromUrl1(fcmdata.get("imageURL"))))/*Notification with Image*/

                .setSmallIcon(R.drawable.logo);



        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
        System.out.println("ststtt  after send notifictio ");
    }

    private void ShowNotificationTextFCM(RemoteMessage.Notification notification, Map<String, String> data) {

        Map<String, String> fcmdata =data;


        System.out.println("ststtt  in Tip send notifictio ");


        Bitmap iconlogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("TypeActivity", fcmdata.get("ActivityTag"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/notification");


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
              //  .setContentTitle(data.get("body"))
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(fcmdata.get("title"))
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent)
                //.setContentInfo("ANY")
                .setLargeIcon(iconlogo)
                .setColor(Color.RED)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

               // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(icon))/*Notification with Image*/
                // .setStyle(new NotificationCompat.BigTextStyle(notificationBuilder))/*Notification with Image*/

                .setStyle(new NotificationCompat.BigTextStyle().bigText(fcmdata.get("body")))

                //.setStyle(new Notification.BigTextStyle(notificationBuilder)
                .setSmallIcon(R.drawable.logo)

;

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        System.out.println("ststtt  after Tip send notifictio ");
    }


    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
           /* URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmapp = BitmapFactory.decodeStream(input);
*/

         //   String urlPath = imageUrl;
           // Bitmap bm = BitmapFactory.decodeStream((InputStream) new URL(urlPath).getContent());


           /* URL imageUrlg = new URL(imageUrl);
            HttpURLConnection conn= (HttpURLConnection)imageUrlg.openConnection();
            conn.setDoInput(true);
            conn.connect();
            int length = conn.getContentLength();
            InputStream is = conn.getInputStream();
            return BitmapFactory.decodeStream(is);*/


            URL url = new URL(imageUrl);
            HttpGet httpRequest = null;

            httpRequest = new HttpGet(url.toURI());

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
            InputStream input = bufHttpEntity.getContent();

            Bitmap bitmap = BitmapFactory.decodeStream(input);


            input.close();
            return bitmap;


          //  return bm;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }






    public Bitmap getBitmapfromUrl1(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }




    public static boolean CheckAppIsRunningForground(Context mcontext) {

        ActivityManager am = (ActivityManager) mcontext
                .getSystemService(mcontext.ACTIVITY_SERVICE);

        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (componentInfo.getPackageName().equalsIgnoreCase("com.bluepearl.dnadiagnostics")) {
            return true;
        } else {
            return false;
        }

    }


}


