package org.activityinfo.client.page.base;

import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.core.El;
/*
 * @author Alex Bertram
 */

public class CollapsibleTabPanel extends TabPanel {



    public El getBody() {
        if (getTabPosition() == TabPosition.TOP) {
            return el().getChild(1);
        } else {
            return el().getChild(0);
        }
    }

    public El getBar() {
       if (getTabPosition() == TabPosition.TOP) {
            return el().getChild(0);
        } else {
            return el().getChild(1);
        }
    }

}
