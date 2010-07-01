/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import com.google.gwt.inject.client.AbstractGinModule;

public class ProjectModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(ProjectListPresenter.View.class).to(ProjectListView.class);
    }
}
