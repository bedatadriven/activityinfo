package org.sigmah.client.report.editor.pivotTable;

import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.report.HasReportElement;
import org.sigmah.client.page.report.ReportChangeHandler;
import org.sigmah.client.page.report.ReportEventHelper;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.AdminDimension;
import org.sigmah.shared.report.model.AttributeGroupDimension;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotTableReportElement;

import com.extjs.gxt.ui.client.dnd.DND;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DimensionSelectionListView implements HasReportElement<PivotTableReportElement> {
	
	public enum Axis {
		ROW, 
		COLUMN
	}
	
	private ReportEventHelper events;
	private Dispatcher dispatcher;
	private Axis axis;
	

	private ListStore<DimensionModel> store;
	private ListView<DimensionModel> list;

	private PivotTableReportElement model;

	
	public DimensionSelectionListView(EventBus eventBus, Dispatcher dispatcher, Axis axis) {
		this.events = new ReportEventHelper(eventBus, this);
		this.events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				onModelChanged();
			}
		});
		this.dispatcher = dispatcher;
		this.axis = axis;
		
		store = new ListStore<DimensionModel>();
		
		list = new ListView<DimensionModel>(store);
		list.setDisplayProperty("name");
		ListViewDragSource source = new ListViewDragSource(list);
		ListViewDropTarget target = new ListViewDropTarget(list);
		target.setFeedback(DND.Feedback.INSERT);
		target.setAllowSelfAsSource(true);
		
		store.addStoreListener(new StoreListener<DimensionModel>() {

			@Override
			public void storeAdd(StoreEvent<DimensionModel> se) {
				updateModelAfterDragDrop();
			}

			@Override
			public void storeRemove(StoreEvent<DimensionModel> se) {
				updateModelAfterDragDrop();
			}
			
		});
	}
	
	private void updateModelAfterDragDrop() {
		List<Dimension> dims = Lists.newArrayList();
		for(DimensionModel model : store.getModels()) {
			dims.add(model.getDimension());
		}
		switch(axis) {
		case ROW:
			model.setRowDimensions(dims);
			break;
		case COLUMN:
			model.setColumnDimensions(dims);
		}
		events.fireChange();
	}

	@Override
	public void bind(PivotTableReportElement model) {
		this.model = model;
		onModelChanged();
	}

	private void onModelChanged() {
		dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				updateStoreAfterModelChanged(result);
			}
		});			
	}

	
	private List<Dimension> getSelection() {
		switch(axis) {
		case ROW:
			return model.getRowDimensions();
		case COLUMN:
			return model.getColumnDimensions();
		}
		throw new IllegalStateException(""+axis);
	}
	
	private void updateStoreAfterModelChanged(SchemaDTO schema) {
		store.setFiresEvents(false);
		store.removeAll();
		for(Dimension dim : getSelection()) {
			DimensionModel model = toModel(dim, schema);
			if(model != null) {
				store.add(model);
			}
		}
		store.setFiresEvents(true);
		list.refresh();
	}
	
	private DimensionModel toModel(Dimension dim, SchemaDTO schema) {
		if(dim instanceof DateDimension) {
			return new DimensionModel(((DateDimension) dim).getUnit());
		} else if(dim instanceof AdminDimension) {
			return new DimensionModel(schema.getAdminLevelById(((AdminDimension) dim).getLevelId()));
		} else if(dim instanceof AttributeGroupDimension) {
			AttributeGroupDTO group = schema.getAttributeGroupById(((AttributeGroupDimension) dim).getAttributeGroupId());
			return group == null ? null : new DimensionModel(group);
		} else {
			switch(dim.getType()) {
			case Database:
				return new DimensionModel(dim, I18N.CONSTANTS.database());
			case Activity:
				return new DimensionModel(dim, I18N.CONSTANTS.activity());
			case Indicator:
				return new DimensionModel(dim, I18N.CONSTANTS.indicator());
			case Partner:
				return new DimensionModel(dim, I18N.CONSTANTS.partner());
			case Project:
				return new DimensionModel(dim, I18N.CONSTANTS.project());
			case Location:
				return new DimensionModel(dim, I18N.CONSTANTS.location());
			}
		}
		return null;
	}

	@Override
	public PivotTableReportElement getModel() {
		return model;
	}

	public Component asComponent() {
		return list;
	}
	
}
