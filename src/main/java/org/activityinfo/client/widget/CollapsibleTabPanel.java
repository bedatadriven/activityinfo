package org.activityinfo.client.widget;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Event;

/**
 * A tab panel that can be collapsed by double-clicking the tab.
 */
public class CollapsibleTabPanel extends TabPanel {

    private int tabPanelExandedSize = 200;
    private boolean tabPanelCollapsed;
    private BorderLayoutData layoutData;

    public CollapsibleTabPanel() {
        setTabPosition(TabPanel.TabPosition.BOTTOM);
        setAutoSelect(false);

        layoutData = new BorderLayoutData(Style.LayoutRegion.SOUTH);
        layoutData.setCollapsible(true);
        layoutData.setSplit(true);
        layoutData.setMargins(new Margins(5, 0, 0, 0));
    }

    public BorderLayoutData getBorderLayoutData() {
        return layoutData;
    }

    @Override
    public boolean add(TabItem tab) {
        if (super.add(tab)) {

            tab.getHeader().addListener(Events.BrowserEvent,
                new Listener<ComponentEvent>() {
                    @Override
                    public void handleEvent(ComponentEvent be) {
                        if (be.getEventTypeInt() == Event.ONCLICK) {
                            onTabClicked((TabItem.HeaderItem) be.getComponent());
                        }
                    }
                });
            return true;
        } else {
            return false;
        }
    }

    private void onTabClicked(TabItem.HeaderItem header) {
        if (getSelectedItem() != null
            && getSelectedItem().getHeader() == header) {
            if (!tabPanelCollapsed) {
                // "collapse" tab panel - show only the tab strip
                collapseTabs();
            } else {
                // expand tab panel to previous size
                expandTabs();
            }
        } else if (tabPanelCollapsed) {
            expandTabs();
        }
    }

    private void collapseTabs() {
        tabPanelExandedSize = (int) layoutData.getSize();
        layoutData.setSize(getBar().getHeight());
        layoutData.setMargins(new Margins(0));
        getBody().setVisible(false);
        layoutData.setSplit(false);
        tabPanelCollapsed = true;
        getParentLayout().layout();
    }

    private void expandTabs() {
        getBody().setVisible(true);
        layoutData.setSize(tabPanelExandedSize);
        layoutData.setMargins(new Margins(5, 0, 0, 0));
        layoutData.setSplit(true);
        tabPanelCollapsed = false;
        getParentLayout().layout();
    }

    private El getBody() {
        if (getTabPosition() == TabPosition.TOP) {
            return el().getChild(1);
        } else {
            return el().getChild(0);
        }
    }

    private El getBar() {
        if (getTabPosition() == TabPosition.TOP) {
            return el().getChild(0);
        } else {
            return el().getChild(1);
        }
    }

    private BorderLayout getParentLayout() {
        return (BorderLayout) ((LayoutContainer) getParent()).getLayout();
    }
}
