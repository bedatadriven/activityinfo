package org.activityinfo.client.common.place;

import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceParser;

public class SimplePlaceParser implements PlaceParser {

    private Place place;

    public SimplePlaceParser(Place place) {
        this.place = place;
    }

    public Place parse(String token) {
        return place;
    }
}
