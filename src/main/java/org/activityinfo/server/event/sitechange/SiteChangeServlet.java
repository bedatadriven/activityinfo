package org.activityinfo.server.event.sitechange;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.form.SiteRenderer;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.MessageBuilder;
import org.activityinfo.server.util.html.HtmlWriter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.teklabs.gwt.i18n.server.LocaleProxy;

@Singleton
public class SiteChangeServlet extends HttpServlet {
	private static final long serialVersionUID = -7693455083421531780L;

	private static final Logger LOGGER = Logger.getLogger(SiteChangeServlet.class.getName());
	
	public static final String ENDPOINT = "/task/notifysitechange";
	public static final String PARAM_USER = "u";
	public static final String PARAM_SITE = "s";
	
	private Provider<EntityManager> entityManager;
	private Provider<MailSender> mailSender;
	private DispatcherSync dispatcher;
	
	
	@Inject
	public SiteChangeServlet(Provider<EntityManager> entityManager, Provider<MailSender> mailSender, DispatcherSync dispatcher) {
		this.entityManager = entityManager;
		this.mailSender = mailSender;
		this.dispatcher = dispatcher;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		try {
			int userId = Integer.parseInt(req.getParameter(PARAM_USER));
			int siteId = Integer.parseInt(req.getParameter(PARAM_SITE));
			
			sendNotifications(userId, siteId);
			
		} catch (Throwable t) {
			LOGGER.warning("can't complete notify task: "+t.getMessage());
			LOGGER.throwing(this.getClass().getSimpleName(), "doGet", t);
		}
	}
	
	private void sendNotifications(int userId, int siteId) {
		User user = entityManager.get().find(User.class, userId);
		
		SiteResult siteResult = dispatcher.execute(GetSites.byId(siteId));
        SiteDTO siteDTO = siteResult.getData().get(0);
        
		SchemaDTO schemaDTO = dispatcher.execute(new GetSchema());
		ActivityDTO activityDTO = schemaDTO.getActivityById(siteDTO.getActivityId());
		UserDatabaseDTO userDatabaseDTO = activityDTO.getDatabase();

		Date date = new Date();
		
		List<User> recipients = findRecipients(userDatabaseDTO.getId());
		for (User recipient : recipients) {
			try {
				LOGGER.fine("sending sitechange notification email to "+recipient.getName()+" <"+recipient.getEmail()+">");
				Message msg = createLocalizedMessage(recipient, user, date, siteDTO, activityDTO, userDatabaseDTO);
				mailSender.get().send(msg);
			} catch (Throwable t) {
				LOGGER.warning("failed sending notification email to "+recipient.getName()+" <"+recipient.getEmail()+">: "+t.getMessage());
			}
		}
	}
	
	// select owners/designers with the emailNotification flag set to true
	@VisibleForTesting
    @SuppressWarnings("unchecked")
	List<User> findRecipients(int userDatabaseId) {
        Query query = entityManager.get().createNativeQuery(
        		"select u.* from userlogin u" +
        		"left join (" +
        			"select p.userid uid from userpermission p where p.databaseid = ?1 and p.allowdesign = b'1'" +
        			"union" +
        			"select d.owneruserid uid from userdatabase d where d.DatabaseId = ?2" +
        		") su on su.uid = u.userid" +
        		"where u.emailnotification = b'1'",
        		User.class)
                .setParameter(1, userDatabaseId)
                .setParameter(2, userDatabaseId);

		return query.getResultList();
	}

	private Message createLocalizedMessage(User recipient, User editor, Date date, SiteDTO siteDTO, 
			ActivityDTO activityDTO, UserDatabaseDTO userDatabaseDTO) throws MessagingException {
		// set the locale of the messages
		LocaleProxy.setLocale(LocaleHelper.getLocaleObject(recipient));
		
		// create message, set recipient & bcc
		MessageBuilder message = new MessageBuilder();
		message.to(recipient.getEmail(), recipient.getName());
		message.bcc("akbertram@gmail.com");
	    
	    // set the subject
	    message.subject(I18N.MESSAGES.sitechangeSubject(userDatabaseDTO.getName()));
	    
	    // create the html body
	    HtmlWriter htmlWriter = new HtmlWriter();
	
	    htmlWriter.startDocument();
	    htmlWriter.startDocumentBody();
	    
	    String greeting = I18N.MESSAGES.sitechangeGreeting(recipient.getFirstName());
	    htmlWriter.paragraph(greeting);

	    String intro = I18N.MESSAGES.sitechangeIntro(User.getUserCompleteName(editor), editor.getEmail(), userDatabaseDTO.getName(), date);
	    htmlWriter.paragraph(intro);

	    String siteHtml = new SiteRenderer().renderSite(siteDTO, activityDTO, false, true);
	    htmlWriter.paragraph(siteHtml);

	    String signature = I18N.MESSAGES.sitechangeSignature();
	    htmlWriter.paragraph(signature);
	
	    htmlWriter.endDocumentBody();
	    htmlWriter.endDocument();
	
	    message.body(htmlWriter.toString());
	    return message.build();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request, response);
	}
}
