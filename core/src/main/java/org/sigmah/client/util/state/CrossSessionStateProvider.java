package org.sigmah.client.util.state;

import com.google.inject.ImplementedBy;

/**
 * A {@link StateProvider} that is guaranteed to provide persistence of state
 * across user sessions.
 * 
 * The normal StateProvider may not persist session on browsers that do not support
 * local storage. Class should qualify dependencies with this interface if they 
 * absolutely require persistence of state across sessions.
 */
@ImplementedBy(CrossSessionStateProviderImpl.class)
public interface CrossSessionStateProvider extends StateProvider {

	
	
}
