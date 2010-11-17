/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Label;

/**
 * Presenter displaying a "Not implemented yet" screen.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class DummyPresenter implements SubPresenter {
    private Component view;

    @Override
    public Component getView() {
        if(view == null) {
            view = new Label("Not implemented yet");
        }
        return view;
    }

    @Override
    public void viewDidAppear() {
    }

}
