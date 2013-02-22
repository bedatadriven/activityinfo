package org.activityinfo.server.bootstrap.model;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
