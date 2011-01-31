package org.sigmah.server.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.server.dao.hibernate.UserDAOImpl;
import org.sigmah.server.mail.Invitation;
import org.sigmah.server.mail.Mailer;
import org.sigmah.server.util.LocaleHelper;
import org.sigmah.shared.dao.UserDAO;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.profile.OrgUnitProfile;
import org.sigmah.shared.domain.profile.Profile;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.inject.Inject;
/**
 * Create user policy.
 * 
 * @author nrebiai
 * 
 */
public class UserPolicy implements EntityPolicy<User> {
	
	private final EntityManager em;
	private final UserDAO userDAO;
	private final Mailer<Invitation> inviteMailer;
	private static final Log log = LogFactory.getLog(UserPolicy.class);
	
	
	@Inject
    public UserPolicy(EntityManager em, UserDAO userDAO, Mailer<Invitation> inviteMailer) {
        this.em = em;
        this.userDAO = userDAO;
        this.inviteMailer = inviteMailer;
    }

	@Override
	public Object create(User executingUser, PropertyMap properties) {
		log.debug("UserPolicy : init");
		User userToPersist  = null;
		OrgUnitProfile orgUnitProfileToPersist = null;
		
		//get User that need to be saved from properties	
		String email = properties.get("email");
		String name = properties.get("name");
		String firstName = properties.get("firstName");
		String locale = properties.get("locale");
		int orgUnitId = properties.get("orgUnit");
		List<Integer> profilesIds = properties.get("profiles");
		
		//Save user
		if(email != null && name != null){			
			log.debug("UserPolicy : email & name " + email + " " + name);
	        if (userDAO.doesUserExist(email)) {
	        	userToPersist = userDAO.findUserByEmail(email);
	        	log.debug("UserPolicy : User does exist");
	        }
	        if (userToPersist == null) {
	        	log.debug("UserPolicy : User does not exist");
	        	
	        	userToPersist = UserDAOImpl.createNewUser(email, name, locale);	
				userToPersist.setFirstName(firstName);
				userToPersist.setOrganization(executingUser.getOrganization());
	        	log.debug("UserPolicy : User persist..." + User.getUserCompleteName(userToPersist));
	        	userDAO.persist(userToPersist);
	        	log.debug("UserPolicy : User mail invitation...");
	        	try {
		    		inviteMailer.send(new Invitation(userToPersist, executingUser), LocaleHelper.getLocaleObject(executingUser));
		        } catch (Exception e) {
		            // ignore, don't abort because mail didn't work
		        }
		        
		        if(userToPersist.getId() != 0){
		        	log.debug("User Policy : User id = " + userToPersist.getId());
		        	orgUnitProfileToPersist = new OrgUnitProfile();
		        	
		        	OrgUnit orgUnit = em.find(OrgUnit.class, orgUnitId);
		        	if(orgUnit != null){
		        		log.debug("User Policy : org unit found for id = " + orgUnitId);
		        		orgUnitProfileToPersist.setOrgUnit(orgUnit);
			        	
		        		List<Profile> profilesToPersist = new ArrayList<Profile>();
			        	for(int profileId : profilesIds){
			        		Profile profile = em.find(Profile.class, profileId);
			        		profilesToPersist.add(profile);
			        	}
			        	orgUnitProfileToPersist.setProfiles(profilesToPersist);
			        	orgUnitProfileToPersist.setUser(userToPersist);
			        	
			        	em.persist(orgUnitProfileToPersist);
			        	if(orgUnitProfileToPersist.getId() != 0){
			        		userToPersist.setOrgUnitWithProfiles(orgUnitProfileToPersist);
			        		userToPersist = em.merge(userToPersist);
			        	}
		        	}
		        	
		        }
	        }else{
	        	//update
	        	log.debug("UserPolicy : User exist and id is : ");
	        	
	        }
		}else{
			log.debug("UserPolicy : email & name are null");
		}
		
		return userToPersist;
	}

	@Override
	public void update(User user, Object entityId, PropertyMap changes) {
		// TODO Auto-generated method stub
		
	}

	public BaseModelData createDraft(Map<String, Object> properties) {
		// TODO Auto-generated method stub
		return null;
	}

}
