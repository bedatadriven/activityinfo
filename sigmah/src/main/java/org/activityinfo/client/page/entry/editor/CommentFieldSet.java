package org.activityinfo.client.page.entry.editor;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.FieldSetFitLayout;

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
