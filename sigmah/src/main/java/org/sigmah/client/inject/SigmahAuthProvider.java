/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.inject;

import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.shared.dto.profile.ProfileDTO;
import org.sigmah.shared.dto.profile.ProfileUtils;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Provider;
import java.util.MissingResourceException;

/**
 * Retrieves the current authenticated user.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class SigmahAuthProvider implements Provider<Authentication> {

    public static final String DICTIONARY_NAME = "SigmahParams";

    public static final String USER_ID = "connectedUserId";
    public static final String USER_TOKEN = "connectedUserAuthToken";
    public static final String USER_EMAIL = "connectedUserEmail";
    public static final String USER_NAME = "connectedUserName";
    public static final String USER_FIRST_NAME = "connectedUserFirstName";
    public static final String USER_ORG_ID = "connectedUserOrgId";
    public static final String USER_ORG_UNIT_ID = "connectedUserOrgUnitId";
    public static final String SHOW_MENUS = "showActivityInfoMenus";
    public static final String USER_AG_PROFILE = "connectedUserProfile";

    /**
     * Retrieves the authentication object.
     * 
     * @return An authentication object if the user is connected,
     *         <code>null</code> otherwise.
     */
    @Override
    public Authentication get() {
        Authentication auth = null;

        try {
            final Dictionary sigmahParams = Dictionary.getDictionary(DICTIONARY_NAME);

            final String userId = sigmahParams.get(USER_ID);
            if (userId != null) {

                auth = new Authentication();

                auth.setUserId((Integer.parseInt(userId)));
                auth.setAuthToken(sigmahParams.get(USER_TOKEN));
                auth.setEmail(sigmahParams.get(USER_EMAIL));

                auth.setOrganizationId(Integer.parseInt(sigmahParams.get(USER_ORG_ID)));
                auth.setOrgUnitId(Integer.parseInt(sigmahParams.get(USER_ORG_UNIT_ID)));
                auth.setShowMenus(Boolean.parseBoolean(sigmahParams.get(SHOW_MENUS)));

                auth.setUserName(sigmahParams.get(USER_NAME));
                auth.setUserFirstName(sigmahParams.get(USER_FIRST_NAME));

                final String aggregatedProfileAsString = sigmahParams.get(USER_AG_PROFILE);
                final ProfileDTO aggregatedProfile = ProfileUtils.readProfile(aggregatedProfileAsString);
                auth.setAggregatedProfile(aggregatedProfile);
                if (Log.isDebugEnabled()) {
                    Log.debug("[get] String representation of the profile: " + aggregatedProfileAsString);
                    Log.debug("[get] Reads aggreated profile: " + aggregatedProfile);
                }
            }

        } catch (MissingResourceException e) {
            return null;
        }

        return auth;
    }
}
