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

import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.Domain;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.login.model.HostPageModel;
import org.junit.Test;

public class HostViewTest extends ViewTestCase {

    @Test
    public void templateProcesses() {

        User user = new User();
        user.setName("Alex");
        user.setEmail("akbertram@gmail.com");
        user.setLocale("fr");

        Authentication auth = new Authentication(user);
        auth.setId("XYZ12345");
        auth.setUser(user);

        HostPageModel pageModel = new HostPageModel(
            "http://www.activityinfo.org");
        pageModel.setMapsApiKey("XYZ123");
        assertProcessable(pageModel);
    }
}
