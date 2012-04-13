package org.sigmah.client.page.report.editor;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.report.ReportEventHelper;
import org.sigmah.client.page.report.editor.AddElementPanel.AddCallback;
import org.sigmah.client.page.report.resources.ReportResources;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class CompositeEditor2 extends LayoutContainer implements 
	ReportElementEditor<Report>, 
	AddCallback,
	ElementWidget.EventHandler {

	private final Provider<ElementWidget> elementWidgetProvider;

	private Report model;
	private LayoutContainer page;
	private AddElementPanel addPanel;
	
	private ReportEventHelper events;
	
	@Inject
	public CompositeEditor2(EventBus eventBus, AddElementPanel addPanel, Provider<ElementWidget> elementWidgetProvider) {
		this.elementWidgetProvider = elementWidgetProvider;
		
		page = new LayoutContainer();
		page.addStyleName(ReportResources.INSTANCE.style().page());
		add(page);

		this.addPanel = addPanel;
		this.addPanel.setCallback(this);
		page.add(addPanel);
		
		ReportResources.INSTANCE.style().ensureInjected();
		
		setLayout(new CompositeEditorLayout());
		setScrollMode(Scroll.AUTOY);
		setMonitorWindowResize(true);
		
		this.events = new ReportEventHelper(eventBus, this);
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
			addElementWidget(element);
		}
		page.layout();
	}


	private void addElementWidget(ReportElement element) {
		ElementWidget widget = elementWidgetProvider.get();
		widget.bindHandler(this);
		widget.bind(element);
		page.insert(widget, page.getItemCount()-1);
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

	@Override
	public void onAdd(ReportElement element) {
		model.addElement(element);
		addElementWidget(element);		
		page.layout();
	}


	@Override
	public void disconnect() {
		
	}


	@Override
	public void onElementRemoveClicked(final ElementWidget widget) {
		MessageBox.confirm(I18N.CONSTANTS.deleteElementTitle(), I18N.CONSTANTS.deleteElementMessage(), new Listener<MessageBoxEvent>() {
			
			@Override
			public void handleEvent(MessageBoxEvent event) {
				if(event.getButtonClicked().getItemId().equals(Dialog.YES)) {
					model.getElements().remove(widget.getModel());
					page.remove(widget);
					page.layout();
					events.fireChange();
				}
			}
		});
	}


	@Override
	public void onElementChanged(ElementWidget widget) {
		events.fireChange();
	}
}
