/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

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

/**
 * Initializes the view elements of a project page.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ProjectView extends LayoutContainer implements ProjectPresenter.View {
    private final ContentPanel panelProjectBanner;
    private final ContentPanel tabPanel;
    private final ContentPanel bottomPanel;
    private final HTML insufficient;
    private Boolean viewDisplayed = null;
    private Widget widget;

    /**
     * Initializes a new ProjectView object.
     */
    public ProjectView() {
        if (Log.isDebugEnabled()) {
            Log.debug("Initializing the ProjectView object.");
        }
        final BorderLayout borderLayout = new BorderLayout();
        borderLayout.setContainerStyle("x-border-layout-ct main-background");
        setLayout(borderLayout);

        /* Project banner */
        panelProjectBanner = new ContentPanel();
        panelProjectBanner.setBorders(false);
        // panelProjectBanner.setHeight(100);
        panelProjectBanner.setLayout(new FitLayout());
        panelProjectBanner.addStyleName("project-label-10");

        bottomPanel = new ContentPanel(new BorderLayout());
        bottomPanel.setHeaderVisible(false);
        bottomPanel.setBodyBorder(false);
        bottomPanel.setBorders(false);

        /* Project tab panel (main tab panel) */
        tabPanel = new ContentPanel(new StylableHBoxLayout("main-background project-top-bar"));
        tabPanel.setHeaderVisible(false);
        tabPanel.setBodyBorder(false);
        tabPanel.setBorders(false);

        bottomPanel.add(tabPanel, new BorderLayoutData(LayoutRegion.NORTH, 20));

        insufficient = new HTML(I18N.CONSTANTS.permViewProjectInsufficient());
        insufficient.addStyleName("important-label-white");
    }

    @Override
    public ContentPanel getPanelProjectBanner() {
        return panelProjectBanner;
    }

    @Override
    public ContentPanel getTabPanel() {
        return tabPanel;
    }

    @Override
    public void insufficient() {

        if (viewDisplayed != null && !viewDisplayed) {
            return;
        }

        if (viewDisplayed != null) {
            remove(panelProjectBanner);
            remove(bottomPanel);
        }

        add(insufficient, new BorderLayoutData(LayoutRegion.CENTER));

        viewDisplayed = false;
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
        data.setCollapsible(true);
        data.setMargins(new Margins(0, 0, 5, 0));
        add(panelProjectBanner, data);
        add(bottomPanel, new BorderLayoutData(LayoutRegion.CENTER));

        viewDisplayed = true;
    }

    @Override
    public void setMainPanel(Widget widget) {
        if (this.widget != null)
            bottomPanel.remove(this.widget);

        bottomPanel.add(widget, new BorderLayoutData(LayoutRegion.CENTER));
        this.widget = widget;

        bottomPanel.layout();
    }
}
