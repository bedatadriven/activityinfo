/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sigmah.server.Cookies;
import org.sigmah.server.auth.impl.PasswordHelper;
import org.sigmah.server.bootstrap.exception.IncompleteFormException;
import org.sigmah.server.bootstrap.exception.InvalidKeyException;
import org.sigmah.server.bootstrap.model.ConfirmInvitePageModel;
import org.sigmah.server.bootstrap.model.InvalidInvitePageModel;
import org.sigmah.server.database.hibernate.dao.Transactional;
import org.sigmah.server.database.hibernate.entity.Authentication;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.util.logging.LogException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import freemarker.template.Configuration;


@Singleton
public class ConfirmInviteController extends AbstractController {
    public static final String ENDPOINT = "/confirm";

    @Inject
    public ConfirmInviteController(Injector injector, Configuration templateCfg) {
        super(injector, templateCfg);
    }

    @Override
    @LogException(emailAlert = true)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = findUserByKey(req.getQueryString());
            ConfirmInvitePageModel model = new ConfirmInvitePageModel(user);

            writeView(resp, model);

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
            writeView(resp, ConfirmInvitePageModel.incompleteForm(user));
        }
    }

    @Transactional
    private void processForm(HttpServletRequest req, HttpServletResponse resp, User user) {
        confirmUserProfile(req, user);

        Authentication auth = createNewAuthToken(user);
        Cookies.addAuthCookie(resp, auth, false);
    }


    @Transactional
    protected void confirmUserProfile(HttpServletRequest request, User user) throws IncompleteFormException {
        String name = getRequiredParameter(request, "name");
        String locale = getRequiredParameter(request, "locale");
        String password = getRequiredParameter(request, "password");

        user.setName(name);
        user.setLocale(locale);
         
        user.setHashedPassword(PasswordHelper.hashPassword(password));
        user.clearChangePasswordKey();
        user.setNewUser(false);
    }
   
}
