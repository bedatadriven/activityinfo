package org.sigmah.client.page.search;

import java.util.List;
import java.util.Map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.search.SearchFilterView.DimensionAddedEvent;
import org.sigmah.client.page.search.SearchFilterView.DimensionAddedEventHandler;
import org.sigmah.client.page.search.SearchPresenter.RecentSiteModel;
import org.sigmah.shared.command.handler.search.QueryChecker;
import org.sigmah.shared.command.handler.search.QueryChecker.QueryFail;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.command.result.SitePointList;
import org.sigmah.shared.dto.SearchHitDTO;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.content.PivotTableData.Axis;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.TextBox;

public class SearchResultsPage extends ContentPanel implements SearchView {
	private VerticalPanel panelSearchResults;
	private LayoutContainer containerFilterAndResult;
	private PivotContent pivotContent;
	private SearchFilterView filterView;
	private RecentSitesView recentSitesView;

	private TextBox textboxSearch;
	private AsyncMonitor loadingMonitor;
	private SimpleEventBus eventBus = new SimpleEventBus();
	private String searchQuery;

	public SearchResultsPage() {
		initializeComponent();

		createRecentSitesView();
		
		createCompleteResultPanel();
		createFilterView();
		createSearchResultsPanel();

		createSearchBox();
	}

	private void initializeComponent() {
		setHeading("Search results");
		setLayout(new BorderLayout());

		SearchResources.INSTANCE.searchStyles().ensureInjected();
		loadingMonitor = new MaskingAsyncMonitor(this, I18N.CONSTANTS.busySearching());
	}

	private void createRecentSitesView() {
		recentSitesView = new RecentSitesView();

		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.EAST);
		bld.setSplit(true);
		bld.setCollapsible(true);
		bld.setMinSize(300);
		bld.setSize(0.4F);

		add(recentSitesView, bld);
	}

	private void createSearchResultsPanel() {
		panelSearchResults = new VerticalPanel();
		panelSearchResults.setScrollMode(Scroll.AUTO);
		containerFilterAndResult.add(panelSearchResults);
	}

	private void createCompleteResultPanel() {
		containerFilterAndResult = new LayoutContainer();
		RowLayout layout = new RowLayout();
		layout.setOrientation(Orientation.VERTICAL);
		containerFilterAndResult.setLayout(layout);
		containerFilterAndResult.setScrollMode(Scroll.AUTOY);
		
		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.CENTER);
		bld.setSplit(true);
		bld.setSize(0.5F);

		add(containerFilterAndResult, bld);
	}

	private void createFilterView() {
		filterView = new SearchFilterView();
		containerFilterAndResult.add(filterView);
		filterView.addDimensionAddedHandler(new DimensionAddedEventHandler() {
			@Override
			public void onDimensionAdded(DimensionAddedEvent event) {
				addEntityToSearchBox(event.getAddedEntity());
			}
		});
	}
	
	private void showError(String error) {
		containerFilterAndResult.el().mask(error);
		recentSitesView.el().mask();
	}

	private void showError(List<QueryFail> fails) {
		StringBuilder sb = new StringBuilder();
		for (QueryFail fail : fails) {
			sb.append(fail.fail());
			sb.append("\r\n");
		}
		showError(sb.toString());
	}

	private void clearErrorsIfShowing() {
		containerFilterAndResult.el().unmask();
		recentSitesView.el().unmask();
	}

	private void addEntityToSearchBox(SearchResultEntity addedEntity) {
		textboxSearch.setText(textboxSearch.getText() + " " + createEntityText(addedEntity));
	}

	private String createEntityText(SearchResultEntity addedEntity) {
		return new StringBuilder()
			.append(I18N.FROM_ENTITIES.localizedStringFrom(addedEntity.getDimension()))
			.append(":")
			.append(addedEntity.getName())
			.toString();
	}

	private void createSearchBox() {
		textboxSearch = new TextBox();
		textboxSearch.setSize("2em", "2em");
		textboxSearch.setStylePrimaryName("searchBox");
		textboxSearch.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					QueryChecker checker = new QueryChecker();
					
					if (checker.checkQuery(textboxSearch.getText())) {
						eventBus.fireEvent(new SearchEvent(textboxSearch.getText()));
					} else {
						showError(checker.getFails());
					}
				}
			}
		});

		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.NORTH);
		bld.setSize(40);
		bld.setMargins(new Margins(16));

		add(textboxSearch, bld);
	}

	@Override
	public void setParent(SearchResult parent) {
//		searchResult = parent;
	}

	@Override
	public void setItems(List<SearchHitDTO> items) {
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public AsyncMonitor getLoadingMonitor() {
		return loadingMonitor;
	}

	@Override
	public void setValue(SearchHitDTO value) {
	}

	@Override
	public SearchHitDTO getValue() {
		return null;
	}

	@Override
	public com.google.gwt.event.shared.HandlerRegistration addSearchHandler(org.sigmah.client.page.search.SearchView.SearchHandler handler) {
		return eventBus.addHandler(SearchEvent.TYPE, handler);
	}

	@Override
	public void setSearchResults(PivotContent pivotTabelData) {
		this.pivotContent = pivotTabelData;

		showSearchResults();
	}

	private void showSearchResults() {
		panelSearchResults.removeAll();
		clearErrorsIfShowing();

		int activities=0;
		int databases=0;
		int indicators=0;
		
		LabelField labelResults = new LabelField();
		panelSearchResults.add(labelResults);
		
		if (pivotContent != null) {
			VerticalPanel panelSpacer = new VerticalPanel();
			panelSpacer.setHeight(16);
			panelSearchResults.add(panelSpacer);
			panelSearchResults.setStylePrimaryName("searchResults");
	
			for (Axis axis : pivotContent.getData().getRootRow().getChildren()) {
				SearchResultItem itemWidget = new SearchResultItem();
				itemWidget.setDabaseName(axis.getLabel());
				itemWidget.setChilds(axis.getChildList());
	
				panelSearchResults.add(itemWidget);
				databases++;
				activities += itemWidget.getActivityCount();
				indicators += itemWidget.getIndicatorCount();
			}
		}

		labelResults.setText(I18N.MESSAGES.searchResultsFound(
				searchQuery, 
				Integer.toString(databases), 
				Integer.toString(activities), 
				Integer.toString(indicators)));

		layout();
	}

	@Override
	public void setSearchQuery(String query) {
		this.searchQuery = query;

		textboxSearch.setText(query);
	}

	@Override
	public void setFilter(Map<DimensionType, List<SearchResultEntity>> affectedEntities) {
		filterView.setFilter(affectedEntities);
	}

	@Override
	public void setSites(List<RecentSiteModel> sites) {
		recentSitesView.setSites(sites);
	}
	
	@Override
	public void setSitePoints(SitePointList sitePoints) {
		recentSitesView.setSitePoins(sitePoints);
	}

}
