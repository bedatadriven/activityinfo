package org.sigmah.client.page.entry;

import java.util.List;

import org.sigmah.client.data.proxy.SafeRpcProxy;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.PivotSites;
import org.sigmah.shared.command.PivotSites.PivotResult;
import org.sigmah.shared.command.PivotSites.ValueType;
import org.sigmah.shared.command.result.Bucket;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.content.MonthCategory;
import org.sigmah.shared.report.content.YearCategory;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SiteTimeTreeLoader extends BaseTreeLoader<ModelData> implements SiteTreeLoader {

	private TreeProxy treeProxy;
	
	public SiteTimeTreeLoader(Dispatcher dispatcher) {
		super(new TreeProxy(dispatcher));
		treeProxy = (TreeProxy)proxy;
	}
		
	@Override
	public void setFilter(Filter filter) {
		treeProxy.setFilter(filter);
	}
	

	@Override
	public String getKey(ModelData model) {
		if(model instanceof YearModel) {
			return ((YearModel) model).getKey();
		} else if(model instanceof MonthModel) {
			return ((MonthModel)model).getKey();
		} else if(model instanceof SiteDTO) {
			return "S" + ((SiteDTO) model).getId();
		} else {
			return "X" + model.hashCode();
		}
	}
	
	@Override
	public boolean hasChildren(ModelData parent) {
		return parent instanceof YearModel || parent instanceof MonthModel;
	}
		
	private static class TreeProxy extends SafeRpcProxy<List<ModelData>> {

		private static final DateDimension YEAR_DIMENSION = new DateDimension(DateUnit.YEAR);
		private static final DateDimension MONTH_DIMENSION = new DateDimension(DateUnit.MONTH);
		
		private final Dispatcher dispatcher;
		private Filter filter;
		
		public TreeProxy(Dispatcher dispatcher) {
			super();
			
			this.dispatcher = dispatcher;
		}

		public void setFilter(Filter filter) {
			this.filter = filter;
		}
		
		@Override
		protected void load(Object parentNode,
				AsyncCallback<List<ModelData>> callback) {
		
			if(parentNode == null) {
				loadYears(callback);
				
			} else if(parentNode instanceof YearModel) {
				loadMonths((YearModel) parentNode, callback);
				
			} else if(parentNode instanceof MonthModel) {
				loadSites((MonthModel) parentNode, callback);
			}
		}

		private void loadYears(final AsyncCallback<List<ModelData>> callback) {
			PivotSites pivot = new PivotSites();
			pivot.setDimensions(Sets.<Dimension>newHashSet(YEAR_DIMENSION));
			pivot.setFilter(filter);
			pivot.setValueType(ValueType.TOTAL_SITES);

			dispatcher.execute(pivot, null, new AsyncCallback<PivotResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);	
				}

				@Override
				public void onSuccess(PivotResult result) {
					List<ModelData> years = Lists.newArrayList();
					for(Bucket bucket : result.getBuckets()) {
						years.add(new YearModel( (YearCategory) bucket.getCategory(YEAR_DIMENSION) ));
					}
					callback.onSuccess(years);
				}
			});
		}

		private void loadMonths(YearModel parentNode,
				final AsyncCallback<List<ModelData>> callback) {
			PivotSites pivot = new PivotSites();
			pivot.setDimensions(Sets.<Dimension>newHashSet(MONTH_DIMENSION));
			pivot.setFilter( narrowFilter( parentNode.getDateRange() ) );
			pivot.setValueType(ValueType.TOTAL_SITES);

			dispatcher.execute(pivot, null, new AsyncCallback<PivotResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);	
				}

				@Override
				public void onSuccess(PivotResult result) {
					List<ModelData> months = Lists.newArrayList();
					for(Bucket bucket : result.getBuckets()) {
						months.add( new MonthModel( (MonthCategory) bucket.getCategory(MONTH_DIMENSION) ));
					}
					callback.onSuccess(months);
				}
			});
		}
		
		private void loadSites(MonthModel parentEntity,
				final AsyncCallback<List<ModelData>> callback) {

			
			GetSites siteQuery = new GetSites();
			siteQuery.setFilter(narrowFilter( parentEntity.getDateRange() ));
			siteQuery.setSortInfo(new SortInfo("date2", SortDir.ASC));
			
			dispatcher.execute(siteQuery, null, new AsyncCallback<SiteResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(SiteResult result) {
					callback.onSuccess((List)result.getData());
				}
			});
		}
		
		private Filter narrowFilter(DateRange range) {
			Filter narrowed = new Filter(filter);
			narrowed.setDateRange( DateRange.intersection( filter.getDateRange(), range ) );
			return narrowed;
		}
	}

}
