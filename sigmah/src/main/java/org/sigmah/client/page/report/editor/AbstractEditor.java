package org.sigmah.client.page.report.editor;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.filter.PartnerFilterPanel;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Store;

public interface AbstractEditor {

	ReportElement getReportElement();
	
	Object getWidget();
	
	void bindReportElement(ReportElement element);	
	
	public class FilterPanelHandler{
		
		final ReportElement element;
		
		public FilterPanelHandler(ReportElement element){
			this.element = element;
		}
		
		public void addIndicatorPanelListener(final IndicatorTreePanel indicatorPanel) {
			indicatorPanel.addListenerToStore(Store.DataChanged,
					new Listener<BaseEvent>() {

						@Override
						public void handleEvent(BaseEvent be) {

							if (element != null) {
								
								Set<Integer> indicators = element.getFilter().getRestrictions(
										DimensionType.Indicator);
								Iterator<Integer> itr = indicators.iterator();

								while (itr.hasNext()) {
									indicatorPanel.setSelection(itr.next(), true);
								}
							}

						}
					});
		}
		

		public void addPartnerPanelListener(final PartnerFilterPanel partnerPanel) {
			partnerPanel.addListenerToStore(Store.Add,
					new Listener<BaseEvent>() {

						@Override
						public void handleEvent(BaseEvent be) {

							if (element != null) {
								Set<Integer> partners = element.getFilter().getRestrictions(
										DimensionType.Partner);
								Iterator<Integer> itr = partners.iterator();

								while (itr.hasNext()) {
									partnerPanel.setSelection(itr.next(), true);
								}
							}
						}
					});
		}
		
		public void addAdminPanelListener(final AdminFilterPanel adminPanel) {
			adminPanel.addListenerToStore(Store.DataChanged,
					new Listener<BaseEvent>() {

						@Override
						public void handleEvent(BaseEvent be) {

							if (element != null) {
								Set<Integer> adminLevels = element.getFilter().getRestrictions(
										DimensionType.AdminLevel);
								
								Iterator<Integer> itr = adminLevels.iterator();

								while (itr.hasNext()) {
									adminPanel.setSelection(itr.next(), true);
								}
							}

						}
					});
		}
		
		public void updateDate(DateRangePanel datePanel){
			Date minDate = element.getFilter().getMinDate();
			datePanel.setMinDate(minDate);

			Date maxDate = element.getFilter().getMaxDate();
			datePanel.setMaxDate(maxDate);
		}
	}
}
