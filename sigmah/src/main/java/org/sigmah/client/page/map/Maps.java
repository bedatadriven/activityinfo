/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import org.sigmah.client.page.PageId;

public class Maps {

    public static class MapId extends PageId {
        public MapId(String id) {
            super(id);
        }
    }
    public static final PageId Maps = new MapId("maps");
}
