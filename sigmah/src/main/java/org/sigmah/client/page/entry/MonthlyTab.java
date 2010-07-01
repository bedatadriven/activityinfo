/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;

public class MonthlyTab extends TabItem {


    public MonthlyTab(MonthlyGrid grid) {
        setText(I18N.CONSTANTS.monthlyReports());
        setIcon(IconImageBundle.ICONS.table());
        setLayout(new FitLayout());
        add(grid);

    }

    @Override
    public void render(Element target, int index) {
        super.render(target, index);
    }
}
