package org.activityinfo.client.util.state;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
