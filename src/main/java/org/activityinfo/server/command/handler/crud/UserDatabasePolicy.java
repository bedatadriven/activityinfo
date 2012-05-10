/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler.crud;

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
            // this was the defaul
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
