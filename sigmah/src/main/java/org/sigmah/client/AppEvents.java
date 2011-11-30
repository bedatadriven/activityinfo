/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.extjs.gxt.ui.client.event.EventType;

public class AppEvents {
	
	private AppEvents() {}

    public static final EventType INIT = new EventBus.NamedEventType("Init");

    public static final EventType SCHEMA_CHANGED = new EventBus.NamedEventType("SchemaChanged");


    public static final EventType SITE_SELECTED = new EventBus.NamedEventType("SiteSelected");
    public static final EventType SITE_CREATED = new EventBus.NamedEventType("SiteCreated");
    
    public static final EventType SITE_CHANGED = new EventBus.NamedEventType("SiteChanged");


    public static final EventType DRILL_DOWN = new EventBus.NamedEventType("Drilldown");

}
