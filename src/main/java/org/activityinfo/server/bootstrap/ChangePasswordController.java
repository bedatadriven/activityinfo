package org.activityinfo.server.bootstrap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.activityinfo.server.authentication.AuthCookieUtil;
import org.activityinfo.server.bootstrap.exception.IncompleteFormException;
import org.activityinfo.server.bootstrap.exception.InvalidKeyException;
import org.activityinfo.server.bootstrap.model.ChangePasswordPageModel;
import org.activityinfo.server.bootstrap.model.InvalidInvitePageModel;
import org.activityinfo.server.bootstrap.model.Redirect;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;

@Path(ChangePasswordController.ENDPOINT)
public class ChangePasswordController extends AbstractController {
	public static final String ENDPOINT = "/changePassword";

	@GET
	public Response onGet(@Context HttpServletRequest req) throws Exception {
        try {
            User user = findUserByKey(req.getQueryString());

			return writeView(req, new ChangePasswordPageModel(user));
        } catch (InvalidKeyException e) {
			return writeView(req, new InvalidInvitePageModel());
        }
	}
	
	@POST
    @LogException(emailAlert = true)
    public Response onPost(@Context HttpServletRequest req) throws IOException, ServletException {
        User user = null;
        try {
            user = findUserByKey(req.getParameter("key"));
        } catch (InvalidKeyException e) {
			return writeView(req, new InvalidInvitePageModel());
        }

        try {
        	ResponseBuilder response = Response.ok(new Redirect(HostController.ENDPOINT));
        	
            processForm(req, response, user);
            
			return response.build();
        } catch (IncompleteFormException e) {
			return writeView(req, new ChangePasswordPageModel(user));
        }
    }

    @Transactional
    private void processForm(HttpServletRequest req, ResponseBuilder response, User user) {
        changePassword(req, user);

        Authentication auth = createNewAuthToken(user);
        
        AuthCookieUtil.addAuthCookie(response, auth, false);
    }


    @Transactional
    protected void changePassword(HttpServletRequest request, User user) throws IncompleteFormException {
        String password = getRequiredParameter(request, "password");         
        user.changePassword(password);
        user.clearChangePasswordKey();
        user.setNewUser(false);
    }
}
