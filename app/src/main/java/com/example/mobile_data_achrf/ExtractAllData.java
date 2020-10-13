/*
 hello 007
 */
package com.example.mobile_data_achrf;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ExtractAllData extends Activity {
	
	private ForensicsGatherer mGatherer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		
		mGatherer = new ForensicsGatherer(this);
		try {
			mGatherer.processProviders();
		} catch (ForensicsException e) {
			processError(e);
		}
		
	}
	
	private void processError(Exception ex) {
		Log.e(this.getClass().getName(), "Error message: ", ex );
		showErrorOccuredToast(ex.getMessage());
	}
	
	public void showErrorOccuredToast(String errorMessage) {
		Toast.makeText( this, ForensicsApplication.getApplicationName() + " detected an error.", Toast.LENGTH_SHORT).show();
	}
}
