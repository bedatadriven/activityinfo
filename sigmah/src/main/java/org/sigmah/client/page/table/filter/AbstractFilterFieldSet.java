/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table.filter;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
/*
 * @author Alex Bertram
 */

public class AbstractFilterFieldSet extends FieldSet {

    public AbstractFilterFieldSet() {
        setCheckboxToggle(true);
        setCollapsible(true);
        setExpanded(false);
    }
}
