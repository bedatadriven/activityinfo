/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import org.activityinfo.server.mail.MessageBuilder;
import org.activityinfo.server.mail.MailSender;
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
