/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.toolbar;

import org.sigmah.shared.command.RenderElement;

/**
 * Callback to t
 */
public interface ExportCallback {

    /**
     * Called when the user has selected a button in the Export menu.
     *
     * @param format the format in which the 
     */
    public void export(RenderElement.Format format);
}
