package com.parse.starter;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.PushService;

import com.parse.ParseUser;

import android.app.Application;


public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		String APP_ID = "UjzhrbcURnpbJppWdLLrFzpV3tVJLUfyMi8GhCY2";
		String CLIENT_KEY = "ITxk2VtfXhJHTTrvlArAvIjz4Ut9fmhlqMs74RCn";
		
		// Add your initialization code here
		Parse.initialize(this, APP_ID, CLIENT_KEY);


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		
		// This is activity that will be opened up when a notification is clicked on.
		PushService.setDefaultPushCallback(getApplicationContext(), PushActivity.class);
		
		
		
	}

}
