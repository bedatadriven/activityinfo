/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class MapPageState implements PageState {

    public PageId getPageId() {
        return Maps.Maps;
    }

    public String serializeAsHistoryToken() {
        return null;
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(getPageId());
    }

    public static class Parser implements PageStateParser {
        public PageState parse(String token) {
            return new MapPageState();
        }
    }
}
