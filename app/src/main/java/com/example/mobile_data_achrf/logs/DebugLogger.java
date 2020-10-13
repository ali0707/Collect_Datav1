/*
 hello 007
 */
package com.example.mobile_data_achrf.logs;

import android.util.Log;

public class DebugLogger {
	
	public static final boolean DEBUG_ON = true;
	
	public static void d(String tag, String msg ){
		if ( DEBUG_ON ) {
			Log.d(tag, msg);
		}
	}
	
	public static void d(String tag, String msg, Throwable t ){
		if ( DEBUG_ON ) {
			Log.d(tag, msg, t);
		}
	}
}
