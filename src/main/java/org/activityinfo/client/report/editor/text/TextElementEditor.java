package org.activityinfo.client.report.editor.text;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.client.page.report.editor.ReportElementEditor;
import org.activityinfo.shared.command.RenderElement.Format;
import org.activityinfo.shared.report.model.TextReportElement;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;

public class TextElementEditor extends LayoutContainer implements ReportElementEditor<TextReportElement> {

	private TextArea textArea;
	private ReportEventHelper events;
	private TextReportElement model = new TextReportElement();
	

	@Inject
	public TextElementEditor(EventBus eventBus) {
		super();
		setLayout(new FitLayout());
		textArea = new TextArea();
		textArea.addListener(Events.Change, new Listener<FieldEvent>() {

			@Override
			public void handleEvent(FieldEvent be) {
				model.setText(textArea.getValue());
			}
		});
		add(textArea);
		events = new ReportEventHelper(eventBus, this);
		events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				textArea.setValue(model.getText());
			}
		});
		
		
	}
	
	@Override
	public void disconnect() {
		events.disconnect();
	}

	@Override
	public Component getWidget() {
		return this;
	}

	@Override
	public List<Format> getExportFormats() {
		return Arrays.asList(Format.PDF, Format.Word);
	}

	@Override
	public void bind(TextReportElement model) {
		this.model = model;
		this.textArea.setValue(model.getText());
	}

	@Override
	public TextReportElement getModel() {
		return model;
	}	
}
