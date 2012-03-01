/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.util.state;

import java.util.Date;
import java.util.Map;

/**
 * Clean abstraction around GXT's {@link com.extjs.gxt.ui.client.state.StateManager},
 * to facilitate dependency injection.
 *
 */
public interface StateProvider {

    Object get(String name);

    Date getDate(String name);

    Integer getInteger(String name);

    Map<String, Object> getMap(String name);

    String getString(String name);

    void set(String name, Object value);

}
