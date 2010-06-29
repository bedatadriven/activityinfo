/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AbstractFieldSet extends FieldSet {

    public AbstractFieldSet(String name, int labelWidth, int fieldWidth) {
        FormLayout layout = new FormLayout();
		layout.setLabelWidth(labelWidth);
		layout.setDefaultWidth(fieldWidth);

		this.setHeading(name);
		this.setLayout(layout);
		this.setCollapsible(false);
    }


}
