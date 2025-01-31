package com.bluepearl.dnadiagnostics;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class UploadPrescriptionTab extends AppCompatActivity {
	private static final int PERMISSION_REQUEST_CODE = 207;
	private static final String[] PERMISSIONS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
	};

	Button bookbtn1, btnCapturePicture, btnBrowser,btnOther;

	int clickcount=0;

	ImageView imgPrescription;
	ImageView imgPrescriptionTwo;
	ImageView imgPrescriptionThree;
	ImageView imgPrescriptionFour;

	EditText patientname;
	//ImageButton btnOther;

	public static final int CAMERA_PERM_CODE = 101;
	public static final int CAMERA_REQUEST_CODE = 102;
	public static final int GALLERY_REQUEST_CODE = 105;
	private static final String TAG = "Upload Prescription";
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO = 110;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE = 120;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR = 130;

	private static final int BROWSER_IMAGE_REQUEST_CODE = 200;
	private static final int BROWSER_IMAGE_REQUEST_CODETWO = 210;
	private static final int BROWSER_IMAGE_REQUEST_CODETHREE = 220;
	private static final int BROWSER_IMAGE_REQUEST_CODEFOUR = 230;
	////////////////////////////////////////////////////////////
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_IMAGETWO = 2;
	public static final int MEDIA_TYPE_IMAGETHREE = 3;
	public static final int MEDIA_TYPE_IMAGEFOUR = 4;

	private static Uri fileUri; // file url to store image
	private Uri fileUriTwo; // file url to store Second image
	private Uri fileUriThree; // file url to store image
	private Uri fileUriFour; // file url to store image

	public static final String IMAGE_DIRECTORY_NAME = "eLAB Prescription";
	public static final String IMAGE_DIRECTORY_NAMETWO = "eLAB Prescription";
	public static final String IMAGE_DIRECTORY_NAMETHREE = "eLAB Prescription";
	public static final String IMAGE_DIRECTORY_NAMEFOUR = "eLAB Prescription";

	//	private static String filePath = null;
	private static String filePathTwo = null;
	private static String filePathThree = null;
	private static String filePathFour = null;

	//////////////////////
	String currentPhotoPath;

	static String outputDate = "";
	Bundle data;
	private static String filePath = null;
	private ProgressDialog pDialog;
	static String finalResult = "";
	GPSTracker gps;
	public static final String MyPREFERENCES = "MyPrefs";
	static SharedPreferences sharedpreferences;
	static String tid = null;
	static String testpid = null;
	static String pid = null;
	static String phone = null;
	static String add_val = null;
	static String date_val = null;
	static String date_val_temp = null;
	static String dname_val = null;
	static String selectedDocId = "";
	static String residencial_address = "";
	static String official_address = "";
	static String status = "";
	static String text = "";
	static String entered_test_name = null;
	static String addval = null;
	static String dnameval = null;
	static String family_pid = null;
	static String family_pname = null;
	static String family_page = null;
	static String family_pgender = "0";
	static String pnameval = null;
	static String pin = null;
	static String selected_labidfrompref = null;
	static String selectedCenterId = null;
	static String register_patientgender = null;
	static String updated_val = null;
	static String pval = null;
	static String selected_patient_name = null;
	static String selectedPatientId = "";
	static String test_value;
	int my_flg = -1;
	static String strDate = "";
	static String pname_fromprofile = "";
	////////////////////////////////////////////////////////
	static String encodedImage = "";
	static String encodedImageFromPref = "";
	static String selectedimagefrompatientinfo = "";

	static String encodedImageTwo = "";
	static String encodedImageFromPrefTwo = "";
	static String selectedimagefrompatientinfoTwo = "";

	static String encodedImageThree = "";
	static String encodedImageFromPrefThree = "";
	static String selectedimagefrompatientinfoThree = "";

	static String encodedImageFour = "";
	static String encodedImageFromPrefFour = "";
	static String selectedimagefrompatientinfoFour = "";

	static String homecollection = "";

	private String appointment_url = "https://www.elabassist.com/Services/BookMyAppointment_Services.svc/PatientAppointMent_Global";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* setContentView(R.layout.fragment_upload_prescription); */

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_btnfont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		sharedpreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

		selected_labidfrompref = sharedpreferences.getString("SelectedLabId", "");
		tid = sharedpreferences.getString("testid", "");
		testpid = sharedpreferences.getString("testprofileid", "");

		pid = sharedpreferences.getString("patientId", "");
		pin = sharedpreferences.getString("pincode", "");
		phone = sharedpreferences.getString("phno", "");
		date_val = sharedpreferences.getString("DateTime", "");
		selectedDocId = sharedpreferences.getString("doc_id", "");
		date_val_temp = date_val;
		selectedCenterId = sharedpreferences.getString("centerid", "");
		selectedPatientId = sharedpreferences.getString("SelectedPatient", "");
		register_patientgender = sharedpreferences.getString("self_patientgender", "");

		filePath = sharedpreferences.getString("filePath", "");
		filePathTwo = sharedpreferences.getString("filePathTwo", "");
		filePathThree = sharedpreferences.getString("filePathThree", "");
		filePathFour = sharedpreferences.getString("filePathFour", "");
