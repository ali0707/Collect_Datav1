package com.example.mobile_data_achrf;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mobile_data_achrf.os.ExternalStorageHandler;
import com.example.mobile_data_achrf.providers.ForensicsProvider;
import com.example.mobile_data_achrf.util.PackageManagerHelper;
import com.example.mobile_data_achrf.view.ForensicsProviderListManager;


public class ForensicsActivity extends ListActivity implements OnClickListener {

	private static final String TAG = "com.example.for_achref.ForensicsActivity";
	private ForensicsGatherer mGatherer = null;
	private ForensicsProviderListManager listManager;
	
	/** Called when the activity is first created. */
	@SuppressLint("LongLogTag")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "Version: " + PackageManagerHelper.getVersionName(this));
		
		if ( ! ExternalStorageHandler.isMounted() ) {
			if ( Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED) ) {
				usbMountedAlert();
			}else if ( Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED) ) {
				sdcardRemovedAlert();
			}else {
				Log.e(TAG, "Storage state: " + Environment.getExternalStorageState() );
				finish();
			}
		}else {
			ExternalStorageHandler sdcardHandler = new ExternalStorageHandler();
			final boolean hasReadWriteAccess= sdcardHandler.checkFsWritable(this);
			if ( ! hasReadWriteAccess ) {
				noReadWriteAlert();
			}else {
				mGatherer = new ForensicsGatherer(this);
				listManager = new ForensicsProviderListManager();
				
				setContentView(R.layout.main);
				
				registerOnClickListenersForButtons();
				
				populateProvidersDisplayList();
			}
		}
		
		
		
	}
	
	public ForensicsProviderListManager getListManager() {
		return listManager;
	}
	
	private void registerOnClickListenersForButtons() {
		setButtonOnClickListener(R.id.capture);
		setButtonOnClickListener(R.id.select_all);
		setButtonOnClickListener(R.id.deselect_all);
	}

	private void setButtonOnClickListener( int buttonId ) {
		Button button = (Button) this.findViewById(buttonId);
		button.setOnClickListener(this);
	}

	private void populateProvidersDisplayList() {
		setListAdapterOnActivity();

		List<ForensicsProvider> providers = mGatherer.getProviders();
		listManager.initializeProviderList(providers);
	}

	private void setListAdapterOnActivity() {
		ArrayAdapter<ForensicsProvider> adapter = 
			new CheckBoxAdapter<ForensicsProvider>(this, R.layout.provider_row, mGatherer.getProviders() );
		setListAdapter(adapter);
	}

	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.capture) {
			this.capture();
		} else if (id == R.id.select_all) {
			listManager.selectAll();
			setValueOnAllCheckboxes(true);
		} else if (id == R.id.deselect_all) {
			listManager.deselectAll();
			setValueOnAllCheckboxes(false);
		} else {
			Log.d(this.getClass().getName(), "new checkbox value = " + ((CheckBox) view).isChecked() + "("+ id +")");
			this.flipACheck((CheckBox) view);
		}
	}

	private void capture() {
		try {
			captureProviders();
		} catch (Exception ex) {
			processError(ex);
		}
	}

	private void captureProviders() throws ForensicsException {
		if (listManager.getCheckedCount() == mGatherer.getProviders().size()) {
			mGatherer.processProviders();
		} else {
			mGatherer.processProviders( listManager.getSelectedContentProviders() );
		}
	}

	private void processError(Exception ex) {
		Log.e(this.getClass().getName(), "Error message: ", ex );
		showErrorOccuredToast(ex.getMessage());
	}
	
	public void showErrorOccuredToast(String errorMessage) {
		Toast.makeText( this, ForensicsApplication.getApplicationName() + " detected an error.", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressWarnings("unchecked")
	private void setValueOnAllCheckboxes(boolean select) {
		setValueOnAllListViewCheckboxes(select, this.getListView() );
		listManager.setManagerCheckedValues(select);
		enableCaptureButton(select);
		((ArrayAdapter<ForensicsProvider>) (this.getListAdapter())).notifyDataSetInvalidated();
	}

	private void setValueOnAllListViewCheckboxes(boolean select, ListView list) {
		for (int checkBoxIndex = 0; checkBoxIndex < list.getChildCount(); checkBoxIndex++ ) {
			CheckBox box = (CheckBox) list.getChildAt( checkBoxIndex );
			box.setChecked(select);
		}
	}



	private void enableCaptureButton(boolean enabled) {
		Button button = (Button) this.findViewById(R.id.capture);
		button.setEnabled(enabled);


	}

	private void flipACheck(CheckBox check) {
		final int positionForView = this.getListView().getPositionForView(check);
//		Log.d(TAG, "position = " + positionForView);
		listManager.setChecked( check.isChecked(), positionForView );
		enableCaptureButton( listManager.getCheckedCount() > 0 );
	}

	private void sdcardRemovedAlert() {
		genericOkAlert(R.string.sdcardRemovedTxt);
	}
	private void usbMountedAlert() {
		genericOkAlert(R.string.usbStorageOffTxt);
	}
	private void noReadWriteAlert() {
		genericOkAlert(R.string.noReadWriteTxt);
	}
	private void genericOkAlert(final int msgResId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msgResId)
		       .setCancelable(false)
		       .setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   ForensicsActivity.this.finish();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if ( mGatherer != null ) mGatherer.closeAllTasks();
	}
	
	
	
	
}
