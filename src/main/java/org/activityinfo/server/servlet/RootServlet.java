package org.activityinfo.server.servlet;

import com.google.inject.Singleton;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;

import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.service.Authenticator;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.InvalidLoginException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/*
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

        if("logout".equals(request.getQueryString())) {
            removeCookie(response, "authToken");
            removeCookie(response, "email");
            showLogin(response, "");
        } else {

            // Check for the presence of the authentication token
            Authentication auth = getAuth(request);
            if(auth == null) {
                showLogin(response, "");
            } else {
                loadApp(response, auth);
            }
        }
    }

    private void showLogin(HttpServletResponse response, String message) throws IOException {
        // create our "model"
        HashMap<String,Object> model = new HashMap<String, Object>();
        model.put("loginError", message);

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

    private void loadApp(HttpServletResponse response, Authentication auth) throws IOException {
        // load the app directly
        response.setContentType("text/html");

        Template template = templateCfg.getTemplate("hostpage.ftl");
        try {
            template.process(auth, response.getWriter());
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
        boolean remember = "true".equals(request.getParameter("remember"));

        Authenticator author = injector.getInstance(Authenticator.class);
        try {
            Authentication auth;
            auth = author.authenticate(email, password);

            Cookie cookie = new Cookie("authToken", auth.getId());
            cookie.setMaxAge(remember ? 30*24*60*60 : -1);
            response.addCookie(cookie);

            response.sendRedirect("/");


        } catch (InvalidLoginException e) {

            showLogin(response, "Le nom d'utilisateur ou le mot de passe que vous avez saisi est incorrect.");

        }
    }

}