/////////////////////////////////////////////////////////
		residencial_address = sharedpreferences.getString("res_add", "");

		official_address = sharedpreferences.getString("office_add", "");
		entered_test_name = sharedpreferences.getString("test_name", "");
		selected_patient_name = sharedpreferences.getString("SelectedPatientName", "");

		addval = sharedpreferences.getString("address", "");
		dnameval = sharedpreferences.getString("Doctor", "");
		updated_val = sharedpreferences.getString("updated", "");

		selected_patient_name = sharedpreferences.getString("SelectedPatientName", "");

		family_pid = sharedpreferences.getString("SelectedPatient", "");
		family_pname = sharedpreferences.getString("SelectedPatientName", "");

		family_page = sharedpreferences.getString("SelectedPatientAge", "");
		family_pgender = sharedpreferences.getString("SelectedPatientGender", "0");

		pname_fromprofile = sharedpreferences.getString("patient_name", "");
		selectedimagefrompatientinfo = sharedpreferences.getString("imageFromPatientInfo", "");
		selectedimagefrompatientinfoTwo = sharedpreferences.getString("imageFromPatientInfoTwo", "");
		selectedimagefrompatientinfoThree = sharedpreferences.getString("imageFromPatientInfoThree", "");
		selectedimagefrompatientinfoFour = sharedpreferences.getString("imageFromPatientInfoFour", "");
		homecollection = sharedpreferences.getString("home_collection", "");

		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.fragment_upload_prescription, null);
		this.setContentView(viewToLoad);

		bookbtn1 = (Button) findViewById(R.id.BookButton);
		btnOther = (Button) findViewById(R.id.MemberButton);
		btnCapturePicture = (Button) findViewById(R.id.CaptureButton);
		//btnBrowser = (Button) findViewById(R.id.BrowseButton);
		imgPrescription = (ImageView) findViewById(R.id.uploadpic);
		imgPrescriptionTwo = (ImageView) findViewById(R.id.uploadpicTwo);
		imgPrescriptionThree = (ImageView) findViewById(R.id.uploadpicThree);
		imgPrescriptionFour = (ImageView) findViewById(R.id.uploadpicFour);
		patientname = (EditText) findViewById(R.id.etPname);
		//bookbtn1.setEnabled(false);

		patientname.setTypeface(custom_font);
		//	btnBrowser.setTypeface(custom_btnfont);
		btnCapturePicture.setTypeface(custom_btnfont);
		bookbtn1.setTypeface(custom_btnfont);
		btnOther.setTypeface(custom_btnfont);

		if (tid.equals("")) {
			tid = "";
		}
		if (testpid.equals("")) {
			testpid = "";
		}
		if (filePath.equals("")) {
			filePath = "";
		}
		if (filePathTwo.equals("")) {
			filePathTwo = "";
		}
		if (filePathThree.equals("")) {
			filePathThree = "";
		}
		if (filePathFour.equals("")) {
			filePathFour = "";
		}
