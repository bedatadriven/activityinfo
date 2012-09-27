/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.endpoint.gwtrpc;

import org.activityinfo.server.attachment.AttachmentServlet;
import org.activityinfo.server.schedule.ReportMailerServlet;

import com.google.inject.servlet.ServletModule;

public class GwtRpcModule extends ServletModule {

    @Override
    protected void configureServlets() {
        // The CacheFilter assures that static files marked with
        // .nocache (e.g. strongly named js permutations) get sent with the
        // appropriate cache header so browsers don't ask for it again.
        filter("/ActivityInfo/*").through(CacheFilter.class);

        serve("/ActivityInfo/cmd").with(CommandServlet.class);
        serve("/Embed/cmd").with(CommandServlet.class);
        serve("/ActivityInfo/download").with(DownloadServlet.class);
        serve("/ActivityInfo/attachment").with(AttachmentServlet.class);
    }
}
