/*
 hello 007
 */
package com.example.mobile_data_achrf.view;

import java.util.ArrayList;
import java.util.List;

import com.example.mobile_data_achrf.providers.ForensicsProvider;

public class ForensicsProviderListManager {
	
	private List<ForensicsProviderListItem> providerItems;

	public void initializeProviderList(List<ForensicsProvider> providers) {
		providerItems = new ArrayList<ForensicsProviderListItem>();
		for(ForensicsProvider provider : providers ) {
			providerItems.add( new ForensicsProviderListItem( provider, Boolean.TRUE ) );
		}
	}

	public List<ForensicsProviderListItem> getProviderItems() {
		return providerItems;
	}

	public void setProviderItems(List<ForensicsProviderListItem> providerItems) {
		this.providerItems = providerItems;
	}

	public void selectAll() {
		for ( ForensicsProviderListItem rowItem : providerItems ) {
			rowItem.setIsChecked( true );
		}
	}
	
	public void deselectAll() {
		for ( ForensicsProviderListItem rowItem : providerItems ) {
			rowItem.setIsChecked( false );
		}
	}

	public int getCheckedCount() {
		int numChecked = 0;//less than 10? item checks, not worried about performance for simplicity of class
		for ( ForensicsProviderListItem rowItem : providerItems ) {
			if ( rowItem.getIsChecked() ) numChecked++;
		}
		return numChecked;
	}

	public boolean isChecked(int i) {
		return ( providerItems == null ? false : providerItems.get(i).getIsChecked() );
	}

	public void setChecked(boolean checked, int checkboxViewIndex) {
		if ( providerItems != null ) {
			providerItems.get( checkboxViewIndex ).setIsChecked( checked );
		}
	}
	public ForensicsProvider getProvider(int i) {
		return ( providerItems == null ? null : providerItems.get(i).getProvider() );
	}
	
	public List<ForensicsProvider> getSelectedContentProviders() {
		List<ForensicsProvider> subset = new ArrayList<ForensicsProvider>();
		for ( int i = 0; i < providerItems.size(); i++ ) {
			addToSubsetIfChecked(subset, i);
		}
		return subset;
	}

	private void addToSubsetIfChecked(List<ForensicsProvider> subset, int i) {
		if ( this.isChecked(i) ) subset.add( this.getProvider(i) );
	}
	
	public void setManagerCheckedValues(boolean select) {
		if ( select ) {
			this.selectAll();
		}else {
			this.deselectAll();
		}
	}

	
}
