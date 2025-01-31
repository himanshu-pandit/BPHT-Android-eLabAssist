package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/*Call this class methods like this
    DownloadReport downloadReport  = new DownloadReport();      //create object
                               1 -> view report
                               2 -> share
                               3 -> whatsapp number share
    downloadReport.downloadPdf(getActivity(),1,pdfurl,"8180921130","8180921130","8180921130","8180921130");     call downloadPdf method using object with parameters
**/
public class DownloadReport {
    long downloadId = -1;
    String filepath = "";
    int actionType = 1;
    String patientno = "";
    String doctorno = "";
    String affiliationno = "";
    String collecetioncenterno = "";


    public void downloadPdf(final Context context, int type, String reportUrl, String patient, String doctor, String collection, String  affiliation) {
        Log.d("DownloadReport","downloadPdf call");
        URL downloadUrl = null;
        filepath = "";
        actionType = type;
        patientno = patient;
        doctorno = doctor;
        collecetioncenterno = collection;
        affiliationno = affiliation;

        try {
            downloadUrl = new URL(reportUrl);
            Log.d("MalformedURLException", "" + downloadUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("MalformedURLException", "" + e);
        }
        filepath = downloadUrl.getPath();
        filepath = filepath.substring(filepath.lastIndexOf("/") + 1);

        Log.d("filepath", "" + filepath);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(reportUrl)));
        request.setTitle(filepath);
        request.setMimeType("application/pdf");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filepath);

        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = dm.enqueue(request);
        Log.d("downloadId", String.valueOf(downloadId));
        // Register a BroadcastReceiver to listen for download completion
        context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Use a Handler to periodically check the download progress
        /*final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor cursor = downloadManager.query(query);

                if (cursor != null && cursor.moveToFirst()) {
                    int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(statusIndex);
                    int progress = 0;
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // Download is complete, you can handle it here
                        progress = 100;
                        Log.d("DownloadProgress", "Progress: " + progress + "%");
                        cursor.close();
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        // Handle the case when the download has failed
                        cursor.close();
                    } else {
                        int totalSizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                        int downloadedSizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

                        if (totalSizeIndex >= 0 && downloadedSizeIndex >= 0) {
                            int totalSize = cursor.getInt(totalSizeIndex);
                            int downloadedSize = cursor.getInt(downloadedSizeIndex);
                            progress = (int) ((downloadedSize * 100L) / totalSize);
                            progress = Math.min(progress, 100);
                            Log.d("DownloadProgress", "Progress: " + progress + "%");
                            // Update your UI or perform actions based on the progress here

                            // Continue checking the progress until download is complete
                            handler.postDelayed(this, 1000); // Check progress every 1 second (adjust as needed)
                        } else {
                            // Handle the case when totalSize or downloadedSize information is not available
                            Log.d("DownloadProgress", "Progress information not available");
                        }
                    }
                }
            }
        });*/
    }

    // BroadcastReceiver to handle download completion
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("DownloadReport","downloadReceiver call");
            long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadId == receivedDownloadId) {
                // Download is complete, open the PDF file
                Log.d("DownloadReport","Download Completed");
                //openDownloadedPDF(context);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor cursor = downloadManager.query(query);

                if (cursor != null && cursor.moveToFirst()) {
                    int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(statusIndex);
                    int totalSize = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);         //total size of file
                    int downloadedSize = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);


                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // Download was successful, open the PDF file
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                        if (columnIndex >= 0) {
                            String downloadedPath = cursor.getString(columnIndex);
                            // Handle the downloaded file path
                            openDownloadedPDF(context);
                        }
                    }else{
                        int progress = (int) ((downloadedSize * 100L) / totalSize);
                        progress = Math.min(progress, 100);
                        Log.d("DownloadProgress", "Progress: " + progress + "%");
                        //Toast.makeText(context, "Download failed network error please try again later.", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
            }
        }
    };

    private void openDownloadedPDF(final Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filepath);
        if(file.exists()){
            final Uri pdfUri = FileProvider.getUriForFile(context, "com.bluepearl.dnadiagnostics.provider", file);
            if(actionType == 2){
                try{
                    Intent sharingIntent = new Intent();
                    sharingIntent.setType("application/pdf");
                    sharingIntent.setAction(Intent.ACTION_SEND);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                    sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share the report file using.."));
                }catch (ActivityNotFoundException exception){
                    Toast.makeText(context, "Something went wrong please try again later.", Toast.LENGTH_SHORT).show();
                }
            }else if(actionType == 3 ){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Send to");
                final Intent sharingIntent = new Intent();
                String[] contactList = {"Patient", "Doctor", "Affiliation", "Collection Center"};
                builder.setItems(contactList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if(patientno.equals("") || patientno.equals("null")){
                                    Toast.makeText(context, "Oops! It looks like you haven't added your phone number during registration.", Toast.LENGTH_SHORT).show();
                                }else{
                                    try{
                                        sharingIntent.setType("application/pdf");
                                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                                        sharingIntent.putExtra("jid", "91"+patientno+ "@s.whatsapp.net");
                                        sharingIntent.setAction(Intent.ACTION_SEND);
                                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                                        sharingIntent.setPackage("com.whatsapp");
                                        context.startActivity(Intent.createChooser(sharingIntent, "Share file using"));
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "Something went wrong please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            case 1:
                                if(doctorno.equals("") || doctorno.equals("null")){
                                    Toast.makeText(context, "Oops! It looks like you haven't added your phone number during registration.", Toast.LENGTH_SHORT).show();
                                }else{
                                    try{
                                        sharingIntent.setType("application/pdf");
                                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                                        sharingIntent.putExtra("jid", "91"+doctorno+ "@s.whatsapp.net");
                                        sharingIntent.setAction(Intent.ACTION_SEND);
                                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                                        sharingIntent.setPackage("com.whatsapp");
                                        context.startActivity(Intent.createChooser(sharingIntent, "Share file using"));
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "Something went wrong please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            case 2:
                                if(affiliationno.equals("") || affiliationno.equals("null")){
                                    Toast.makeText(context, "Oops! It looks like you haven't added your phone number during registration.", Toast.LENGTH_SHORT).show();
                                }else{
                                    try{
                                        sharingIntent.setType("application/pdf");
                                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                                        sharingIntent.putExtra("jid", "91"+affiliationno+ "@s.whatsapp.net");
                                        sharingIntent.setAction(Intent.ACTION_SEND);
                                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                                        sharingIntent.setPackage("com.whatsapp");
                                        context.startActivity(Intent.createChooser(sharingIntent, "Share file using"));
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "Something went wrong please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            case 3:
                                if(collecetioncenterno.equals("") || collecetioncenterno.equals("null")){
                                    Toast.makeText(context, "Oops! It looks like you haven't added your phone number during registration.", Toast.LENGTH_SHORT).show();
                                }else{
                                    try {
                                        sharingIntent.setType("application/pdf");
                                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                                        sharingIntent.putExtra("jid", "91" + collecetioncenterno + "@s.whatsapp.net");
                                        sharingIntent.setAction(Intent.ACTION_SEND);
                                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                                        sharingIntent.setPackage("com.whatsapp");
                                        context.startActivity(Intent.createChooser(sharingIntent, "Share file using"));
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "Something went wrong please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                try{
                    Intent intent = new Intent();
                    intent.setDataAndType(pdfUri,"application/pdf");
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }catch(ActivityNotFoundException exception){
                    Toast.makeText(context, "You do not have a PDF viewing application installed.", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(context, "file does not exist!", Toast.LENGTH_SHORT).show();
        }
    }
}