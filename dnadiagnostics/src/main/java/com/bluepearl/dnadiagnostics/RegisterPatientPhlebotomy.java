package com.bluepearl.dnadiagnostics;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class RegisterPatientPhlebotomy extends BaseActivityPhlebotomy {



	ScrollView scrollView;

	Typeface custom_font;
	Button btn1,btnOther ,SelectTextButton,UploadImgButton, selectTextbtn;
	EditText patientname;
	static String outputDate = "";
	Bundle data;
	static String finalResult = "";
	GPSTracker gps;
	public static final String MyPREFERENCES = "MyPrefs";
	static SharedPreferences sharedpreferences;
	static String tid = null;
	static String phone = null;
	static String text = "";
	static String selected_labidfrompref = null;
	private static final String TAG_TESTNAME = "TestName";
	private static final String TAG_TESTID = "TestID";
	ArrayList<String> test_name = new ArrayList<String>();
	ArrayList<String> testid = new ArrayList<String>();
	ArrayList<String> testpid = new ArrayList<String>();
	ArrayList<String> List = new ArrayList<String>();
	ImageButton btnLabchange;
	public EditText editTxtPatientName,editTxtAge,editTxtTestName,editTxtRefDocName,editTxtPatientMobileNo, editTxtPatientAddress;
	private TextInputLayout inputLayoutPatientName, inputLayoutAge , inputLayoutMobileNo, inputLayoutAdress;
	TextView lblgender, txtlname;
	private static RadioButton rb_male;
	private RadioButton rb_female;
	ImageButton imgbtnClear, imgbtnClear2, imgbtnClear3, imgbtnClear4;
	static String patient_name = "";
	static String pid = "";
	static String selected_labnamefrompref_other = "";
	AlertDialog alertbox;
	static String status = "";
	private ProgressDialog pDialog;
	String StrPatientName , StrPatientAge  , StrPatientMobileNo ,	 StrPatientAdress , StrPatientRefDoctor;
	static String StrPatientGeder = "";

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface custom_bold_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

		getLayoutInflater().inflate(R.layout.activity_register_patient_phlebotomy, frameLayout);
		mDrawerList.setItemChecked(position, true);

		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0054A6"));
		getSupportActionBar().setBackgroundDrawable(colorDrawable);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		selected_labnamefrompref_other = sharedpreferences.getString("SelectedLabName", "");


		imgbtnClear = (ImageButton) findViewById(R.id.clear2);
		imgbtnClear2 = (ImageButton) findViewById(R.id.clear3);

		txtlname = (TextView) findViewById(R.id.txtlabname2);
		rb_male = (RadioButton) findViewById(R.id.radioMale2);
		rb_female = (RadioButton) findViewById(R.id.radioFemale2);
		btnLabchange = (ImageButton) findViewById(R.id.changelabbtn2);

		scrollView = (ScrollView) findViewById(R.id.ScrollView_id);
		inputLayoutPatientName = (TextInputLayout) findViewById(R.id.input_layout_name);
		inputLayoutAge = (TextInputLayout) findViewById(R.id.input_layout_age);
		inputLayoutMobileNo = (TextInputLayout) findViewById(R.id.input_layout_mobileNo);
		inputLayoutAdress = (TextInputLayout) findViewById(R.id.input_layout_address);

		editTxtPatientName = (EditText) findViewById(R.id.etPatientName2);
		editTxtAge = (EditText) findViewById(R.id.etPAge2);
		editTxtPatientMobileNo = (EditText) findViewById(R.id.etMobileNo_id);
		editTxtPatientAddress = (EditText) findViewById(R.id.etAddress_id);
		editTxtRefDocName = (EditText) findViewById(R.id.etRefDoctor2);


		editTxtPatientName.addTextChangedListener(new MyTextWatcher(editTxtPatientName));
		editTxtAge.addTextChangedListener(new MyTextWatcher(editTxtAge));
		editTxtPatientMobileNo.addTextChangedListener(new MyTextWatcher(editTxtPatientMobileNo));
		//editTxtPatientAddress.addTextChangedListener(new MyTextWatcher(editTxtPatientAddress));

		selectTextbtn = (Button) findViewById(R.id.TextSelectionButton_id);


		editTxtPatientName.setTypeface(custom_font);
		editTxtAge.setTypeface(custom_font);
		editTxtPatientMobileNo.setTypeface(custom_font);
		editTxtPatientAddress.setTypeface(custom_font);
		editTxtRefDocName.setTypeface(custom_font);
		//lblgender.setTypeface(custom_font);

		txtlname.setTypeface(custom_font);
		rb_male.setTypeface(custom_font);
		rb_female.setTypeface(custom_font);
		//btnQuickBook.setTypeface(custom_bold_font);

		if (selected_labnamefrompref_other != null || !(selected_labnamefrompref_other.equalsIgnoreCase(""))) {
			txtlname.setText(selected_labnamefrompref_other);
		}


		selectTextbtn.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {

						submitForm();

						//Intent i = new Intent(RegisterPatientPhlebotomy.this, RegisterPatientPhlebotomyAdv.class);
						//startActivity(i);
						//finish();

					}
				}
		);


		btnLabchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor1 = sharedpreferences.edit();

				editor1.remove("CheckedValue");

				editor1.commit();

				Intent i = new Intent(RegisterPatientPhlebotomy.this, LabSelectionForDoctor.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				//finish();
			}
		});


		imgbtnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editTxtPatientName.setText("");
			}
		});

		imgbtnClear2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editTxtAge.setText("");
			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(RegisterPatientPhlebotomy.this, PhlebotomyTabOn.class);
		startActivity(i);
		//finish();
	}





	void scrollDown()
	{
		Thread scrollThread = new Thread(){
			public void run(){
				try {
					sleep(200);
					RegisterPatientPhlebotomy.this.runOnUiThread(new Runnable() {
						public void run() {
							scrollView.fullScroll(View.FOCUS_DOWN);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		scrollThread.start();
	}

	//editTxtPatientName = (EditText)findViewById(R.id.etPatientName2);
	//editTxtAge = (EditText)findViewById(R.id.etPAge2);
	//editTxtPatientMobileNo = (EditText)findViewById(R.id.etMobileNo_id);
	//editTxtPatientAddress = (EditText)findViewById(R.id.etEmail_id);
//	private TextInputLayout inputLayoutPatientName, inputLayoutAge , inputLayoutMobileNo, inputLayoutAdress ;

	private boolean validatePatientName() {
		if (editTxtPatientName.getText().toString().trim().isEmpty()) {
			inputLayoutPatientName.setError("Please Enter Patient Name");
			requestFocus(editTxtPatientName);
			return false;
		} else {
			inputLayoutPatientName.setErrorEnabled(false);
		}

		return true;
	}


	private boolean validateAge() {
		if (editTxtAge.getText().toString().trim().isEmpty()) {
			inputLayoutAge.setError("Enter Patient Age");
			requestFocus(editTxtAge);
			return false;
		} else {
			inputLayoutAge.setErrorEnabled(false);
		}

		return true;
	}


	private boolean isValidMobile(String phone2) {
		boolean check;
		if (phone2.length() < 10 || phone2.length() > 10) {
			check = false;
		} else {
			check = true;
		}
		return check;
	}

	private boolean validateMobileNo() {

		String PmonileNo = editTxtPatientMobileNo.getText().toString().trim();

		//	if (editTxtPatientMobileNo.getText().toString().trim().isEmpty()) {
		if (PmonileNo.length() < 10 || PmonileNo.length() > 10) {
			inputLayoutMobileNo.setError("Enter Valid Mobile Number");
			requestFocus(editTxtPatientMobileNo);
			return false;
		} else {
			inputLayoutMobileNo.setErrorEnabled(false);
		}

		return true;
	}

	private boolean validateEmail() {
		String email = editTxtPatientAddress.getText().toString().trim();

		if (email.isEmpty() || !isValidEmail(email)) {
			inputLayoutAdress.setError("Enter valid email address");
			requestFocus(editTxtPatientAddress);
			return false;
		} else {
			inputLayoutAdress.setErrorEnabled(false);
		}

		return true;
	}

	private static boolean isValidEmail(String email) {
		return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {
				case R.id.etPatientName2:
					validatePatientName();
					break;
				case R.id.etPAge2:
					validateAge();
					break;
				case R.id.etMobileNo_id:
					validateMobileNo();
					break;
				//case R.id.etEmail_id:
				//	validateEmail();
				//	break;
			}
		}
	}

	private void submitForm() {
		if (!validatePatientName()) {
			return;
		}

		if (!validateAge()) {
			return;
		}

		if (!validateMobileNo()) {
			return;
		}

		//if (!validateEmail()) {
		//	return;
		//}

		StrPatientName = editTxtPatientName.getText().toString();
		StrPatientAge = editTxtAge.getText().toString();

		if(rb_male.isChecked())
		{
			StrPatientGeder="0";
		}
		else
		{
			StrPatientGeder="1";
		}

		StrPatientMobileNo = editTxtPatientMobileNo.getText().toString();
		StrPatientAdress = editTxtPatientAddress.getText().toString();
		StrPatientRefDoctor = editTxtRefDocName.getText().toString();

		Intent intent = new Intent(RegisterPatientPhlebotomy.this, RegisterPatientPhlebotomyAdv.class);

		intent.putExtra("StrPatientName", StrPatientName);
		intent.putExtra("StrPatientAge", StrPatientAge);
		intent.putExtra("StrPatientGeder", StrPatientGeder);
		intent.putExtra("StrPatientMobileNo", StrPatientMobileNo);
		intent.putExtra("StrPatientAdress", StrPatientAdress);
		intent.putExtra("StrPatientRefDoctor", StrPatientRefDoctor);

		startActivity(intent);
		//finish();
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

}
