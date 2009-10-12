package org.activityinfo.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import org.activityinfo.shared.dto.SiteModel;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteEvent extends BaseEvent {

    private int siteId;
    private SiteModel site;
    
    public SiteEvent(EventType type, Object source, SiteModel site) {
        super(type);
        this.setSource(source);
        this.site = site;
        this.siteId = site.getId();
    }

    public SiteEvent(EventType type, Object source, int siteId) {
        super(type);
        this.setSource(source);
        this.siteId = siteId;
    }

    public SiteModel getSite() {
        return site;
    }

    public int getSiteId() {
        return siteId;
    }
}
