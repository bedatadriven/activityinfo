package org.sigmah.client.page.report;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.grid.AbstractEditorGridPresenter;
import org.sigmah.client.page.common.grid.GridView;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.google.inject.ImplementedBy;

public class ReportDesignListPresenter extends AbstractEditorGridPresenter<ModelData>{

	@ImplementedBy(ReportDesignListPage.class)
    public interface View extends GridView<ReportDesignListPresenter, ModelData> {
        void init(ReportDesignListPresenter presenter, ListStore<ModelData> store);
    }
	 
	private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;
    
    private ListStore<ModelData> store;

	protected ReportDesignListPresenter(EventBus eventBus, Dispatcher service,
			StateProvider stateMgr, View view) {
		
		super(eventBus, service, stateMgr, view);
		this.eventBus = eventBus;
        this.service = service;
        this.view = view;
        store = new ListStore<ModelData>();
        this.view.init(this, store);

	}

	public void go(ReportDefinitionDTO dto){
		List<ModelData> reportTitles = new ArrayList<ModelData>();
		for(ReportElement element : dto.getReport().getElements()) {
			reportTitles.add(reportTitle(element));
		}
		store.removeAll();
		store.add(reportTitles);
	}
	
	private ModelData reportTitle(ReportElement element){
		BaseModelData title = new BaseModelData();
		title.set("title", element.getTitle());
		
		return title;
	}
	
	@Override
	public void onSelectionChanged(ModelData selectedItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PageId getPageId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getWidget() {
		return view;
	}

	@Override
	public boolean navigate(PageState place) {
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Command<?> createSaveCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Store<ModelData> getStore() {
		return store;
	}

	@Override
	protected String getStateId() {
		 return "reportDesignGrid";
	}

}
