package org.sigmah.client.page.report.editor;

import org.sigmah.client.page.report.editor.ElementDialog.Callback;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Window;

public class ElementDialog extends Dialog {
	private ReportElement model;
	private ReportElementEditor editor;
	private EditorProvider editorProvider;
	private Callback callback;
	
	public interface Callback {
		void onOK();
	}

	public ElementDialog(EditorProvider editorProvider, String buttons) {
		this.editorProvider = editorProvider;
		setLayout(new FitLayout());
		setButtons(buttons);
	}
	
	public void show(ReportElement element, Callback callback) {
		this.callback = callback;
		
		setWidth((int) (Window.getClientWidth() * 0.90));
		setHeight((int) (Window.getClientHeight() * 0.90));
		if(element.getTitle() == null) {
			setHeading("New Report Element");
		} else {
			setHeading(element.getTitle());
		}
		
		removeAll();

		this.model = element;
		this.editor = editorProvider.create(model);
		this.editor.bind(model);
		add(editor.getWidget());
		layout();
		
		super.show();
	}

	@Override
	protected void onButtonPressed(Button button) {
		hide();
		editor.disconnect();
		if(button.getItemId().equals(Dialog.OK)) {
			callback.onOK();
		}
	}
	
	
}
