package edu.fsu.cs.scramd.camera;


import edu.fsu.cs.scramd.R;
import edu.fsu.cs.scramd.data.UserAccount;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CameraActivity extends Activity {

	private UserAccount myAccount;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		myAccount = new UserAccount();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.frame_camera);
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
 
        if (fragment == null) {
        	fragment = new CameraConfirmFragment();
        	
        	if(getIntent().getExtras().getString("friendName") != null)
        	{
        		String friendName = getIntent().getExtras().getString("friendName");
        		Bundle b = new Bundle();
        		b.putString("friendName", friendName);
        		fragment.setArguments(b);
        		
        	}
        	
            manager.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
	}
	
	public UserAccount getCurrentAccount(){
		return myAccount;
	}
}
