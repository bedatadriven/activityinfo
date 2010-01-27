/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.policy;

import com.google.inject.Inject;
import org.activityinfo.server.dao.CountryDAO;
import org.activityinfo.server.dao.UserDatabaseDAO;
import org.activityinfo.server.domain.Country;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;

import java.util.Date;
import java.util.Map;

public class UserDatabasePolicy implements EntityPolicy<UserDatabase> {

    private final UserDatabaseDAO databaseDAO;
    private final CountryDAO countryDAO;

    @Inject
    public UserDatabasePolicy(UserDatabaseDAO databaseDAO, CountryDAO countryDAO) {
        this.databaseDAO = databaseDAO;
        this.countryDAO = countryDAO;
    }

    @Override
    public Object create(User user, Map<String, Object> properties) {

        UserDatabase database = new UserDatabase();
        database.setCountry(findCountry(properties));
        database.setOwner(user);

        updateDatabaseProperties(database, properties);

        databaseDAO.persist(database);

        return database.getId();
    }

    private Country findCountry(Map<String, Object> properties) {
        int countryId;
        if (properties.containsKey("countryId")) {
            countryId = (Integer) properties.get("countryId");
        } else {
            // this was the defaul
            countryId = 1;
        }
        Country country = countryDAO.findById(countryId);
        return country;
    }

    @Override
    public void update(User user, Object entityId, Map<String, Object> properties) {

    }

    private void updateDatabaseProperties(UserDatabase database, Map<String, Object> properties) {

        database.setLastSchemaUpdate(new Date());

        if (properties.containsKey("name"))
            database.setName((String) properties.get("name"));

        if (properties.containsKey("fullName"))
            database.setFullName((String) properties.get("fullName"));
    }

}
