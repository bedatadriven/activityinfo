package org.activityinfo.server.command.handler.crud;

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

import java.util.Date;

import org.activityinfo.server.database.hibernate.dao.CountryDAO;
import org.activityinfo.server.database.hibernate.dao.UserDatabaseDAO;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;

import com.google.inject.Inject;

public class UserDatabasePolicy implements EntityPolicy<UserDatabase> {

    private final UserDatabaseDAO databaseDAO;
    private final CountryDAO countryDAO;

    @Inject
    public UserDatabasePolicy(UserDatabaseDAO databaseDAO, CountryDAO countryDAO) {
        this.databaseDAO = databaseDAO;
        this.countryDAO = countryDAO;
    }

    @Override
    public Object create(User user, PropertyMap properties) {

        UserDatabase database = new UserDatabase();
        database.setCountry(findCountry(properties));
        database.setOwner(user);

        applyProperties(database, properties);

        databaseDAO.persist(database);

        return database.getId();
    }

    private Country findCountry(PropertyMap properties) {
        int countryId;
        if (properties.containsKey("countryId")) {
            countryId = (Integer) properties.get("countryId");
        } else {
            // this was the default
            countryId = 1;
        }
        return countryDAO.findById(countryId);
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {

    }

    private void applyProperties(UserDatabase database, PropertyMap properties) {

        database.setLastSchemaUpdate(new Date());

        if (properties.containsKey("name")) {
            database.setName((String) properties.get("name"));
        }

        if (properties.containsKey("fullName")) {
            database.setFullName((String) properties.get("fullName"));
        }
    }

}
