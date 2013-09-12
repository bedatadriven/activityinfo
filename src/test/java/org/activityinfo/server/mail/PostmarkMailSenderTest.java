package org.activityinfo.server.mail;

import java.util.Properties;

import org.activityinfo.server.database.hibernate.entity.Domain;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.TemplateModule;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.junit.Before;
import org.junit.Test;

import freemarker.template.TemplateModelException;

public class PostmarkMailSenderTest {
    
    private PostmarkMailSender sender;

    @Before
    public void setUp() throws TemplateModelException {
        Properties properties = new Properties();
        properties.setProperty(PostmarkMailSender.POSTMARK_API_KEY, "POSTMARK_API_TEST");
        DeploymentConfiguration config = new DeploymentConfiguration(properties);
        
        TemplateModule templateModule = new TemplateModule();
        
        sender = new PostmarkMailSender(config, templateModule.provideConfiguration());
    }
    
    @Test
    public void textEmail() {
        User user = new User();
        user.setChangePasswordKey("xyz123");
        user.setName("Alex");
        user.setEmail("akbertram@gmail.com");

        ResetPasswordMessage model = new ResetPasswordMessage(user, Domain.DEFAULT);
        sender.send(model);
    }
    
    
    
}
