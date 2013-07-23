package org.activityinfo.server.login.model;


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

public class SignUpPageModel extends PageModel {
    // used for form population after a continuable message
    private String email = "";
    private String name = "";
    private String organization = "";
    private String jobtitle = "";
    private String locale = "";

    // messages
    private boolean formError;
    private boolean genericError;
    private boolean confirmationEmailSent;

    public SignUpPageModel() {
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getOrganization() {
        return organization;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public String getLocale() {
        return locale;
    }

    public SignUpPageModel set(String email, String name, String organization, String jobtitle, String locale) {
        this.email = email;
        this.name = name;
        this.organization = organization;
        this.jobtitle = jobtitle;
        this.locale = locale;
        return this;
    }

    public boolean isFormError() {
        return formError;
    }

    public boolean isGenericError() {
        return genericError;
    }

    public boolean isConfirmationEmailSent() {
        return confirmationEmailSent;
    }

    public static SignUpPageModel formErrorModel() {
        SignUpPageModel model = new SignUpPageModel();
        model.formError = true;
        return model;
    }

    public static SignUpPageModel genericErrorModel() {
        SignUpPageModel model = new SignUpPageModel();
        model.genericError = true;
        return model;
    }

    public static SignUpPageModel confirmationEmailSentModel() {
        SignUpPageModel model = new SignUpPageModel();
        model.confirmationEmailSent = true;
        return model;
    }
}
