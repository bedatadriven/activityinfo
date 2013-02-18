/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client;

import com.extjs.gxt.ui.client.event.EventType;

public final class AppEvents {
	
	private AppEvents() {}

    public static final EventType INIT = new EventBus.NamedEventType("Init");

    public static final EventType SCHEMA_CHANGED = new EventBus.NamedEventType("SchemaChanged");

    public static final EventType DRILL_DOWN = new EventBus.NamedEventType("Drilldown");
        
}
