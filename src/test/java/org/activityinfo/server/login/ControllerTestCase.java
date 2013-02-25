package org.activityinfo.server.login;

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
public abstract class ControllerTestCase {

    protected static final String USER_EMAIL = "alex@bertram.com";
    protected static final String CORRECT_USER_PASS = "mypass";
    protected static final String WRONG_USER_PASS = "notmypass";

    protected static final String NEW_AUTH_TOKEN = "XYZ123";

    protected static final String NEW_USER_KEY = "ABC456";
    protected static final String NEW_USER_EMAIL = "bart@bart.nl";
    protected static final String BAD_KEY = "muwahaha";
    protected static final String NEW_USER_NAME = "Henry";
    protected static final String NEW_USER_CHOSEN_LOCALE = "fr";

    protected static final String GOOD_AUTH_TOKEN = "BXD556";
    protected static final String BAD_AUTH_TOKEN = "NONSENSE";

}
