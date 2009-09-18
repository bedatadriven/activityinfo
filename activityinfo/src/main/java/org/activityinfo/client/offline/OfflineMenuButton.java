package org.activityinfo.client.offline;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.inject.Singleton;
import com.google.inject.Inject;

import org.activityinfo.shared.i18n.UIMessages;

@Singleton
public class OfflineMenuButton extends Button {

    @Inject
    public OfflineMenuButton(UIMessages messages) {
        super("Activer Mode Hors Connection");
    }
}
