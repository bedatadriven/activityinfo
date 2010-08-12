/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.common.nav.NavigationPanel;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

/**
 * A VSplit panel with an accordion layout.
 */
public class VSplitFilteredFrameSet extends VSplitFrameSet {

	private ContentPanel filterPane;

    public VSplitFilteredFrameSet(PageId pageId, NavigationPanel navPanel) {
		super(pageId, navPanel);
	}

    @Override
	protected void addNavigationPanel() {
        BorderLayoutData layoutData = new BorderLayoutData(Style.LayoutRegion.WEST);
        layoutData.setSplit(true);
        layoutData.setCollapsible(true);
        layoutData.setMargins(new Margins(0, 5, 0, 0));

        filterPane = new ContentPanel();
		filterPane.setHeading(null);
		filterPane.setLayout(new AccordionLayout());
		filterPane.add(super.getNavPanel());
		container.add(filterPane, layoutData);
	}
	
	public void addFilterPanel(ContentPanel panel) {
		filterPane.add(panel);
    }
}
