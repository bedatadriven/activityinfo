package org.sigmah.shared.dto.element.handler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * A GWT event that cannot be killed.
 * 
 * @author tmi
 * 
 * @param <H>
 */
public abstract class GWTImmortalEvent<H extends EventHandler> extends GwtEvent<H> {

    @Override
    protected void kill() {
        // nothing.
    }

    @Override
    protected void revive() {
        // nothing.
    }
}
