/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import javax.inject.Provider;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.bootstrap.model.ConfirmInvitePageModel;
import org.activityinfo.server.bootstrap.model.InvalidInvitePageModel;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(ConfirmInviteController.ENDPOINT)
public class ConfirmInviteController {
    public static final String ENDPOINT = "/confirm";

    private final Provider<UserDAO> userDAO;
    private final AuthTokenProvider authTokenProvider;
    
    @Inject
    public ConfirmInviteController(Provider<UserDAO> userDAO, AuthTokenProvider authTokenProvider) {
        super();
        this.userDAO = userDAO;
        this.authTokenProvider = authTokenProvider;
    }

    @GET
    @LogException(emailAlert = true)
    public Viewable getPage(@Context UriInfo uri) throws Exception {
        try {
            User user = userDAO.get().findUserByChangePasswordKey(uri.getRequestUri().getQuery());
            return new ConfirmInvitePageModel(user).asViewable();

         } catch (NoResultException e) {
			return new InvalidInvitePageModel().asViewable();
        }
    }

    @POST
    @LogException(emailAlert = true)
    public Response confirm(
        @Context UriInfo uri,
        @FormParam("key") String key,
        @FormParam("locale") String locale,
        @FormParam("password") String password,
        @FormParam("name") String name) throws Exception {
        
        User user = null;
        try {
            user = userDAO.get().findUserByChangePasswordKey(key);
            user.setName(checkNonEmpty(name));
            user.setLocale(checkNonEmpty(locale));
            user.changePassword(checkNonEmpty(password));
            user.clearChangePasswordKey();
            user.setNewUser(false);
            return Response
                .seeOther(uri.getAbsolutePathBuilder().replacePath("/").build())
                .cookie(authTokenProvider.createNewAuthCookies(user))
                .build();
                
        } catch (EntityNotFoundException e) {
			return Response.ok(new InvalidInvitePageModel().asViewable()).build();
        } catch (IllegalArgumentException e) {
            return Response.ok(ConfirmInvitePageModel.incompleteForm(user).asViewable()).build();
        }
    }
    
    private String checkNonEmpty(String value) {
        if(Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
