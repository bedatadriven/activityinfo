package org.sigmah.client.page.entry.form;


import java.util.Arrays;

import org.sigmah.client.page.entry.form.resources.SiteFormResources;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;

public class FormNavigationListView extends ListView<FormSectionModel> {

	private ListStore<FormSectionModel> store;
	
	public FormNavigationListView() {
		
		store = new ListStore<FormSectionModel>();

		
		setStore(store);
		setTemplate(SiteFormResources.INSTANCE.formNavigationTemplate().getText());
		setItemSelector(".formSec");
		
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
	}

	public void addSection(FormSectionModel model) {
		store.add(model);
		if(store.getCount() == 1) {
			select(model);
		}
	}
	
	private void select(FormSectionModel model) {
		getSelectionModel().setSelection(Arrays.asList(model));		
	}
	
	private void select(int index) {
		select(store.getAt(index));
	}
	
	private void selectAndFire(int index) {
		select(index);
		ListViewEvent<FormSectionModel> event = new ListViewEvent<FormSectionModel>(this);
		event.setModel(getSelectionModel().getSelectedItem());
	}
	
	public void prev() {
		if(!getSelectionModel().getSelection().isEmpty()) {
			int index = store.indexOf(getSelectionModel().getSelectedItem());
			if(index > 0) {
				selectAndFire(index-1);
			}
		}
	}
	
	public void next() {
		if(!getSelectionModel().getSelection().isEmpty()) {
			int index = store.indexOf(getSelectionModel().getSelectedItem());
			if(index+1 < store.getCount()) {
				selectAndFire(index+1);
			}
		}
	}
	

	
}
