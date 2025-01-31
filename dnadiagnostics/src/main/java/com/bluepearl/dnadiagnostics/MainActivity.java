package com.bluepearl.dnadiagnostics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends Activity {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;
	private Thread mSplashThread;

	ImageButton btnOfferImg;
	 ImageView zoom ;
	 Animation zoomAnimation;
	 Animation bottomUp;
	ImageView cncl;

	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//getSupportActionBar().hide();


		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		// Splash screen view
		setContentView(R.layout.activity_main);
		btnOfferImg = (ImageButton) findViewById(R.id.ViewOfferBtnimg);
		btnOfferImg.setVisibility(View.GONE);
		  zoom = (ImageView) findViewById(R.id.imageView);
		cncl = (ImageView) findViewById(R.id.cancel_id);

		FirebaseApp.initializeApp(this);
		subscribeToFCMPushService();

		  zoomAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom);

		  bottomUp = AnimationUtils.loadAnimation(this,R.anim.anmationlogo);


		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				btnOfferImg.setVisibility(View.VISIBLE);
				btnOfferImg.startAnimation(bottomUp);
			}
		}, 1000);


		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				zoom.startAnimation(zoomAnimation);
			}
		}, 2000);



		// The thread to wait for splash screen events
		mSplashThread =  new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					synchronized(this)
					{
						// Wait given period of time or exit on touch
						wait(SPLASH_TIME_OUT);
					}
				}
				catch(InterruptedException ex)
				{ }
				// finish()();
				//homecollection.clearAnimation();
				// Run next activity
				Intent i = new Intent();
				i.setClass(MainActivity.this, SigninActivity.class);
				i.putExtra("fromLogout", "no");
				i.putExtra("fromchange", "yes");

				startActivity(i);
				finish();
			}
		};

		mSplashThread.start();

		cncl.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//	homecollection.clearAnimation();
						//	mSplashThread.stop();

						synchronized(mSplashThread)
						{
							mSplashThread.notifyAll();
						}
					}
				}
		);

		zoomAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// finish();
				//  Handler mHandler = new Handler(getMainLooper());
				//  Runnable mRunnable = new Runnable() {
				//    @Override
				//    public void run() {
			/*	finish();
				Intent i = new Intent();
				i.setClass(MainActivity.this, SigninActivity.class);
				i.putExtra("fromLogout", "no");
				i.putExtra("fromchange", "yes");
				//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();*/

				//     }
				//  };
				// mHandler.postDelayed(mRunnable, 100);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});




	}

		/*** Processes splash screen touch events */

	@Override
	public boolean onTouchEvent(MotionEvent evt)
	{
		if(evt.getAction() == MotionEvent.ACTION_DOWN)
		{
			synchronized(mSplashThread)
			{
				mSplashThread.notifyAll();
			}

			/*finish();
			Intent i = new Intent();
			i.setClass(MainActivity.this, SigninActivity.class);
			i.putExtra("fromLogout", "no");
			i.putExtra("fromchange", "yes");
			//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();*/

		}
		return true;
	}

	private void subscribeToFCMPushService() {
		FirebaseMessaging.getInstance().subscribeToTopic("Public");
		String token = FirebaseInstanceId.getInstance().getToken();
		//fcmtoken.setText(token);
		System.out.println("ststtt  tokn "+token);
		FirebaseMessaging.getInstance().subscribeToTopic("Public");

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.putString("FirebaseToken", token);;
		editor.commit();



	}



}