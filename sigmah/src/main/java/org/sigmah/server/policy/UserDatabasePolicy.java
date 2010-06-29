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

package org.sigmah.server.policy;

import com.google.inject.Inject;
import org.sigmah.server.dao.CountryDAO;
import org.sigmah.server.dao.UserDatabaseDAO;
import org.sigmah.server.domain.Country;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.UserDatabase;

import java.util.Date;

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
            // this was the defaul
            countryId = 1;
        }
        Country country = countryDAO.findById(countryId);
        return country;
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
