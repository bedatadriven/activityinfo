package org.sigmah.client.page.entry.location;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.admin.AdminComboBox;
import org.sigmah.client.page.entry.admin.AdminComboBoxSet;
import org.sigmah.client.page.entry.admin.AdminFieldSetPresenter;
import org.sigmah.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

public class SearchAdminComboBoxSet extends AdminComboBoxSet {

	private final LayoutContainer container;
	private final AdminFieldSetPresenter presenter;

	
	public SearchAdminComboBoxSet(LayoutContainer container, AdminFieldSetPresenter presenter) {
		super(presenter, new SearchStyle(container));
		assert container != null;
		this.container = container;
		this.presenter = presenter;
		
		updateCombos(EditMode.SEARCH);
	}

	public void setMode(EditMode mode) {
		((SearchStyle)getStyle()).setMode(mode);
		updateCombos(mode);
		container.layout();
	}
	
	private void updateCombos(EditMode mode) {
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
	
	private static class SearchStyle implements Style {
	
		private final LayoutContainer container;
		private EditMode mode = EditMode.SEARCH;
		
		public SearchStyle(LayoutContainer container) {
			this.container = container;
		}
		
		public void setMode(EditMode mode) {
			this.mode = mode;
		}

		@Override
		public void initializeComboBox(AdminComboBox comboBox, AdminLevelDTO level) {
			
		}
		
		@Override
		public void onComboStateUpdated(AdminComboBox comboBox,
				boolean enabled) {
			if(mode == EditMode.SEARCH) {
				comboBox.setVisible(enabled);
				container.layout();
			}
		}
	}

}
