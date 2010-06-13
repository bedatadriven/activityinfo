package org.activityinfo.client.page.common.nav;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.activityinfo.client.page.PageState;

/*
 * @author Alex Bertram
 */

public class Link extends BaseTreeModel {

    private PageState place;
    private AbstractImagePrototype icon;

    public Link(String name, PageState place) {
        set("name", name);
        this.place = place;
    }

    public Link(String name, PageState place, AbstractImagePrototype icon) {
        this(name, place);
        this.icon = icon;
    }

    public String getName() {
        return get("name");
    }

    public PageState getPlace() {
        return place;
    }

    public AbstractImagePrototype getIcon() {
        return icon;
    }

    public String getKey() {
        return get("key");
    }

    public void setKey(String key) {
        set("key", key);
    }

}
