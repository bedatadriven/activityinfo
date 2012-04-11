package org.sigmah.client.page.report.editor;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.report.editor.AddElementPanel.AddCallback;
import org.sigmah.client.page.report.resources.ReportResources;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.inject.Inject;

public class CompositeEditor2 extends LayoutContainer implements ReportElementEditor<Report> {

	private final EditorProvider editorProvider;

	private Report model;
	private LayoutContainer page;
	private AddElementPanel addPanel;
	
	@Inject
	public CompositeEditor2(EditorProvider editorProvider) {
		this.editorProvider = editorProvider;
		
		page = new LayoutContainer();
		page.addStyleName(ReportResources.INSTANCE.style().page());
		add(page);

		addPanel = new AddElementPanel(editorProvider, new AddCallback() {
			
			@Override
			public void onAdd(ReportElement element) {
				onElementAdded(element);
			}
		});
		page.add(addPanel);
		
		ReportResources.INSTANCE.style().ensureInjected();
		
		setLayout(new CompositeEditorLayout());
		setScrollMode(Scroll.AUTOY);
		setMonitorWindowResize(true);
	}


	@Override
	protected void onWindowResize(int width, int height) {
		layout(true);
	}

	@Override
	public void bind(Report model) {
		this.model = model;
		for(Component child : page.getItems()) {
			if(child != addPanel) {
				page.remove(child);
			}
		}
		for(ReportElement element : model.getElements()) {
			page.insert(new ElementWidget(editorProvider, element), page.getItemCount()-1);
		}
		page.layout();
	}
	
	@Override
	public Report getModel() {
		return this.model;
		
	}

	@Override
	public Component getWidget() {
		return this;
	}

	@Override
	public List<Format> getExportFormats() {
		return Arrays.asList(Format.Word, Format.PDF);
	}

	private void onElementAdded(ReportElement element) {
		model.addElement(element);
		page.insert(new ElementWidget(editorProvider, element), page.getItemCount()-1);
		page.layout();
	}


	@Override
	public void disconnect() {
		
	}

}
