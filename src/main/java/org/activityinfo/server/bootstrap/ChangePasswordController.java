package org.activityinfo.server.bootstrap;

import java.io.IOException;
import java.net.URI;

import javax.inject.Provider;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.bootstrap.exception.IncompleteFormException;
import org.activityinfo.server.bootstrap.model.ChangePasswordPageModel;
import org.activityinfo.server.bootstrap.model.InvalidInvitePageModel;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;

import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(ChangePasswordController.ENDPOINT)
public class ChangePasswordController {
	public static final String ENDPOINT = "/changePassword";

	private final Provider<UserDAO> userDAO;
	
	@Inject
	public ChangePasswordController(Provider<UserDAO> userDAO) {
        super();
        this.userDAO = userDAO;
    }

    @GET
	public Viewable getPage(@Context UriInfo uri) throws Exception {
        try {
            User user = userDAO.get().findUserByChangePasswordKey(uri.getRequestUri().getQuery());
            return new ChangePasswordPageModel(user).asViewable();
        } catch (NoResultException e) {
			return new InvalidInvitePageModel().asViewable();
        }
	}
	
	@POST
    @LogException(emailAlert = true)
    public Response changePassword(@Context UriInfo uri, @FormParam("key") String key, @FormParam("password") String password) throws IOException, ServletException {
        User user = null;
        try {
            user = userDAO.get().findUserByChangePasswordKey(key);
        } catch (NoResultException e) {
			return Response.ok()
			    .entity(new InvalidInvitePageModel().asViewable())
			    .build();
        }

        changePassword(user, password);
        
        URI appUri = uri.getAbsolutePathBuilder().replacePath("/").build();
        
        return Response.seeOther(appUri)
            .build();
    }

    @Transactional
    protected void changePassword(User user, String newPassword) throws IncompleteFormException {
        user.changePassword(newPassword);
        user.clearChangePasswordKey();
        user.setNewUser(false);
    }
}
