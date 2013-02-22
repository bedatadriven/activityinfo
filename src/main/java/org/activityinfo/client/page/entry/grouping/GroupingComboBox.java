package org.activityinfo.client.page.entry.grouping;

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

import java.util.Collections;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class GroupingComboBox extends ComboBox<GroupingModelData> {

	private final Dispatcher dispatcher;
	private Filter currentFilter = new Filter();
	
	public GroupingComboBox(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		ListLoader<ListLoadResult<GroupingModelData>> loader = new BaseListLoader<ListLoadResult<GroupingModelData>>(new GroupingProxy());
		ListStore<GroupingModelData> store = new ListStore<GroupingModelData>(loader);

		setStore(store);
		setUseQueryCache(false);
		setDisplayField("label");
		setValue(GroupingModelData.NONE);
	}
	
	public GroupingComboBox withSelectionListener(Listener<FieldEvent> listener) {
		addListener(Events.Select, listener);
		return this;
	}
	
	public void setFilter(Filter filter) {
		assert filter != null;
		currentFilter = filter;
	}
	
	public GroupingModel getGroupingModel() {
		return getValue().getModel();
	}
	
	private class GroupingProxy extends RpcProxy<ListLoadResult<GroupingModelData>> {

		@Override
		protected void load(Object loadConfig,
				final AsyncCallback<ListLoadResult<GroupingModelData>> callback) {

			dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(SchemaDTO result) {
					List<GroupingModelData> models = Lists.newArrayList();
					models.add(GroupingModelData.NONE);
					models.add(GroupingModelData.TIME);
					for(AdminLevelDTO level : adminLevels(result)) {
						models.add(new GroupingModelData(level.getName(), new AdminGroupingModel(level.getId())));
					}
					callback.onSuccess(new BaseListLoadResult<GroupingModelData>(models));
				}
			});
			
		}
		
		private List<AdminLevelDTO> adminLevels(SchemaDTO schema) {
			if(currentFilter.getRestrictions(DimensionType.Activity).size() == 1) {
				int activityId = currentFilter.getRestrictedCategory(DimensionType.Activity);
				ActivityDTO activity = schema.getActivityById(activityId);
				List<AdminLevelDTO> levels = Lists.newArrayList(activity.getAdminLevels());
				if(activity.getLocationType().isAdminLevel()) {
					// since the location type is actually bound to the leaf here,
					// we don't want to repeat it in the hieararhcy
					levels.remove(levels.size()-1);
				} 
				return levels;
			} else if(currentFilter.getRestrictions(DimensionType.Database).size() == 1) {
				int databaseId = currentFilter.getRestrictedCategory(DimensionType.Database);
				return schema.getDatabaseById(databaseId).getCountry().getAdminLevels();
			} else if(schema.getCountries().size() == 1) {
				return schema.getDatabases().get(0).getCountry().getAdminLevels();
			} else {
				return Collections.emptyList();
			}
		}
	}
}
