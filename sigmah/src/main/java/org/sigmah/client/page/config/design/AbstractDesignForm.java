/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config.design;

import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;

/**
 * Base class for design forms 
 *
 * @author Alex Bertram
 */
abstract class AbstractDesignForm extends FormPanel {


    public abstract FormBinding getBinding();

    public int getPreferredDialogWidth() {
        return 450;
    }

    public int getPreferredDialogHeight() {

        return 300;

    }
}
