/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.dao;

import org.sigmah.client.offline.command.handler.GetSitesHandlerLocal;
import org.sigmah.shared.command.handler.GetSitesHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class OfflineEndpointModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GetSitesHandler.class).to(GetSitesHandlerLocal.class).in(Singleton.class);
    }
}
