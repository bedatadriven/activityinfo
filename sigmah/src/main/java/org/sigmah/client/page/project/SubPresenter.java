/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import com.extjs.gxt.ui.client.widget.Component;

/**
 * Describes a sub-presenter of the Project page.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public interface SubPresenter {
    public Component getView();
    public void viewDidAppear();
}
