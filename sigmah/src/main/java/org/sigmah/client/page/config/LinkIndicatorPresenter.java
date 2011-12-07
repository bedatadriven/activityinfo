package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.grid.AbstractEditorGridPresenter;
import org.sigmah.client.page.common.grid.TreeGridView;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.GetSchema;
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
	private TreeStore<ModelData> destinationTreeStore;

	@ImplementedBy(IndicatorLinkView.class)
	public interface View extends
			TreeGridView<LinkIndicatorPresenter, ModelData> {
		public void init(LinkIndicatorPresenter presenter, UserDatabaseDTO db,
				TreeStore sourceStore, TreeStore destinationStore);

		public void addDatabasesToList(List<ModelData> models);
	}

	@Inject
	public LinkIndicatorPresenter(EventBus eventBus, Dispatcher service,
			StateProvider stateMgr, View view) {
		super(eventBus, service, stateMgr, view);

		this.eventBus = eventBus;
		this.service = service;
		this.view = view;
	}

	public void go(SchemaDTO schema,  DbPageState place) {
		this.schema = schema;
		this.db = schema.getDatabaseById(place.getDatabaseId());
		
		sourceTreeStore = new TreeStore<ModelData>();
		destinationTreeStore = new TreeStore<ModelData>();

		fillStore();

		initListeners(sourceTreeStore, null);

		this.view.init(this, db, sourceTreeStore, destinationTreeStore);
		this.view.addDatabasesToList(new ArrayList(schema.getDatabases()));
	}

	private void fillStore() {

	}

	public void loadDestinationIndicators(int databaseId) {

	}

	public UserDatabaseDTO loadDestinationDatabase(int databaseId){
		return schema.getDatabaseById(databaseId);
	}
	
	@Override
	public void onSelectionChanged(ModelData selectedItem) {
		// TODO Auto-generated method stub

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
