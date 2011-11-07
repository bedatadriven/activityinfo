package org.sigmah.client.page.entry.location;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.admin.AdminComboBox;
import org.sigmah.client.page.entry.admin.AdminComboBoxSet;
import org.sigmah.client.page.entry.admin.AdminFieldSetPresenter;
import org.sigmah.client.page.entry.admin.AdminSelectionEvent;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class SearchForm extends LayoutContainer {
	
	private final TextField<String> locationName;
	
	private LocationSearchPresenter searchPresenter;
	private AdminFieldSetPresenter adminPresenter;
	
	
	public SearchForm(Dispatcher dispatcher, LocationSearchPresenter presenter) {
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(100);
		layout.setDefaultWidth(150);
		setLayout(layout);
		setStyleAttribute("padding", "5px");
		
		locationName = new TextField<String>();
		locationName.setFieldLabel(I18N.CONSTANTS.location());
		locationName.setEmptyText("Type to filter");
		add(locationName);
		
		searchPresenter = presenter;
		adminPresenter = new AdminFieldSetPresenter(dispatcher, 
				presenter.getCountry(), presenter.getCountry().getAdminLevels());
		AdminComboBoxSet comboBoxes = new AdminComboBoxSet(adminPresenter) {

			@Override
			protected void updateComboBoxState(AdminComboBox comboBox,
					boolean enabled) {
				comboBox.setVisible(enabled);
				SearchForm.this.layout();
			}
		};
		
		for(AdminComboBox comboBox : comboBoxes) {
			add(comboBox);
		}	
		
		adminPresenter.addListener(AdminSelectionEvent.TYPE, new Listener<AdminSelectionEvent>() {

			@Override
			public void handleEvent(AdminSelectionEvent be) {
				searchPresenter.search("", adminPresenter.getAdminEntityIds());
			}
		});
		
	}
}
