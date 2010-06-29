/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.servlet.ServletContext;

public class MapIconPathProvider implements Provider<String> {

    private String path;

    @Inject
    public MapIconPathProvider(ServletContext context) {
        path = context.getRealPath("/mapicons");
    }

    public String get() {
        return path;
    }
}
