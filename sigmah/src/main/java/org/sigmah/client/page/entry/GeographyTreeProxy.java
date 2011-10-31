package org.sigmah.client.page.entry;

import java.util.List;
import java.util.Map.Entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.SitesPerAdminEntity;
import org.sigmah.shared.command.SitesPerAdminEntity.SitesPerAdminEntityResult;
import org.sigmah.shared.command.SitesPerAdminEntity.SitesPerAdminEntityResult.AmountPerAdminEntity;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GeographyTreeProxy implements DataProxy<List<ModelData>> {

	private final Dispatcher dispatcher;
	private SitesPerAdminEntityResult sitesPerAdminEntityResult;
	private final static int treshold = 25; // maximum sites for a parent 
	private int activityId;


	public GeographyTreeProxy(Dispatcher dispatcher, int activityId) {
		super();
		this.dispatcher = dispatcher;
		this.activityId = activityId;
	}


	@Override
	public void load(DataReader<List<ModelData>> reader, Object parent,
			AsyncCallback<List<ModelData>> callback) {
		// No AdminEntities available yet, grab them first 
		if (parent == null) {
			grabAdminEntities(callback);
		}
		
		// User clicked "Show more AdminEntities
		if (parent instanceof ShowSitesViewModel) {
			showAllAdminEntityChildren(parent, callback);
		}
		
		// User drills down on an AdminEntity
		if (parent instanceof AdminViewModel) {
			grabAdminEntityChildren(parent, callback);
		}
	}


	/** Grabs a list of AdminEntities with amount of sites contained by them */
	private void grabAdminEntities(final AsyncCallback<List<ModelData>> callback) {
		GetAdminEntities getAdminEntities = new GetAdminEntities();
		dispatcher.execute(new SitesPerAdminEntity(activityId), null, new AsyncCallback<SitesPerAdminEntityResult>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
			@Override
			public void onSuccess(SitesPerAdminEntityResult result) {
				sitesPerAdminEntityResult = result;
				List<ModelData> adminlevels = Lists.newArrayList();
				for (Entry<Integer, AmountPerAdminEntity> entry : result.getAdminEntitiesById().entrySet()) {
					if (entry.getValue().getAdminEntity().getParentId() == null) {
						adminlevels.add(new AdminViewModel(entry.getValue().getAdminEntity(), entry.getValue().getAmountSites()));
					}
				}
				callback.onSuccess(adminlevels);
			}
		});
	}
	

	/** Grabs all sites beloning to a target AdminEntity */
	private void getSitesByAdminEntity(
			final AsyncCallback<List<ModelData>> callback,
			Integer adminEntityParentId,
			final List<ModelData> adminlevels) {
		
		GetSites getSites = new GetSites();
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.AdminLevel, adminEntityParentId);
		//filter.addRestriction(DimensionType.Activity, place.getActivityId());
		getSites.setFilter(filter);
		
		dispatcher.execute(getSites, null, new AsyncCallback<SiteResult>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
			@Override
			public void onSuccess(SiteResult result) {
				for (SiteDTO site : result.getData()) {
					adminlevels.add(site);
				}
				callback.onSuccess(adminlevels);
			}
		});
	}


	/** Grabs children of target AdminEntity */
	private void grabAdminEntityChildren(Object parent,final AsyncCallback<List<ModelData>> callback) {
		AdminViewModel adminEntityViewModelParent = (AdminViewModel) parent;
		AdminEntityDTO adminEntityParent = adminEntityViewModelParent.getAdminEntity();
		
		final List<ModelData> adminlevels = Lists.newArrayList();
		for (Entry<Integer, AmountPerAdminEntity> entry : sitesPerAdminEntityResult.getAdminEntitiesById().entrySet()) {
			if (entry.getValue().getAdminEntity().getParentId() != null && entry.getValue().getAdminEntity().getParentId() == adminEntityParent.getId()) {
				adminlevels.add(new AdminViewModel(entry.getValue().getAdminEntity(), entry.getValue().getAmountSites()));
			}
		}
		if (adminEntityViewModelParent.getAmountSites() < treshold) {
			getSitesByAdminEntity(callback, adminEntityParent.getId(), adminlevels);
		} else {
			ModelData showMore = new ShowSitesViewModel(adminEntityViewModelParent.getAmountSites(), adminEntityParent);
			adminlevels.add(showMore);
			callback.onSuccess(adminlevels);
		}
	}


	/** Displays _all_ sites of target AdminEntity in a drilldown parent */
	private void showAllAdminEntityChildren(Object parent, final AsyncCallback<List<ModelData>> callback) {
		ShowSitesViewModel showSites = (ShowSitesViewModel) parent;
		final List<ModelData> adminlevels = Lists.newArrayList();
		getSitesByAdminEntity(callback, showSites.getAdminEntityId(), adminlevels);
	}

	
}
