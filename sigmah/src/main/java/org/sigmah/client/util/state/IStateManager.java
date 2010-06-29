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
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface IStateManager {

    public Object get(String name);

    public Date getDate(String name);

    public Integer getInteger(String name);

    public Map<String, Object> getMap(String name);

    public String getString(String name);

    public void set(String name, Object value);

}
