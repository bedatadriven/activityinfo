

package org.activityinfo.client.page;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.client.Log;
import com.google.inject.Singleton;

/**
 *
 * Handles serialization/deserialization of {@link org.activityinfo.client.page.PageState}s into/from
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


    public static String serialize(PageState place) {
        StringBuilder sb = new StringBuilder();
        sb.append(place.getPageId());

        String pageState = place.serializeAsHistoryToken();
        if(pageState!=null) {
            sb.append("/").append(place.serializeAsHistoryToken());
        }

        return sb.toString();
    }
    
    public static String asLink(PageState place) {
    	return "#" + serialize(place);
    }

    public PageState deserialize(String token) {
        // If an ID separator is found ('!'), uses it instead of the path separator
        int i = token.indexOf('!');
        if(i == -1) {
            i = token.indexOf('/');
        }
            
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
