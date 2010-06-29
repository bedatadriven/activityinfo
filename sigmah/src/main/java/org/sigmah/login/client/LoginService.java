/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.login.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface LoginService extends RemoteService {

    /**
     * Attempts to establish an authentication and locale cookies for the given
     * user. 
     *
     * @param email
     * @param password
     * @throws LoginException
     */
    LoginResult login(String email, String password) throws LoginException;

}
