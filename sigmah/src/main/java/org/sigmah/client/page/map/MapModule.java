/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import com.google.gwt.inject.client.AbstractGinModule;

public class MapModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(MapPresenter.View.class).to(MapPage.class);
    }
}
