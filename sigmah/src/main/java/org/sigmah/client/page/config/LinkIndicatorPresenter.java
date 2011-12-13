package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.grid.AbstractEditorGridPresenter;
import org.sigmah.client.page.common.grid.TreeGridView;
import org.sigmah.client.page.common.nav.Link;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.UpdateIndicatorLink;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.IndicatorLinkDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class LinkIndicatorPresenter extends
		AbstractEditorGridPresenter<ModelData> implements Page {

	public static final PageId PAGE_ID = new PageId("LinkIndicator");

	private final EventBus eventBus;
	private final Dispatcher service;
	private final View view;

	private SchemaDTO schema;
	private UserDatabaseDTO db;
	private TreeStore<ModelData> sourceTreeStore;

	@ImplementedBy(IndicatorLinkView.class)
	public interface View extends
			TreeGridView<LinkIndicatorPresenter, ModelData> {
		public void init(LinkIndicatorPresenter presenter, UserDatabaseDTO db,
				TreeStore sourceStore);

		public void addDatabasesToList(List<ModelData> models);
		public void defaultSelectionForIndicatorTree();
		public void clearAllCheckedDestinations();
	}

	@Inject
	public LinkIndicatorPresenter(EventBus eventBus, Dispatcher service,
			StateProvider stateMgr, View view) {
		super(eventBus, service, stateMgr, view);

		this.eventBus = eventBus;
		this.service = service;
		this.view = view;
	}

	public void go(SchemaDTO schema, DbPageState place) {
		this.schema = schema;
		this.db = schema.getDatabaseById(place.getDatabaseId());

		sourceTreeStore = new TreeStore<ModelData>();

		fillStore();

		initListeners(sourceTreeStore, null);

		this.view.init(this, db, sourceTreeStore);
		this.view.addDatabasesToList(new ArrayList(schema.getDatabases()));
	}

	private void fillStore() {

		Map<String, Link> categories = new HashMap<String, Link>();
		for (ActivityDTO activity : db.getActivities()) {

			if (activity.getCategory() != null) {
				Link actCategoryLink = categories.get(activity.getCategory());

				if (actCategoryLink == null) {

					actCategoryLink = createCategoryLink(activity, categories);
					categories.put(activity.getCategory(), actCategoryLink);
					sourceTreeStore.add(actCategoryLink, false);
				}

				sourceTreeStore.add(actCategoryLink, activity, false);
				addIndicatorLinks(activity, activity);

			} else {
				sourceTreeStore.add(activity, false);
				addIndicatorLinks(activity, activity);
			}

		}
	}

	private void addIndicatorLinks(ActivityDTO activity, ModelData parent) {
		Map<String, Link> indicatorCategories = new HashMap<String, Link>();

		for (IndicatorDTO indicator : activity.getIndicators()) {

			if (indicator.getCategory() != null) {
				Link indCategoryLink = indicatorCategories.get(indicator
						.getCategory());

				if (indCategoryLink == null) {
					indCategoryLink = createIndicatorCategoryLink(indicator,
							indicatorCategories);
					indicatorCategories.put(indicator.getCategory(),
							indCategoryLink);
					sourceTreeStore.add(parent, indCategoryLink, false);
				}
				sourceTreeStore.add(indCategoryLink, indicator, false);
			} else {
				sourceTreeStore.add(parent, indicator, false);
			}
		}

	}

	private Link createIndicatorCategoryLink(IndicatorDTO indicatorNode,
			Map<String, Link> categories) {
		return Link.folderLabelled(indicatorNode.getCategory())
				.usingKey(categoryKey(indicatorNode, categories))
				.withIcon(IconImageBundle.ICONS.folder()).build();
	}

	private Link createCategoryLink(ActivityDTO activity,
			Map<String, Link> categories) {

		return Link.folderLabelled(activity.getCategory())
				.usingKey(categoryKey(activity, categories))
				.withIcon(IconImageBundle.ICONS.folder()).build();
	}

	private String categoryKey(ActivityDTO activity,
			Map<String, Link> categories) {
		return "category" + activity.getDatabase().getId()
				+ activity.getCategory() + categories.size();
	}

	private String categoryKey(IndicatorDTO indicatorNode,
			Map<String, Link> categories) {
		return "category-indicator" + indicatorNode.getCategory()
				+ categories.size();
	}

	public void updateIndicatorDestination(IndicatorDTO indicatorDTO) {
		
	}

	public UserDatabaseDTO loadDestinationDatabase(int databaseId) {
		return schema.getDatabaseById(databaseId);
	}

	public void updateLinkIndicator(final IndicatorDTO sourceIndicator, final IndicatorDTO destinationIndicator,
			final boolean checked) {

		int srcId = sourceIndicator.getId();
		int destId = destinationIndicator.getId();
		
		service.execute(new UpdateIndicatorLink(srcId, destId, !checked), null,
				new AsyncCallback<VoidResult>() {
					@Override
					public void onFailure(Throwable caught) {
						//TODO errors?
					}

					@Override
					public void onSuccess(VoidResult result) {
						if(sourceIndicator.getIndicatorLinks()== null){
							IndicatorLinkDTO linkDTO = new IndicatorLinkDTO();
							linkDTO.setSourceIndicator(sourceIndicator.getId());
							sourceIndicator.setIndicatorLinks(linkDTO);
						}
						
						if(checked){
							sourceIndicator.getIndicatorLinks().getDestinationIndicator().put(destinationIndicator.getId(), destinationIndicator.getName());
						}else{
							sourceIndicator.getIndicatorLinks().getDestinationIndicator().remove(destinationIndicator.getId());
						}
						
						sourceTreeStore.update(sourceIndicator);
						sourceTreeStore.commitChanges();
					}
				});
	}

	@Override
	public void onSelectionChanged(ModelData selectedItem) {
		view.clearAllCheckedDestinations();
		view.defaultSelectionForIndicatorTree();
	}

	@Override
	public PageId getPageId() {
		return PAGE_ID;
	}

	@Override
	public Object getWidget() {
		return view;
	}

	@Override
	public boolean navigate(PageState place) {
		return place instanceof DbPageState
				&& place.getPageId().equals(PAGE_ID)
				&& ((DbPageState) place).getDatabaseId() == db.getId();
	}

	@Override
	protected String getStateId() {
		return "LinkIndicator";
	}

	@Override
	protected Command createSaveCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Store getStore() {
		return sourceTreeStore;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
