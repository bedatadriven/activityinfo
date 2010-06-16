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

import com.google.inject.Inject;
import org.activityinfo.server.mail.MailSender;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LoggingInterceptor implements MethodInterceptor {

    private MailSender mailSender;
    private List<String> alertRecipients = new ArrayList<String>(0);

    @Inject(optional = true)
    public void setMailSender(MailSender sender) {
        this.mailSender = sender;
    }

    @Inject(optional = true)
    public void setProperties(Properties properties) {
        String alertRecipients = properties.getProperty("alert.recipients");
        if(alertRecipients != null) {
            for(String alertRecipient : alertRecipients.split(",")) {
                String alertRecipientTrimmed = alertRecipient.trim();
                if(alertRecipientTrimmed.length() > 0) {
                    this.alertRecipients.add(alertRecipientTrimmed);
                }
            }
        }
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if(isTraceEnabled(invocation)) {
            trace(invocation);
        }
        try {
            return invocation.proceed();
        } catch(Throwable e) {
            if(isExceptionLoggingEnabled(invocation)) {
                onException(invocation, e);
            }
            throw e;
        }
    }

    private void onException(MethodInvocation invocation, Throwable caught) {
        Logger logger = Logger.getLogger(getOriginalClass(invocation));
        logException(invocation, caught, logger);
        if(isEmailAlertEnabled(invocation)) {
            mailException(invocation, caught, logger);
        }
    }

    private void logException(MethodInvocation invocation, Throwable e, Logger logger) {
        logger.error("Exception was thrown while executing " + invocation.getMethod().getName(), e);
    }

    private void mailException(MethodInvocation invocation, Throwable caught, Logger logger) {
        if(mailSender == null) {
            logger.warn("emailAlert is enabled for " + invocation.getMethod().getName() + " but no MailSender is conigured");
        }
        if(alertRecipients.isEmpty()) {
            logger.warn("emailAlert is enabled for " + invocation.getMethod().getName() + " but no alert recipients are specified. " +
                    "Please set the 'alert.recipients' property in the activityinfo.config file");
        }
        if(mailSender != null && !alertRecipients.isEmpty()) {
            try {
                sendMail(caught);
            } catch (EmailException e) {
                logger.warn("Exception thrown while trying to email alert about previous exception", e);
            }
        }
    }

    private void sendMail(Throwable caught) throws EmailException {
        SimpleEmail email = new SimpleEmail();
        for(String address : alertRecipients) {
            email.addTo(address);
        }
        email.setSubject("[ACTIVITYINFO EXCEPTION] " + caught.getMessage());
        email.setMsg( stackTraceToString(caught));
        mailSender.send(email);
    }


    private String stackTraceToString(Throwable caught) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        caught.printStackTrace(printWriter);
        String trace = stringWriter.toString();
        return trace;
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

    private boolean isEmailAlertEnabled(MethodInvocation invocation) {
        LogException logSetting =  invocation.getMethod().getAnnotation(LogException.class);
        return logSetting != null && logSetting.emailAlert();
    }

    private boolean isTraceEnabled(MethodInvocation invocation) {
        return invocation.getMethod().getAnnotation(Trace.class) != null;
    }
}
