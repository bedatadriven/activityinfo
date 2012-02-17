package org.sigmah.client.page.table;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.report.HasReportElement;
import org.sigmah.client.page.report.ReportEventHelper;
import org.sigmah.client.page.report.editor.ReportElementEditor;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.DimensionFolder;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.dnd.DND;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.common.collect.Lists;

/**
 * User interface for selecting row / column dimensions
 *
 */
public class PivotTrayPanel extends ContentPanel implements HasReportElement<PivotTableReportElement> {
	private final Dispatcher dispatcher;
	private final ReportEventHelper events;

	private ListStore<Dimension> rowDims;
	private ListStore<Dimension> colDims;

	private TreeStore<ModelData> dimensionStore;
	private TreePanel<ModelData> treePanel;
	
	private PivotTableReportElement model;
	
	public PivotTrayPanel(EventBus eventBus, Dispatcher dispatcher) {
		this.events = new ReportEventHelper(eventBus, this);
		this.dispatcher = dispatcher;
		
		setHeading(I18N.CONSTANTS.dimensions());
		setScrollMode(Style.Scroll.NONE);
		setIcon(null);
		
		VBoxLayout layout = new VBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
		setLayout(layout);

		VBoxLayoutData labelLayout = new VBoxLayoutData();
		VBoxLayoutData listLayout = new VBoxLayoutData();
		listLayout.setFlex(1.0);

		createDimensionsTree();
		add(treePanel, listLayout);

		rowDims = createStore();
		rowDims.addStoreListener(new DimensionStoreListener() {
			
			@Override
			public void onChanged() {
				model.setRowDimensions(Lists.newArrayList(rowDims.getModels()));
				events.fireChange();
			}
		});
		add(new Text(I18N.CONSTANTS.rows()), labelLayout);
		add(createList(rowDims), listLayout);

		colDims = createStore();
		colDims.addStoreListener(new DimensionStoreListener() {
			
			@Override
			public void onChanged() {
				model.setColumnDimensions(Lists.newArrayList(colDims.getModels()));
				events.fireChange();
			}
		});
		add(new Text(I18N.CONSTANTS.columns()), labelLayout);
		add(createList(colDims), listLayout);
	}
	

	private void createDimensionsTree() {
		TreeLoader<ModelData> loader = new BaseTreeLoader<ModelData>(
				new DimensionTreeProxy(dispatcher)) {
			@Override
			public boolean hasChildren(ModelData parent) {
				if (parent instanceof AttributeGroupDTO) {
					return !((AttributeGroupDTO) parent).isEmpty();
				}
				return parent instanceof DimensionFolder
						|| parent instanceof AttributeGroupDTO;
			}
		};

		// tree store
		dimensionStore = new TreeStore<ModelData>(loader);
		dimensionStore.setKeyProvider(new ModelKeyProvider<ModelData>() {
			@Override
			public String getKey(ModelData model) {
				return "node_" + model.get("id");
			}
		});

		treePanel = new TreePanel<ModelData>(dimensionStore);
		treePanel.setBorders(true);
		treePanel.setCheckable(true);
		treePanel.setCheckNodes(TreePanel.CheckNodes.LEAF);
		treePanel.setCheckStyle(TreePanel.CheckCascade.NONE);
		treePanel.getStyle().setNodeCloseIcon(null);
		treePanel.getStyle().setNodeOpenIcon(null);

		treePanel.setStateful(true);
		treePanel.setLabelProvider(new ModelStringProvider<ModelData>() {
			@Override
			public String getStringValue(ModelData model, String property) {
				return trim((String) model.get("caption"));
			}
		});

		/* enable drag and drop for dev */
		// TreePanelDragSource source = new TreePanelDragSource(treePanel);
		// source.setTreeSource(DND.TreeSource.LEAF);
		/* end enable drag and drop for dev */

		treePanel.setId("statefullavaildims");
		treePanel.collapseAll();

		final ArrayList<ModelData> dimensions = new ArrayList<ModelData>();
		dimensions.add(new Dimension(I18N.CONSTANTS.database(),
				DimensionType.Database));
		dimensions.add(new Dimension(I18N.CONSTANTS.activity(),
				DimensionType.Activity));
		dimensions.add(new Dimension(I18N.CONSTANTS.indicators(),
				DimensionType.Indicator));
		dimensions.add(new Dimension(I18N.CONSTANTS.partner(),
				DimensionType.Partner));
		dimensions.add(new Dimension(I18N.CONSTANTS.project(),
				DimensionType.Project));
		dimensions.add(new Dimension(I18N.CONSTANTS.location(),
				DimensionType.Location));

		dimensions.add(new DimensionFolder(I18N.CONSTANTS.geography(),
				DimensionType.AdminLevel, 0, 0));
		dimensions.add(new DimensionFolder(I18N.CONSTANTS.time(),
				DimensionType.Date, 0, 0));
		dimensions.add(new DimensionFolder(I18N.CONSTANTS.attributes(),
				DimensionType.AttributeGroup, 0, 0));

		dimensionStore.add(dimensions, false);
		
		treePanel.addCheckListener(new CheckChangedListener<ModelData>() {
			@Override
			public void checkChanged(CheckChangedEvent<ModelData> event) {
				onDimensionChecked(event);
			}
		});
	}
	
