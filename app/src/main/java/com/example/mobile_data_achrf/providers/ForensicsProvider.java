/*
 hello 007
 */
package com.example.mobile_data_achrf.providers;

import java.io.File;

import android.content.Context;
import android.net.Uri;

import com.example.mobile_data_achrf.ForensicsException;

public abstract class ForensicsProvider {
	
	protected String displayName = null;
	protected Uri uri = null;
	
	protected final static char COMMA_SEP = ',';
	
	public ForensicsProvider (String displayName, Uri uri) {
		this.displayName = displayName;
		this.uri = uri;
	}

	public abstract void process(Context context, File forensicsDir) throws ForensicsException;

	
	@Override
	public String toString() {
		return displayName;
	}
	
	
}
