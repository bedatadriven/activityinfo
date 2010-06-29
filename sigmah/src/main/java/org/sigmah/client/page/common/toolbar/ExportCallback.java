/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.toolbar;

import org.sigmah.shared.command.RenderElement;

public interface ExportCallback {

    public void export(RenderElement.Format format);
}
