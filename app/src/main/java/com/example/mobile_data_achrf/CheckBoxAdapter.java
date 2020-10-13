/*
 hello 007
 */
package com.example.mobile_data_achrf;

import java.util.List;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;



import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class CheckBoxAdapter<T> extends ArrayAdapter<T> {
	
	private Context mContext = null;
	
	public CheckBoxAdapter(Context context, int textViewResourceId, List<T> objects) {
		super(context, textViewResourceId, objects);
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckBox box = (CheckBox) super.getView(position, convertView, parent);
		
		final ForensicsActivity activity = (ForensicsActivity) mContext;
		box.setChecked( activity.getListManager().isChecked(position) );
		
		// Check to see if this is a new box
		if (convertView == null || box != convertView) {
			box.setOnClickListener( activity );		
		}
		
		return box;
	}
	

}
