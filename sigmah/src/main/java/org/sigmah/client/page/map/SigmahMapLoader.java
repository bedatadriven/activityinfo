/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.PageStateSerializer;

/**
 * This version of the MapLoader uses SigmahInjector instead of the AppInjector.
 * @author Alex Bertram (akbertram@gmail.com)
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class SigmahMapLoader extends MapLoader {

    @Inject
    public SigmahMapLoader(NavigationHandler pageManager, PageStateSerializer placeSerializer, Provider<MapPresenter> mapPageProvider) {
        super(pageManager, placeSerializer, mapPageProvider);
    }
}