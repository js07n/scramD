package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class ParseStarterProjectActivity extends Activity implements OnClickListener{
	/** Called when the activity is first created. */
	
	
	Button testButton;
	
	Button LogBro;
	Button LogDino;

	Button stBro;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		testButton = (Button) findViewById(R.id.testButton);
		LogBro = (Button) findViewById(R.id.LogBro);
		LogDino = (Button) findViewById(R.id.LogDino);
		stBro = (Button) findViewById(R.id.stBro);
		
		

		testButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ParseUser user = new ParseUser();
				user.setUsername("Derp");
				user.setPassword("Derp");
				//user.setEmail("james.silvera@yahoo.com");
				 
				// other fields can be set just like with ParseObject
				//user.put("phone", "650-253-0000");
				 
				user.signUpInBackground(new SignUpCallback() {
				  public void done(ParseException e) {
				    if (e == null) {
				      // Hooray! Let them use the app now.
				    } else {
				      // Sign up didn't succeed. Look at the ParseException
				      // to figure out what went wrong
				    }
				  }
				});
			}
		});
		
		

		ParseAnalytics.trackAppOpened(getIntent());
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		if(v == LogBro)
		{

			
			ParseUser.logInInBackground("Derp", "Derp", new LogInCallback() {
				  public void done(ParseUser user, ParseException e) {
				    if (user != null) {
				      // Hooray! The user is logged in.
				    	Toast.makeText(getApplicationContext(), "login derp", Toast.LENGTH_SHORT).show();
				    	
				    	ParseInstallation installation = new ParseInstallation();
				    	installation.put("user",ParseUser.getCurrentUser().getUsername());
						installation.saveInBackground();
				    	
				    	// Save the current Installation to Parse.
						//ParseInstallation.getCurrentInstallation().saveInBackground();
						
						
						// Associate the device with a user
						//ParseInstallation installation = ParseInstallation.getCurrentInstallation();
						//installation.put("user",ParseUser.getCurrentUser());
						//installation.saveInBackground();
						
						
				    } else {
				      // Signup failed. Look at the ParseException to see what happened.
				    }
				  }
				});
		}
		else if (v == LogDino)
		{
			
			

			
			ParseUser.logInInBackground("Dino", "Saur", new LogInCallback() {
				  public void done(ParseUser user, ParseException e) {
				    if (user != null) {
				      // Hooray! The user is logged in.
				    	Toast.makeText(getApplicationContext(), "login Dino Saur", Toast.LENGTH_SHORT).show();
				    	
				    	
				    	ParseInstallation installation = new ParseInstallation();
				    	installation.put("user", ParseUser.getCurrentUser().getUsername());
				    	//installation.put("channels", "feet");
						installation.saveInBackground();
						
				    	// Save the current Installation to Parse.
					//	ParseInstallation.getCurrentInstallation().saveInBackground();
						
				/*		
						// Associate the device with a user
						ParseInstallation installation = ParseInstallation.getCurrentInstallation();
						installation.put("user",ParseUser.getCurrentUser());			
						installation.saveInBackground();
					*/	
						
				    } else {
				      // Signup failed. Look at the ParseException to see what happened.
				    }
				  }
				});
		}
		else if(v == stBro)
		{

			// Associate the device with a user
			//ParseInstallation installation = ParseInstallation.getCurrentInstallation();
			//installation.put("user",ParseUser.getCurrentUser());			
			//installation.saveInBackground();
			
			String CU = ParseUser.getCurrentUser().getUsername();
			Toast.makeText(getApplicationContext(), CU, Toast.LENGTH_SHORT).show();

			// Find users near a given location
			ParseQuery<ParseInstallation> userQuery = ParseInstallation.getQuery();
			userQuery.whereContains("user", "Derp");
			//userQuery.whereWithinMiles("location", stadiumLocation, 1.0)
			 
			// Find devices associated with these users
			//ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
			//pushQuery.whereMatchesQuery("user", userQuery);
			 
			// Send push notification to query
			ParsePush push = new ParsePush();
			push.setQuery(userQuery); //pushQuery); // Set our Installation query
			push.setMessage("SPARKLES!");
			push.sendInBackground();
		
		}
		else
			;
		
	}
	
	
	
	
}
