package org.sigmah.client.page.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.grid.AbstractEditorGridPresenter;
import org.sigmah.client.page.common.grid.TreeGridView;
import org.sigmah.client.page.common.nav.Link;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.UpdateTargetValue;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.TargetDTO;
import org.sigmah.shared.dto.TargetValueDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class TargetIndicatorPresenter extends AbstractEditorGridPresenter<ModelData> {

	@ImplementedBy(TargetIndicatorView.class)
	public interface View extends TreeGridView<TargetIndicatorPresenter, ModelData> {
		void init(TargetIndicatorPresenter presenter, UserDatabaseDTO db, TreeStore store);
		void expandAll();
	}

	private final EventBus eventBus;
	private final Dispatcher service;
	private final View view;
	private final UIConstants messages;
	private TargetDTO targetDTO;

	private UserDatabaseDTO db;
	private TreeStore<ModelData> treeStore;

	@Inject
	public TargetIndicatorPresenter(EventBus eventBus, Dispatcher service,
			StateProvider stateMgr, View view, UIConstants messages) {
		super(eventBus, service, stateMgr, view);
		this.eventBus = eventBus;
		this.service = service;
		this.view = view;
		this.messages = messages;
	}

	public void go(UserDatabaseDTO db) {

		this.db = db;

		treeStore = new TreeStore<ModelData>();
		
		initListeners(treeStore, null);

		this.view.init(this, db, treeStore);
		this.view.setActionEnabled(UIActions.DELETE, false);
	}

	public void load(TargetDTO targetDTO) {
		this.targetDTO = targetDTO;
		treeStore.removeAll();

		fillStore();
		view.expandAll();
	}

	
	private void fillStore() {

		Map<String, Link> categories = new HashMap<String, Link>();
		for (ActivityDTO activity : db.getActivities()) {

			if (activity.getCategory() != null) {
				Link actCategoryLink = categories.get(activity.getCategory());

				if (actCategoryLink == null) {
					
					actCategoryLink =createCategoryLink(activity, categories);
					categories.put(activity.getCategory(), actCategoryLink);
					treeStore.add(actCategoryLink, false);
				}

				treeStore.add(actCategoryLink, activity, false);
				addIndicatorLinks(activity, activity);
				
			} else {
				treeStore.add(activity, false);
				addIndicatorLinks(activity, activity);
			}

		}
	}
	
	private void addIndicatorLinks(ActivityDTO activity, ModelData parent){
		Map<String, Link> indicatorCategories = new HashMap<String, Link>();
		

		for (IndicatorDTO indicator : activity.getIndicators()) {
			
			if(indicator.getCategory()!=null){
				Link indCategoryLink = indicatorCategories.get(indicator.getCategory());
				
				if(indCategoryLink  == null){
					indCategoryLink = createIndicatorCategoryLink(indicator, indicatorCategories);							
					indicatorCategories.put(indicator.getCategory(), indCategoryLink);
					treeStore.add(parent, indCategoryLink, false);
				}
		
				TargetValueDTO targetValueDTO = getTargetValueByIndicatorId(indicator.getId());
				if(null != targetValueDTO){
					treeStore.add(indCategoryLink, targetValueDTO, false);	
				}else{
					treeStore.add(indCategoryLink, createTargetValueModel(indicator), false);
				}
				
			}else{
				TargetValueDTO targetValueDTO = getTargetValueByIndicatorId(indicator.getId());
				if(null != targetValueDTO){
					treeStore.add(parent, targetValueDTO, false);
				}else{
					treeStore.add(parent, createTargetValueModel(indicator), false);
				}
			}
		}

	}
	
	private TargetValueDTO createTargetValueModel(IndicatorDTO indicator){
		TargetValueDTO targetValueDTO = new TargetValueDTO();
		targetValueDTO.setTargetId(targetDTO.getId());
		targetValueDTO.setIndicatorId(indicator.getId());
		targetValueDTO.setName(indicator.getName());
		
		return targetValueDTO ;
	}
		
	private TargetValueDTO getTargetValueByIndicatorId(int indicatorId){
		List<TargetValueDTO> values =  targetDTO.getTargetValues();
		
		if(values == null){
			return null;
		}
		
		for(TargetValueDTO dto : values){
			if(dto.getIndicatorId() == indicatorId){
				return dto;
			}
		}
		
		return null;
	}
	
	private Link createIndicatorCategoryLink(IndicatorDTO indicatorNode, Map<String, Link> categories){
		return Link.folderLabelled(indicatorNode.getCategory())
				.usingKey(categoryKey(indicatorNode, categories))
				.withIcon(IconImageBundle.ICONS.folder()).build();
	}
	
	private Link createCategoryLink(ActivityDTO activity,Map<String, Link> categories) {

		return Link.folderLabelled(activity.getCategory())
				.usingKey(categoryKey(activity, categories))
				.withIcon(IconImageBundle.ICONS.folder()).build();
	}

	private String categoryKey(ActivityDTO activity, Map<String, Link> categories) {
		return "category" + activity.getDatabase().getId()	+ activity.getCategory() + categories.size();
	}

	private String categoryKey(IndicatorDTO indicatorNode, Map<String, Link> categories) {
		return "category-indicator" +  indicatorNode.getCategory() + categories.size();
	}


	@Override
	public Store<ModelData> getStore() {
		return treeStore;
	}

	public TreeStore<ModelData> getTreeStore() {
		return treeStore;
	}

	protected ActivityDTO findActivityFolder(ModelData selected) {

		while (!(selected instanceof ActivityDTO)) {
			selected = treeStore.getParent(selected);
		}

		return (ActivityDTO) selected;
	}
	
	public void updateTargetValue(){
			onSave();	
	}

	public void rejectChanges(){
		treeStore.rejectChanges();
	}
	
	@Override
	protected void onDeleteConfirmed(final ModelData model) {
		service.execute(new Delete((EntityDTO) model),
				view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(VoidResult result) {
						treeStore.remove(model);
						eventBus.fireEvent(AppEvents.SCHEMA_CHANGED);
					}
				});
	}

	@Override
	protected String getStateId() {
		return "target" + db.getId();
	}

	@Override
	protected Command createSaveCommand() {
		BatchCommand batch = new BatchCommand();

		for (ModelData model : treeStore.getRootItems()) {
			prepareBatch(batch, model);
		}
		return batch;
	}

	protected void prepareBatch(BatchCommand batch, ModelData model) {
		if (model instanceof EntityDTO) {
			Record record = treeStore.getRecord(model);
			if (record.isDirty()) {
				UpdateTargetValue cmd = new UpdateTargetValue((Integer)model.get("targetId"), (Integer)model.get("indicatorId"), this.getChangedProperties(record));
				
				batch.add(cmd);
			}
		}

		for (ModelData child : treeStore.getChildren(model)) {
			prepareBatch(batch, child);
		}
	}

	public void onSelectionChanged(ModelData selectedItem) {
		view.setActionEnabled(UIActions.DELETE, this.db.isDesignAllowed()
				&& selectedItem instanceof EntityDTO);
	}
	
	public Object getWidget() {
		return view;
	}

	@Override
	protected void onSaved() {
		treeStore.commitChanges();
	}

	@Override
	public PageId getPageId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}
}
