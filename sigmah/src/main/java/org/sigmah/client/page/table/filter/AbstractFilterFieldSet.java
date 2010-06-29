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
