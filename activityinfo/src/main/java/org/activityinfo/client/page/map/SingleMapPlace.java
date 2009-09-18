package org.activityinfo.client.page.map;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class SingleMapPlace implements Place {

    public PageId getPageId() {
        return Maps.Single;
    }

    public String pageStateToken() {
        return null;
    }

    public List<ViewPath.Node> getViewPath() {
        return ViewPath.make(getPageId());
    }

    public static class Parser implements PlaceParser {
        public Place parse(String token) {
            return new SingleMapPlace();
        }
    }
}
