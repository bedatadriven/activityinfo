package org.sigmah.client.page.entry.form;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class CommentSection extends LayoutContainer {

	public CommentSection() {
		setLayout(new FitLayout());

		TextArea commentField = new TextArea();
        commentField.setName("comments");
		add(commentField);
	}
	
}
