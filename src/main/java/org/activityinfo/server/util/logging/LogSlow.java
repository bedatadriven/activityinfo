package org.activityinfo.server.util.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogSlow {
	/**
	 * 
	 * @return threshold to log warning, in milliseconds. 
	 */
	long threshold();
}
