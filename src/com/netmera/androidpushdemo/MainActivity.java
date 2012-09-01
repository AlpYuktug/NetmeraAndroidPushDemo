package com.netmera.androidpushdemo;

import com.netmera.androidpushdemo.R;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraPushService;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

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

		// Calling init() method with api key to be able to use netmera services.
		NetmeraClient.init(this, GlobalVariables.apiKey);

		// Unregister button
		Button unregButton = new Button(this);
		unregButton.setText("Unregister");
		unregButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (registered) {
					// If registered to the service, unregister from it
					NetmeraPushService.unregister(getApplicationContext(), registrationId);
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
					 * activityClass : This is the activity launched when user clickes on the notification.
					 * projectID : This is the ID that you get from the Google on the step-1.
					 */
					NetmeraPushService.register(getApplicationContext(), PushActivity.class, GlobalVariables.projectId);
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