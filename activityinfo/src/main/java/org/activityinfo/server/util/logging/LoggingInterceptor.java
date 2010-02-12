/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.util.logging;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

public class LoggingInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if(isTraceEnabled(invocation)) {
            trace(invocation);
        }
        try {
            return invocation.proceed();
        } catch(Throwable e) {
            if(isExceptionLoggingEnabled(invocation)) {
                logException(invocation, e);
            }
            throw e;
        }
    }

    private void logException(MethodInvocation invocation, Throwable e) {
        Logger logger = Logger.getLogger(getOriginalClass(invocation));
        logger.error("Exception was thrown while executing " + invocation.getMethod().getName(), e);
    }

    private Class<?> getOriginalClass(MethodInvocation invocation) {
        return invocation.getThis().getClass().getSuperclass();
    }

    private void trace(MethodInvocation invocation) {
        Logger logger = Logger.getLogger(invocation.getThis().getClass());
        logger.trace("Calling " + invocation.getMethod().getName());
    }

    private boolean isExceptionLoggingEnabled(MethodInvocation invocation) {
        return invocation.getMethod().getAnnotation(LogException.class) != null;
    }

    private boolean isTraceEnabled(MethodInvocation invocation) {
        return invocation.getMethod().getAnnotation(Trace.class) != null;
    }
}
