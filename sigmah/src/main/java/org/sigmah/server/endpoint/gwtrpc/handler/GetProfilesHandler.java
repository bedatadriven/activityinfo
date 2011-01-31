package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetProfiles;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProfileListResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.profile.Profile;
import org.sigmah.shared.dto.profile.ProfileDTO;
import org.sigmah.shared.dto.profile.ProfileDTOLight;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetProfilesHandler implements CommandHandler<GetProfiles> {

	private static final Log log = LogFactory.getLog(GetProfilesHandler.class);
	
	private final EntityManager em;
	private final Mapper mapper;
	
	@Inject
    public GetProfilesHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }
			
	@SuppressWarnings("unchecked")
	@Override
	public CommandResult execute(GetProfiles cmd, User user)
			throws CommandException {
		List<ProfileDTOLight> profiles = new ArrayList<ProfileDTOLight>();
		
		final Query query = em.createQuery("SELECT p FROM Profile p ORDER BY p.name");
		
		final List<Profile> resultProfiles = (List<Profile>) query.getResultList();
		
		if(resultProfiles != null){
			for(final Profile oneProfile : resultProfiles){
				ProfileDTOLight profile = mapper.map(oneProfile, ProfileDTOLight.class);
				profiles.add(profile);
			}
		}
		
		return new ProfileListResult(profiles);
	}

}
