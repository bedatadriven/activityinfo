package org.activityinfo.server.entity.auth;

import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.database.hibernate.entity.UserPermission;
import org.activityinfo.shared.auth.AuthenticatedUser;

/**
 * Checks the requesting user's authorization to modify / create a Site
 *
 */
public class SiteAuthorizationHandler implements AuthorizationHandler<Site> {

    @Override
    public boolean isAuthorized(AuthenticatedUser requestingUser, Site site) {
        UserDatabase database = site.getActivity().getDatabase();

        // is the user the owner?
        if(database.getOwner().getId() == requestingUser.getId()) {
            return true;
        }
        
        for(UserPermission permission : database.getUserPermissions()) {
            if(permission.getUser().getId() == requestingUser.getId()) {
                if(permission.isAllowEditAll()) {
                    // user can edit all sites
                    return true;
                } else if(permission.isAllowEdit() && 
                    permission.getPartner().getId() == site.getPartner().getId()) {
                    // the user can edit this partner
                    return true;
                }
            }
        }
        return false;
    }
}
