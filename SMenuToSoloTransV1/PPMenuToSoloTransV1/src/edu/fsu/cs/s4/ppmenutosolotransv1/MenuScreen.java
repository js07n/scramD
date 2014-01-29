package edu.fsu.cs.s4.ppmenutosolotransv1;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MenuScreen extends Activity implements OnClickListener {

	
	Button soloB;
	DialogDifficulty soloD;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_screen);
		
		soloB = (Button) findViewById(R.id.soloB);
		soloD = new DialogDifficulty();
		
		soloB.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_screen, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		if (v == soloB)
		{			
        	// Send Game Type to Dialog Fragment
        	Bundle dialogBundle = new Bundle();	        			        			
			dialogBundle.putString("GameType", "solo");
			dialogBundle.putBoolean("fromMenu", true);
        	soloD.setArguments(dialogBundle);
        	soloD.setCancelable(false);
			soloD.show(getFragmentManager(), "DialogDifficulty");
			
		}
		
	}

}
