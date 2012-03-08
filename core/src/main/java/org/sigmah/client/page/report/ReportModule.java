/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;

import org.sigmah.client.page.report.json.ReportJsonFactory;
import org.sigmah.client.page.report.json.ReportSerializer;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * GIN module that provides bindings for the Reports section
 *
 * @author Alex Bertram
 */
public class ReportModule extends AbstractGinModule {
    @Override
    protected void configure() {
        // bind views to their implementations
    	bind(ReportSerializer.class).to(ReportJsonFactory.class);
    }
}
