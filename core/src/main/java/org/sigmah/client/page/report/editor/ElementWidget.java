package org.sigmah.client.page.report.editor;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.report.editor.ElementDialog.Callback;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.Style.AutoSizeMode;
import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Editor;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class ElementWidget extends Widget {

	private static ElementWidgetUiBinder uiBinder = GWT
			.create(ElementWidgetUiBinder.class);
	
	interface ElementWidgetUiBinder extends UiBinder<Element, ElementWidget> {
	}

	
	interface MyStyle extends CssResource {
		String title();
		String container();
		String editButton();
		String removeButton();
	}
	
	@UiField
	MyStyle style;

	@UiField DivElement titleElement;
	@UiField DivElement buttonElement;
	
	private Editor titleEditor;
	private ReportElement model;

	private EditorProvider editorProvider;

	public ElementWidget(EditorProvider editorProvider, final ReportElement model) {
		this.editorProvider = editorProvider;
		this.model = model;
		
		setElement(uiBinder.createAndBindUi(this));
		
		titleElement.setInnerText(formatTitle(model));
		
		TextField<String> titleField = new TextField<String>();
		titleEditor = new Editor(titleField);
		titleEditor.setAutoSizeMode(AutoSizeMode.HEIGHT);
		titleEditor.setConstrain(true);
		titleEditor.setAlignment("cl");
		titleEditor.setWidth(250);
		titleEditor.setHeight(16);
		titleEditor.setCompleteOnEnter(true);
		titleEditor.addListener(Events.Complete, new Listener<EditorEvent>() {

			@Override
			public void handleEvent(EditorEvent be) {
				if(!Strings.isNullOrEmpty((String)be.getValue())) {
					model.setTitle((String)be.getValue());
					titleElement.setInnerText((String)be.getValue());
				}
			}
		});

		
		sinkEvents(Event.MOUSEEVENTS |  Event.ONCLICK);
		
	}


	private String formatTitle(ReportElement model) {
		if(model.getTitle() != null) {
			return model.getTitle();
		} else if(model instanceof PivotChartReportElement) {
			return I18N.CONSTANTS.untitledChart();
		} else if(model instanceof PivotTableReportElement) {
			return I18N.CONSTANTS.untitledTable();
		} else if(model instanceof MapReportElement) {
			return I18N.CONSTANTS.untitledMap();
		} else {
			return "Untitled";
		}
	}
	

	@Override
	public void onBrowserEvent(Event event) {
		Element clicked = event.getEventTarget().cast();
		if(event.getTypeInt() == Event.ONCLICK) {
			if(clicked.getClassName().equals(style.title())) {
				titleEditor.startEdit(  (com.google.gwt.user.client.Element) titleElement.cast(), model.getTitle());
			} else if(clicked.getClassName().contains(style.editButton())) {
				edit();
			} 
			
		} else if(event.getTypeInt() == Event.ONMOUSEOVER) {
			buttonElement.getStyle().setVisibility(Visibility.VISIBLE);
		} else if(event.getTypeInt() == Event.ONMOUSEOUT) {
			buttonElement.getStyle().setVisibility(Visibility.HIDDEN);
		}	
	}


	private void edit() {
		ElementDialog dialog = new ElementDialog(editorProvider, Dialog.OK);
		dialog.show(model, new Callback() {
			
			@Override
			public void onOK() {
				
			}
		});
		
	}

}
