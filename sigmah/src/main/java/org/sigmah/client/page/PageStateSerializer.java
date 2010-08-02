/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Handles serialization/deserialization of {@link org.sigmah.client.page.PageState}s into/from
 * history tokens.
 *
 * E.g. #site-grid/33 => new SiteGrid{ activityId=33 }
 *
 * Page components must register themselves for history support to work.
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
@Singleton
public class PageStateSerializer {

    /**
     * Maps pageIds to place parsers
     */
    private final Map<String, PageStateParser> parsers = new HashMap<String, PageStateParser>();


    public String serialize(PageState place) {
        StringBuilder sb = new StringBuilder();
        sb.append(place.getPageId());

        String pageState = place.serializeAsHistoryToken();
        if(pageState!=null) {
            sb.append("/").append(place.serializeAsHistoryToken());
        }

        return sb.toString();
    }

    public PageState deserialize(String token) {
        // If an ID separator is found ('!'), uses it instead of the path separator
        int i = token.indexOf('!');
        if(i == -1)
            i = token.indexOf('/');

        String pageId;
        String pageState;
        if(i == -1) {
            pageId = token;
            pageState = null;
        }  else {
            pageId = token.substring(0, i);
            pageState = token.substring(i+1);
        }

        PageStateParser parser = parsers.get(pageId);
        if(parser != null) {
            return parser.parse(pageState);
        } else {

            Log.warn("PlaceDeserializer: No parser registered for page '" + pageId + ".");

            return null;
        }
    }

    public void registerParser(PageId pageId, PageStateParser parser) {
        parsers.put(pageId.toString(), parser);
        Log.debug("PageSerializer: registered page serializer " + parser.toString() + " for pageId '" + pageId + "'");
    }

    public void registerStatelessPlace(PageId pageId, final PageState place) {
        parsers.put(pageId.toString(), new PageStateParser() {
            @Override
            public PageState parse(String token) {
                return place;
            }
        });
    }
}
