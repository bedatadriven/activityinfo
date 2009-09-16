package org.activityinfo.client.page.config.design;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.binding.FormBinding;
/*
 * @author Alex Bertram
 */

public abstract class AbstractDesignForm extends FormPanel {


    public abstract FormBinding getBinding();

    public int getPreferredDialogWidth() {
        return 450;
    }

    public int getPreferredDialogHeight() {

        return 300;

    }
}
