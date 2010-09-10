/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.inject;

import org.sigmah.client.dispatch.remote.Authentication;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Provider;

/**
 * Temporary class that simulate an authenticated user.
 * @author rca
 */
public class SigmahAuthProvider implements Provider<Authentication> {
	
    /**
     * Gives a valid authentication object.
     * @return A forged but valid authentication object.
     */
    @Override
    public Authentication get() {
    	// Temporary code to read connected user data
    	Dictionary sigmahParams;
        try {
            sigmahParams = Dictionary.getDictionary("SigmahParams");
            return new Authentication(
                    Integer.parseInt(sigmahParams.get("connectedUserId")),
                    sigmahParams.get("connectedUserAuthToken"),
                    sigmahParams.get("connectedUserEmail"));
        } catch (Exception e) {
            Log.fatal("DictionaryAuthenticationProvider: exception retrieving dictionary 'SigmahParams' from page", e);
            throw new Error();
        }
    }

}
