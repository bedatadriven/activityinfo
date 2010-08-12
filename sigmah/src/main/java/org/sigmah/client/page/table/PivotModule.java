/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author Alex Bertram
 */
public class PivotModule extends AbstractGinModule {
    @Override
    protected void configure() {
        // bind views to widgets
        bind(PivotPresenter.View.class).to(PivotPage.class);
    }
}
