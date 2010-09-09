/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dao.UserDatabaseDAO;
import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.domain.UserPermission;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.DTOMapper;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;


/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetSchema
 */
public class GetSchemaHandler implements CommandHandler<GetSchema> {

    private UserDatabaseDAO userDatabaseDAO;
    private DTOMapper mapper;

    @Inject
    public GetSchemaHandler(UserDatabaseDAO userDatabaseDAO, DTOMapper mapper) {
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
            Log.debug("country: " +country);
            if (country == null) {
            	Log.debug("country.locationTypes " +database.getCountry().getLocationTypes().size());
                country = mapper.map(database.getCountry(), CountryDTO.class);
                countries.put(country.getId(), country);
                schema.getCountries().add(country);
            }
            
            Log.debug("" + country.getLocationTypeById(2));
            
            databaseDTO.setCountry(country);
            databaseDTO.setAmOwner(database.getOwner().getId() == user.getId());

            UserPermission permission = null;
            if (!databaseDTO.getAmOwner()) {
            	// don't support user permission when running in browser
            	if (database.getPermissionByUser(user) != null) {
            		if (database.getPermissionByUser(user).getPartner() != null) {
            			  databaseDTO.setMyPartnerId(database.getPermissionByUser(user).getPartner().getId());
            		}
            	}

                permission = database.getPermissionByUser(user);

                if (permission != null && permission.getLastSchemaUpdate().after(lastUpdate)) {
                    lastUpdate = permission.getLastSchemaUpdate();
                }
            }
            if (permission == null) {
            	// don't support user permission when running in browser
            	permission = new UserPermission();
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