	private void setDimensionChecked(ModelData d, boolean checked) {
		treePanel.setChecked(d, checked);
	}

	private ListStore<Dimension> createStore() {
		ListStore<Dimension> store = new ListStore<Dimension>();
		return store;
	}

	private ListView createList(ListStore<Dimension> store) {
		ListView<Dimension> list = new ListView<Dimension>(store);
		list.setDisplayProperty("caption");
		ListViewDragSource source = new ListViewDragSource(list);
		ListViewDropTarget target = new ListViewDropTarget(list);
		target.setFeedback(DND.Feedback.INSERT);
		target.setAllowSelfAsSource(true);
		return list;
	}
	

	private void onDimensionChecked(CheckChangedEvent<ModelData> event) {
		List<ModelData> checked = event.getCheckedSelection();
		for (ModelData r : rowDims.getModels()) {
			if (checked.contains(r)) {
				checked.remove(r);
			} else {
				rowDims.remove((Dimension) r);
			}
		}

		for (ModelData c : colDims.getModels()) {
			if (checked.contains(c)) {
				checked.remove(c);
			} else {
				colDims.remove((Dimension) c);
			}
		}

		for (ModelData newItem : checked) {
			if (rowDims.getModels().size() > colDims.getModels().size()) {
				colDims.add((Dimension) newItem);
			} else {
				rowDims.add((Dimension) newItem);
			}
		}
	}
	
	private String trim(String s) {
		if (s == null || "".equals(s)) {
			return "NO_NAME";
		}
		s = s.trim();
		if (s.length() > 20) {
			return s.substring(0, 19) + "...";
		} else {
			return s;
		}
	}


	@Override
	public void bind(PivotTableReportElement model) {
		this.model = model;
		applyInternalState();
	}
	
	private void applyInternalState() {
		rowDims.setFiresEvents(false);
		colDims.setFiresEvents(false);
		try {
			rowDims.removeAll();
			colDims.removeAll();
			for(ModelData treeNode : treePanel.getStore().getModels()) {
				if(model.getRowDimensions().contains(treeNode)) {
					rowDims.add((Dimension)treeNode);
					treePanel.setChecked(treeNode, true);
				}
				if(model.getColumnDimensions().contains(treeNode)) {
					colDims.add((Dimension)treeNode);
					treePanel.setChecked(treeNode, true);
				}
			}
		} finally {
			rowDims.setFiresEvents(true);
			colDims.setFiresEvents(true);
		}
	}
	
	@Override
	public PivotTableReportElement getModel() {
		return model;
	}
	
	private static abstract class DimensionStoreListener extends StoreListener<Dimension> {
		
		
		@Override
		public final void storeAdd(StoreEvent<Dimension> se) {
			onChanged();
		}

		@Override
		public final void storeRemove(StoreEvent<Dimension> se) {
			onChanged();
		}

		public abstract void onChanged();
	}
}
