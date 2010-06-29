/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import java.util.Map;
import java.util.Set;

public class PropertyMap {
    private Map<String, Object> map;

    public PropertyMap(Map<String, Object> map) {
        this.map = map;
    }

    public <X> X get(String propertyName) {
        return (X) map.get(propertyName);
    }

    public boolean containsKey(String propertyName) {
        return map.containsKey(propertyName);
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}
