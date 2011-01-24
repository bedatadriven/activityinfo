package org.sigmah.client.page.orgunit;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.ui.StylableHBoxLayout;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class OrgUnitView extends LayoutContainer implements OrgUnitPresenter.View {

    private final ContentPanel panelBanner;
    private final ContentPanel tabPanel;
    private final ContentPanel bottomPanel;

    private Widget widget;

    private final HTML insufficient;
    private Boolean viewDisplayed = null;

    public OrgUnitView() {

        if (Log.isDebugEnabled()) {
            Log.debug("Initializing the OrgUnitView object.");
        }

        final BorderLayout borderLayout = new BorderLayout();
        borderLayout.setContainerStyle("x-border-layout-ct main-background");
        setLayout(borderLayout);

        // Banner
        panelBanner = new ContentPanel();
        panelBanner.setBorders(false);
        panelBanner.setLayout(new FitLayout());
        panelBanner.addStyleName("project-label-10");

        bottomPanel = new ContentPanel(new BorderLayout());
        bottomPanel.setHeaderVisible(false);
        bottomPanel.setBodyBorder(false);
        bottomPanel.setBorders(false);

        // Tabs pannel
        tabPanel = new ContentPanel(new StylableHBoxLayout("main-background project-top-bar"));
        tabPanel.setHeaderVisible(false);
        tabPanel.setBodyBorder(false);
        tabPanel.setBorders(false);

        bottomPanel.add(tabPanel, new BorderLayoutData(LayoutRegion.NORTH, 20));

        final BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 100);
        data.setCollapsible(true);
        data.setMargins(new Margins(0, 0, 5, 0));
        add(panelBanner, data);
        add(bottomPanel, new BorderLayoutData(LayoutRegion.CENTER));

        insufficient = new HTML(I18N.CONSTANTS.permViewOrgUnitInsufficient());
        insufficient.addStyleName("important-label-white");
    }

    @Override
    public ContentPanel getPanelBanner() {
        return panelBanner;
    }

    @Override
    public ContentPanel getTabPanel() {
        return tabPanel;
    }

    @Override
    public void setMainPanel(Widget widget) {
        if (this.widget != null)
            bottomPanel.remove(this.widget);

        bottomPanel.add(widget, new BorderLayoutData(LayoutRegion.CENTER));
        this.widget = widget;

        bottomPanel.layout();
    }

    @Override
    public void insufficient() {

        if (viewDisplayed != null && !viewDisplayed) {
            return;
        }

        if (viewDisplayed != null) {
            remove(panelBanner);
            remove(bottomPanel);
        }

        add(insufficient, new BorderLayoutData(LayoutRegion.CENTER));

        viewDisplayed = false;

        layout();
    }

    @Override
    public void sufficient() {

        if (viewDisplayed != null && viewDisplayed) {
            return;
        }

        if (viewDisplayed != null) {
            remove(insufficient);
        }

        final BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 100);
        data.setMargins(new Margins(0, 0, 5, 0));
        add(panelBanner, data);
        add(bottomPanel, new BorderLayoutData(LayoutRegion.CENTER));

        viewDisplayed = true;

        layout();
    }
}
