/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.inject;

import com.google.inject.Provider;
import org.sigmah.client.dispatch.remote.Authentication;

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
        return new Authentication(3, "844ff3bd2b7e79e5a65365fd6aa38fc0", "akbertrand@gmail.com");
    }

}
