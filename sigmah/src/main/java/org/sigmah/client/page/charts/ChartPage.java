/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.ExportCallback;
import org.sigmah.client.page.common.toolbar.ExportMenuButton;
import org.sigmah.client.page.table.PivotGridPanel;
import org.sigmah.client.report.DimensionStoreFactory;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotChartElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartPage extends LayoutContainer implements Charter.View {

    private final EventBus eventBus;
    private final Dispatcher service;

    private Charter presenter;

    private ActionToolBar toolBar;
    private List<ToggleButton> chartTypeButtons;

    private ToolBar dimBar;
    private ComboBox<Dimension> categoryCombo;
    private ComboBox<Dimension> legendCombo;

    private ChartPreviewOFC preview;
    private ContentPanel center;
    private PivotGridPanel gridPanel;

    private IndicatorTreePanel indicatorPanel;
    private AdminFilterPanel adminFilterPanel;
    private DateRangePanel dateFilterPanel;

    @Inject
    public ChartPage(EventBus eventBus, Dispatcher service) {
        this.eventBus = eventBus;
        this.service = service;

        setLayout(new BorderLayout());

        createWest();
        createCenter();
        createToolBar();
        createChartPane();
        createDimBar();
        createGridPane();
    }

    private void createToolBar() {
        toolBar = new ActionToolBar(presenter);

        toolBar.addRefreshButton();
        toolBar.add(new SeparatorToolItem());

        chartTypeButtons = new ArrayList<ToggleButton>();

        toolBar.add(new LabelToolItem(I18N.CONSTANTS.chartType()));

        ToggleButton button = new ToggleButton("", IconImageBundle.ICONS.barChart());
        button.setToggleGroup("chartType");
        button.setData("chartType", PivotChartElement.Type.Bar);
        chartTypeButtons.add(button);
        toolBar.add(button);

        button = new ToggleButton("", IconImageBundle.ICONS.curveChart());
        button.setToggleGroup("chartType");
        chartTypeButtons.add(button);
        toolBar.add(button);

        button = new ToggleButton("", IconImageBundle.ICONS.pieChart());
        button.setToggleGroup("chartType");
        chartTypeButtons.add(button);
        toolBar.add(button);

        toolBar.add(new SeparatorToolItem());

        toolBar.add(new ExportMenuButton(RenderElement.Format.PowerPoint, new ExportCallback() {
            public void export(RenderElement.Format format) {
                presenter.export(format);
            }
        }));

        center.setTopComponent(toolBar);

    }

    private void createWest() {

        ContentPanel westPanel = new ContentPanel(new AccordionLayout());
        westPanel.setHeading(I18N.CONSTANTS.filter());

        indicatorPanel = new IndicatorTreePanel(service, true);
        indicatorPanel.setHeaderVisible(true);
        westPanel.add(indicatorPanel);

        adminFilterPanel = new AdminFilterPanel(service);
        westPanel.add(adminFilterPanel);

        dateFilterPanel = new DateRangePanel();
        westPanel.add(dateFilterPanel);

        BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST, 0.30f);
        west.setCollapsible(true);
        west.setSplit(true);
        west.setMargins(new Margins(0, 5, 0, 0));

        add(westPanel, west);
    }

    private void createCenter() {

        center = new ContentPanel(new BorderLayout());
        center.setHeaderVisible(false);

        add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    private void createChartPane() {

        preview = new ChartPreviewOFC();
        center.add(preview, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }


    private void createGridPane() {
        BorderLayoutData south = new BorderLayoutData(Style.LayoutRegion.SOUTH, 0.30f);
        south.setCollapsible(true);
        south.setSplit(true);
        south.setMargins(new Margins(5, 0, 0, 0));

        gridPanel = new PivotGridPanel(eventBus);
        gridPanel.setHeading("Table");

        center.add(gridPanel, south);
    }

    private void createDimBar() {
        dimBar = new ToolBar();
        ListStore<Dimension> store = DimensionStoreFactory.create(service);

        dimBar.add(new LabelToolItem(I18N.CONSTANTS.categories()));
        categoryCombo = new ComboBox<Dimension>();
        categoryCombo.setForceSelection(true);
        categoryCombo.setEditable(false);
        categoryCombo.setStore(store);
        categoryCombo.setDisplayField("caption");
        dimBar.add(categoryCombo);

        dimBar.add(new FillToolItem());

        dimBar.add(new LabelToolItem(I18N.CONSTANTS.legend()));
        legendCombo = new ComboBox<Dimension>();
        legendCombo.setForceSelection(true);
        legendCombo.setEditable(false);
        legendCombo.setStore(store);
        legendCombo.setDisplayField("caption");
        dimBar.add(legendCombo);

        preview.setBottomComponent(dimBar);
    }

    @Override
    public void bindPresenter(Charter presenter) {
        this.presenter = presenter;
        this.toolBar.setListener(presenter);
    }

    private PivotChartElement.Type getChartType() {
        for (ToggleButton button : chartTypeButtons) {
            if (button.isPressed()) {
                return button.getData("chartData");
            }
        }
        return null;
    }


    @Override
    public PivotChartElement getChartElement() {
        PivotChartElement element = new PivotChartElement();
        element.setType(getChartType());

        if (categoryCombo.getValue() instanceof DateDimension) {
            DateDimension dim = (DateDimension) categoryCombo.getValue();
            if (dim.getUnit() != DateUnit.YEAR) {
                element.addCategoryDimension(new DateDimension(DateUnit.YEAR));
            }
        }

        element.addCategoryDimension(categoryCombo.getValue());

        for (Integer indicatorId : indicatorPanel.getSelectedIds()) {
            element.addIndicator(indicatorId);
        }

        return element;
    }

    @Override
    public AsyncMonitor getMonitor() {
        return new MaskingAsyncMonitor(preview, I18N.CONSTANTS.loading());
    }

    public void setData(PivotChartElement element) {
        preview.setContent(element.getContent());
        gridPanel.setData(element);
    }


}
