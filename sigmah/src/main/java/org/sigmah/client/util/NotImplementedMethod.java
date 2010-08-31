package org.sigmah.client.util;

import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.MessageBox;

public final class NotImplementedMethod {

    private NotImplementedMethod() {
    }

    /**
     * Informs the user that a feature isn't yet implemented.
     */
    public static void methodNotImplemented() {
        MessageBox.info(I18N.CONSTANTS.featureNotImplemented(), I18N.CONSTANTS.featureNotImplementedDetails(), null);
    }

}
