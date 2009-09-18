package org.activityinfo.client.page.map;

import org.activityinfo.client.page.PageId;

public class Maps {

    public static class MapId extends PageId {
        public MapId(String id) {
            super(id);
        }
    }
    public static final PageId Home = new MapId("maps");
    public static final PageId Single = new MapId("sindicator");
}
