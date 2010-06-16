package org.activityinfo.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import org.activityinfo.shared.dto.SiteDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteEvent extends BaseEvent {

    private int siteId;
    private SiteDTO site;
    
    public SiteEvent(EventType type, Object source, SiteDTO site) {
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

    public SiteDTO getSite() {
        return site;
    }

    public int getSiteId() {
        return siteId;
    }
}
