/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.ui;

import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;

/**
 * VBoxLayout that can be customized through css styles.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class StylableVBoxLayout extends VBoxLayout {
    private String customStyle;

    public StylableVBoxLayout(String customStyle) {
        this.customStyle = customStyle;
    }

    @Override
    protected void initTarget() {
        super.initTarget();
        target.addStyleName(customStyle);
    }
}
