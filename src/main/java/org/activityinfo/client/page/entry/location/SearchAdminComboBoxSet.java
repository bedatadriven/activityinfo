package org.activityinfo.client.page.entry.location;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Iterator;
import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.admin.AdminComboBox;
import org.activityinfo.client.page.entry.admin.AdminComboBoxSet;
import org.activityinfo.client.page.entry.admin.AdminFieldSetPresenter;
import org.activityinfo.client.page.entry.admin.AdminComboBoxSet.ComboBoxFactory;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.common.collect.Lists;

public class SearchAdminComboBoxSet implements ComboBoxFactory, Iterable<SearchAdminComboBox> {

	private List<SearchAdminComboBox> comboBoxes = Lists.newArrayList();
	private final LayoutContainer container;

	private EditMode mode = EditMode.SEARCH;
		
	public SearchAdminComboBoxSet(LayoutContainer container, AdminFieldSetPresenter presenter) {
		super();
		this.container = container;
		new AdminComboBoxSet(presenter, this);
	}

	@Override
	public AdminComboBox create(AdminLevelDTO level,
			ListStore<AdminEntityDTO> store) {
		final SearchAdminComboBox box = new SearchAdminComboBox(level, store);
		box.addListener(Events.Enable, new Listener<ComponentEvent>() {

			@Override
			public void handleEvent(ComponentEvent be) {
				if(mode == EditMode.SEARCH) {
					box.setVisible(box.isEnabled());
					container.layout();
				}
			}
		});
		comboBoxes.add(box);
		return box;
	}
	
	public void setMode(EditMode mode) {
		this.mode = mode;
		updateCombos();
		container.layout();
	}
	
	private void updateCombos() {
		for(SearchAdminComboBox comboBox : comboBoxes) {
			
			if(mode == EditMode.NEW_LOCATION) {
				comboBox.setVisible(true);
				comboBox.setEmptyText("");
			} else {
				comboBox.setVisible(comboBox.isEnabled());
				comboBox.setEmptyText(I18N.CONSTANTS.clickToFilter());
			}
		}
	}

	@Override
	public Iterator<SearchAdminComboBox> iterator() {
		return comboBoxes.iterator();
	}
	

}
