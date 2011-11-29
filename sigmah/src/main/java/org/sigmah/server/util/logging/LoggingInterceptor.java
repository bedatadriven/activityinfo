/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;
import org.sigmah.server.mail.MailSender;
import org.sigmah.server.util.config.DeploymentConfiguration;

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
        return stringWriter.toString();
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
