package org.sigmah.client.page.dashboard;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
/** A VerticalPanel with a title and some custom CSS */
public class Block extends VerticalPanel {
	private Label lblSomeTitleOf;
	private String title;

	public Block() {
		
		initializeComponent();
	}
	private void initializeComponent() {
		setStyleName("dashboard-block");
		setSize("100%", "auto");
		{
			lblSomeTitleOf = new Label("Some title of an awesome block");
			lblSomeTitleOf.setStyleName("dashboard-header");
			add(lblSomeTitleOf);
		}
	}
	public void setTitle(String title) {
		this.title=title;
		lblSomeTitleOf.setText(title);
	}
	public String getTitle() {
		return title;
	}
}
