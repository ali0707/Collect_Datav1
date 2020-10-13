/*
 hello 007
 */
package com.example.mobile_data_achrf;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.provider.Contacts;
import android.util.Log;
import android.widget.Toast;

import com.example.mobile_data_achrf.logs.DebugLogger;
import com.example.mobile_data_achrf.os.ExternalStorageHandler;
import com.example.mobile_data_achrf.providers.CSVForensicsProvider;
import com.example.mobile_data_achrf.providers.CallLogProvider;
import com.example.mobile_data_achrf.providers.ForensicsProvider;
import com.example.mobile_data_achrf.providers.MmsPartsProvider;
import  com.example.mobile_data_achrf.R;


public class ForensicsGatherer {
	
	private Context mContext = null;

	private List<ForensicsProvider> configuredProviders = null;
	private InfoFileTask mInfoFileTask = null;
	private String mCurrentDateTimeAsString = null;
	private static final String DATE_TIME_PATTERN = "yyyyMMdd.kkmm";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);//TODO: maybe locale later

	private static final String TAG = "AndroidForensics.ForensicsGatherer";

	private static final int MAX_POOL_SIZE = 10;
	private static ProgressDialog mWaitDialog = null;
	private int mNumProvidersToRun;

	private ExecutorService mExecutor = null;
	private Handler mHandler = null;
	
	
	public ForensicsGatherer(Context context) {
		this.mContext = context;
		mCurrentDateTimeAsString = createDateTimeAsString();
		mExecutor = Executors.newFixedThreadPool(MAX_POOL_SIZE);
		initHandler();
		initializeContentProviders();
		
	}
	
	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				final int progress = mWaitDialog.getProgress() + 1;
				if (mWaitDialog != null) {
					if (progress >= mNumProvidersToRun) {
						mWaitDialog.dismiss();
						DialogManager.showOkDialog(mContext, "Ok",
								"Data extraction completed.");
					} else {
						mWaitDialog.setProgress(progress);
					}
				}
			}
		};
	}

	private String createDateTimeAsString() {
		return sdf.format(new Date() );
	}

	public void initializeContentProviders() {
		configuredProviders = new ArrayList<ForensicsProvider>();
		configuredProviders.add( new CallLogProvider("CallLog Calls", CallLog.Calls.CONTENT_URI) );
		configuredProviders.add( new CSVForensicsProvider("Contacts Phones", Contacts.Phones.CONTENT_URI) );
		configuredProviders.add( new CSVForensicsProvider("MMS", Uri.parse("content://mms") ) );
		configuredProviders.add( new MmsPartsProvider("MMSParts", Uri.parse(MmsPartsProvider.CONTENT_URI) ) );
		configuredProviders.add( new CSVForensicsProvider("SMS", Uri.parse("content://sms") ) );
		Log.i( ForensicsGatherer.class.getName(), configuredProviders.size() + " providers initialized.");
	}


	public List<ForensicsProvider> getProviders() {
		return configuredProviders;
	}
	
	public void processProviders(List<ForensicsProvider> providers ) throws ForensicsException {
		mCurrentDateTimeAsString = createDateTimeAsString();
		final File forensicsDir = new ExternalStorageHandler().prepareStorageLocation( mContext, mCurrentDateTimeAsString );
		createInformationDetailsFile(forensicsDir);
		safelyFireBackgroundThreads(providers, forensicsDir);
	}

	private void createInformationDetailsFile(File forensicsDir) {
		mInfoFileTask = new InfoFileTask();
		mInfoFileTask.execute(forensicsDir);
	}

	private void safelyFireBackgroundThreads(List<ForensicsProvider> providers, final File forensicsDir) {
		if ( providers == null ) return;
		
		mNumProvidersToRun = providers.size();
		DebugLogger.d(TAG, " Providers Running = " + mNumProvidersToRun);
		showWaitDialog( mNumProvidersToRun );

		for ( final ForensicsProvider provider : providers ) {
			mExecutor.execute( new Runnable() {
	            public void run() {
	            	try {
						provider.process(mContext, forensicsDir );
					} catch (ForensicsException e) {
						e.printStackTrace();
					}
	                mHandler.sendEmptyMessage(0);
	            }
	        });
	        
		}
		
	}

	private void showWaitDialog( int max) {
		if ( mWaitDialog != null && mWaitDialog.isShowing() ) {
			mWaitDialog.dismiss();
		}
		mWaitDialog = new ProgressDialog( mContext );
		mWaitDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mWaitDialog.setMessage( mContext.getText( R.string.extracting_data ) );
		mWaitDialog.setCancelable(true);
		mWaitDialog.setMax( max );
		mWaitDialog.show();
	}

	public void closeAllTasks() {
		if ( mInfoFileTask != null ) {
			mInfoFileTask.cancel(true);
		}
		mInfoFileTask = null;
		mExecutor.shutdownNow();
	}
	
	private class InfoFileTask extends AsyncTask<File, Void, Void> {
		protected Void doInBackground(File... params) {
			DataCaptureDetailProvider.createDetailsFile( mContext, params[0], mCurrentDateTimeAsString, mContext.getPackageManager() );
			return null;
		}
		
	}
	
	public void processProviders() throws ForensicsException {
		processProviders(configuredProviders);
	}
	
	public void showDataCapturedToast() {
		Toast.makeText(mContext, "Data capture completed.", Toast.LENGTH_SHORT).show();
	}

}
