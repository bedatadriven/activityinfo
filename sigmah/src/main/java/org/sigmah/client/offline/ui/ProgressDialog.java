/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import org.sigmah.client.i18n.I18N;

class ProgressDialog extends Dialog {
    private ProgressBar progressBar;

    public ProgressDialog() {
        setHeading(I18N.CONSTANTS.statusOfflineMode());
        setWidth(400);
        setHeight(75);
        setLayout(new CenterLayout());

        progressBar = new ProgressBar();
        progressBar.setWidth(325);
        add(progressBar);
    }

    @Override
    protected void createButtons() {
        // no buttons
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
