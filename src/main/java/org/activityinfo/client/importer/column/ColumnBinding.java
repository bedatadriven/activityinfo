package org.activityinfo.client.importer.column;

import org.activityinfo.shared.command.MatchLocation;
import org.activityinfo.shared.dto.SiteDTO;

public abstract class ColumnBinding  {

    public String getLabel() {
        return getClass().getName();
    }

    public void bindLocation(String rowValue, MatchLocation location) {
        
    }

    public void bindSite(String string, SiteDTO site) {

        
    }   
}
