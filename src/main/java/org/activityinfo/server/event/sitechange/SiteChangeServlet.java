package org.activityinfo.server.event.sitechange;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailMessage;
import org.activityinfo.server.mail.MailSender;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class SiteChangeServlet extends HttpServlet {
	private static final long serialVersionUID = -7693455083421531780L;

	private static final Logger LOGGER = Logger.getLogger(SiteChangeServlet.class.getName());
	
	public static final String ENDPOINT = "/ActivityInfo/event/sitechange";
	public static final String PARAM_SITE = "site";
	public static final String PARAM_USER = "user";
	
	private Provider<EntityManager> entityManager;
	private Provider<MailSender> mailSender;
	
	@Inject
	public SiteChangeServlet(Provider<EntityManager> entityManager, Provider<MailSender> mailSender) {
		this.entityManager = entityManager;
		this.mailSender = mailSender;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		Site site = entityManager.get().find(Site.class, Integer.parseInt(req.getParameter(PARAM_SITE)));
		User editor = entityManager.get().find(User.class, Integer.parseInt(req.getParameter(PARAM_USER)));
		Date date = new Date();
		List<User> recipients = findRecipients(site);
		
		for (User recipient : recipients) {
			LOGGER.fine("sending sitechange message to recipient ["+recipient.getId()+"] for site ["+site.getId()+"]");
			
			MailMessage message = new SiteChangeMessage(recipient, editor, site, date);
			mailSender.get().send(message);
		}
	}
	
	private List<User> findRecipients(Site site) {
		// TODO find real recipients:
		// - those with "Design" priviledge and database owner OR
		// - those users with userpermissions who have the EmailNotifications checked in UserLogin
		List<User> recipients = new ArrayList<User>();
		return recipients;
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request, response);
	}
}
