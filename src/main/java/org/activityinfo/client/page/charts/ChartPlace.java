package org.activityinfo.client.page.charts;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartPlace implements Place  {

    public String pageStateToken() {
        return null;
    }

    public List<ViewPath.Node> getViewPath() {
        return ViewPath.make(getPageId());
    }

    @Override
    public PageId getPageId() {
        return Charts.Charts;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof ChartPlace;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static class Parser implements PlaceParser {

        @Override
        public Place parse(String token) {
            return new ChartPlace();
        }
    }
}
