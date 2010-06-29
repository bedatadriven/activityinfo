/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
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
