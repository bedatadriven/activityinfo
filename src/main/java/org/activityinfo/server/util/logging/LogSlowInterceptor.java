package org.activityinfo.server.util.logging;

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
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LogSlowInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		LogSlow annotation = invocation.getMethod().getAnnotation(LogSlow.class);
		long start = System.currentTimeMillis();
		try { 
			Object result = invocation.proceed();
			long elapsed = System.currentTimeMillis() - start;
			if(elapsed > annotation.threshold()) {
				emitLog(invocation, "Slow execution in "  + elapsed + "ms");
			}
			return result;
		} catch(Throwable caught) {
			emitLog(invocation, "Slow log (did not complete)");
			throw caught;
		}
	}

	private void emitLog(MethodInvocation invocation, String message) {
		LogRecord record = new LogRecord(Level.WARNING, message);
		String declaringClassName = invocation.getMethod().getDeclaringClass().getName();
		record.setSourceClassName(declaringClassName);
		record.setSourceMethodName(invocation.getMethod().getName());
		Logger.getLogger(declaringClassName).log(record);
	}

}
