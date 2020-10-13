/*
 hello 007
 */
package com.example.mobile_data_achrf.providers;

import android.net.Uri;
import android.provider.CallLog;

public class CallLogProvider extends CSVForensicsProvider {

	public CallLogProvider(String displayName, Uri uri) {
		super(displayName, uri);
	}
	
	protected String[] getProviderProjection() {
        String[] projection = new String[] {
        		CallLog.Calls._ID,
        		CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.NEW,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_NUMBER_TYPE,
                CallLog.Calls.CACHED_NUMBER_LABEL };
        return projection;
	}
}
