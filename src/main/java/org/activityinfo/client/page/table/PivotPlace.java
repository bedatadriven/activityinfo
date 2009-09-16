package org.activityinfo.client.page.table;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPlace implements Place {

    @Override
    public PageId getPageId() {
        return Pages.Pivot;
    }

    @Override
    public String pageStateToken() {
        return null;
    }

    @Override
    public List<ViewPath.Node> getViewPath() {
        return ViewPath.make(Pages.Pivot);
    }

    public static class Parser implements PlaceParser {
        @Override
        public Place parse(String token) {
            return new PivotPlace();
        }
    }
}
