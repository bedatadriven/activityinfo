/**
 * The set of pages comprising the Data Entry module
 */
package org.sigmah.client.page.entry;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * Defines binding and intializations for the Data Entry section
 *
 * @author Alex Bertram
 */
public class EntryModule extends AbstractGinModule {

    @Override
    protected void configure() {

        // ensures that the loader is created and plugged in
       // bind(DataEntryLoader.class).asEagerSingleton();

        
    }
}
