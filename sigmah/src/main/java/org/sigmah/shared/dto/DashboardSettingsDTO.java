package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class DashboardSettingsDTO extends BaseModelData implements DTO {
	private int amountColumns;

	public void setAmountColumns(int amountColumns) {
		this.amountColumns = amountColumns;
	}

	public int getAmountColumns() {
		return amountColumns;
	}
	
}
