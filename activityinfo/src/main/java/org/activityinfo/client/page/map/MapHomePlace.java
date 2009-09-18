package org.activityinfo.client.page.map;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MapHomePlace implements Place {

    public String pageStateToken() {
        return null;
    }

    public List<ViewPath.Node> getViewPath() {
        return ViewPath.make(Maps.Home);
    }

    public PageId getPageId() {
        return Maps.Home;
    }

    public static class Parser implements PlaceParser {

        @Override
        public Place parse(String token) {
            return new MapHomePlace();
        }
    }
}