/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.grid.AbstractEditorGridView;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.SiteDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteGrid extends AbstractEditorGridView<SiteDTO, SiteEditor>
        implements SiteEditor.View {


    private ActivityDTO activity;
    protected EditorGrid<SiteDTO> grid;
    private boolean enableDragSource;

//    private Map<String, ComboBox<AdminEntityDTO>> adminComboBoxes =
//            new HashMap<String, ComboBox<AdminEntityDTO>>();


    public SiteGrid(boolean enableDragSource) {
        this();
        this.enableDragSource = enableDragSource;
    }

    public SiteGrid() {
        this.setLayout(new BorderLayout());
    }


    @Override
    public void init(SiteEditor presenter, ActivityDTO activity, ListStore<SiteDTO> store) {

        this.activity = activity;
        setHeading(I18N.MESSAGES.activityTitle(activity.getDatabase().getName(), activity.getName()));

        super.init(presenter, store);

    }

    @Override
    public AsyncMonitor getLoadingMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
    }


    public Grid<SiteDTO> createGridAndAddToContainer(Store store) {

        grid = new EditorGrid<SiteDTO>((ListStore)store, createColumnModel(activity));
        grid.setEnableColumnResize(true);
        grid.setLoadMask(true);
        grid.setStateful(true);
        grid.setStateId("SiteGrid" + activity.getId());
//        grid.addListener(Events.BeforeEdit, new Listener<GridEvent>() {
//            public void handleEvent(GridEvent be) {
//                if(be.getProperty().startsWith(AdminLevelDTO.PROPERTY_PREFIX)) {
//                    prepareAdminEditor(be);
//                }
//            }
//        });

        add(grid, new BorderLayoutData(Style.LayoutRegion.CENTER));

        if(enableDragSource) {
            new SiteGridDragSource(grid);
        }

        return grid;
    }

