package org.sigmah.server.endpoint.kml;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.sigmah.server.authentication.Authenticator;
import org.sigmah.server.authentication.ServerSideAuthProvider;
import org.sigmah.server.command.DispatcherSync;
import org.sigmah.server.database.hibernate.dao.UserDAO;
import org.sigmah.server.database.hibernate.entity.DomainFilters;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.util.xml.XmlBuilder;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.DatabaseDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Singleton
public class KmlActivityServlet extends HttpServlet {

	@Inject
	private Injector injector;

	@Inject
	private DispatcherSync dispatcher;

	@Inject
	ServerSideAuthProvider authProvider;

	private final Configuration templateCfg;

	@Inject
	public KmlActivityServlet(Configuration templateCfg) {
		this.templateCfg = templateCfg;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// Get Authorization header
		String auth = req.getHeader("Authorization");

		// Do we allow that user?

		User user = authenticate(auth);
		if (user == null) {
			// Not allowed, or no password provided so report unauthorized
			res.setHeader("WWW-Authenticate",
					"BASIC realm=\"Utilisateurs authorises\"");
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		authProvider.set(new AuthenticatedUser("", user.getId(), user
				.getEmail()));

		List<ActivityLink> links = Lists.newArrayList();
		
		List<ActivityDTO> activities = loadActivities(user);
		for(ActivityDTO activity : activities ){
			String link = createLink(activity.getId(), req);
			links.add(new ActivityLink(activity.getName(), link));
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("links", links);

		Template tpl = templateCfg.getTemplate("kml/ActivitiesNetworkLink.kml.ftl");
		res.setContentType("application/vnd.google-earth.kml+xml; filename=ActivityInfo.kml");
		res.setCharacterEncoding("UTF-8");

		try {
			tpl.process(map, res.getWriter());
		} catch (TemplateException e) {
			res.setStatus(500);
			e.printStackTrace();
		}
	}

	// This method checks the user information sent in the Authorization
	// header against the database of users maintained in the users Hashtable.

	protected User authenticate(String auth) throws IOException {
		if (auth == null) {
			return null;
		}// no auth

		if (!auth.toUpperCase().startsWith("BASIC ")) {
			return null;
		}// we only do BASIC

		// Get encoded user and password, comes after "BASIC "
		String emailpassEncoded = auth.substring(6);

		// Decode it, using any base 64 decoder

		byte[] emailpassDecodedBytes = Base64.decodeBase64(emailpassEncoded
				.getBytes());
		String emailpassDecoded = new String(emailpassDecodedBytes,
				Charset.defaultCharset());
		String[] emailPass = emailpassDecoded.split(":");

		if (emailPass.length != 2) {
			return null;
		}

		// look up the user in the database
		UserDAO userDAO = injector.getInstance(UserDAO.class);
		User user = null;
		try {
			user = userDAO.findUserByEmail(emailPass[0]);
		} catch (NoResultException e) {
			return null;
		}

		Authenticator checker = injector.getInstance(Authenticator.class);
		if (!checker.check(user, emailPass[1])) {
			return null;
		}
		return user;

	}

	protected List<ActivityDTO> loadActivities(User user)
			throws CommandException {

		EntityManager em = injector.getInstance(EntityManager.class);
		DomainFilters.applyUserFilter(user, em);

		SchemaDTO schema = dispatcher.execute(new GetSchema());

		List<UserDatabaseDTO> databases= schema.getDatabases();
		List<ActivityDTO> activities = Lists.newArrayList();
		for(UserDatabaseDTO db: databases){
			activities.addAll(db.getActivities());
		}
		
		return activities;
	}

	private String createLink(int activityId, HttpServletRequest req) {
		return "http://" + req.getServerName() + ":" + req.getServerPort()
				+ "/" + req.getRequestURI() + "/data?activityId=" + activityId;
	}

	
	public class ActivityLink{
		
		public String name;
		public String href;
		
		public ActivityLink(String name,String href) {
			this.href = href;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		
	}
	
}
