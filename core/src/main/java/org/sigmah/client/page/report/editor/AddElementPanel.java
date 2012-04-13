package org.sigmah.client.page.report.editor;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.report.editor.ElementDialog.Callback;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
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
	public AddElementPanel(EditorProvider editorProvider, Provider<ElementDialog> dialogProvider) {
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
				addElement(new PivotChartReportElement());
			}
		}));
		add(createAddButton(I18N.CONSTANTS.addTable(), IconImageBundle.ICONS.table(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				addElement(new PivotTableReportElement());
			}
		}));
		add(createAddButton(I18N.CONSTANTS.addMap(), IconImageBundle.ICONS.map(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				addElement(new MapReportElement());
			}
		}));
		
	}
	
	public void setCallback(AddCallback callback) {
		this.addCallback = callback;
	}

	protected void addElement(final ReportElement element) {
		ElementDialog dialog = dialogProvider.get();
		dialog.show(element, new Callback() {
			
			@Override
			public void onOK(boolean dirty) {
				addCallback.onAdd(element);
			}
		});
		
	}

	private Button createAddButton(String text, AbstractImagePrototype icon, SelectionListener<ButtonEvent> listener) {
		Button button = new Button(text, icon, listener);
		button.setIconAlign(IconAlign.TOP);
		return button;
	}
}
