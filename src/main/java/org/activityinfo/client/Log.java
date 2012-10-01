package org.activityinfo.client;

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
