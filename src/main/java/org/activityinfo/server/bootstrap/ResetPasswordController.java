/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.activityinfo.server.authentication.SecureTokenGenerator;
import org.activityinfo.server.bootstrap.model.ResetPasswordPageModel;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.ResetPasswordMessage;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.exception.InvalidLoginException;

import com.google.inject.Inject;

@Path(ResetPasswordController.ENDPOINT)
public class ResetPasswordController extends AbstractController {
	public static final String ENDPOINT = "/loginProblem";

	@Inject
	private MailSender mailer;

	@GET
	@LogException(emailAlert = true)
	public Response onGet(@Context HttpServletRequest req)
			throws ServletException, IOException {
		return writeView(req, new ResetPasswordPageModel());
	}

	@POST
    @LogException(emailAlert = true)
    @Transactional
    public Response onPost(@Context HttpServletRequest req) throws ServletException, IOException {
        try {
            User user = findUserByEmail(req.getParameter("email"));
            user.setChangePasswordKey(SecureTokenGenerator.generate());
            user.setDateChangePasswordKeyIssued(new Date());
            
            mailer.send(new ResetPasswordMessage(user));
                        
        	ResetPasswordPageModel model = new ResetPasswordPageModel();
        	model.setEmailSent(true);
        	
			return writeView(req, model);
        } catch (InvalidLoginException e) {
        	ResetPasswordPageModel model = new ResetPasswordPageModel();
        	model.setLoginError(true);
        	
			return writeView(req, model);
        } catch (Exception e) {
        	ResetPasswordPageModel model = new ResetPasswordPageModel();
        	model.setEmailError(true);
        	
			return writeView(req, model);
		}
    }
}
