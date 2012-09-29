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
        serve("/ActivityInfo/cmd").with(CommandServlet.class);
        serve("/Embed/cmd").with(CommandServlet.class);
        serve("/ActivityInfo/download").with(DownloadServlet.class);
        serve("/ActivityInfo/attachment").with(AttachmentServlet.class);
    }
}
