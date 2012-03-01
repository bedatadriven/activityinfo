/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;


/**
 * @author Alex Bertram
 */
public class ResetPasswordPageModel extends PageModel {

    private boolean loginError;     
    private boolean emailSent;
    private boolean emailError;

    public ResetPasswordPageModel() {
    }

	public boolean isLoginError() {
		return loginError;
	}

	public void setLoginError(boolean loginError) {
		this.loginError = loginError;
	}

	public boolean isEmailSent() {
		return emailSent;
	}

	public void setEmailSent(boolean emailSent) {
		this.emailSent = emailSent;
	}

	public boolean isEmailError() {
		return emailError;
	}

	public void setEmailError(boolean emailError) {
		this.emailError = emailError;
	}

    
    
}
