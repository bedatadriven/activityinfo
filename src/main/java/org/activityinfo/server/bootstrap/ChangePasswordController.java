package org.activityinfo.server.bootstrap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.authentication.AuthCookieUtil;
import org.activityinfo.server.bootstrap.exception.IncompleteFormException;
import org.activityinfo.server.bootstrap.exception.InvalidKeyException;
import org.activityinfo.server.bootstrap.model.ChangePasswordPageModel;
import org.activityinfo.server.bootstrap.model.InvalidInvitePageModel;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import freemarker.template.Configuration;

@Singleton
public class ChangePasswordController extends AbstractController {

	public static final String ENDPOINT = "/changePassword";

	@Inject
	public ChangePasswordController(Injector injector, Configuration templateCfg) {
		super(injector, templateCfg);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        try {
            User user = findUserByKey(req.getQueryString());

			writeView(resp, req, new ChangePasswordPageModel(user));

        } catch (InvalidKeyException e) {
			writeView(resp, req, new InvalidInvitePageModel());
        }

	}
    @Override
    @LogException(emailAlert = true)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = null;
        try {
            user = findUserByKey(req.getParameter("key"));
        } catch (InvalidKeyException e) {
			writeView(resp, req, new InvalidInvitePageModel());
        }

        try {
            processForm(req, resp, user);
            resp.sendRedirect(HostController.ENDPOINT);

        } catch (IncompleteFormException e) {
			writeView(resp, req, new ChangePasswordPageModel(user));
        }
    }

    @Transactional
    private void processForm(HttpServletRequest req, HttpServletResponse resp, User user) {
        changePassword(req, user);

        Authentication auth = createNewAuthToken(user);
        AuthCookieUtil.addAuthCookie(resp, auth, false);
    }


    @Transactional
    protected void changePassword(HttpServletRequest request, User user) throws IncompleteFormException {
        String password = getRequiredParameter(request, "password");         
        user.changePassword(password);
        user.clearChangePasswordKey();
        user.setNewUser(false);
    }


}