////////////////////////////////////////////////////////////////////////////
		if (entered_test_name.equals("")) {
			entered_test_name = "";
		}
		String strpname=  sharedpreferences.getString("SelectedPatientName","");

		if(strpname.equalsIgnoreCase("null") || strpname.equalsIgnoreCase(""))
		{
			patientname.setText("SELF");
		}
		else
		{
			patientname.setText(selected_patient_name);
		}
		if(selectedimagefrompatientinfo != null && !(selectedimagefrompatientinfo.equalsIgnoreCase("")))
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			final Bitmap bitmap = BitmapFactory.decodeFile(selectedimagefrompatientinfo, options);
			imgPrescription.setImageBitmap(bitmap);
			bookbtn1.setEnabled(true);


			if (!(selectedimagefrompatientinfo.equals("")) ) {
				BitmapFactory.Options options1 = new BitmapFactory.Options();
				options.inSampleSize = 2;
				final Bitmap bitmap1 = BitmapFactory.decodeFile(selectedimagefrompatientinfo, options1);
				/*
				 * MultipartEntity entity1 = new MultipartEntity(
				 * HttpMultipartMode.BROWSER_COMPATIBLE);
				 */
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, bos);
				byte[] data = bos.toByteArray();

				encodedImageFromPref = Base64.encodeToString(data, Base64.NO_WRAP);

			} else {
				encodedImageFromPref = "";
			}
		}
		///////////////////////// for second image //////////////////////

		if(selectedimagefrompatientinfoTwo != null && !(selectedimagefrompatientinfoTwo.equalsIgnoreCase("")))
		{
			BitmapFactory.Options optionsTwo = new BitmapFactory.Options();
			optionsTwo.inSampleSize = 8;
			final Bitmap bitmapTwo = BitmapFactory.decodeFile(selectedimagefrompatientinfoTwo, optionsTwo);
			imgPrescriptionTwo.setImageBitmap(bitmapTwo);

			bookbtn1.setEnabled(true);


			if (!(selectedimagefrompatientinfoTwo.equals("")) ) {
				BitmapFactory.Options options1Two = new BitmapFactory.Options();
				optionsTwo.inSampleSize = 2;
				final Bitmap bitmap1Two = BitmapFactory.decodeFile(selectedimagefrompatientinfoTwo, options1Two);

				// MultipartEntity entity1 = new MultipartEntity(
				// HttpMultipartMode.BROWSER_COMPATIBLE);

				ByteArrayOutputStream bosTwo = new ByteArrayOutputStream();
				bitmap1Two.compress(Bitmap.CompressFormat.JPEG, 50, bosTwo);
				byte[] dataTwo = bosTwo.toByteArray();

				encodedImageFromPrefTwo = Base64.encodeToString(dataTwo, Base64.NO_WRAP);

			} else {
				encodedImageFromPrefTwo = "";
			}
		}



		//////////////////////////////// for Third image///////////////////////////////////
		if(selectedimagefrompatientinfoThree != null && !(selectedimagefrompatientinfoThree.equalsIgnoreCase("")))
		{
			BitmapFactory.Options optionsThree = new BitmapFactory.Options();
			optionsThree.inSampleSize = 8;
			final Bitmap bitmapThree = BitmapFactory.decodeFile(selectedimagefrompatientinfoThree, optionsThree);
			imgPrescriptionThree.setImageBitmap(bitmapThree);

			bookbtn1.setEnabled(true);


			if (!(selectedimagefrompatientinfoThree.equals("")) ) {
				BitmapFactory.Options options1Three = new BitmapFactory.Options();
				optionsThree.inSampleSize = 2;
				final Bitmap bitmap1Three = BitmapFactory.decodeFile(selectedimagefrompatientinfoThree, options1Three);

				//  MultipartEntity entity1 = new MultipartEntity(
				// HttpMultipartMode.BROWSER_COMPATIBLE);

				ByteArrayOutputStream bosThree = new ByteArrayOutputStream();
				bitmap1Three.compress(Bitmap.CompressFormat.JPEG, 50, bosThree);
				byte[] dataThree = bosThree.toByteArray();

				encodedImageFromPrefThree = Base64.encodeToString(dataThree, Base64.NO_WRAP);

			} else {
				encodedImageFromPrefThree = "";
			}
		}

///////////////////////////////for fourth image/////////////////////////////////////
		if(selectedimagefrompatientinfoFour != null && !(selectedimagefrompatientinfoFour.equalsIgnoreCase("")))
		{
			BitmapFactory.Options optionsFour = new BitmapFactory.Options();
			optionsFour.inSampleSize = 8;
			final Bitmap bitmapFour = BitmapFactory.decodeFile(selectedimagefrompatientinfoFour, optionsFour);
			imgPrescriptionFour.setImageBitmap(bitmapFour);

			bookbtn1.setEnabled(true);


			if (!(selectedimagefrompatientinfoFour.equals("")) ) {
				BitmapFactory.Options options1Four = new BitmapFactory.Options();
				optionsFour.inSampleSize = 2;
				final Bitmap bitmap1Four = BitmapFactory.decodeFile(selectedimagefrompatientinfoFour, options1Four);

				//  MultipartEntity entity1 = new MultipartEntity(
				// HttpMultipartMode.BROWSER_COMPATIBLE);

				ByteArrayOutputStream bosFour = new ByteArrayOutputStream();
				bitmap1Four.compress(Bitmap.CompressFormat.JPEG, 50, bosFour);
				byte[] dataFour = bosFour.toByteArray();

				encodedImageFromPrefFour = Base64.encodeToString(dataFour, Base64.NO_WRAP);

			} else {
				encodedImageFromPrefFour = "";
			}
		}

