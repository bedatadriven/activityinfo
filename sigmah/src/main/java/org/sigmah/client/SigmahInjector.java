/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.project.ProjectListPresenter;
import org.sigmah.client.page.project.ProjectModule;
import org.sigmah.client.page.project.ProjectPageLoader;

@GinModules({SigmahModule.class, ProjectModule.class})
public interface SigmahInjector extends Ginjector {

    NavigationHandler getNavigationHandler();
    ProjectListPresenter getProjectListPresenter();
    ProjectPageLoader getProjectPageLoader();
    EventBus getEventBus();
}
