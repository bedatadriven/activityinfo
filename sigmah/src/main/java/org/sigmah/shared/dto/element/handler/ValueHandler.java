package org.sigmah.shared.dto.element.handler;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public interface ValueHandler extends EventHandler {
	
	public void onValueChange(ValueEvent event);

}