////////////////////////////////////////////////////////////////////

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(
				"dd-MMM-yyyy HH:mm");
		strDate = sdf.format(c.getTime());


		/*** Capture image button click event */
		btnCapturePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// capture picture
				if(checkPermission()){
					captureImage();
				}else{
					showPermissionSettingsDialog();
				}
			}
		});


		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.my_dialog,null);

			TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
			text.setText("     Sorry! Your device doesn't support camera     ");
			Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
			text.setTypeface(typeface);
			text.setTextColor(Color.WHITE);
			text.setTextSize(18);
			Toast toast = new Toast(getApplicationContext());
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);

			toast.show();

			//finish();
		}

		/*** Capture image button click event */
	/*	btnBrowser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getParent().startActivityForResult(i,
						BROWSER_IMAGE_REQUEST_CODE);
			}
		});
*/

		imgPrescription.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getParent().startActivityForResult(i,BROWSER_IMAGE_REQUEST_CODE);
			}
		});


		imgPrescriptionTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getParent().startActivityForResult(i,BROWSER_IMAGE_REQUEST_CODETWO);
			}
		});

		imgPrescriptionThree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getParent().startActivityForResult(i,BROWSER_IMAGE_REQUEST_CODETHREE);
			}
		});

		imgPrescriptionFour.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getParent().startActivityForResult(i,BROWSER_IMAGE_REQUEST_CODEFOUR);
			}
		});



		bookbtn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Intent i = new Intent(UploadPrescriptionTab.this,
				 * FinalAppointmentActivity.class); startActivity(i);
				 */
				pval = patientname.getText().toString();
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("filePath", filePath);
				editor.putString("filePathTwo", filePathTwo);
				editor.putString("filePathThree", filePathThree);
				editor.putString("filePathFour", filePathFour);

				editor.commit();

				if (isInternetOn(getApplicationContext())) {
					new uploadAllData().execute(new String[] { appointment_url });
				} else {
					alertBox("Internet Connection is not available..!");
				}
				//launchUploadActivity(true);
			}
		});

		btnOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString("ActivityName", "Upload");

				editor.commit();

				Intent i = new Intent(UploadPrescriptionTab.this,PatientInformation.class);
				//i.putExtra("FromActivity", "Upload");
				i.putExtra("isFromUploadTab", "Yes");
				i.putExtra("ImageFomTab", filePath);
				i.putExtra("ImageFomTabTwo", filePathTwo);
				i.putExtra("ImageFomTabThree", filePathThree);
				i.putExtra("ImageFomTabFour", filePathFour);
				startActivity(i);
				//finish();
			}
		});

	}

	/*** Checking device has camera hardware or not **/
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/*** Launching camera app to capture image */
	private void captureImage() {

		clickcount=clickcount+1;
		if(clickcount==1)
		{
			Log.d("clickcount",""+clickcount);
			Log.d("clickcount","clickcount called");
			Intent iOne = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			Log.d("fileUri",fileUri.toString());
			iOne.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			// start the image capture Intent
			getParent().startActivityForResult(iOne,
					CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

			//Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		}


		if(clickcount==2)
		{
			Log.d("clickcount",""+clickcount);
			Log.d("clickcount","clickcount 2 called");
			Intent iTwo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUriTwo = getOutputMediaFileUriTwo(MEDIA_TYPE_IMAGETWO);
			iTwo.putExtra(MediaStore.EXTRA_OUTPUT, fileUriTwo);
			Log.e("fileUriTwo",fileUriTwo.toString());

			// start the image capture Intent
			getParent().startActivityForResult(iTwo,
					CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO);

			//Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO);
		}
		if(clickcount==3)
		{
			Intent iThree = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUriThree = getOutputMediaFileUriThree(MEDIA_TYPE_IMAGETHREE);
			iThree.putExtra(MediaStore.EXTRA_OUTPUT, fileUriThree);
			// start the image capture Intent
			getParent().startActivityForResult(iThree,
					CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE);

			//Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE);
		}
		if(clickcount==4)
		{
			Intent iFour = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUriFour = getOutputMediaFileUriFour(MEDIA_TYPE_IMAGEFOUR);
			iFour.putExtra(MediaStore.EXTRA_OUTPUT, fileUriFour);
			// start the image capture Intent
			getParent().startActivityForResult(iFour,
					CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR);

			//	Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(iCam, CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR);
			clickcount=0;
		}
	}

	/***
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on screen orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
		outState.putParcelable("file_uriTwo", fileUriTwo);
		outState.putParcelable("file_uriThree", fileUriThree);
		outState.putParcelable("file_uriFour", fileUriFour);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
		fileUriTwo = savedInstanceState.getParcelable("file_uriTwo");
		fileUriThree = savedInstanceState.getParcelable("file_uriThree");
		fileUriFour = savedInstanceState.getParcelable("file_uriFour");
	}

	/*** Receiving activity result method will be called after closing the camera **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Log.d("fileUri+",""+fileUri);
		Log.d("requestCode+",""+requestCode);

		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Log.d("filePath+",filePath);
				Log.d("fileUri+", String.valueOf(fileUri));
				File f = new File(filePath);
				imgPrescription.setImageURI(fileUri);

				Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
				bookbtn1.setEnabled(true);
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 8;
//				final Bitmap bitmap = BitmapFactory.decodeFile(
//						fileUri.getPath(), options);
//				imgPrescription.setImageBitmap(bitmap);
//				filePath = fileUri.getPath();
//				bookbtn1.setEnabled(true);
				// launchUploadActivity(true);
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);

				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     User cancelled image capture     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);

				toast.show();

				/*Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();*/
			} else {
				// failed to capture image
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);

				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Sorry! Failed to capture image     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);

				toast.show();

				/*Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();*/
			}
		}


		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODETWO) {
			if (resultCode == RESULT_OK) {
				File f = new File(filePathTwo);
				imgPrescriptionTwo.setImageURI(fileUriTwo);

				Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
				bookbtn1.setEnabled(true);
				//odl code
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 8;
//				final Bitmap bitmap = BitmapFactory.decodeFile(fileUriTwo.getPath(), options);
//				imgPrescriptionTwo.setImageBitmap(bitmap);
//				filePathTwo = fileUriTwo.getPath();
//				bookbtn1.setEnabled(true);
				// launchUploadActivity(true);

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     User cancelled image capture     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				// Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();

			} else {
				// failed to capture image
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Sorry! Failed to capture image     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				//Toast.makeText(getApplicationContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODETHREE) {
			if (resultCode == RESULT_OK) {
				File f = new File(filePathThree);
				imgPrescriptionThree.setImageURI(fileUriThree);

				Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
				bookbtn1.setEnabled(true);

//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 8;
//				final Bitmap bitmap = BitmapFactory.decodeFile(fileUriThree.getPath(), options);
//				imgPrescriptionThree.setImageBitmap(bitmap);
//				filePathThree = fileUriThree.getPath();
//				bookbtn1.setEnabled(true);
				// launchUploadActivity(true);

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     User cancelled image capture     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();

			} else {
				// failed to capture image
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Sorry! Failed to capture image     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODEFOUR) {
			if (resultCode == RESULT_OK) {
				File f = new File(filePathFour);
				imgPrescriptionFour.setImageURI(fileUriFour);

				Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
				bookbtn1.setEnabled(true);
				//old code
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 8;
//				final Bitmap bitmap = BitmapFactory.decodeFile(fileUriFour.getPath(), options);
//				imgPrescriptionFour.setImageBitmap(bitmap);
//				filePathFour = fileUriFour.getPath();
//				bookbtn1.setEnabled(true);
				// launchUploadActivity(true);

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     User cancelled image capture     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();

			} else {
				// failed to capture image
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.my_dialog, null);
				TextView text = (TextView) layout.findViewById(R.id.dialog_box_title_text);
				text.setText("     Sorry! Failed to capture image     ");
				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
				text.setTypeface(typeface);
				text.setTextColor(Color.WHITE);
				text.setTextSize(18);
				Toast toast = new Toast(getApplicationContext());
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == BROWSER_IMAGE_REQUEST_CODE) {
			if (requestCode == BROWSER_IMAGE_REQUEST_CODE
					&& resultCode == RESULT_OK && null != data) {
				fileUri = data.getData();
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Log.e("onActivtyR", "selectedImage" + selectedImage);
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				Log.e("onActivtyR cursor", "cursor" + cursor);

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				Log.e("onActivtyR columnIndex", "columnIndex" + columnIndex);

				String picturePath = cursor.getString(columnIndex);
				Log.e("onActivtyR picturePath", "picturePath" + picturePath);

				cursor.close();

				/*
				 * imgPrescription.setImageBitmap(BitmapFactory.decodeFile(
				 * picturePath)); filePath = picturePath;
				 */
				BitmapFactory.Options options = new BitmapFactory.Options();
				Log.e("onActivtyR options", options.toString());

				options.inSampleSize = 8;
				final Bitmap bitmap = BitmapFactory.decodeFile(picturePath,
						options);
				Log.e("onActivtyR bitmap", bitmap.toString());

				imgPrescription.setImageBitmap(bitmap);
				filePath = picturePath;
				Log.e("onActivtyR filePath", filePath);

				bookbtn1.setEnabled(true);
			}
		}


		if (requestCode == BROWSER_IMAGE_REQUEST_CODETWO) {
			if (requestCode == BROWSER_IMAGE_REQUEST_CODETWO && resultCode == RESULT_OK && null != data) {
				fileUriTwo = data.getData();
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				final Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
				imgPrescriptionTwo.setImageBitmap(bitmap);
				filePathTwo = picturePath;
				bookbtn1.setEnabled(true);
			}
		}

		if (requestCode == BROWSER_IMAGE_REQUEST_CODETHREE) {
			if (requestCode == BROWSER_IMAGE_REQUEST_CODETHREE && resultCode == RESULT_OK && null != data) {
				fileUriThree = data.getData();
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				final Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
				imgPrescriptionThree.setImageBitmap(bitmap);
				filePathThree = picturePath;
				bookbtn1.setEnabled(true);
			}
		}


		if (requestCode == BROWSER_IMAGE_REQUEST_CODEFOUR) {
			if (requestCode == BROWSER_IMAGE_REQUEST_CODEFOUR && resultCode == RESULT_OK && null != data) {
				fileUriFour = data.getData();
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				final Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
				imgPrescriptionFour.setImageBitmap(bitmap);
				filePathFour = picturePath;
				bookbtn1.setEnabled(true);
			}
		}


	}

	/*private void launchUploadActivity(boolean isImage) {
		Intent i = new Intent(UploadPrescriptionTab.this,
				DateTimeSelection.class);
		i.putExtra("filePath", filePath);
		i.putExtra("isImage", isImage);

		SharedPreferences.Editor editor = sharedpreferences.edit();

		editor.putString("filePath", filePath);

		editor.commit();
		startActivity(i);
	}*/

	/*** Creating file uri to store image */
	public Uri getOutputMediaFileUri(int type) {

		return  FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", Objects.requireNonNull(getOutputMediaFile(type)));

//		Uri.fromFile(getOutputMediaFile(type));
//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//			return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", Objects.requireNonNull(getOutputMediaFile(type)));
//		else
//		return	Uri.fromFile(getOutputMediaFile(type));
//		return Uri.fromFile(getOutputMediaFile(type));

	}


	public Uri getOutputMediaFileUriTwo(int type) {
		return  FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", Objects.requireNonNull(getOutputMediaFileTwo(type)));

//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//			return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", Objects.requireNonNull(getOutputMediaFile(type)));
//		else
//			return	Uri.fromFile(getOutputMediaFile(type));
//		return Uri.fromFile(getOutputMediaFileTwo(type));
	}
	public Uri getOutputMediaFileUriThree(int type) {
		return  FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", Objects.requireNonNull(getOutputMediaFileThree(type)));

//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//			return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", Objects.requireNonNull(getOutputMediaFile(type)));
//		else
//			return	Uri.fromFile(getOutputMediaFile(type));
//		return Uri.fromFile(getOutputMediaFileThree(type));
	}
	public Uri getOutputMediaFileUriFour(int type) {
		return  FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", Objects.requireNonNull(getOutputMediaFileFour(type)));

//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//			return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+ ".provider", Objects.requireNonNull(getOutputMediaFile(type)));
//		else
//			return	Uri.fromFile(getOutputMediaFile(type));
//		return Uri.fromFile(getOutputMediaFileFour(type));
	}

	/*** returning image / video */
	private static File getOutputMediaFile(int type) {
		Log.d("getOutputMediaFile","getOutputMediaFile called");
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		Log.d("mediaStorageDir","mediaStorageDir"+mediaStorageDir);
		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAME+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator	+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}
		Log.d("mediaFile",""+mediaFile);
		return mediaFile;
	}
	private static File getOutputMediaFileTwo(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAMETWO);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs())
			{Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAMETWO+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGETWO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator	+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	private static File getOutputMediaFileThree(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAMETHREE);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs())
			{Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAMETHREE+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGETHREE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator	+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	private static File getOutputMediaFileFour(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAMEFOUR);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs())
			{Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAMEFOUR+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGEFOUR) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator	+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}


	////////////////////////////////////////////////////
	@Override
	public void onBackPressed() {
		//this.getParent().onBackPressed();
		Intent i = new Intent(getApplicationContext(),DateTimeSelection.class);
		startActivity(i);
		finish();
	}

	private class uploadAllData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing progress dialog pDialog = new
			pDialog = new ProgressDialog(UploadPrescriptionTab.this);
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
			Log.e("result+",result);

			// Dismiss the progress dialog
			if (pDialog != null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}


			if (result.equals("Appointment_Booked")) {
				showAlert("Your Appointment Booked Successfully. Please make yourself available on time at your booking location.");
				clearPreferences();

			} else {
				showAlert1("Appointment Not Booked Please Try Again");
				date_val = date_val_temp;

			}
		}
	}
	@SuppressWarnings("deprecation")
	public String POST(String url) {
		InputStream inputStream = null;
		String result = "";

		if (fileUri != null && !fileUri.getPath().isEmpty() ) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			try {
				// Convert the fileUri to a Bitmap
				InputStream inputStream1 = getContentResolver().openInputStream(fileUri);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream1, null, options);
				inputStream1.close();

				// Compress the Bitmap to a byte array
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
				byte[] data = bos.toByteArray();

				// Encode the byte array to Base64
				encodedImage = Base64.encodeToString(data, Base64.NO_WRAP);
				Log.d("encodedImage",""+encodedImage);

				// Now you can use the 'encodedImage' as needed
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			encodedImage = "";
		}

		if (fileUriTwo != null && !fileUriTwo.getPath().isEmpty()) {
			BitmapFactory.Options optionsTwo = new BitmapFactory.Options();
			optionsTwo.inSampleSize = 2;
			try {
				// Convert the fileUri to a Bitmap
				InputStream inputStream2 = getContentResolver().openInputStream(fileUriTwo);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream2, null, optionsTwo);
				inputStream2.close();

				// Compress the Bitmap to a byte array
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
				byte[] data = bos.toByteArray();

				// Encode the byte array to Base64
				encodedImageTwo = Base64.encodeToString(data, Base64.NO_WRAP);
				Log.d("encodedImageTwo",""+encodedImageTwo);

				// Now you can use the 'encodedImage' as needed
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			encodedImageTwo = "";
		}
