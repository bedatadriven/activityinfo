/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import org.sigmah.client.Application;
import org.sigmah.client.page.common.FieldSetFitLayout;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CommentFieldSet extends FieldSet {

    public CommentFieldSet() {


		setHeading(Application.CONSTANTS.comments());
		setLayout(new FieldSetFitLayout());
		setCollapsible(false);
		setHeight(200);

		TextArea commentField = new TextArea();
        commentField.setName("comments");
		add(commentField);
    }
}
