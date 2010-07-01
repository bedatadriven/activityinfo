/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/**
 * Application icons
 */
package org.sigmah.client.icon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * Provides access to the application's icons through
 * GWT's magic ImageBundle generator.
 */
public interface IconImageBundle extends ImageBundle {
    IconImageBundle ICONS = (IconImageBundle) GWT.create(IconImageBundle.class);

    AbstractImagePrototype add();

    AbstractImagePrototype delete();

    AbstractImagePrototype editPage();

    AbstractImagePrototype save();

    AbstractImagePrototype database();

    AbstractImagePrototype design();

    AbstractImagePrototype addDatabase();

    AbstractImagePrototype editDatabase();

    AbstractImagePrototype excel();

    AbstractImagePrototype activity();

    AbstractImagePrototype addActivity();

    AbstractImagePrototype deleteActivity();

    @Resource(value = "editPage.png")
    AbstractImagePrototype editActivity();

    AbstractImagePrototype user();

    AbstractImagePrototype editUser();

    AbstractImagePrototype addUser();

    AbstractImagePrototype deleteUser();

    /**
     *
     * @return  Icon for a user group
     */
    AbstractImagePrototype group();

    AbstractImagePrototype table();

    AbstractImagePrototype report();

    AbstractImagePrototype sum();

    AbstractImagePrototype curveChart();

    AbstractImagePrototype map();

    AbstractImagePrototype filter();

    @Resource(value = "key.png")
    AbstractImagePrototype login();

    AbstractImagePrototype cancel();

    AbstractImagePrototype barChart();

    @Resource(value = "barChart.png")
    AbstractImagePrototype analysis();

    @Resource(value = "keyboard.png")
    AbstractImagePrototype dataEntry();

    @Resource(value = "ruler.png")
    AbstractImagePrototype indicator();

    AbstractImagePrototype attributeGroup();

    AbstractImagePrototype attribute();

    AbstractImagePrototype refresh();
    
    @Resource(value = "wrench_orange.png")
    AbstractImagePrototype setup();

    AbstractImagePrototype mapped();

    AbstractImagePrototype unmapped();

    @Resource(value = "gs.png")
    AbstractImagePrototype graduatedSymbol();

    AbstractImagePrototype ppt();

    AbstractImagePrototype image();

    AbstractImagePrototype msword();

    AbstractImagePrototype pdf();

    AbstractImagePrototype pieChart();
}
