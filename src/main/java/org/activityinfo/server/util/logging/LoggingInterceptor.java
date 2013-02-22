

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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.MessageBuilder;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;

public class LoggingInterceptor implements MethodInterceptor {

    private MailSender mailSender;
    private final List<String> alertRecipients = new ArrayList<String>(0);
    
    
    @Inject(optional = true)
    public void setMailSender(MailSender sender) {
        this.mailSender = sender;
    }

    @Inject(optional = true)
    public void setProperties(DeploymentConfiguration properties) {
        String alertRecipientsProperty = properties.getProperty("alert.recipients");
        if(alertRecipientsProperty != null) {
            for(String alertRecipient : alertRecipientsProperty.split(",")) {
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
        } catch(Exception e) {
            if(isExceptionLoggingEnabled(invocation)) {
                onException(invocation, e);
            }
            throw e;
        }
    }

    private void onException(MethodInvocation invocation, Throwable caught) {
        Logger logger = Logger.getLogger(getOriginalClass(invocation).getName());
        logException(invocation, caught, logger);
        if(isEmailAlertEnabled(invocation)) {
            mailException(invocation, caught, logger);
        }
    }

    private void logException(MethodInvocation invocation, Throwable e, Logger logger) {
        logger.log(Level.SEVERE, "Exception was thrown while executing " + invocation.getMethod().getName(), e);
    }

    private void mailException(MethodInvocation invocation, Throwable caught, Logger logger) {
        if(mailSender == null) {
            logger.log(Level.WARNING, "emailAlert is enabled for " + invocation.getMethod().getName() + " but no MailSender is conigured");
        }
        if(alertRecipients.isEmpty()) {
            logger.log(Level.WARNING, "emailAlert is enabled for " + invocation.getMethod().getName() + " but no alert recipients are specified. " +
                    "Please set the 'alert.recipients' property in the activityinfo.config file");
        }
        if(mailSender != null && !alertRecipients.isEmpty()) {
            try {
                sendMail(caught);
            } catch (MessagingException e) {
                logger.log(Level.WARNING, "Exception thrown while trying to email alert about previous exception", e);
            }
        }
    }

    private void sendMail(Throwable caught) throws MessagingException {
    	MessageBuilder email = new MessageBuilder();
        for(String address : alertRecipients) {
            email.to(address);
        }
        email.subject("[ACTIVITYINFO EXCEPTION] " + caught.getMessage());
        email.body( stackTraceToString(caught));
        mailSender.send(email.build());
    }


    private String stackTraceToString(Throwable caught) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        caught.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private Class<?> getOriginalClass(MethodInvocation invocation) {
        return invocation.getThis().getClass().getSuperclass();
    }

    private void trace(MethodInvocation invocation) {
        Logger logger = Logger.getLogger(invocation.getThis().getClass().getName());
        logger.finer("Calling " + invocation.getMethod().getName());
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