////////////////////////////////////////////////////////////////////////////////////////
		if (fileUriThree != null && !fileUriThree.getPath().isEmpty()) {
			BitmapFactory.Options optionsThree = new BitmapFactory.Options();
			optionsThree.inSampleSize = 2;
			try {
				// Convert the fileUri to a Bitmap
				InputStream inputStream3 = getContentResolver().openInputStream(fileUriThree);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream3, null, optionsThree);
				inputStream3.close();

				// Compress the Bitmap to a byte array
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
				byte[] data = bos.toByteArray();

				// Encode the byte array to Base64
				encodedImageThree = Base64.encodeToString(data, Base64.NO_WRAP);
				Log.d("encodedImageThree",""+encodedImageThree);

				// Now you can use the 'encodedImage' as needed
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			encodedImageThree = "";
		}
///////////////////////////////////////////////////////////////////////////////
		if (fileUriFour != null && !fileUriFour.getPath().isEmpty()) {
			BitmapFactory.Options optionsFour = new BitmapFactory.Options();
			optionsFour.inSampleSize = 2;
			try {
				// Convert the fileUri to a Bitmap
				InputStream inputStream4 = getContentResolver().openInputStream(fileUriFour);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream4, null, optionsFour);
				inputStream4.close();

				// Compress the Bitmap to a byte array
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
				byte[] data = bos.toByteArray();

				// Encode the byte array to Base64
				encodedImageFour = Base64.encodeToString(data, Base64.NO_WRAP);
				Log.d("encodedImageFour",""+encodedImageFour);

				// Now you can use the 'encodedImage' as needed
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			encodedImageFour = "";
		}


		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			Log.e("url+",url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(selected_labidfrompref != null && !(selected_labidfrompref.equalsIgnoreCase("")))
			{
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			}
			else if(selected_labidfrompref != null && !(selected_labidfrompref.equalsIgnoreCase(""))) {
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\"" + selected_labidfrompref + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"LabID\"", "\""+ selected_labidfrompref + "\""));
			}

			nameValuePairs.add(new BasicNameValuePair("\"PatientID\"", "\"" + pid + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"Username\"", "\"" + phone + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedTest\"", "\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedProfile\"","\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"SelectedPopularTest\"","\"\""));
			nameValuePairs.add(new BasicNameValuePair("\"EnteredTest\"", "\"\""));
			if(selectedimagefrompatientinfo.equalsIgnoreCase("null") || selectedimagefrompatientinfo.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"Prescription\"", "\""+ encodedImage + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"Prescription\"", "\""+ encodedImageFromPref + "\""));
			}

			if(selectedimagefrompatientinfoTwo.equalsIgnoreCase("null") || selectedimagefrompatientinfoTwo.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionTwo\"", "\""+ encodedImageTwo + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionTwo\"", "\""+ encodedImageFromPrefTwo + "\""));
			}
