package org.activityinfo.client.page.report.editor;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.editor.ElementDialog.Callback;
import org.activityinfo.shared.command.RenderReportHtml;
import org.activityinfo.shared.command.result.HtmlResult;
import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.MessageBox.MessageBoxType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ElementWidget extends Widget {

	private static ElementWidgetUiBinder uiBinder = GWT
			.create(ElementWidgetUiBinder.class);
	
	interface ElementWidgetUiBinder extends UiBinder<Element, ElementWidget> {
	}

	
	interface EventHandler {
		void onElementRemoveClicked(ElementWidget widget);
		void onElementChanged(ElementWidget widget);
	}
	
	interface MyStyle extends CssResource {
		String title();
		String container();
		String editButton();
		String removeButton();
		String blockHover();
	}
	
	@UiField
	MyStyle style;

	@UiField SpanElement titleElement;
	@UiField SpanElement titleChangeElement;
	@UiField DivElement buttonElement;
	@UiField DivElement contentElement;
	@UiField DivElement contentContainerElement;
	@UiField DivElement loadingElement;
	
	private ReportElement model;

	private Dispatcher dispatcher;
	private Provider<ElementDialog> dialogProvider;

	private EventHandler parent;
	
	@Inject
	public ElementWidget(Dispatcher dispatcher, Provider<ElementDialog> dialogProvider) {
		this.dispatcher = dispatcher;
		this.dialogProvider = dialogProvider;
		
		setElement(uiBinder.createAndBindUi(this));
						
		sinkEvents(Event.MOUSEEVENTS |  Event.ONCLICK);		
	}
	
	public void bindHandler(EventHandler handler) {
		this.parent = handler;
	}

	public void bind(ReportElement model) {
		this.model = model;
		titleElement.setInnerText(ElementTitles.format(model));
		loadHtml();
	}
	
	public ReportElement getModel() {
		return model;
	}
	
	private void loadHtml() {
		
		contentElement.setInnerHTML("");
		loadingElement.getStyle().setDisplay(Display.BLOCK);
		dispatcher.execute(new RenderReportHtml(model), null, new AsyncCallback<HtmlResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(HtmlResult result) {
				loadingElement.getStyle().setDisplay(Display.NONE);
				contentElement.setInnerHTML(result.getHtml());
			}
		});
	}

	@Override
	public void onBrowserEvent(Event event) {
		Element clicked = event.getEventTarget().cast();
		if(event.getTypeInt() == Event.ONCLICK) {
			if(titleChangeElement.isOrHasChild(clicked)) {
				editTitle();
			} else if(clicked.getClassName().contains(style.editButton())) {
				edit();
			} else if(clicked.getClassName().contains(style.removeButton())) {
				parent.onElementRemoveClicked(this);
			} else if(contentElement.isOrHasChild(clicked)) {
				edit();
			}
			
		} else if(event.getTypeInt() == Event.ONMOUSEOVER) {
			buttonElement.getStyle().setVisibility(Visibility.VISIBLE);
			titleChangeElement.getStyle().setVisibility(Visibility.VISIBLE);
			contentContainerElement.addClassName(style.blockHover());
		} else if(event.getTypeInt() == Event.ONMOUSEOUT) {
			buttonElement.getStyle().setVisibility(Visibility.HIDDEN);
			titleChangeElement.getStyle().setVisibility(Visibility.HIDDEN);
			contentContainerElement.removeClassName(style.blockHover());
		}	
	}

	private void editTitle() {
		final MessageBox box = new MessageBox();
	    box.setTitle(I18N.CONSTANTS.changeTitleDialogTitle());
	    box.setType(MessageBoxType.PROMPT);
	    box.setButtons(Dialog.OKCANCEL);
	    box.show();
	    box.getTextBox().setValue(model.getTitle());
	    box.addCallback(new Listener<MessageBoxEvent>() {

			@Override
			public void handleEvent(MessageBoxEvent be) {
				if(be.getButtonClicked().getItemId().equals(Dialog.OK)) {
					model.setTitle(box.getTextBox().getValue());
					titleElement.setInnerText(ElementTitles.format(model));
				}
			}
	    });
	}


	private void edit() {
		ElementDialog dialog = dialogProvider.get();
		dialog.hideCancel();
		dialog.show(model, new Callback() {
			
			@Override
			public void onOK(boolean dirty) {
				loadHtml();
				parent.onElementChanged(ElementWidget.this);
			}
		});
		
	}

}
