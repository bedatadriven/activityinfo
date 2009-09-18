package org.activityinfo.client.common.nav;

import com.extjs.gxt.ui.client.data.DataProxy;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface Navigator extends DataProxy<List<Link>> {

    public String getHeading();

    public boolean hasChildren(Link parent);

    String getStateId();
}
