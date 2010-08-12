/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.nav;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.sigmah.client.page.PageState;

/**
 * A TreeModel that a represents a link to another PageId+PageState
 *
 * @author Alex Bertram
 */
public class Link extends BaseTreeModel {
    private PageState pageState;
    private AbstractImagePrototype icon;

    /**
     * @param name name of the link to appear in the tree
     * @param pageState the pageState to which to navigate on click
     */
    public Link(String name, PageState pageState) {
        set("name", name);
        this.pageState = pageState;
    }

    /**
     * @param name name of the link to appear in the tree
     * @param pageState the pageState to which to navigate on click
     * @param icon the icon to apper in the tree
     */
    public Link(String name, PageState pageState, AbstractImagePrototype icon) {
        this(name, pageState);
        this.icon = icon;
    }

    public String getName() {
        return get("name");
    }

    public PageState getPageState() {
        return pageState;
    }

    public AbstractImagePrototype getIcon() {
        return icon;
    }

    /**
     * @return the link's key, to be used for state management
     */
    public String getKey() {
        return get("key");
    }
}
