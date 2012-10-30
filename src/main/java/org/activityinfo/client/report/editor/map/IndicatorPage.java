package org.activityinfo.client.report.editor.map;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.IndicatorTreePanel;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.dto.IndicatorDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;

public class IndicatorPage extends WizardPage {
    // List of all indicators
	private IndicatorTreePanel treePanel;
	
	
	public IndicatorPage(Dispatcher service) {
		
		VBoxLayout pageLayout = new VBoxLayout();
		pageLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		pageLayout.setPadding(new Padding(15));
		setLayout(pageLayout);
		
		Text header = new Text(I18N.CONSTANTS.chooseIndicatorsToMap());
		header.setTagName("h2");
		add(header);
		
		VBoxLayoutData indicatorLayout = new VBoxLayoutData();
		indicatorLayout.setFlex(1);
		
		treePanel = new IndicatorTreePanel(service, false);
		treePanel.setHeaderVisible(false);
		treePanel.setLeafCheckableOnly();
		treePanel.addCheckChangedListener(new Listener<TreePanelEvent>(){
			@Override
			public void handleEvent(TreePanelEvent be) {	
				IndicatorPage.this.fireEvent(Events.SelectionChange, new BaseEvent(Events.SelectionChange));
				IndicatorPage.this.fireEvent(Events.Enable, new BaseEvent(Events.Enable));
			}
		});
		
		add(treePanel, indicatorLayout);
	}
	
	public Collection<IndicatorDTO> getSelection() {
		return treePanel.getSelection();
	}

	public List<Integer> getSelectedIds() {
		return treePanel.getSelectedIds();
	}

	@Override
	public boolean isNextEnabled() {
		return !treePanel.getSelection().isEmpty();
	}
}
