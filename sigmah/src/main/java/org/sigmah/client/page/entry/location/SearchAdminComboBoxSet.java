package org.sigmah.client.page.entry.location;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.admin.AdminComboBox;
import org.sigmah.client.page.entry.admin.AdminComboBoxSet;
import org.sigmah.client.page.entry.admin.AdminFieldSetPresenter;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

public class SearchAdminComboBoxSet extends AdminComboBoxSet {

	private final LayoutContainer container;
	private final AdminFieldSetPresenter presenter;
	private EditMode mode = EditMode.SEARCH;

	
	public SearchAdminComboBoxSet(LayoutContainer container, AdminFieldSetPresenter presenter) {
		super(presenter);
		this.container = container;
		this.presenter = presenter;
		
		updateCombos();
	}

	public void setMode(EditMode mode) {
		this.mode = mode;
		
		updateCombos();
		container.layout();
	}

	private void updateCombos() {
		for(AdminComboBox comboBox : this) {
			if(mode == EditMode.NEW_LOCATION) {
				comboBox.setVisible(true);
				comboBox.setEmptyText("");
			} else {
				comboBox.setVisible(presenter.isLevelEnabled(comboBox.getLevel()));
				comboBox.setEmptyText(I18N.CONSTANTS.clickToFilter());
			}
		}
	}
	
	@Override
	protected void onComboStateUpdated(AdminComboBox comboBox,
			boolean enabled) {
		if(mode == EditMode.SEARCH) {
			comboBox.setVisible(enabled);
			container.layout();
		}
	}
}
