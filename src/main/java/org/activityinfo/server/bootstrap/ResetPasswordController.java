/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.io.IOException;
import java.util.Date;

import javax.inject.Provider;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.activityinfo.server.authentication.SecureTokenGenerator;
import org.activityinfo.server.bootstrap.model.ResetPasswordPageModel;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.ResetPasswordMessage;
import org.activityinfo.server.util.logging.LogException;

import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(ResetPasswordController.ENDPOINT)
public class ResetPasswordController {
	public static final String ENDPOINT = "/loginProblem";

	@Inject
	private MailSender mailer;

	@Inject
	private Provider<UserDAO> userDAO;
	
	@GET
	@LogException(emailAlert = true)
	public Viewable getPage(@Context HttpServletRequest req)
			throws ServletException, IOException {
		return new ResetPasswordPageModel().asViewable();
	}

	@POST
    @LogException(emailAlert = true)
    @Transactional
    public Viewable resetPassword(@FormParam("email") String email)  {
        try {
            User user = userDAO.get().findUserByEmail(email);
            user.setChangePasswordKey(SecureTokenGenerator.generate());
            user.setDateChangePasswordKeyIssued(new Date());
            
            mailer.send(new ResetPasswordMessage(user));
                        
        	ResetPasswordPageModel model = new ResetPasswordPageModel();
        	model.setEmailSent(true);
        	
			return model.asViewable();
			
        } catch (NoResultException e) {
        	ResetPasswordPageModel model = new ResetPasswordPageModel();
        	model.setLoginError(true);
        	
			return model.asViewable();
			
        } catch (Exception e) {
        	ResetPasswordPageModel model = new ResetPasswordPageModel();
        	model.setEmailError(true);
        	
			return model.asViewable();
		}
    }
}
