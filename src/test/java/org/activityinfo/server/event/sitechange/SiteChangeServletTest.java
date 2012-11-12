package org.activityinfo.server.event.sitechange;

import static org.easymock.EasyMock.createMock;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.google.inject.Provider;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SiteChangeServletTest extends CommandTestCase2 {

	@Inject
    private EntityManager entityManager;

	@Test
    public void testSubscriptions() throws Exception {
		SiteChangeServlet underTest = createServlet();
		
		List<User> users = underTest.findRecipients(1);
		assertThat(users.size(), is(equalTo(2)));
		
		User alex = users.get(0);
		assertThat(alex.getName(), is(equalTo("Alex")));
		assertThat(alex.isEmailNotification(), is(true));
		assertThat(alex.getLocale(), is(equalTo("fr")));
		
		User marlene = users.get(1);
		assertThat(marlene.getName(), is(equalTo("Marlene")));
		assertThat(marlene.isEmailNotification(), is(true));
		assertThat(marlene.getLocale(), is(equalTo("en")));
    }

	private SiteChangeServlet createServlet() {
		return new SiteChangeServlet(
				new Provider<EntityManager>() {
					@Override public EntityManager get() { return entityManager; }
				}, 
				new Provider<MailSender>() {
					@Override public MailSender get() { return createMock("mailer", MailSender.class); }
				}, 
				getDispatcherSync());
	}
}
