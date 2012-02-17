/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.PivotCellEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.charts.ReportView;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotReportElement;

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


public class PivotGridPanel extends ContentPanel implements ReportView<PivotReportElement> {

    private final EventBus eventBus;

    private PivotReportElement element;
    private TreeGrid<PivotTableRow> grid;
    private TreeStore<PivotTableRow> store;
    private ColumnModel columnModel;
    private Map<PivotTableData.Axis, String> propertyMap;
    private Map<Integer, PivotTableData.Axis> columnMap;

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

    @Override
    public void show(final PivotReportElement element) {
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
            @Override
			public void handleEvent(GridEvent<PivotTableRow> ge) {
                if(ge.getColIndex() != 0) {
                    eventBus.fireEvent(new PivotCellEvent(AppEvents.DRILL_DOWN,
                            element,
                            ge.getModel().getRowAxis(),
                            columnMap.get(ge.getColIndex())));
                }
            }
        });

        add(grid);

        layout();

        new DelayedTask(new Listener<BaseEvent>() {
            @Override
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
                label = I18N.CONSTANTS.value();
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
