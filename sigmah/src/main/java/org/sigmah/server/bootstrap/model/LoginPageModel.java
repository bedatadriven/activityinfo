/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;

public class LoginPageModel extends PageModel {
    private String urlSuffix;
    private boolean loginError;         

    public LoginPageModel() {
        this.urlSuffix = "";
    }

    public LoginPageModel(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public static LoginPageModel unsuccessful(String urlSuffix) {
        LoginPageModel model = new LoginPageModel(urlSuffix);
        model.loginError = true;
        return model;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public boolean isLoginError() {
        return loginError;
    }
}
