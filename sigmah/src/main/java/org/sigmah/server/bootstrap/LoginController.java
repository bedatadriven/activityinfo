/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sigmah.server.bootstrap.model.LoginPageModel;
import org.sigmah.server.mail.MailSender;
import org.sigmah.server.util.logging.LogException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import freemarker.template.Configuration;

@Singleton
public class LoginController extends AbstractController {
    public static final String ENDPOINT = "/login";

    private final MailSender sender;
    
    @Inject
    public LoginController(Injector injector, Configuration templateCfg, MailSender sender) {
        super(injector, templateCfg);
        this.sender = sender;
    }

    @Override
    @LogException(emailAlert = true)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writeView(resp, new LoginPageModel(parseUrlSuffix(req)));
    }

}
