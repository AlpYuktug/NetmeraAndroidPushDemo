package com.netmera.androidpushdemo;


import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netmera.mobile.Netmera;
import com.netmera.mobile.NetmeraCallback;
import com.netmera.mobile.NetmeraDeviceDetail;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraPushObject;
import com.netmera.mobile.NetmeraPushSender;
import com.netmera.mobile.NetmeraPushService;

public class MainActivity extends Activity {

	private LinearLayout pushLayout;

	private TextView registerIdTextView;		// To show the reg. ID
	private TextView registerStatusTextView;	// To show the reg. status

	private String registrationId;				// Registration ID of the device
	private boolean registered;					// Registration status of the device

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);
		pushLayout = (LinearLayout) findViewById(R.id.mainLayout);

		// Add status text views to the layout
		registerIdTextView = new TextView(this);
		pushLayout.addView(registerIdTextView);
		registerStatusTextView = new TextView(this);
		pushLayout.addView(registerStatusTextView);



		Netmera.init(this, GlobalVariables.apiKey);

		// Unregister button
		Button unregButton = new Button(this);
		unregButton.setText("Unregister");
		unregButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (registered) {
					// If registered to the service, unregister from it
					NetmeraDeviceDetail deviceDetail = new NetmeraDeviceDetail(getApplicationContext(), GlobalVariables.projectId, PushActivity.class);
					NetmeraPushService.unregister(deviceDetail);
				} else {
					// If not show an error toast
					Toast.makeText(getBaseContext(), "Not registered to push!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		pushLayout.addView(unregButton);

		// Register button
		Button regButton = new Button(this);
		regButton.setText("Register");
		regButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (registered) {
					Toast.makeText(getBaseContext(), "Already registered", Toast.LENGTH_SHORT).show();
				} else {
					/*
					 * Register method has three parameters. 
					 * applicationContext : current application context.
					 * activityClass : This is the activity launched when user clickes on the notification, this class extends NetmeraActivity.
					 * projectID : This is the ID that you get from the Google on the step-1.
					 */
					try {
						NetmeraDeviceDetail deviceDetail = new NetmeraDeviceDetail(getApplicationContext(), GlobalVariables.projectId, PushActivity.class);
						NetmeraPushService.register(deviceDetail);
					} catch (NetmeraException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		pushLayout.addView(regButton);

		// Button to refresh the registration status
		Button refreshButton = new Button(this);
		refreshButton.setText("Refresh");
		refreshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPage();
			}
		});
		pushLayout.addView(refreshButton);

		Button sendPushButton = new Button(this);
		sendPushButton.setText("send hello world push");
		sendPushButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NetmeraPushObject push = new NetmeraPushObject();

				push.setMessage("Iniesta scored second goal in tonight's game!");
				push.setTitle("Live Score");
				NetmeraPushSender.setSendToAndroid(true);
				NetmeraPushSender.setSendToIOS(true);
				
				NetmeraPushSender.sendPushNotificationInBackground(push, new NetmeraCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
					}
					@Override
					public void onFail(NetmeraException exception) {
						exception.printStackTrace();
					}
				});
				
			}
		});
		pushLayout.addView(sendPushButton);
		showPage();
	}

	/**
	 * Refresh the registration status and registration ID
	 */
	private void showPage() {
		registrationId = NetmeraPushService.getRegistrationId(getApplicationContext());
		registered = NetmeraPushService.isRegistered(getApplicationContext());

		registerIdTextView.setText(Html.fromHtml("<b>Is registered:</b> " + (registered ? "true" : "false")));
		registerStatusTextView.setText(Html.fromHtml("<b>Registration Id:</b> " + registrationId));
	}
}