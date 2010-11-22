/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.inject;

import org.sigmah.client.dispatch.remote.Authentication;

import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Provider;
import java.util.MissingResourceException;

/**
 * Retrieves the current authenticated user.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class SigmahAuthProvider implements Provider<Authentication> {

    /**
     * Retrieves the authentication object.
     * @return An authentication object if the user is connected, <code>null</code> otherwise.
     */
    @Override
    public Authentication get() {
        Authentication auth = null;

        try {
            final Dictionary sigmahParams = Dictionary.getDictionary("SigmahParams");

            final String userId = sigmahParams.get("connectedUserId");
            if(userId != null) {
                auth = new Authentication(
                        Integer.parseInt(userId),
                        sigmahParams.get("connectedUserAuthToken"),
                        sigmahParams.get("connectedUserEmail"));

                auth.setOrganizationId(Integer.parseInt(sigmahParams.get("connectedUserOrgId")));
            }

        } catch (MissingResourceException e) {
            return null;
        }

        return auth;
    }
}
