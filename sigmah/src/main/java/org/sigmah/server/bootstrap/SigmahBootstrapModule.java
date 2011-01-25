/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import com.google.inject.servlet.ServletModule;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class SigmahBootstrapModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/" + SigmahHostController.ENDPOINT).with(SigmahHostController.class);
    }
    
}
