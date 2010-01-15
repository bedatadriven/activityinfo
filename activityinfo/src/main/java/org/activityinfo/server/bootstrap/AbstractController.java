package org.activityinfo.server.bootstrap;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.activityinfo.server.bootstrap.exception.IncompleteFormException;
import org.activityinfo.server.bootstrap.model.PageModel;
import org.activityinfo.server.dao.AuthenticationDAO;
import org.activityinfo.server.dao.Transactional;
import org.activityinfo.server.dao.UserDAO;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.exception.InvalidLoginException;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Serves either the initial login page, or the GWT host page depending on
 * authentication status.
 * <p/>
 * The servlet uses FreeMarker (http://www.freemarker.org) to generate content
 * based on the templates in /war/ftl
 * <p/>
 * Using a static login page, rather than incorporating login into the
 * app itself simplifies the app a bit, allows browsers to remember username/passwords,
 * and allows us to load the correct, localized, permutation of ActivityInfo depending
 * on the user's choice of locale.
 *
 * @author Alex Bertram
 */
@Singleton
public class AbstractController extends HttpServlet {

    protected final Injector injector;
    private final Configuration templateCfg;

    public static final String AUTH_TOKEN_COOKIE = "authToken";

    @Inject
    public AbstractController(Injector injector, Configuration templateCfg) {
        this.injector = injector;
        this.templateCfg = templateCfg;
    }

    /**
     * Calls doGet. Wrapped in package visibility for testing.
     */
    void callGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }

    /**
     * Calls doPost. Wrapped in package visibilty for testing.
     */
    void callPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doPost(req, resp);
    }

    protected void writeView(HttpServletResponse response, PageModel model) throws IOException {
        Template template = templateCfg.getTemplate(model.getTemplateName());
        response.setContentType("text/html");
        try {
            template.process(model, response.getWriter());
        } catch (TemplateException e) {
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }

    protected String getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    protected void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    protected void assertParameterPresent(String value) {
        if (value == null || value.length() == 0)
            throw new IncompleteFormException();
    }

    protected User findUserByEmail(String email) throws InvalidLoginException {
        try {
            UserDAO userDAO = injector.getInstance(UserDAO.class);
            return userDAO.findUserByEmail(email);

        } catch (NoResultException e) {
            throw new InvalidLoginException();
        }
    }

    protected void createAuthCookie(HttpServletRequest req, HttpServletResponse resp, User user, boolean remember) throws IOException {
        Authentication auth = createNewAuthToken(user);
        addAuthCookie(resp, auth, remember);
    }

    @Transactional
    protected Authentication createNewAuthToken(User user) {
        Authentication auth = new Authentication(user);
        AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
        authDAO.persist(auth);
        return auth;
    }

    protected void addAuthCookie(HttpServletResponse response, Authentication auth, boolean remember) {
        Cookie cookie = new Cookie(AUTH_TOKEN_COOKIE, auth.getId());
        cookie.setMaxAge(remember ? 30 * 24 * 60 * 60 : -1);
        response.addCookie(cookie);
    }

    protected <T extends AbstractController> void delegateGet(Class<T> controllerClass,
                                                              HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        AbstractController controller = injector.getInstance(controllerClass);
        controller.doGet(req, resp);
    }

    protected <T extends AbstractController> void delegatePost(Class<T> controllerClass,
                                                               HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        AbstractController controller = injector.getInstance(controllerClass);
        controller.doPost(req, resp);
    }

    String parseUrlSuffix(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        if (req.getParameter("gwt.hosted") != null && req.getParameter("gwt.hosted").length() != 0)
            sb.append("?gwt.hosted=").append(req.getParameter("gwt.hosted"));

        int hash = req.getRequestURL().lastIndexOf("/#");
        if (hash != -1)
            sb.append(req.getRequestURL().substring(hash + 1));
        return sb.toString();
    }
}
