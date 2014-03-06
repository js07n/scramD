package edu.fsu.cs.scramd.friend;


import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import edu.fsu.cs.scramd.R;
import edu.fsu.cs.scramd.data.DatabaseHandler;
import edu.fsu.cs.scramd.data.Friend;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class FriendScreen extends Activity {

	TabHost tabHost;
	
	ListView lv;
	
	EditText addET;
	EditText removeET;
	
	ParseUser user;
	
	DatabaseHandler db;
	
	DialogCamera cameraD;
	DialogDifficulty playD;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	
	    setContentView(R.layout.activity_friend_screen);
	    
	    db = new DatabaseHandler(this);
	    
	    user = ParseUser.getCurrentUser();
	    
	    tabHost = (TabHost) findViewById(R.id.tabhost); 
	    tabHost.setup();
	   	    	   
	    TabSpec spec1 = tabHost.newTabSpec("Friend List");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Friend List");

        TabSpec spec2 = tabHost.newTabSpec("Add Friends");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Add Friends");
        
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				if(tabId.equals("Friend List"))
					refreshList();				
			}
		});
        
        // =====================================================================================
        //FRIEND LIST TAB
        
        cameraD = new DialogCamera();
        
        playD = new DialogDifficulty();
        
        lv = (ListView) findViewById(R.id.lv);
        
        refreshList();
         
      
        // ============================================================================
        // ADD FRIEND TAB
        
        addET = (EditText) findViewById(R.id.addEt);
        removeET = (EditText) findViewById(R.id.removeEt);
        
	}
	
	

	private void refreshList()
	{		
		Toast.makeText(getApplicationContext(), "Tab changed", Toast.LENGTH_SHORT).show();
		
		 //Retrieve friendlist from user object
		JSONArray jarr = user.getJSONArray("friendList");

		//!!! Show length
		Toast.makeText(getApplicationContext(), Integer.toString(jarr.length()), Toast.LENGTH_SHORT).show();
		
		String[] freunde;
		
		// If friendlist is not empty then perform copy
		if(jarr.length() != 0)
		{
			freunde = new String[jarr.length()];
			
			//copy JSONArray elements to string array (freunde)
			for(int i = 0; i < jarr.length(); i++)		
				freunde[i] = jarr.optString(i);		
		}
		else
			freunde = new String[1];  //Prog may break w/o this.
			
		
		//THIS IS FOR TESTING THIS WILL *NOT* STAY IN FINAL CODE
		//USE THIS TO FIX DB ERRORS
		//db.deleteFriend(new Friend("goomail"));
		
		
		//TESTING - GET FRIENDS FROM APP DB		
		if(db.getFriendsCount() != 0)
		{
			Toast.makeText(getApplicationContext(), "DB " + Integer.toString(db.getFriendsCount()), Toast.LENGTH_SHORT).show();
			List<Friend> friends = db.getAllFriends();
			freunde = new String[db.getFriendsCount()];
			for(int i = 0; i < friends.size(); i++)
				freunde[i] = friends.get(i).getUsername();
			

			
			//TESTING 0.25.14
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//Toast.makeText(getApplicationContext(), arg0.getAdapter().getItem(arg2).toString(), Toast.LENGTH_SHORT).show();
					
					//fight Status
					//if(db.getFriend(arg0.getAdapter().getItem(arg2).toString()).getStatus().equals("fight"))
					if(db.getFriend(arg0.getAdapter().getItem(arg2).toString()).getStatus().equals("fight"))
					{
						Toast.makeText(getApplicationContext(), 
								db.getFriend(arg0.getAdapter().getItem(arg2).toString()).getStatus(), 
								Toast.LENGTH_SHORT).show();
						
						// Send Friend Name to Dialog Bundle
						Bundle dialogBundle = new Bundle();	        			        			
						dialogBundle.putString("friendName", arg0.getAdapter().getItem(arg2).toString());					
						cameraD.setArguments(dialogBundle);
						//cameraD.setCancelable(false);
						cameraD.show(getFragmentManager(), "DialogCamera");
					}
					//play Status
					else if(db.getFriend(arg0.getAdapter().getItem(arg2).toString()).getStatus().equals("play"))
					{
						Toast.makeText(getApplicationContext(), 
								db.getFriend(arg0.getAdapter().getItem(arg2).toString()).getStatus(), 
								Toast.LENGTH_SHORT).show();
						
						String friendName = arg0.getAdapter().getItem(arg2).toString();
						
			        	// Send Game Type to Dialog Fragment
			        	Bundle dialogBundle = new Bundle();	        			        			
						dialogBundle.putString("GameType", "friend");
						dialogBundle.putString("friendName", friendName);
						//dialogBundle.putBoolean("fromMenu", true);
			        	playD.setArguments(dialogBundle);
			        	playD.setCancelable(false);
						playD.show(getFragmentManager(), "DialogDifficulty");
						
					}
					else
						Toast.makeText(getApplicationContext(), "else Status", Toast.LENGTH_SHORT).show();
					
					
					
				}
			});
			
		}
		else
			Toast.makeText(getApplicationContext(), "DB has ZERO entries", Toast.LENGTH_SHORT).show();
		//END TESTING
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_friend, 
				R.id.lvtv, freunde);
		
		lv.setAdapter(adapter);

	}
	
	
	

	public void addFriend(View v)
	{		
		//TESTING !!
		//Toast.makeText(getApplicationContext(), user.getUsername(), Toast.LENGTH_SHORT).show();

		//1. Check to see if User doesn't ALREADY have friend in friendlist
		if(isUserAFriend(addET) || addET.getText().toString().equals(""))
			Toast.makeText(getApplicationContext(), "Invalid Request", Toast.LENGTH_SHORT).show();
		else
		{
			//2. Check Server info to see if username exists.				
			ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");		
			//query.whereContains("username", addET.getText().toString());
			query.whereEqualTo("username", addET.getText().toString());
		
			query.getFirstInBackground(new GetCallback<ParseUser>(){

				@Override
				public void done(ParseUser object, ParseException e) {
				
					// 	2.1 If username exists, add it to user's friend list
					if(e == null)
					{
						Toast.makeText(getApplicationContext(), "User Found", Toast.LENGTH_SHORT).show();
						
						//Add to app DB
						db.addFriend(new Friend(object.getUsername(), "fight", null));
						
						//Copy current user's Friend list to variable
						JSONArray jarr = user.getJSONArray("friendList");
					
						//Add friend name to JSON
						jarr.put(addET.getText().toString());

						//Attach updated friendList to user
						user.put("friendList", jarr);
												
						saveList(addET);					
								
					}				
					else // 2.2 If username doesn't exist, print out an error message.
					{
						Toast.makeText(getApplicationContext(), "User NOT FOUND", Toast.LENGTH_SHORT).show();
						addET.setText("");
					}
				}
			});
		}
		
		
	}


	
	public void removeFriend(View v)
	{		
		//Checks to see if the friend
		if(isUserAFriend(removeET))
		{
			String remFriend = "";
			
			// This creates a new copy of JSONArray with the omission of the deleted friend		
			JSONArray jarr = new JSONArray();
		
			for(int i = 0; i < user.getJSONArray("friendList").length(); i++)
			{
				// don't add "deleted" friend
				if(removeET.getText().toString().equals(user.getJSONArray("friendList").optString(i)))
				{
					//remove friend from ap DB
					remFriend = user.getJSONArray("friendList").optString(i);
				}
				else
					jarr.put(user.getJSONArray("friendList").optString(i));
			}

			//remove friend from app DB
			db.deleteFriend(new Friend(remFriend));
			
			//Update friendlist 
			user.put("friendList", jarr);
			
			//Save friendlist
			saveList(removeET);
		}
	}
	
	
	
	
	//Checks to see if Username entered in EditText is already the user's friend
	private boolean isUserAFriend(EditText v)
	{
		//First Check to to see if user is trying to be-/de-friend himself
		if(v.getText().toString().equals(user.getUsername()))
			return true;
		
		
		JSONArray jarr = user.getJSONArray("friendList");
		
		//Check to see if User is friends with requested user
		for(int i = 0; i < jarr.length(); i++)
		{
			if(v.getText().toString().equals(jarr.optString(i)))
				return true;
		}		
		return false;
	}
	
	
	
	
	private void saveList(final EditText et)
	{
		user.saveInBackground(new SaveCallback() {			
			@Override
			public void done(ParseException e) {
				if(e == null)
				{
					Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
					et.setText("");
				}
				else
				{
					Toast.makeText(getApplicationContext(), "FAILED", Toast.LENGTH_SHORT).show();
					et.setText("");
				}
			}
		});
				
	}
	
	
	

	
	
}

