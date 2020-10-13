/*
 hello 007
 */
package com.example.mobile_data_achrf.view;

import com.example.mobile_data_achrf.providers.ForensicsProvider;

public class ForensicsProviderListItem {
	
	private ForensicsProvider provider;
	private Boolean isChecked;
	
	public ForensicsProviderListItem(ForensicsProvider provider, Boolean isChecked) {
		this.provider = provider;
		this.isChecked = isChecked;
	}
	
	public ForensicsProvider getProvider() {
		return provider;
	}
	public void setProvider(ForensicsProvider provider) {
		this.provider = provider;
	}
	public Boolean getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	
}
