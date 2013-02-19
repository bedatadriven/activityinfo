/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap.model;

public class LoginPageModel extends PageModel {
    private boolean loginError;         

    public LoginPageModel() {
    }

    public static LoginPageModel unsuccessful() {
        LoginPageModel model = new LoginPageModel();
        model.loginError = true;
        return model;
    }

    public boolean isLoginError() {
        return loginError;
    }
}
