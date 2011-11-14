package org.sigmah.client.page.entry.grouping;

import java.util.Collections;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.DimensionType;

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

			dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(SchemaDTO result) {
					List<GroupingModelData> models = Lists.newArrayList();
					models.add(GroupingModelData.NONE);
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
				return schema.getActivityById(activityId).getAdminLevels();
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
