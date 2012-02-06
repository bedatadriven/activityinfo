package org.sigmah.server.bootstrap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sigmah.server.authentication.AuthCookieUtil;
import org.sigmah.server.bootstrap.exception.IncompleteFormException;
import org.sigmah.server.bootstrap.exception.InvalidKeyException;
import org.sigmah.server.bootstrap.model.ChangePasswordPageModel;
import org.sigmah.server.bootstrap.model.InvalidInvitePageModel;
import org.sigmah.server.database.hibernate.dao.Transactional;
import org.sigmah.server.database.hibernate.entity.Authentication;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.util.logging.LogException;

import com.google.common.base.Preconditions;
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

            writeView(resp, new ChangePasswordPageModel(user));

        } catch (InvalidKeyException e) {
            writeView(resp, new InvalidInvitePageModel());
        }

	}
    @Override
    @LogException(emailAlert = true)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = null;
        try {
            user = findUserByKey(req.getParameter("key"));
        } catch (InvalidKeyException e) {
            writeView(resp, new InvalidInvitePageModel());
        }

        try {
            processForm(req, resp, user);
            resp.sendRedirect(HostController.ENDPOINT);

        } catch (IncompleteFormException e) {
            writeView(resp, new ChangePasswordPageModel(user));
        }
    }

    @Transactional
    private void processForm(HttpServletRequest req, HttpServletResponse resp, User user) {
    	Preconditions.checkNotNull(user, "user");
    	
        changePassword(req, user);

        Authentication auth = createNewAuthToken(user);
        AuthCookieUtil.addAuthCookie(resp, auth, false);
    }


    @Transactional
    protected void changePassword(HttpServletRequest request, User user) throws IncompleteFormException {
    	Preconditions.checkNotNull(user, "user");
        String password = getRequiredParameter(request, "password");         
        user.changePassword(password);
        user.clearChangePasswordKey();
        user.setNewUser(false);
    }


}
