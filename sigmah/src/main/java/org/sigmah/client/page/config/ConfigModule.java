/**
 * The set of pages comprising the configuration rubrique, including definition and creation
 * of databases, user settings, etc.
 */
package org.sigmah.client.page.config;

import com.google.gwt.inject.client.AbstractGinModule;
import org.sigmah.client.page.config.design.DesignTree;
import org.sigmah.client.page.config.design.Designer;

/**
 * @author Alex Bertram
 */
public class ConfigModule extends AbstractGinModule {

    @Override
    protected void configure() {

        // ensures the loader is created upon initialization and
        // plugged in

        //bind(ConfigLoader.class).asEagerSingleton();

        // binds the view components
        bind(AccountEditor.View.class).to(AccountPanel.class);
        bind(DbListPresenter.View.class).to(DbListPage.class);
        bind(DbUserEditor.View.class).to(DbUserGrid.class);
        bind(DbPartnerEditor.View.class).to(DbPartnerGrid.class);
        bind(Designer.View.class).to(DesignTree.class);
    }
}
