/*
 hello 007
 */
package com.example.mobile_data_achrf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;

public class DataCaptureDetailProvider {

	public static void createDetailsFile(final Context ctx, File forensicsDir, String timeStampAsString, PackageManager packageManager) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(forensicsDir, "info.xml")));
			writer.write("<android-forensics>\n");
			writer.write("<date-time>" + timeStampAsString + "</date-time>\n");
			if ( telephonyManager != null ) {
				writer.write("<IMSI>" + telephonyManager.getSubscriberId() + "</IMSI>\n");
				writer.write("<IMEI-MEID>" + telephonyManager.getDeviceId() + "</IMEI-MEID>\n");

				//indicates the device phone type (CDMA, GSM, None, SIP)
				writer.write("<phone-type>" + telephonyManager.getPhoneType() + "</phone-type>\n");

				//Returns the MDN / MSISDN for the primary line.
				writer.write("<MSISDN-MDN>" + telephonyManager.getLine1Number() + "</MSISDN-MDN>\n");

				//Returns the ICCID
				writer.write("<ICCID>" + telephonyManager.getSimSerialNumber() + "</ICCID>\n");

			}

			writer.write("<build>\n");
				writer.write("\t<version.release>" + Build.VERSION.RELEASE +"</version.release>\n");
				writer.write("\t<version.sdk>" + Build.VERSION.SDK +"</version.sdk>\n");
				writer.write("\t<version.incremental>" + Build.VERSION.INCREMENTAL +"</version.incremental>\n");
				writer.write("\t<board>" + Build.BOARD +"</board>\n");
				writer.write("\t<brand>" + Build.BRAND +"</brand>\n");
				writer.write("\t<device>" + Build.DEVICE +"</device>\n");
				writer.write("\t<display>" + Build.DISPLAY +"</display>\n");
				writer.write("\t<fingerprint>" + Build.FINGERPRINT +"</fingerprint>\n");
				writer.write("\t<host>" + Build.HOST +"</host>\n");
				writer.write("\t<id>" + Build.ID +"</id>\n");
				writer.write("\t<model>" + Build.MODEL +"</model>\n");
				writer.write("\t<product>" + Build.PRODUCT +"</product>\n");
				writer.write("\t<tags>" + Build.TAGS +"</tags>\n");
				writer.write("\t<time>" + Build.TIME +"</time>\n");
				writer.write("\t<type>" + Build.TYPE +"</type>\n");
				writer.write("\t<user>" + Build.USER +"</user>\n");
			writer.write("</build>\n");
			
			writer.write("<applications>\n");
			List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
			for ( ApplicationInfo appInfo : apps ) {
				writer.write("\t<app>\n");
					writer.write("\t\t<label>" + appInfo.loadLabel(packageManager) + "</label>\n");
					writer.write("\t\t<className>" + appInfo.className + "</className>\n");
					writer.write("\t\t<dataDir>" + appInfo.dataDir + "</dataDir>\n");
					writer.write("\t\t<descriptionRes>" + appInfo.descriptionRes + "</descriptionRes>\n");
					writer.write("\t\t<flags>" + appInfo.flags + "</flags>\n");
					writer.write("\t\t<manageSpaceActivityName>" + appInfo.manageSpaceActivityName + "</manageSpaceActivityName>\n");
					writer.write("\t\t<name>" + appInfo.name + "</name>\n");
					writer.write("\t\t<packageName>" + appInfo.packageName + "</packageName>\n");
					writer.write("\t\t<permission>" + appInfo.permission + "</permission>\n");
					writer.write("\t\t<processName>" + appInfo.processName + "</processName>\n");
					writer.write("\t\t<publicSourceDir>" + appInfo.publicSourceDir + "</publicSourceDir>\n");
					writer.write("\t\t<sourceDir>" + appInfo.sourceDir + "</sourceDir>\n");
					writer.write("\t\t<taskAffinity>" + appInfo.taskAffinity + "</taskAffinity>\n");
					writer.write("\t\t<uid>" + appInfo.uid + "</uid>\n");
					writer.write("\t\t<enabled>" + appInfo.enabled + "</enabled>\n");
					writer.write("\t\t<description>" + appInfo.loadDescription(packageManager) + "</description>\n");
					try {
						PackageInfo pkgInfo = packageManager.getPackageInfo(appInfo.packageName, PackageManager.GET_META_DATA);
						writer.write("\t\t<packageinfo>");
							writer.write("\t\t\t<versionCode>" + pkgInfo.versionCode + "</versionCode>\n");
							writer.write("\t\t\t<versionName>" + pkgInfo.versionName + "</versionName>\n");
						writer.write("\t\t</packageinfo>");
					} catch (NameNotFoundException e) {}
				writer.write("\t</app>\n");
			}
			writer.write("</applications>\n");
			writer.write("</android-forensics>\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
