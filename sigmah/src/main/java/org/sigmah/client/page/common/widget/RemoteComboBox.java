/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

/**
 * Subclass of {@link com.extjs.gxt.ui.client.widget.form.ComboBox} that is better
 * at handling remote connection failures than the standard GXT implementation.
 *
 * In GXT's implementation, the remote query is executed when the user clicks the trigger
 * button for the first time; if this query fails, the ComboBox populates its internal
 * list with an empty set, and so subsequent clicks on the trigger are not even executed.
 *
 * This implementation assures that the remote call is made each time the trigger button is clicked.
 * Caching is then done on the dispatch level, for example, at
 * {@link org.sigmah.client.dispatch.remote.cache.AdminEntityCache}
 *
 * @author Alex Bertram
 */
public class RemoteComboBox<T extends ModelData> extends ComboBox<T> {

    @Override
    public void doQuery(String q, boolean forceAll) {
        // load every time
        store.getLoader().load(getParams(q));
        expand();
    }
}
