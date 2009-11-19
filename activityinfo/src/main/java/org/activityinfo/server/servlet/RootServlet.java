package org.activityinfo.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.service.Authenticator;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.InvalidLoginException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * Serves either the initial login page, or the GWT host page depending on
 * authentication status.
 *
 * The servlet uses FreeMarker (http://www.freemarker.org) to generate content
 * based on the templates in /war/ftl
 *
 * Using a static login page, rather than incorporating login into the
 * app itself simplifies the app a bit, allows browsers to remember username/passwords,
 * and allows us to load the correct, localized, permutation of ActivityInfo depending
 * on the user's choice of locale.
 *
 * @author Alex Bertram
 */
@Singleton
public class RootServlet extends HttpServlet {


    private final Injector injector;
    private final Configuration templateCfg;

    @Inject
    public RootServlet(Injector injector, Configuration templateCfg) {
        this.injector = injector;
        this.templateCfg = templateCfg;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // there is some sort of enormous problem with IE6 and something
        // to do with the tomcat isapi redirector.
        // When *some* IE6 users access the page through the tomcat redirect,
        // IE6 hangs when images are dynamically added to the page
        // This does not happen when accessed directly through the tomcat server.
        //
        // Until we can figure out what the heck is going on and "fix" the redirector,
        // we are just going to redirect all IE6 users to port 8080

        if(request.getServerPort() == 80 &&
                request.getHeader("User-Agent").indexOf("MSIE 6.0") != -1) {

            StringBuilder directUrl = new StringBuilder();
            directUrl.append("http://");
            directUrl.append(request.getServerName()).append(":8080");
            directUrl.append(request.getRequestURI());
            if(request.getQueryString() != null && request.getQueryString().length() != 0)
                directUrl.append("?").append(request.getQueryString());

            String bookmark = getBookmark(request);
            if(bookmark != null && bookmark.length() != 0)
                directUrl.append("#").append(bookmark);

            response.sendRedirect(directUrl.toString());
            return;
        }

        if("logout".equals(request.getQueryString())) {
            removeCookie(response, "authToken");
            removeCookie(response, "email");
            showLogin(response, "", "");
        } else {

            // Check for the presence of the authentication token
            Authentication auth = getAuth(request);
            if(auth == null) {
                showLogin(response, getBookmark(request), "");
            } else {

                loadApp(response, auth, "offline".equals(request.getQueryString()));
            }
        }
    }

    private String getBookmark(HttpServletRequest req) {
        int hash = req.getRequestURL().lastIndexOf("/#");
        if(hash == -1)
            return "";
        else 
            return req.getRequestURL().substring(hash+2);
    }

    private void showLogin(HttpServletResponse response, String bookmark, String message) throws IOException {
        // create our "model"
        HashMap<String,Object> model = new HashMap<String, Object>();
        model.put("loginError", message);
        model.put("bookmark", bookmark == null ? "" : bookmark);

        // show the login page
        Template template = templateCfg.getTemplate("login.ftl");

        response.setContentType("text/html");
        try {
            template.process(model, response.getWriter());
        } catch (TemplateException e) {
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }

    private void loadApp(HttpServletResponse response, Authentication auth, boolean offline) throws IOException {
        // define model
        Map<String,Object> model = new HashMap<String, Object>();
        model.put("auth", auth);
        model.put("offline", offline);

        // load the app directly
        response.setContentType("text/html");

        Template template = templateCfg.getTemplate("hostpage.ftl");
        try {
            template.process(model, response.getWriter());
        } catch (TemplateException e) {
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());
        }
    }

    protected Authentication getAuth(HttpServletRequest request) {
        String authToken = getCookie(request, "authToken");
        if(authToken != null) {
            Authenticator ator = injector.getInstance(Authenticator.class);
            try {
                return ator.getSession(authToken);

            } catch (InvalidAuthTokenException e) {
                return null;
            }
        }
        return null;
    }

    protected String getCookie(HttpServletRequest request, String name) {
        if(request.getCookies() == null)
            return null;

        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(name)) {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // handle new login

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String bookmark = request.getParameter("bookmark");
        if(bookmark == null) {
            bookmark = "";
        }

        boolean remember = "true".equals(request.getParameter("remember"));

        Authenticator author = injector.getInstance(Authenticator.class);
        try {
            Authentication auth;
            auth = author.authenticate(email, password);

            Cookie cookie = new Cookie("authToken", auth.getId());
            cookie.setMaxAge(remember ? 30*24*60*60 : -1);
            response.addCookie(cookie);

            response.sendRedirect((bookmark.length()==0 ? "" : "#" + bookmark));

        } catch (InvalidLoginException e) {

            // TODO: i18n
            showLogin(response, bookmark, "Le nom d'utilisateur ou le mot de passe que vous avez saisi est incorrect.");

        }
    }

}
