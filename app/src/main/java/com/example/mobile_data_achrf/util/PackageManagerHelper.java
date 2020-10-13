/*
 hello 007
 */
package com.example.mobile_data_achrf.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageManagerHelper {
	public static String getVersionName(Context c) {
		String versionName;
		try {
			versionName = c.getPackageManager().getPackageInfo(
					c.getPackageName(), 
					PackageManager.GET_META_DATA).versionName;
		} catch (NameNotFoundException e) {
			versionName = "";
		}
		return versionName;
	}
}
