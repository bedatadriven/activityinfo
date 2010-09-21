/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.ui;

import com.extjs.gxt.ui.client.util.Theme;

/**
 * Sigmah theme for ExtGWT
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class SigmahTheme extends Theme {
    public static Theme SIGMAH = new SigmahTheme();

    public SigmahTheme() {
        super("sigmah", "Sigmah", "gxt/themes/sigmah/css/xtheme-sigmah.css");
    }
}
