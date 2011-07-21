/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map.mapOptions;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Form for choosing options related to the MapElement
 */
public class AllMapOptionsWidget extends ContentPanel {

    protected final Dispatcher service;
    protected final IconImageBundle icons;

    // All the encapsulated widgets containing some map options
    private AdminFilterPanel adminPanel;
    private LayoutOptionsWidget layoutForm;
    private DateRangePanel datePanel;
    private FilterOptionsWidget filterOptionsWidget;
    private BaseMapPickerWidget mapOptionsWidget;
    
    private FieldSet fieldsetBaseMaps = new FieldSet();
    private FieldSet fieldsetLayoutOptions = new FieldSet();
    private FieldSet fieldsetFilterOptions = new FieldSet();

    @Inject
    public AllMapOptionsWidget(Dispatcher service, IconImageBundle icons) {
        this.service = service;
        this.icons = icons;

        initializeComponent();

        createLayoutOptions();
        createBaseMapPicker();
        createFilterOptions();

//        adminPanel = new AdminFilterPanel(service);
//        add(adminPanel);
//
//        datePanel = new DateRangePanel();
//        add(datePanel);
    }
    
    private void createFilterOptions() {
    	filterOptionsWidget = new FilterOptionsWidget();
    	fieldsetFilterOptions.add(filterOptionsWidget);
	}

	private void createBaseMapPicker() {
    	mapOptionsWidget = new BaseMapPickerWidget(service);
        fieldsetBaseMaps.add(getMapOptionsWidget());
	}

	private void createLayoutOptions() {
        layoutForm = new LayoutOptionsWidget(service);
        fieldsetLayoutOptions.add(layoutForm);
	}

	private void setFieldsetDefaults(FieldSet fieldset) {
    	fieldset.setCollapsible(true);
    }

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.settings());
		
		fieldsetBaseMaps.setHeading("Base maps");
		fieldsetLayoutOptions.setHeading("Layout");
		fieldsetFilterOptions.setHeading(I18N.CONSTANTS.timePeriod());
		
		setFieldsetDefaults(fieldsetBaseMaps);
		setFieldsetDefaults(fieldsetLayoutOptions);
		setFieldsetDefaults(fieldsetFilterOptions);
		
		add(fieldsetBaseMaps);
		add(fieldsetLayoutOptions);
		add(fieldsetFilterOptions);
	}

	public BaseMapPickerWidget getMapOptionsWidget() {
		return mapOptionsWidget;
	}

//    public ReportElement getMapElement() {
////        MapReportElement element = new MapReportElement();
////        layoutForm.updateElement(element);
////
////        List<IndicatorDTO> sel = indicatorTree.getSelection();
////        BubbleMapLayer layer = new BubbleMapLayer();
////        
////        if (!sel.isEmpty()) {
////            layer.addIndicatorId(sel.get(0).getId());
////        }
////
////        symbolForm.updateLayer(layer);
////        element.addLayer(layer);
////
////        datePanel.updateFilter(element.getFilter());
////
////        for (AdminEntityDTO entity : adminPanel.getSelection()) {
////            element.getFilter().addRestriction(DimensionType.AdminLevel, entity.getId());
////        }
////        return element;
//    }


//    public boolean validate() {
//        if (indicatorTree.getSelection().size() == 0) {
//
//            MessageBox.alert(I18N.CONSTANTS.appTitle(), I18N.CONSTANTS.pleaseSelectIndicator(), null);
//            return false;
//        }
//        return true;
//    }
}
