/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import org.sigmah.server.endpoint.file.FileDownloadServlet;
import org.sigmah.server.endpoint.file.FileUploadServlet;
import org.sigmah.server.endpoint.file.ImageServlet;

import com.google.inject.servlet.ServletModule;
import org.sigmah.server.auth.SigmahAuthDictionaryServlet;

public class GwtRpcModule extends ServletModule {

    @Override
    protected void configureServlets() {
        // The CacheFilter assures that static files marked with
        // .nocache (e.g. strongly named js permutations) get sent with the
        // appropriate cache header so browsers don't ask for it again.
        filter("/ActivityInfo/*").through(CacheFilter.class);
        filter("/Sigmah/*").through(CacheFilter.class);
        filter("/Login/*").through(CacheFilter.class);

        serve("/ActivityInfo/cmd").with(CommandServlet.class);
        serve("/Sigmah/cmd").with(CommandServlet.class);
        serve("/ActivityInfo/download").with(DownloadServlet.class);

        // this is here for now but should be probably live elsewhere, if
        // we really need it at all
        serve("/icon").with(MapIconServlet.class);

        serve("/Login/service").with(LoginServiceServlet.class);

        // Sigmah services
        serve("/SigmahAuthToken").with(SigmahAuthDictionaryServlet.class); // Authentication
        serve("/Sigmah/upload").with(FileUploadServlet.class); // Files upload.
        serve("/Sigmah/download").with(FileDownloadServlet.class); // Files download.
        serve("/Sigmah/image-provider").with(ImageServlet.class); // Image provider.
    }
}
