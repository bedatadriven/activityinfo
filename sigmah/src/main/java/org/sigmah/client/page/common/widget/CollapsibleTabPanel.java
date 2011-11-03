/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

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
		if(super.add(tab)) {
	
	        tab.getHeader().addListener(Events.BrowserEvent, new Listener<ComponentEvent>() {
	            public void handleEvent(ComponentEvent be) {
	                if(be.getEventTypeInt() == Event.ONCLICK) {
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
        if(getSelectedItem()!=null && getSelectedItem().getHeader() == header) {
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
            getParentLayout().layout();
        }
    }

    private void collapseTabs() {
        tabPanelExandedSize = (int)layoutData.getSize();
        layoutData.setSize(getBar().getHeight());
        layoutData.setMargins(new Margins(0));
        getBody().setVisible(false);
        layoutData.setSplit(false);
        tabPanelCollapsed = true;
    }

    private void expandTabs() {
        getBody().setVisible(true);
        layoutData.setSize(tabPanelExandedSize);
        layoutData.setMargins(new Margins(5, 0, 0, 0));
        layoutData.setSplit(true);
        tabPanelCollapsed = false;
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
    	return (BorderLayout)((LayoutContainer)getParent()).getLayout();
    }
 }
