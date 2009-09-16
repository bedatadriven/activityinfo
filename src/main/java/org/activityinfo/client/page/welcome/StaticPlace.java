package org.activityinfo.client.page.welcome;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class StaticPlace implements Place {

    String keyword;

    public StaticPlace(String keyword) {
        this.keyword = keyword;
    }

    public PageId getPageId() {
        return Pages.Static;
    }

    public String pageStateToken() {
        return keyword;
    }

    public List<ViewPath.Node> getViewPath() {
        return ViewPath.make(Pages.Static);
    }

    public String getKeyword() {
        return keyword;
    }
    
    public static class Parser implements PlaceParser {

        public Place parse(String token) {
            return new StaticPlace(token);
        }
    }


}
