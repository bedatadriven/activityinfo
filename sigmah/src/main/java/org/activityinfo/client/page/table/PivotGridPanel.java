package org.activityinfo.client.page.table;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.HeaderGroupConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.inject.Inject;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.Application;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.PivotCellEvent;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * @author Alex Bertram
 */

public class PivotGridPanel extends ContentPanel {

    protected EventBus eventBus;

    protected PivotElement element;
    protected TreeGrid<PivotTableRow> grid;
    protected TreeStore<PivotTableRow> store;
    protected ColumnModel columnModel;
    protected Map<PivotTableData.Axis, String> propertyMap;
    protected Map<Integer, PivotTableData.Axis> columnMap;

    @Inject
    public PivotGridPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setLayout(new FitLayout());

    }


    public class PivotTableRow extends BaseTreeModel {

        private PivotTableData.Axis rowAxis;

        public PivotTableRow(PivotTableData.Axis axis) {
            this.rowAxis = axis;
            set("header", axis.getLabel());

            for(Map.Entry<PivotTableData.Axis, PivotTableData.Cell> entry : axis.getCells().entrySet()) {
                set(propertyMap.get(entry.getKey()), entry.getValue().getValue());
            }

            for(PivotTableData.Axis child : axis.getChildren()) {
                add(new PivotTableRow(child));
            }
        }

        public PivotTableData.Axis getRowAxis() {
            return rowAxis;
        }
    }

    public void setData(final PivotElement element) {
        if(grid != null) {
            removeAll();
        }

        this.element = element;

        PivotTableData data = element.getContent().getData();

        propertyMap = new HashMap<PivotTableData.Axis, String>();
        columnMap = new HashMap<Integer, PivotTableData.Axis>();

        store = new TreeStore<PivotTableRow>();

        columnModel = createColumnModel(data);

        for(PivotTableData.Axis axis : data.getRootRow().getChildren()) {
            store.add(new PivotTableRow(axis), true);
        }

        grid = new TreeGrid<PivotTableRow>(store, createColumnModel(data));
        grid.getStyle().setNodeCloseIcon(null);
        grid.getStyle().setNodeOpenIcon(null);
        grid.setAutoExpandColumn("header");
        grid.setAutoExpandMin(150);
        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<PivotTableRow>>() {
            public void handleEvent(GridEvent<PivotTableRow> ge) {
                if(ge.getColIndex() != 0) {
                    eventBus.fireEvent(new PivotCellEvent(AppEvents.Drilldown,
                            element,
                            ge.getModel().getRowAxis(),
                            columnMap.get(ge.getColIndex())));
                }
            }
        });

        add(grid);

        layout();

        new DelayedTask(new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                for(PivotTableRow row : store.getRootItems()) {
                    grid.setExpanded(row, true, true);
                }

            }
        }).delay(1);

    }

    protected int findIndicatorId(PivotTableData.Axis axis) {
        while(axis != null) {
            if(axis.getDimension().getType() == DimensionType.Indicator) {
                return ((EntityCategory)axis.getCategory()).getId();
            }
            axis = axis.getParent();
        }
        return -1;
    }

    protected ColumnModel createColumnModel(PivotTableData data) {

        List<ColumnConfig> config = new ArrayList<ColumnConfig>();

        ColumnConfig rowHeader = new ColumnConfig("header", "", 150);
        rowHeader.setRenderer(new TreeGridCellRenderer());
        rowHeader.setSortable(false);
        rowHeader.setMenuDisabled(true);
        config.add(rowHeader);

        int colIndex = 1;

        List<PivotTableData.Axis> leaves = data.getRootColumn().getLeaves();
        for(PivotTableData.Axis axis : leaves) {


            String id = "col" + colIndex;

            String label = axis.getLabel();
            if(label == null) {
                label = Application.CONSTANTS.value();
            }
            ColumnConfig column = new ColumnConfig(id, label, 75);
            column.setNumberFormat(NumberFormat.getFormat("#,##0"));
            column.setAlignment(Style.HorizontalAlignment.RIGHT);
            column.setSortable(false);
            column.setMenuDisabled(true);

            propertyMap.put(axis, id);
            columnMap.put(colIndex, axis);


            config.add(column);
            colIndex++;
        }

        ColumnModel columnModel = new ColumnModel(config);

        int depth = data.getRootColumn().getDepth();
        int row = 0;

        for(int d = 1; d<=depth; ++d) {

            List<PivotTableData.Axis> children = data.getRootColumn().getDescendantsAtDepth(d);

            // first add a group identifying the dimension

            Dimension dim = children.get(0).getDimension();
            String name = dim.get("caption") == null ? dim.toString() : (String)dim.get("caption");

            columnModel.addHeaderGroup(row++, 1, new HeaderGroupConfig(name, 1, leaves.size()));

            // now add child columsn

            if(d < depth) {

                int col = 1;
                for(PivotTableData.Axis child : children) {

                    int colSpan = child.getLeaves().size();
                    columnModel.addHeaderGroup(row, col, new HeaderGroupConfig(child.getLabel(), 1, colSpan) );

                    col += colSpan;

                }
                row++;

            }

        }
        return columnModel;
    }

}
