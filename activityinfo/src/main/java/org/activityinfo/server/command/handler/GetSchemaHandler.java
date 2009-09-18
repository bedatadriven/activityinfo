package org.activityinfo.server.command.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.domain.*;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;

import com.google.inject.Inject;


public class GetSchemaHandler implements CommandHandler<GetSchema> {

    private SchemaDAO schemaDAO;
    private Mapper mapper;

    @Inject
    public GetSchemaHandler(SchemaDAO schemaDAO, Mapper mapper) {
        this.schemaDAO = schemaDAO;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetSchema cmd, User user) throws CommandException {

    	Schema schema = new Schema();

    	List<UserDatabase> databases = schemaDAO.getDatabases(user);
    	
    	Map<Integer, CountryModel> countries = new HashMap<Integer, CountryModel>();
    	
		for(UserDatabase database : databases) {
	
			UserDatabaseDTO databaseDTO = new UserDatabaseDTO();
			
			databaseDTO.setId( database.getId() );
			databaseDTO.setName( database.getName() );
			databaseDTO.setFullName( database.getFullName() );
			databaseDTO.setOwnerName( database.getOwner().getName() );
			databaseDTO.setOwnerEmail( database.getOwner().getEmail() );
			
			CountryModel country = countries.get( database.getCountry().getId() );
			if(country == null) {
				country = mapper.map(database.getCountry(), CountryModel.class);
				countries.put(country.getId(), country);
				
				schema.getCountries().add(country);
			}
			databaseDTO.setCountry(country);
			databaseDTO.setAmOwner( database.getOwner().getId() == user.getId() );

			UserPermission permission = null;
			if(!databaseDTO.getAmOwner()) {
				databaseDTO.setMyPartnerId(
							database.getPermissionByUser(user).getPartner().getId());
				
				permission = database.getPermissionByUser(user);
			}
				
			databaseDTO.setViewAllAllowed( databaseDTO.getAmOwner() || permission.isAllowViewAll() );
			databaseDTO.setEditAllowed( databaseDTO.getAmOwner() || permission.isAllowEdit() );
			databaseDTO.setEditAllAllowed( databaseDTO.getAmOwner() || permission.isAllowEditAll() );
			databaseDTO.setDesignAllowed( databaseDTO.getAmOwner() || permission.isAllowDesign() );
				
			for(Partner partner : database.getPartners()) {
				
				databaseDTO.getPartners().add( mapper.map( partner, PartnerModel.class ));
			}

			for(Activity activity : database.getActivities()) {

                ActivityModel activityDTO = mapper.map(activity, ActivityModel.class);
                databaseDTO.getActivities().add(activityDTO);
                activityDTO.setDatabase(databaseDTO);

			}
			
			schema.getDatabases().add(databaseDTO);
			
		}

        return schema;


    }
}
