package org.sigmah.client.page.report.editor;

import org.sigmah.client.EventBus;
import org.sigmah.client.page.report.HasReportElement;
import org.sigmah.client.page.report.ReportChangeHandler;
import org.sigmah.client.page.report.ReportEventHelper;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class ElementDialog extends Dialog implements HasReportElement<ReportElement> {
	private ReportElement model;
	private ReportElementEditor editor;
	private EditorProvider editorProvider;
	private Callback callback;
	
	private ReportEventHelper events;
	private boolean dirty;
	
	public interface Callback {
		void onOK(boolean dirty);
	}

	@Inject
	public ElementDialog(EventBus eventBus, EditorProvider editorProvider) {
		this.editorProvider = editorProvider;
		setLayout(new FitLayout());
		setButtons(OKCANCEL);
		
		events = new ReportEventHelper(eventBus, this);
		events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				dirty = true;
			}
		});
		
		setMonitorWindowResize(true);
	}
	
	public void hideCancel() {
		getButtonById(CANCEL).setVisible(false);
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
		
		bind(element);
		
		this.editor = editorProvider.create(model);
		this.editor.bind(model);
		add(editor.getWidget());
		layout();
		
		super.show();
	}
	
	public ReportElement getModel() {
		return model;
	}

	@Override
	protected void onButtonPressed(Button button) {
		hide();
		editor.disconnect();
		if(button.getItemId().equals(Dialog.OK)) {
			callback.onOK(dirty);
		}
	}
	
	@Override
	protected void onWindowResize(int width, int height) {
		setWidth((int) (width * 0.90));
		setHeight((int) (height * 0.90));	
		setPosition((int)(width * 0.05), (int)(height * 0.05));
	}

	@Override
	public void bind(ReportElement model) {
		this.model = model;
	}

	@Override
	public void disconnect() {
		events.disconnect();
	}	
}
