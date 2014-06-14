package com.example.activitylifecycle;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "In the onCreate() event");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "In the onStart() event");
    }
    
    public void onRestart()
    {
        super.onRestart();
        Log.d(TAG, "In the onRestart() event");
    }
    
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "In the onResume() event");
    }
    
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "In the onPause() event");
    }
    
    public void onStop()
    {
        super.onStop();
        Log.d(TAG, "In the onStop() event");
    }
    
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "In the onDestroy() event");
    }

}
