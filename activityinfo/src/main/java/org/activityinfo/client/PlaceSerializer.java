package org.activityinfo.client;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.page.PageId;

import java.util.Map;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
@Singleton
public class PlaceSerializer {

    private final Map<String, PlaceParser> parsers = new HashMap<String, PlaceParser>();

    
    public String serialize(Place place) {
        StringBuilder sb = new StringBuilder();
        sb.append(place.getPageId());

        String pageState = place.pageStateToken();
        if(pageState!=null) {
            sb.append("/").append(place.pageStateToken());
        }

        return sb.toString();
    }

    public Place deserialize(String token) {

        int i = token.indexOf('/');

        String pageId;
        String pageState;
        if(i == -1) {
            pageId = token;
            pageState = null;
        }  else {
            pageId = token.substring(0, i);
            pageState = token.substring(i+1);
        }

        PlaceParser parser = parsers.get(pageId);
        if(parser != null) {
            return parser.parse(pageState);
        } else {

            GWT.log("PlaceDeserializer: No parser registered for page '" + pageId + ".", null);

            return null;
        }
    }

    public void registerParser(PageId pageId, PlaceParser parser) {
        parsers.put(pageId.toString(), parser);
    }

}
