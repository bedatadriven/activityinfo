/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.dozer.Mapper;
import org.sigmah.server.dao.UserDatabaseDAO;
import org.sigmah.server.domain.*;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.*;
import org.sigmah.shared.exception.CommandException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetSchema
 */
public class GetSchemaHandler implements CommandHandler<GetSchema> {

    private UserDatabaseDAO userDatabaseDAO;
    private Mapper mapper;

    @Inject
    public GetSchemaHandler(UserDatabaseDAO userDatabaseDAO, Mapper mapper) {
        this.userDatabaseDAO = userDatabaseDAO;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetSchema cmd, User user) throws CommandException {

        SchemaDTO schema = new SchemaDTO();
        Date lastUpdate = new Date(0);

        // Note that hibernate is already filtering by user so it is not necessary to pass the user
        List<UserDatabase> databases = userDatabaseDAO.queryAllUserDatabasesAlphabetically();

        Map<Integer, CountryDTO> countries = new HashMap<Integer, CountryDTO>();

        for (UserDatabase database : databases) {

            if (database.getLastSchemaUpdate().after(lastUpdate)) {
                lastUpdate = database.getLastSchemaUpdate();
            }

            UserDatabaseDTO databaseDTO = new UserDatabaseDTO();

            databaseDTO.setId(database.getId());
            databaseDTO.setName(database.getName());
            databaseDTO.setFullName(database.getFullName());
            databaseDTO.setOwnerName(database.getOwner().getName());
            databaseDTO.setOwnerEmail(database.getOwner().getEmail());

            CountryDTO country = countries.get(database.getCountry().getId());
            if (country == null) {
                country = mapper.map(database.getCountry(), CountryDTO.class);
                countries.put(country.getId(), country);

                schema.getCountries().add(country);
            }
            databaseDTO.setCountry(country);
            databaseDTO.setAmOwner(database.getOwner().getId() == user.getId());

            UserPermission permission = null;
            if (!databaseDTO.getAmOwner()) {
                databaseDTO.setMyPartnerId(
                        database.getPermissionByUser(user).getPartner().getId());

                permission = database.getPermissionByUser(user);

                if (permission.getLastSchemaUpdate().after(lastUpdate)) {
                    lastUpdate = permission.getLastSchemaUpdate();
                }
            }

            databaseDTO.setViewAllAllowed(databaseDTO.getAmOwner() || permission.isAllowViewAll());
            databaseDTO.setEditAllowed(databaseDTO.getAmOwner() || permission.isAllowEdit());
            databaseDTO.setEditAllAllowed(databaseDTO.getAmOwner() || permission.isAllowEditAll());
            databaseDTO.setDesignAllowed(databaseDTO.getAmOwner() || permission.isAllowDesign());
            databaseDTO.setManageUsersAllowed(databaseDTO.getAmOwner() || permission.isAllowManageUsers());
            databaseDTO.setManageAllUsersAllowed(databaseDTO.getAmOwner() || permission.isAllowManageAllUsers());

            for (OrgUnit partner : database.getPartners()) {

                databaseDTO.getPartners().add(mapper.map(partner, PartnerDTO.class));
            }

            for (Activity activity : database.getActivities()) {

                ActivityDTO activityDTO = mapper.map(activity, ActivityDTO.class);
                databaseDTO.getActivities().add(activityDTO);
                activityDTO.setDatabase(databaseDTO);

            }

            schema.getDatabases().add(databaseDTO);

        }

        schema.setVersion(lastUpdate.getTime());

        return schema;


    }
}
