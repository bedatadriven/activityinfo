/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.report.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.util.IndicatorNumberFormat;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotReportElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.HeaderGroupConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


public class PivotGridPanel extends ContentPanel implements ReportView<PivotReportElement> {

	private static final int ROW_INDENT = 15;
	
    private PivotReportElement element;
    private Grid<PivotTableRow> grid;
    private ListStore<PivotTableRow> store;
    private Map<PivotTableData.Axis, String> propertyMap;
    private Map<Integer, PivotTableData.Axis> columnMap;

	private ColumnModel columnModel;

    public PivotGridPanel() {
        setLayout(new FitLayout());
    }

    public class PivotTableRow extends BaseTreeModel {

        private PivotTableData.Axis rowAxis;
        private int depth;

        public PivotTableRow(PivotTableData.Axis axis, int depth) {
            this.rowAxis = axis;
            this.depth = depth;
            set("header", axis.getLabel());

            for(Map.Entry<PivotTableData.Axis, PivotTableData.Cell> entry : axis.getCells().entrySet()) {
                set(propertyMap.get(entry.getKey()), entry.getValue().getValue());
            }
        }

        public PivotTableData.Axis getRowAxis() {
            return rowAxis;
        }

		public int getDepth() {
			return depth;
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
        columnModel = createColumnModel(data);
        
        store = new ListStore<PivotTableRow>();

        addRows(data.getRootRow(), 0);

        grid = new Grid<PivotTableRow>(store, columnModel);
        grid.setAutoExpandColumn("header");
        grid.setAutoExpandMin(150);
        grid.setSelectionModel(new CellSelectionModel<PivotGridPanel.PivotTableRow>());
        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<PivotTableRow>>() {
            @Override
			public void handleEvent(GridEvent<PivotTableRow> ge) {
                if(ge.getColIndex() != 0) {
//                    eventBus.fireEvent(new PivotCellEvent(AppEvents.DRILL_DOWN,
//                            element,
//                            ge.getModel().getRowAxis(),
//                            columnMap.get(ge.getColIndex())));
                }
            }
        });

        add(grid);

        layout();

    }

	private void addRows(PivotTableData.Axis parent, int depth) {
		for(PivotTableData.Axis axis : parent.getChildren()) {
            store.add(new PivotTableRow(axis, depth));
            addRows(axis, depth+1);
        }
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

    private static class RowHeaderRenderer implements GridCellRenderer<PivotTableRow> {


		@Override
		public Object render(PivotTableRow model, String property,
				ColumnData config, int rowIndex, int colIndex,
				ListStore<PivotTableRow> store, Grid<PivotTableRow> grid) {
			String indent = (model.getDepth() * ROW_INDENT) + "px";
			return "<span style=\"margin-left:" + indent + "\">" + 
					Format.htmlEncode((String)model.get("header")) + "</span>";
		}
    	
    }
    
    protected ColumnModel createColumnModel(PivotTableData data) {

        List<ColumnConfig> config = new ArrayList<ColumnConfig>();

        ColumnConfig rowHeader = new ColumnConfig("header", "", 150);
        rowHeader.setRenderer(new RowHeaderRenderer());
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
            
            column.setNumberFormat(IndicatorNumberFormat.INSTANCE);
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

	@Override
	public Component asComponent() {
		return this;
	}
}
