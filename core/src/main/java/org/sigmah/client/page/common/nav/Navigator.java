/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.nav;

import java.util.List;

import com.extjs.gxt.ui.client.data.DataProxy;

/**
 * Provides the data for the contents of a
 * {@link org.sigmah.client.page.common.nav.NavigationPanel}
 *
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface Navigator extends DataProxy<List<Link>> {

    /**
     *
     * @return The text to display in the heading of the navigation panel
     */
    public String getHeading();

    /**
     *
     * @param parent
     * @return True if the given parent has children
     */
    public boolean hasChildren(Link parent);

    /**
     * @return A unique id used for storing the state of this
     * tree.
     */
    String getStateId();
}
