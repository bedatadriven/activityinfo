package org.sigmah.client.page.orgunit;

import org.sigmah.client.CountriesList;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.page.orgunit.calendar.OrgUnitCalendarPresenter;
import org.sigmah.client.page.orgunit.dashboard.OrgUnitDashboardPresenter;
import org.sigmah.client.page.orgunit.details.OrgUnitDetailsPresenter;
import org.sigmah.client.page.project.DummyPresenter;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.client.ui.ToggleAnchor;
import org.sigmah.shared.command.GetOrgUnit;
import org.sigmah.shared.dto.OrgUnitBannerDTO;
import org.sigmah.shared.dto.OrgUnitDTO;
import org.sigmah.shared.dto.element.DefaultFlexibleElementDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.layout.LayoutConstraintDTO;
import org.sigmah.shared.dto.layout.LayoutDTO;
import org.sigmah.shared.dto.layout.LayoutGroupDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class OrgUnitPresenter implements Frame, TabPage {

    public static final PageId PAGE_ID = new PageId("orgunit");

    /**
     * Description of the view managed by this presenter.
     */
    @ImplementedBy(OrgUnitView.class)
    public interface View {

        public ContentPanel getPanelBanner();

        public ContentPanel getTabPanel();

        public void setMainPanel(Widget widget);
    }

    private final View view;
    private final Dispatcher dispatcher;
    private final Authentication authentication;
    private final CountriesList countriesList;
    private Page activePage;
    private OrgUnitState currentState;
    private ToggleAnchor currentTab;
    private OrgUnitDTO currentOrgUnitDTO;
    private final SubPresenter[] presenters;

    private final static String[] MAIN_TABS = { I18N.CONSTANTS.dashboard(), I18N.CONSTANTS.projectDetails(),
            I18N.CONSTANTS.projectTabCalendar(), I18N.CONSTANTS.projectTabReports() };

    @Inject
    public OrgUnitPresenter(final Dispatcher dispatcher, View view, Authentication authentication,
            final EventBus eventBus, final CountriesList countriesList) {

        this.dispatcher = dispatcher;
        this.view = view;
        this.authentication = authentication;
        this.countriesList = countriesList;

        final DummyPresenter dummyPresenter = new DummyPresenter();

        this.presenters = new SubPresenter[] { new OrgUnitDashboardPresenter(dispatcher, eventBus, this),
                new OrgUnitDetailsPresenter(dispatcher, authentication, this, countriesList),
                new OrgUnitCalendarPresenter(dispatcher, this), dummyPresenter

        };

        for (int i = 0; i < MAIN_TABS.length; i++) {
            final int index = i;

            String tabTitle = MAIN_TABS[i];

            final HBoxLayoutData layoutData = new HBoxLayoutData();
            layoutData.setMargins(new Margins(0, 10, 0, 0));

            final ToggleAnchor anchor = new ToggleAnchor(tabTitle);
            anchor.setAnchorMode(true);

            anchor.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, currentState
                            .deriveTo(index)));
                }
            });

            this.view.getTabPanel().add(anchor, layoutData);
        }
    }

    private void selectTab(int index, boolean force) {
        final ToggleAnchor anchor = (ToggleAnchor) this.view.getTabPanel().getWidget(index);

        if (currentTab != anchor) {
            if (currentTab != null)
                currentTab.toggleAnchorMode();

            anchor.toggleAnchorMode();
            currentTab = anchor;

            OrgUnitPresenter.this.view.setMainPanel(presenters[index].getView());
            presenters[index].viewDidAppear();
        } else if (force) {
            OrgUnitPresenter.this.view.setMainPanel(presenters[index].getView());
            presenters[index].viewDidAppear();
        }
    }

    @Override
    public boolean navigate(final PageState place) {

        final OrgUnitState state = (OrgUnitState) place;
        final int id = state.getOrgUnitId();

        if (currentOrgUnitDTO == null || id != currentOrgUnitDTO.getId()) {
            if (Log.isDebugEnabled()) {
                Log.debug("Loading org unit #" + id + "...");
            }

            dispatcher.execute(new GetOrgUnit(id), null, new AsyncCallback<OrgUnitDTO>() {

                @Override
                public void onFailure(Throwable throwable) {
                    Log.error("Error, org unit #" + id + " not loaded.");
                }

                @Override
                public void onSuccess(OrgUnitDTO orgUnitDTO) {

                    if (Log.isDebugEnabled()) {
                        Log.debug("Org unit loaded : " + orgUnitDTO.getName());
                    }

                    currentState = state;

                    boolean orgUnitChanged = !orgUnitDTO.equals(currentOrgUnitDTO);

                    state.setTabTitle(orgUnitDTO.getName());
                    loadOrgUnitOnView(orgUnitDTO);

                    selectTab(state.getCurrentSection(), orgUnitChanged);
                }
            });
        } else {
            boolean change = false;

            if (!currentState.equals(state)) {
                change = true;
                currentState = state;
            }

            selectTab(state.getCurrentSection(), change);
        }

        return true;
    }

    /**
     * Loads a {@link OrgUnitDTO} object on the view.
     * 
     * @param orgUnitDTO
     *            the {@link OrgUnitDTO} object loaded on the view
     */
    private void loadOrgUnitOnView(OrgUnitDTO orgUnitDTO) {

        currentOrgUnitDTO = orgUnitDTO;
        refreshBanner();
    }

    /**
     * Refreshes the org unit banner for the current org unit.
     */
    public void refreshBanner() {

        // Panel.
        final ContentPanel panel = view.getPanelBanner();
        panel.setHeading(currentOrgUnitDTO.getOrgUnitModel().getTitle() + ' ' + currentOrgUnitDTO.getName() + " ("
                + currentOrgUnitDTO.getFullName() + ") : " + I18N.CONSTANTS.projectInfos());
        panel.removeAll();

        final Grid gridPanel = new Grid(1, 2);
        gridPanel.addStyleName("banner");
        gridPanel.setCellPadding(0);
        gridPanel.setCellSpacing(0);
        gridPanel.setWidth("100%");
        gridPanel.setHeight("100%");

        // Logo.
        final Image logo = OrgUnitImageBundle.ICONS.orgUnitLarge().createImage();
        gridPanel.setWidget(0, 0, logo);
        gridPanel.getCellFormatter().addStyleName(0, 0, "banner-logo");

        // Banner.
        final OrgUnitBannerDTO banner = currentOrgUnitDTO.getOrgUnitModel().getBanner();
        final LayoutDTO layout = banner.getLayout();

        // Executes layout.
        if (banner != null && layout != null && layout.getLayoutGroupsDTO() != null
                && !layout.getLayoutGroupsDTO().isEmpty()) {

            // For visibility constraints, the banner accept a maximum of 2 rows
            // and 4 columns.
            final int rows = layout.getRowsCount() > 2 ? 2 : layout.getRowsCount();
            final int cols = layout.getColumnsCount() > 4 ? 4 : layout.getColumnsCount();

            final Grid gridLayout = new Grid(rows, cols);
            gridLayout.addStyleName("banner-flex");
            gridLayout.setCellPadding(0);
            gridLayout.setCellSpacing(0);
            gridLayout.setWidth("100%");
            gridLayout.setHeight("100%");

            for (int i = 0; i < gridLayout.getColumnCount() - 1; i++) {
                gridLayout.getColumnFormatter().setWidth(i, "325px");
            }

            for (final LayoutGroupDTO groupLayout : layout.getLayoutGroupsDTO()) {

                // Checks group bounds.
                if (groupLayout.getRow() + 1 > rows || groupLayout.getColumn() + 1 > cols) {
                    continue;
                }

                final ContentPanel groupPanel = new ContentPanel();
                groupPanel.setLayout(new FormLayout());
                groupPanel.setTopComponent(null);
                groupPanel.setHeaderVisible(false);

                gridLayout.setWidget(groupLayout.getRow(), groupLayout.getColumn(), groupPanel);

                if (groupLayout.getLayoutConstraintsDTO() != null) {
                    for (final LayoutConstraintDTO constraint : groupLayout.getLayoutConstraintsDTO()) {

                        final FlexibleElementDTO element = constraint.getFlexibleElementDTO();

                        // Only default elements are allowed.
                        if (!(element instanceof DefaultFlexibleElementDTO)) {
                            continue;
                        }

                        // Builds the graphic component
                        final DefaultFlexibleElementDTO defaultElement = (DefaultFlexibleElementDTO) element;
                        defaultElement.setService(dispatcher);
                        defaultElement.setAuthentication(authentication);
                        defaultElement.setCountries(countriesList);
                        defaultElement.setCurrentContainerDTO(currentOrgUnitDTO);

                        final Component component = defaultElement.getElementComponent(null, false);
                        groupPanel.add(component);

                        // Only one element per cell.
                        break;
                    }
                }
            }

            gridPanel.setWidget(0, 1, gridLayout);
        }
        // Default banner.
        else {

            panel.setLayout(new FormLayout());

            final LabelField codeField = new LabelField();
            codeField.setReadOnly(true);
            codeField.setFieldLabel(I18N.CONSTANTS.projectName());
            codeField.setLabelSeparator(":");
            codeField.setValue(currentOrgUnitDTO.getName());

            gridPanel.setWidget(0, 1, codeField);
        }

        panel.add(gridPanel);
        panel.layout();
    }

    public OrgUnitDTO getCurrentOrgUnitDTO() {
        return currentOrgUnitDTO;
    }

    public void setCurrentOrgUnitDTO(OrgUnitDTO currentOrgUnitDTO) {
        this.currentOrgUnitDTO = currentOrgUnitDTO;
    }

    @Override
    public String getTabTitle() {
        return I18N.CONSTANTS.orgunit();
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void setActivePage(Page page) {
        this.activePage = page;
    }

    @Override
    public Page getActivePage() {
        return this.activePage;
    }

    public OrgUnitState getCurrentState() {
        return currentState;
    }

    @Override
    public AsyncMonitor showLoadingPlaceHolder(PageId pageId, PageState loadingPlace) {
        return null;
    }
}
