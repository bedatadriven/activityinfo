package org.sigmah.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import org.sigmah.client.page.PageState;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class NavigationEvent extends BaseEvent {

    private final PageState place;

    public NavigationEvent(EventType type, PageState place) {
        super(type);
        this.place = place;

        assert this.place != null;
    }

    public PageState getPlace() {
        return place;
    }

    @Override
    public String toString() {
        return place.getPageId() + "/" + place.serializeAsHistoryToken();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NavigationEvent that = (NavigationEvent) o;

        if (place != null ? !place.equals(that.place) : that.place != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return place != null ? place.hashCode() : 0;
    }
}
