package edu.fsu.cs.scramd.camera;


import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import edu.fsu.cs.scramd.R;
import edu.fsu.cs.scramd.data.UserAccount;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CameraConfirmFragment extends Fragment {

	private Button photoButton;
	private Button saveButton;
	private Button cancelButton;
	private Button logoutButton;
	private TextView status;
	private Spinner sendSpinner;
	private ParseImageView imagePreview;	
	
	private String friendName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle SavedInstanceState){
		
			View v = inflater.inflate(R.layout.fragment_camera_confirm, parent, false);
			
			status = ((EditText) v.findViewById(R.id.statusEt));
			
			Bundle b = getArguments();			
			if(b != null)
				friendName = b.getString("friendName");
			else
				friendName = "";
			
			
			photoButton = ((Button) v.findViewById(R.id.photo_button));
			photoButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(status.getWindowToken(), 0);
					startCamera();
				}
			});
			
			saveButton = ((Button) v.findViewById(R.id.save_button));
			saveButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					UserAccount myAccount = ((CameraActivity) getActivity()).getCurrentAccount();
					
					// When the user clicks "Save," upload the meal to Parse
					// Add data to the meal object:
					myAccount.setStatus("sent");
					
					//Associate the scram with current user
					myAccount.setSentBy(ParseUser.getCurrentUser());
					
					
//////////////////////IN the future, send it to desired person
					myAccount.setSendTo(friendName);
					
				
					
					//If the user added a photo, that data will
					//be added in the CameraFragment
					
					//Save the myAcc and return
					myAccount.saveInBackground(new SaveCallback(){

						@Override
						public void done(ParseException e) {
							if(e == null){
								
								// Find users near a given location
								ParseQuery<ParseInstallation> userQuery = ParseInstallation.getQuery();
								userQuery.whereContains("user", friendName);
								
								// Send push notification to query
								ParsePush push = new ParsePush();
								push.setQuery(userQuery); //pushQuery); // Set our Installation query			
								push.setMessage("NEW CHALLENGE!");
								push.sendInBackground();
																
								
								getActivity().setResult(Activity.RESULT_OK);
								getActivity().finish();
							}else{
								Toast.makeText(getActivity().getApplicationContext(), 
										"Error Saving: " + e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
							
						}

					});
				}
			});
			
			cancelButton = ((Button) v.findViewById(R.id.cancel_button));
			cancelButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getActivity().setResult(Activity.RESULT_CANCELED);
					getActivity().finish();
					
				}
			});
			
			//*********************************************************
			// Logout Button Listener
			//*********************************************************
			logoutButton = ((Button) v.findViewById(R.id.logout_button));
			logoutButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//ParseUser.logOut();
					//ParseUser currentUser = ParseUser.getCurrentUser();
					//finish();
					// ^^ Not working b/c there is no intent asking to return data ^^^
					// ****************************************************************
					Toast.makeText(getActivity().getApplicationContext(), "LogOut", Toast.LENGTH_SHORT).show();
				}
			});
			
			//Until usee has taken a photo, hide the preview
			imagePreview = (ParseImageView) v.findViewById(R.id.preview_image);
			imagePreview.setVisibility(View.INVISIBLE);
		
			return v;
		
	}
	
	/*
	 * All data entry about a Meal object is managed from the NewMealActivity.
	 * When the user wants to add a photo, we'll start up a custom
	 * CameraFragment that will let them take the photo and save it to the Meal
	 * object owned by the NewMealActivity. Create a new CameraFragment, swap
	 * the contents of the fragmentContainer (see activity_new_meal.xml), then
	 * add the NewMealFragment to the back stack so we can return to it when the
	 * camera is finished.
	 */
	public void startCamera() {
		Toast.makeText(getActivity(), "cam", Toast.LENGTH_SHORT).show();
		Fragment cameraFragment = new CameraFragment();
		FragmentTransaction transaction = getActivity().getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragmentContainer, cameraFragment);
		transaction.addToBackStack("CameraConfirmFragment");
		transaction.commit();
	}	
	
	/*
	 * On resume, check and see if a meal photo has been set from the
	 * CameraFragment. If it has, load the image in this fragment and make the
	 * preview image visible.
	 */
	@Override
	public void onResume() {
		super.onResume();
		ParseFile photoFile = ((CameraActivity) getActivity())
				.getCurrentAccount().getPhotoFile();
		if (photoFile != null) {
			imagePreview.setParseFile(photoFile);
			imagePreview.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					imagePreview.setVisibility(View.VISIBLE);
				}
			});
		}
	}
	
}