//    private void prepareAdminEditor(GridEvent be) {
//
//        ComboBox comboBox = adminComboBoxes.get(be.getProperty());
//
//        ListStore<AdminEntityDTO> store =
//                presenter.getAdminEntityStore(be.getProperty(), (SiteDTO)be.getRecord().getModel());
//
//        if(store == null) {
//            be.setCancelled(true);
//        } else {
//            comboBox.setStore(store);
//        }
//    }



    protected void initToolBar() {

        toolBar.addSaveSplitButton();
        toolBar.add(new SeparatorToolItem());

                           
        toolBar.add(new LabelToolItem(I18N.CONSTANTS.filter()));

        toolBar.addButton(UIActions.add, I18N.CONSTANTS.newSite(), IconImageBundle.ICONS.add());
        toolBar.addEditButton();
        toolBar.addDeleteButton(I18N.CONSTANTS.deleteSite());

        toolBar.add(new SeparatorToolItem());

        toolBar.addExcelExportButton();
    }

    @Override
    public void setActionEnabled(String actionId, boolean enabled) {
        super.setActionEnabled(actionId, enabled);
    }

    protected ColumnModel createColumnModel(ActivityDTO activity) {

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig mapColumn = new ColumnConfig("x", "", 25);
        mapColumn.setRenderer(new GridCellRenderer<SiteDTO>() {
            @Override
            public Object render(SiteDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                if(model.hasCoords()) {
                    return "<div class='mapped'>&nbsp;&nbsp;</div>";
                } else {
                    return "<div class='unmapped'>&nbsp;&nbsp;</div>";
                }
            }
        });
        columns.add(mapColumn);


        /*
         * Date Column
         */

        if(activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {

            DateField dateField = new DateField();
            dateField.getPropertyEditor().setFormat(DateTimeFormat.getFormat("MM/dd/y"));

            ColumnConfig dateColumn = new ColumnConfig("date2", I18N.CONSTANTS.date(), 100);
            dateColumn.setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MMM-dd"));
            dateColumn.setEditor(new CellEditor(dateField));

            columns.add(dateColumn);
        }

        /*
         * Partner
         * Should we allow edit from here? Not comfortable with people
         * changing the partner really at all after the activity has been created
         *
         */

        if(activity.getDatabase().isViewAllAllowed()) {
            columns.add(new ColumnConfig("partner", I18N.CONSTANTS.partner(), 100));
        }

        if(activity.getLocationType().getBoundAdminLevelId() == null) {

            /*
             * Location (Name)
             */

            TextField<String> locationField = new TextField<String>();
            locationField.setAllowBlank(false);

            ColumnConfig locationColumn = new ColumnConfig("locationName", I18N.CONSTANTS.location(), 100);
            locationColumn.setEditor(new CellEditor(locationField));
            columns.add(locationColumn);

            /*
             * Axe
             */

            TextField<String> locationAxeField = new TextField<String>();

            ColumnConfig axeColumn = new ColumnConfig("locationAxe", I18N.CONSTANTS.axe(), 75);
            axeColumn.setEditor(new CellEditor(locationAxeField));
            columns.add(axeColumn);
        }

        addIndicatorColumns(activity, columns);


        // add the admin columns (province, territoire, etc)

        List<AdminLevelDTO> levels ;

        if( activity.getLocationType().isAdminLevel()) {

            levels = activity.getDatabase().getCountry().getAdminLevelAncestors(activity.getLocationType().getBoundAdminLevelId());

        } else {

            levels = activity.getDatabase().getCountry().getAdminLevels();
        }

        for (AdminLevelDTO level : levels) {


//            ComboBox<AdminEntityDTO> combo = new RemoteComboBox<AdminEntityDTO>();
//            combo.setTypeAheadDelay(50);
//            combo.setForceSelection(false);
//            combo.setEditable(false);
//            combo.setValueField("id");
//            combo.setDisplayField("name");
//            combo.setTriggerAction(ComboBox.TriggerAction.ALL);

            ColumnConfig adminColumn = new ColumnConfig(level.getPropertyName(), level.getName(), 75);
//            adminColumn.setEditor(new CellEditor(combo));

//            adminComboBoxes.put(level.getPropertyName(), combo);

            columns.add(adminColumn);
        }


        return new ColumnModel(columns);
    }

    protected void addIndicatorColumns(ActivityDTO activity, List<ColumnConfig> columns) {
        /*
        * Add columns for all indicators that have a queries heading
        */

        for (IndicatorDTO indicator : activity.getIndicators()) {
            if(indicator.getListHeader() != null && !indicator.getListHeader().isEmpty()) {

                columns.add(createIndicatorColumn(indicator, indicator.getListHeader()));
            }
        }
    }

    protected ColumnConfig createIndicatorColumn(IndicatorDTO indicator, String header) {
        final NumberFormat format = NumberFormat.getFormat("0");

        NumberField indicatorField = new NumberField();
        indicatorField.getPropertyEditor().setFormat(format);

        ColumnConfig indicatorColumn = new ColumnConfig(indicator.getPropertyName(),
                header, 50);

        indicatorColumn.setNumberFormat(format);
        indicatorColumn.setEditor(new CellEditor(indicatorField));
        indicatorColumn.setAlignment(Style.HorizontalAlignment.RIGHT);

        // For SUM indicators, don't show ZEROs in the Grid
        // (it looks better if we don't)
        if(indicator.getAggregation() == IndicatorDTO.AGGREGATE_SUM) {
            indicatorColumn.setRenderer(new GridCellRenderer() {
                @Override
                public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                    Double value = model.get(property);
                    if(value != null && value != 0) {
                        return format.format(value);
                    } else {
                        return "";
                    }
                }
            });
        }

        return indicatorColumn;
    }

    public void setSelection(int siteId) {

        for(int r=0; r!=grid.getStore().getCount(); ++r) {
            if(grid.getStore().getAt(r).getId() == siteId) {
                grid.getView().ensureVisible(r, 0, false);
                ((CellSelectionModel) grid.getSelectionModel()).selectCell(r, 0);
            }
        }
    }

	@Override
	public Filter getFilter() {
		return new Filter();
	}
}
