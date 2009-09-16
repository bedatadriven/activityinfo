package org.activityinfo.client.offline;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
/*
 * @author Alex Bertram
 */

public class OfflineMenuButtonNoGears extends Button {

    public OfflineMenuButtonNoGears() {
        super("Activer Mode Hors Connexion", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {

                MessageBox.alert("Mode Hors Connexion", "Le mode hors connexion exige l'installation de" +
                        " <b>Google Gears</b>. Cliquer <a href='http://tools.google.com/gears/' target='_blank'>ici</a>" +
                        " à l'installer. ", null);

            }
        });
    }
}
