package org.sigmah.client.page.entry;

import java.util.Date;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.entry.SiteTreeGridPageState.TreeType;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.common.collect.Lists;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SiteTreeProxy implements DataProxy<List<ModelData>> {
		private Dispatcher service;
		private SiteTreeGridPageState place;
		private Filter filter;
		
		public SiteTreeProxy(Dispatcher service) {
			super();
			this.service = service;
		}

		@Override
		public void load(DataReader<List<ModelData>> reader, Object parent,
				final AsyncCallback<List<ModelData>> callback) {
			if (place.getTreeType() == TreeType.GEO) {
				getDataForGeo(parent, callback);
			}
			if (place.getTreeType() == TreeType.TIME) {
				getDataForTime(parent, callback);
			}
		}
		
		private void getDataForTime(Object parent, final AsyncCallback<List<ModelData>> callback) {
			if (parent == null) {
				List<ModelData> yearsAndProvinces = Lists.newArrayList();
				callback.onSuccess(createYears());
			}
			
			if (parent instanceof YearViewModel) {
				YearViewModel year = (YearViewModel) parent;
				callback.onSuccess(createMonths(year.getYear()));
			}
			
			if (parent instanceof MonthViewModel) {
				MonthViewModel month = (MonthViewModel) parent;
				GetSites getSites = new GetSites();
				filter.setMinDate(new Date(month.getYear(), month.getMonth(), 1));
				filter.setMaxDate(new Date(month.getYear(), month.getMonth(), 28));
				getSites.setFilter(filter);
				getSites.setSortInfo(new SortInfo("date2", SortDir.ASC));
				service.execute(getSites, null, new AsyncCallback<SiteResult>() {
					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(SiteResult result) {
						List<ModelData> sites = Lists.newArrayList();
						for (SiteDTO site : result.getData()) {
							sites.add(site);
							site.set("name", DateTimeFormat.getFormat("dd, EEEE").format(fromLocal(site.getDate2())));
							//site.set("name", "");
						}
						callback.onSuccess(sites);
					}
				});
			}
		}
		
		private Date fromLocal(LocalDate localDate) {
			return new Date(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
		}

		private void getDataForGeo(Object parent, AsyncCallback<List<ModelData>> callback) {
			if (parent instanceof AdminEntityDTO) {
				GetAdminEntities getAdminEntities = new GetAdminEntities();
				getAdminEntities.setFilter(filter);
				service.execute(getAdminEntities, null, new AsyncCallback<AdminEntityResult>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO: handle failure
					}

					@Override
					public void onSuccess(AdminEntityResult result) {
						List<AdminLevelViewModel> adminlevels = Lists.newArrayList();
						for (AdminEntityDTO adminEntity : result.getData()) {
							adminlevels.add(new AdminLevelViewModel()
								.setEntityId(adminEntity.getId())
								.setName(adminEntity.getName())
								.setLevelId(adminEntity.getLevelId()));
						}
					}
				});
			}
		}

		private List<ModelData> createMonths(int year) {
			List<ModelData> months = Lists.newArrayList();
			for (int i=0; i<12; i++) {
				months.add(new MonthViewModel()
					.setName(DateTimeFormat.getFormat("MMMM yyyy").format(new Date(year -1900, i, 1)))
					.setYear(year)
					.setMonth(i));
			}
			return months;
		}

		private List<ModelData> createYears() {
			List<ModelData> years= Lists.newArrayList();
			years.add(new YearViewModel().setName("2008").setYear(2008));
			years.add(new YearViewModel().setName("2009").setYear(2009));
			years.add(new YearViewModel().setName("2010").setYear(2010));
			years.add(new YearViewModel().setName("2011").setYear(2011));
			return years;
		}
		
		public SiteTreeGridPageState getPlace() {
			return place;
		}
		public void setPlace(SiteTreeGridPageState place) {
			this.place = place;
		}

		public void setFilter(Filter filter) {
			this.filter=filter;
		}
	}