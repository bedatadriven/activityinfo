package org.activityinfo.server.util.logging;

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
