package org.activityinfo.client;

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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Facade to ease transition from com.allen_sauer.log to
 * JULI logging
 *
 */
public class Log {

	public static Logger LOGGER = Logger.getLogger(Log.class.getName());

	public static void trace(String message) {
		LOGGER.finest(message);
	}

	public static void debug(String message) {
		LOGGER.fine(message);
	}

	public static boolean isDebugEnabled() {
		return LOGGER.isLoggable(Level.FINE);
	}

	public static void info(String string) {
		LOGGER.info(string);
	}

	public static void error(String message, Throwable throwable) {
		LOGGER.log(Level.SEVERE, message, throwable);		
	}

	public static boolean isErrorEnabled() {
		return LOGGER.isLoggable(Level.SEVERE);
	}

	public static void error(String string) {
		LOGGER.severe(string);
	}

	public static void debug(String message, Throwable exception) {
		LOGGER.log(Level.FINE, message, exception);
	}

	public static void warn(String string) {
		LOGGER.log(Level.WARNING, string);
	}
	
	
	
	
}
