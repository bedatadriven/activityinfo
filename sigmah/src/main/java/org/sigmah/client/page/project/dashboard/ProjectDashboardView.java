/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.dashboard;

import java.util.Arrays;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.FlexibleElementType;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectDashboardView extends ProjectDashboardPresenter.View {
    private final ToolBar toolBar;

    private TabPanel tabPanelPhases;
    private LayoutContainer panelProjectModel;
    private LayoutContainer panelSelectedPhase;

    private Button buttonSavePhase;
    private Button buttonActivatePhase;
    private Button buttonPhaseGuide;

    private ContentPanel panelReminders;
    private ContentPanel panelWatchedPoints;
    private ContentPanel panelFinancialProjects;
    private ContentPanel panelLocalProjects;
    private Grid<FlexibleElementDTO> gridRequiredElements;

    public ProjectDashboardView() {
        final BorderLayout borderLayout = new BorderLayout();
        // borderLayout.setContainerStyle("x-border-layout-ct panel-background");
        // -- White background
        setLayout(borderLayout);
        setHeight("100%");

        /* Center panel */
        ListStore<FlexibleElementDTO> storeRequiredElements = new ListStore<FlexibleElementDTO>();
        storeRequiredElements.setStoreSorter(new StoreSorter<FlexibleElementDTO>() {
            @Override
            public int compare(Store<FlexibleElementDTO> store, FlexibleElementDTO m1, FlexibleElementDTO m2,
                    String property) {
                if ("type".equals(property)) {
                    return FlexibleElementType.getFlexibleElementTypeName(m1).compareTo(
                            FlexibleElementType.getFlexibleElementTypeName(m2));
                } else {
                    return super.compare(store, m1, m2, property);
                }
            }
        });
        gridRequiredElements = new Grid<FlexibleElementDTO>(storeRequiredElements, getColumModel());
        gridRequiredElements.setAutoExpandColumn("label");
        gridRequiredElements.getView().setForceFit(true);

        // Phases tab panel
        tabPanelPhases = new TabPanel();
        tabPanelPhases.setPlain(true);
        tabPanelPhases.addStyleName("project-tabPhases");
        tabPanelPhases.setHeight("100%");
        tabPanelPhases.setBorders(false);
        tabPanelPhases.setBodyBorder(false);

        // Toolbar
        toolBar = new ToolBar();
        toolBar.setAlignment(HorizontalAlignment.LEFT);
        toolBar.setBorders(false);

        buttonSavePhase = new Button(I18N.CONSTANTS.projectSavePhaseButton());
        buttonActivatePhase = new Button(I18N.CONSTANTS.projectClosePhaseButton(), IconImageBundle.ICONS.activate());
        buttonPhaseGuide = new Button(I18N.CONSTANTS.projectPhaseGuideHeader());

        buttonActivatePhase.setEnabled(false);
        buttonSavePhase.setEnabled(false);
        buttonPhaseGuide.setEnabled(false);

        toolBar.add(buttonActivatePhase);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(buttonSavePhase);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(buttonPhaseGuide);

        // Tab item main panel
        panelProjectModel = new LayoutContainer(new BorderLayout());
        panelProjectModel.setBorders(false);
        panelProjectModel.addStyleName("project-current-phase-panel");

        panelSelectedPhase = new LayoutContainer(new FitLayout());

        final BorderLayoutData wd = new BorderLayoutData(LayoutRegion.WEST, 250);
        wd.setMargins(new Margins(4));

        final ContentPanel cp = new ContentPanel(new FitLayout());
        cp.setBorders(false);
        cp.setBodyBorder(false);
        cp.setHeading(I18N.CONSTANTS.projectRequiredElements());

        cp.add(gridRequiredElements);
        panelProjectModel.add(cp, wd);

        final BorderLayoutData cd = new BorderLayoutData(LayoutRegion.CENTER);
        cd.setMargins(new Margins(4));

        final ContentPanel cp2 = new ContentPanel(new FitLayout());
        cp2.setBorders(false);
        cp2.setBodyBorder(false);
        cp2.setHeading(I18N.CONSTANTS.phaseDetails());
        cp2.setScrollMode(Scroll.AUTO);

        cp2.setTopComponent(toolBar);
        cp2.add(panelSelectedPhase, new FitData(new Margins(4)));

        panelProjectModel.add(cp2, cd);

        /* West panel */
        VerticalPanel westPanel = new VerticalPanel();
        panelReminders = new ContentPanel();
        panelReminders.setHeading(I18N.CONSTANTS.projectRemindersHeader());
        panelReminders.addText("This panel displays the reminders.");
        panelReminders.setBorders(false);
        panelReminders.setCollapsible(true);
        panelReminders.setWidth(250);
        panelReminders.addStyleName("sigmah-panelReminders");

        panelWatchedPoints = new ContentPanel();
        panelWatchedPoints.setHeading(I18N.CONSTANTS.projectWatchedPointsHeader());
        panelWatchedPoints.addText("This panel displays the watched points.");
        panelWatchedPoints.setBorders(false);
        panelWatchedPoints.setCollapsible(true);
        panelWatchedPoints.setWidth(250);

        westPanel.add(panelReminders);
        westPanel.add(panelWatchedPoints);

        /* South panel */
        LayoutContainer southPanel = new LayoutContainer(new RowLayout(Orientation.VERTICAL));
        panelFinancialProjects = new ContentPanel();
        panelFinancialProjects.setBorders(false);
        panelFinancialProjects.setHeading(I18N.CONSTANTS.projectFinancialProjectsHeader());
        panelFinancialProjects.addText("This panel displays the financial projects.");

        panelLocalProjects = new ContentPanel();
        panelLocalProjects.setHeading(I18N.CONSTANTS.projectLocalPartnerProjectsHeader());
        panelLocalProjects.setBorders(false);
        panelLocalProjects.addText("This panel displays the local partner projects.");
        southPanel.add(panelFinancialProjects, new RowData(1, 0.5, new Margins(0, 0, 10, 0)));
        southPanel.add(panelLocalProjects, new RowData(1, 0.5));

        /* BorderLayoutData */
        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 170);
        southData.setMargins(new Margins(5));
        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 250);
        westData.setMargins(new Margins(5));
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(5));

        add(westPanel, westData);
        add(tabPanelPhases, centerData);
        add(southPanel, southData);
    }

    /**
     * Generates the {@link ColumnModel} for the required elements grid.
     * 
     * @return the {@link ColumnModel} for the required elements grid.
     */
    private ColumnModel getColumModel() {

        // Element's label.
        final ColumnConfig labelColumn = new ColumnConfig("label", I18N.CONSTANTS.projectRequiredElementsGridLabel(),
                150);

        // Element's completion.
        final CheckColumnConfig filledInColumn = new CheckColumnConfig("filledIn",
                I18N.CONSTANTS.projectRequiredElementsGridChecked(), 40);
        filledInColumn.setMenuDisabled(false);
        filledInColumn.setSortable(false);
        filledInColumn.setRenderer(new GridCellRenderer<FlexibleElementDTO>() {
            @Override
            public Object render(FlexibleElementDTO model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<FlexibleElementDTO> store, Grid<FlexibleElementDTO> grid) {
                if (model.isFilledIn()) {
                    return IconImageBundle.ICONS.elementCompleted().createImage();
                } else {
                    return IconImageBundle.ICONS.elementUncompleted().createImage();
                }
            }
        });

        // Element's type.
        final ColumnConfig typeColumn = new ColumnConfig("type", I18N.CONSTANTS.projectRequiredElementsElementType(),
                75);
        typeColumn.setRenderer(new GridCellRenderer<FlexibleElementDTO>() {
            @Override
            public Object render(FlexibleElementDTO model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<FlexibleElementDTO> store, Grid<FlexibleElementDTO> grid) {
                return FlexibleElementType.getFlexibleElementTypeName(model);
            }
        });

        return new ColumnModel(Arrays.asList(filledInColumn, labelColumn, typeColumn));
    }

    @Override
    public Button getButtonActivatePhase() {
        return buttonActivatePhase;
    }

    @Override
    public Button getButtonPhaseGuide() {
        return buttonPhaseGuide;
    }

    @Override
    public Button getButtonSavePhase() {
        return buttonSavePhase;
    }

    @Override
    public Grid<FlexibleElementDTO> getGridRequiredElements() {
        return gridRequiredElements;
    }

    @Override
    public ContentPanel getPanelFinancialProjects() {
        return panelFinancialProjects;
    }

    @Override
    public ContentPanel getPanelLocalProjects() {
        return panelLocalProjects;
    }

    @Override
    public LayoutContainer getPanelProjectModel() {
        return panelProjectModel;
    }

    @Override
    public ContentPanel getPanelReminders() {
        return panelReminders;
    }

    @Override
    public LayoutContainer getPanelSelectedPhase() {
        return panelSelectedPhase;
    }

    @Override
    public ContentPanel getPanelWatchedPoints() {
        return panelWatchedPoints;
    }

    @Override
    public TabPanel getTabPanelPhases() {
        return tabPanelPhases;
    }

    @Override
    public TabPanel getTabPanelProject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flushToolbar() {
        toolBar.removeAll();
        toolBar.removeAllListeners();
    }

    @Override
    public void fillToolbar() {
        
        flushToolbar();
        
        toolBar.add(buttonActivatePhase);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(buttonSavePhase);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(buttonPhaseGuide);
    }
}
