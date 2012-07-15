package org.activityinfo.client.page.report.editor;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.report.editor.ElementDialog.Callback;
import org.activityinfo.client.page.report.template.ChartTemplate;
import org.activityinfo.client.page.report.template.MapTemplate;
import org.activityinfo.client.page.report.template.PivotTableTemplate;
import org.activityinfo.client.page.report.template.ReportElementTemplate;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AddElementPanel extends LayoutContainer {

	private EditorProvider editorProvider;
	private AddCallback addCallback;
	private Provider<ElementDialog> dialogProvider;
	
	public interface AddCallback {
		void onAdd(ReportElement element);
	}

	@Inject
	public AddElementPanel(final Dispatcher dispatcher, EditorProvider editorProvider, Provider<ElementDialog> dialogProvider) {
		this.editorProvider = editorProvider;
		this.dialogProvider = dialogProvider;
		
		HBoxLayout layout = new HBoxLayout();
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCHMAX);
		layout.setPack(BoxLayoutPack.CENTER);
		layout.setPadding(new Padding(15));
		setLayout(layout);
		
		add(createAddButton(I18N.CONSTANTS.addChart(), IconImageBundle.ICONS.barChart(), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				addElement(new ChartTemplate(dispatcher));
			}
		}));
		add(createAddButton(I18N.CONSTANTS.addTable(), IconImageBundle.ICONS.table(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				addElement(new PivotTableTemplate(dispatcher));
			}
		}));
		add(createAddButton(I18N.CONSTANTS.addMap(), IconImageBundle.ICONS.map(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				addElement(new MapTemplate(dispatcher));
			}
		}));
		
	}
	

	public void setCallback(AddCallback callback) {
		this.addCallback = callback;
	}
	
	private void addElement(ReportElementTemplate template) {
		template.createElement(new AsyncCallback<ReportElement>() {
			
			@Override
			public void onSuccess(ReportElement result) {
				addElement(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void addElement(final ReportElement element) {
		ElementDialog dialog = dialogProvider.get();
		dialog.show(element, new Callback() {
			
			@Override
			public void onOK(boolean dirty) {
				addCallback.onAdd(element);
			}

			@Override
			public void onClose(boolean dirty) {

			}
		});
		
	}

	private Button createAddButton(String text, AbstractImagePrototype icon, SelectionListener<ButtonEvent> listener) {
		Button button = new Button(text, icon, listener);
		button.setIconAlign(IconAlign.TOP);
		return button;
	}
}
