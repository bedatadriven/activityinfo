package org.sigmah.client.page.entry;

import java.util.Date;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SiteTreeLoader  extends BaseTreeLoader<SiteDTO> {
	
	public SiteTreeLoader(Dispatcher service) {
		super(new Proxy(service));
	}

	@Override
	public boolean hasChildren(SiteDTO parent) {
		return parent instanceof YearViewModel || parent instanceof MonthViewModel;
	}
	
	public static class Proxy implements DataProxy<List<ModelData>> {
		private Dispatcher service;
		
		public Proxy(Dispatcher service) {
			super();
			this.service = service;
		}
		@Override
		public void load(DataReader<List<ModelData>> reader, Object parent,
				final AsyncCallback<List<ModelData>> callback) {
			
			if (parent == null) {
				List<ModelData> yearsAndProvinces = Lists.newArrayList();
				callback.onSuccess(createYears());
			}
			
			if (parent instanceof YearViewModel) {
				YearViewModel year = (YearViewModel) parent;
				callback.onSuccess(createMonths(year.getYear()));
			}
			
			if (parent instanceof AdminEntityDTO) {
				// Getsites based on adminentity filter
			}
			
			if (parent instanceof MonthViewModel) {
				MonthViewModel month = (MonthViewModel) parent;
				GetSites getSites = new GetSites();
				Filter filter = new Filter();
				filter.setMinDate(new Date(month.getYear(), month.getMonth(), 1));
				filter.setMaxDate(new Date(month.getYear(), month.getMonth(), 28));
				getSites.setFilter(filter);
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
						}
						callback.onSuccess(sites);
					}
				});
			}
			
		}
		private List<ModelData> createMonths(int year) {
			List<ModelData> months = Lists.newArrayList();
			for (int i=1; i<13; i++) {
				months.add(new MonthViewModel()
					.setName(Integer.toString(i))
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
		
	}
}