//////////////////////////////////////// three image/////////////////////////////////////////////////////////////////////////////

			if(selectedimagefrompatientinfoThree.equalsIgnoreCase("null") || selectedimagefrompatientinfoThree.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionThree\"", "\""+ encodedImageThree + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionThree\"", "\""+ encodedImageFromPrefThree + "\""));
			}
//////////////////////////////////////////// fourth image//////////////////////////////////////////////////////////////

			if(selectedimagefrompatientinfoFour.equalsIgnoreCase("null") || selectedimagefrompatientinfoFour.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionFour\"", "\""+ encodedImageFour + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"PrescriptionFour\"", "\""+ encodedImageFromPrefFour + "\""));
			}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if(date_val.equalsIgnoreCase("null") || date_val.equalsIgnoreCase(""))
			{
				nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"","\"" + strDate + "\""));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("\"AppointmentDate\"","\"" + date_val + "\""));
			}
			nameValuePairs.add(new BasicNameValuePair("\"AppointmentAddress\"","\"" + addval.toString().trim() + "\""));

			if (dnameval.equalsIgnoreCase("") || dnameval.equalsIgnoreCase("null")) {
				nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\"" + 0 + "\""));
				nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"",
						"\"SELF\""));


			} else {
				if(selectedDocId.equalsIgnoreCase("null") || selectedDocId.equalsIgnoreCase(""))
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\"" + 0 + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"",
							"\"" + dnameval + "\""));
				}
				else
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefDocID\"", "\""
							+ selectedDocId + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"RefDocName\"",
							"\"\""));
				}
			}

			String str=sharedpreferences.getString("self", "");
			if (str.equals("yes"))  {


				nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
				if(pval.equalsIgnoreCase("null") || pval.equalsIgnoreCase("") || pval.equalsIgnoreCase("self"))
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

				if(register_patientgender.equalsIgnoreCase("null") || register_patientgender.equalsIgnoreCase(""))
				{
					nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
							"\"" + 0 + "\""));
				}
				else
				{
					nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
							"\"" + register_patientgender + "\""));
				}

			}
			else
			{
				if(pval.equalsIgnoreCase("self"))
				{
					nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
					nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
							"\"SELF\""));
					nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"false\""));

					nameValuePairs.add(new BasicNameValuePair("\"Age\"",
							"\"" + 0 + "\""));
					if(register_patientgender.equalsIgnoreCase("null") || register_patientgender.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + 0 + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + register_patientgender + "\""));
					}
				}
				else
				{
					if(selectedPatientId.equalsIgnoreCase("null") || selectedPatientId.equalsIgnoreCase(""))
					{
						nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\"" + 0 + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
								"\"" + pval + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"true\""));

						if(family_page.equalsIgnoreCase(""))
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"",
									"\"" + 0 + "\""));
						}
						else
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + family_page + "\""));
						}
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + family_pgender + "\""));
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair("\"RefPatientID\"", "\""
								+ selectedPatientId + "\""));
						nameValuePairs.add(new BasicNameValuePair("\"PatientName\"",
								"\"\""));
						nameValuePairs.add(new BasicNameValuePair("\"IsRefering\"","\"true\""));

						if(family_page.equalsIgnoreCase(""))
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"",
									"\"" + 0 + "\""));
						}
						else
						{
							nameValuePairs.add(new BasicNameValuePair("\"Age\"","\"" + family_page + "\""));
						}
						nameValuePairs.add(new BasicNameValuePair("\"Gender\"",
								"\"" + family_pgender + "\""));
					}
				}
			}
			nameValuePairs.add(new BasicNameValuePair("\"Pincode\"","\"" + pin + "\""));

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

			nameValuePairs.add(new BasicNameValuePair("\"IsUpdated\"","\"" + updated_val + "\""));
			nameValuePairs.add(new BasicNameValuePair("\"DeviceType\"", "\"" + 1 + "\""));

			nameValuePairs.add(new BasicNameValuePair("\"patientaddress\"","\""+add_val+"\""));

			nameValuePairs.add(new BasicNameValuePair("\"emailid\"","\"\""));
			String finalString = nameValuePairs.toString();
			Log.e("Para+",finalString);
			finalString = finalString.replace('=', ':');
			finalString = finalString.substring(1, finalString.length() - 1);
			String strToServer = "{" + finalString + "}";
			Log.e("strToServer++", strToServer);
			System.out.println("finalString"+finalString);
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
					Log.e("finalResult+",finalResult);
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

	private static String convertInputStreamToString(InputStream inputStream)throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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

						Intent i = new Intent(UploadPrescriptionTab.this,
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
		Button noButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font);
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
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

						Intent i = new Intent(UploadPrescriptionTab.this,
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
		Button noButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		textView.setTypeface(custom_font);
		yesButton.setTypeface(custom_bold_font);
		noButton.setTypeface(custom_bold_font);
	}

	public void clearPreferences() {
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.remove("testid");
		editor.remove("testprofileid");
		editor.remove("test_name");
		editor.remove("filePath");
		editor.remove("filePathTwo");
		editor.remove("filePathThree");
		editor.remove("filePathFour");
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
		editor.remove("imageFromPatientInfo");
		editor.remove("imageFromPatientInfoTwo");
		editor.remove("imageFromPatientInfoThree");
		editor.remove("imageFromPatientInfoFour");
		editor.remove("homecollection");
		editor.remove("centerid");

		selectedDocId = "";

		editor.commit();
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

	private void showPermissionSettingsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Permissions Required")
				.setMessage("To take a photo of prescriptions & directly upload it to the app, the eLAB Assist requires camera and storage permissions. Please enable these permissions in the app settings.")
				.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						openAppSettings();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
	}

	private void openAppSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
				Uri.fromParts("package", getPackageName(), null));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}


	private boolean checkPermission() {
		int writePermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int readPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
		int cameraPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
		return writePermissionResult == PackageManager.PERMISSION_GRANTED && readPermissionResult == PackageManager.PERMISSION_GRANTED && cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
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
