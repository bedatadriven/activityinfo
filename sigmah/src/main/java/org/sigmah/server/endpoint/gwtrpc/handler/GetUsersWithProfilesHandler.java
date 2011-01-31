package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetUsersWithProfiles;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.UserListResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.profile.Profile;
import org.sigmah.shared.dto.OrgUnitDTO;
import org.sigmah.shared.dto.UserDTO;
import org.sigmah.shared.dto.profile.ProfileDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * Retrieves the organization users list.
 * 
 * @author nrebiai
 * 
 */
public class GetUsersWithProfilesHandler implements CommandHandler<GetUsersWithProfiles> {
	
	private static final Log log = LogFactory.getLog(GetUsersWithProfilesHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetUsersWithProfilesHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }
	
	@Override
    public CommandResult execute(GetUsersWithProfiles cmd, User user) throws CommandException {

        final List<UserDTO> userDTOList = new ArrayList<UserDTO>();

        // Creates selection query.
        final Query query = em.createQuery("SELECT u FROM User u ORDER BY u.name");

        // Gets all users entities.
        @SuppressWarnings("unchecked")
        final List<User> users = (List<User>) query.getResultList();

        // Mapping (entity -> dto).
        if (users != null) {
            for (final User oneUser : users) {
            	UserDTO userDTO = mapper.map(oneUser, UserDTO.class);
            	
            	if(oneUser.getOrgUnitWithProfiles() != null){
            		if(oneUser.getOrgUnitWithProfiles().getOrgUnit() != null)
            			userDTO.setOrgUnitWithProfiles(
            					mapper.map(oneUser.getOrgUnitWithProfiles().getOrgUnit(), OrgUnitDTO.class));            			
            		
            		List<Profile> profiles = oneUser.getOrgUnitWithProfiles().getProfiles();
            		List<ProfileDTO> profilesDTO = new ArrayList<ProfileDTO>();
                	for(final Profile profile : profiles){
                		ProfileDTO profileDTO = new ProfileDTO();
                		profileDTO.set("id", profile.getId());
                		profileDTO.set("name", profile.getName());
                		profilesDTO.add(profileDTO);
                	}
                	log.debug("[execute] Found " + profilesDTO.size() + " profiles for user " + 
                			oneUser.getName());
                	userDTO.setProfilesDTO(profilesDTO);                    
            	}else{
            		log.debug("[execute] No profiles found for user " + 
                			oneUser.getName());
            	}
            	
            	userDTOList.add(userDTO);               
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("[execute] Found " + userDTOList.size() + " users.");
        }

        return new UserListResult(userDTOList);
    }
}
