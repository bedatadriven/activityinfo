package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.sigmah.client.page.common.widget.CollapsibleTabPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteGridPage extends SiteGrid {

    private CollapsibleTabPanel tabPanel;
    private List<ToggleButton> sideBarButtons = new ArrayList<ToggleButton>();
    private LayoutContainer sidePanel;

    private int tabPanelExandedSize = 200;
    private boolean tabPanelCollapsed;
    private BorderLayoutData tabPanelLayout;

    public SiteGridPage(boolean enableDragSource) {
        super(enableDragSource);
    }

    public void addSidePanel(String name, AbstractImagePrototype icon, final Component component) {

        final ToggleButton sideBarButton = new ToggleButton(name, icon);
        sideBarButton.setToggleGroup("sideBar");
        sideBarButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                BorderLayout borderLayout = (BorderLayout) getLayout();
                if(sideBarButton.isPressed()) {

                    if(sidePanel == null) {
                        sidePanel = new LayoutContainer();
                        sidePanel.setLayout(new CardLayout());

                        BorderLayoutData east = new BorderLayoutData(Style.LayoutRegion.EAST, 0.4f);
                        east.setSplit(true);
                        east.setMargins(new Margins(0, 0, 0, 5));

                        add(sidePanel, east);
                    } else if(isRendered()) {
                        borderLayout.show(Style.LayoutRegion.EAST);
                    }
                    if(!component.isAttached()) {
                        sidePanel.add(component);
                    }
                    ((CardLayout)sidePanel.getLayout()).setActiveItem(component);
                    borderLayout.layout();
                } else {
                    borderLayout.hide(Style.LayoutRegion.EAST);
                }
            }
        });
        sideBarButtons.add(sideBarButton);
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();

        for(ToggleButton button : sideBarButtons) {
            toolBar.add(button);
        }
    }

    public void addSouthTab(TabItem tab) {

        if(tabPanel == null) {

            tabPanelLayout = new BorderLayoutData(Style.LayoutRegion.SOUTH);
            tabPanelLayout.setCollapsible(true);
            tabPanelLayout.setSplit(true);
            tabPanelLayout.setMargins(new Margins(5, 0, 0, 0));

            tabPanel = new CollapsibleTabPanel();
            tabPanel.setTabPosition(TabPanel.TabPosition.BOTTOM);
            tabPanel.setAutoSelect(false);
            add(tabPanel, tabPanelLayout);
        }

        tab.getHeader().addListener(Events.BrowserEvent, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
                if(be.getEventTypeInt() == Event.ONCLICK) {
                    onTabClicked((TabItem.HeaderItem) be.getComponent());
                }
            }
        });

        tabPanel.add(tab);


    }

    private void onTabClicked(TabItem.HeaderItem header) {

        if(tabPanel.getSelectedItem()!=null && tabPanel.getSelectedItem().getHeader() == header) {

            if(!tabPanelCollapsed) {
                // "collapse" tab panel - show only the tab strip
                collapseTabs();
            } else {
                // expand tab panel to previous size
                expandTabs();
            }
            getLayout().layout();
        } else if(tabPanelCollapsed) {
            expandTabs();
            getLayout().layout();
        }

    }

    private void collapseTabs() {
        tabPanelExandedSize = (int)tabPanelLayout.getSize();
        tabPanelLayout.setSize(tabPanel.getBar().getHeight());
        tabPanelLayout.setMargins(new Margins(0));
        tabPanel.getBody().setVisible(false);
        tabPanelLayout.setSplit(false);
        tabPanelCollapsed = true;
    }

    private void expandTabs() {
        tabPanel.getBody().setVisible(true);
        tabPanelLayout.setSize(tabPanelExandedSize);
        tabPanelLayout.setMargins(new Margins(5, 0, 0, 0));
        tabPanelLayout.setSplit(true);
        tabPanelCollapsed = false;
    }
}
