/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.html;

import org.sigmah.server.report.util.HtmlWriter;
import org.sigmah.shared.report.model.ReportElement;

import java.io.IOException;

public interface HtmlRenderer<T extends ReportElement> {


	public abstract void render(HtmlWriter html, ImageStorageProvider provider, T element) throws IOException;


}
