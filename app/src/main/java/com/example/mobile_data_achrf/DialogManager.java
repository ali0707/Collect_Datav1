/*
 hello 007
 */
package com.example.mobile_data_achrf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogManager {
	
	public static void showOkDialog(Context ctx, String buttonText, String messageTxt) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage( messageTxt )
		       .setCancelable(false)
		       .setPositiveButton( buttonText, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
}
