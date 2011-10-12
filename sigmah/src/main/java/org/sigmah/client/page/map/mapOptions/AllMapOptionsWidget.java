/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map.mapOptions;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;

/**
 * Form for choosing options related to the MapElement
 */
public class AllMapOptionsWidget extends ContentPanel implements HasValue<MapReportElement> {

    protected final Dispatcher service;
    protected final IconImageBundle icons;
    
    private MapReportElement map;

    // All the encapsulated widgets containing some map options
    private AdminFilterPanel adminPanel;
    private LayoutOptionsWidget layoutForm;
    private DateRangePanel datePanel;
    private BaseMapDialog baseMapPickerWidget;
    
    private FieldSet fieldsetBaseMaps = new FieldSet();
    private FieldSet fieldsetLayoutOptions = new FieldSet();
    private FieldSet fieldsetFilterOptions = new FieldSet();

    @Inject
    public AllMapOptionsWidget(Dispatcher service, IconImageBundle icons) {
        this.service = service;
        this.icons = icons;

        initializeComponent();

        //createLayoutOptions();
        createBaseMapPicker();
//        createDateFilterOptions();
        // TODO:hookup valuechanged event so that the filter actually is applied to the mapreportelement 
//        createAdminFilterOptions(service);
    } 
    
    public HandlerRegistration addBaseMapChangedHandler(BasemapChangedEventHandler handler) {
    	return this.addHandler(handler, BaseMapChangedEvent.TYPE);
    }

	private void createAdminFilterOptions(Dispatcher service) {
		adminPanel = new AdminFilterPanel(service);
        add(adminPanel);
	}

	private void createDateFilterOptions() {
		datePanel = new DateRangePanel();
		datePanel.setHeading(I18N.CONSTANTS.filterByDate());
		datePanel.setIcon(IconImageBundle.ICONS.filter());
        add(datePanel);
	}

	private void createBaseMapPicker() {
    	baseMapPickerWidget = new BaseMapDialog(service);
    	
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
//		add(fieldsetLayoutOptions);
//		add(fieldsetFilterOptions);
	}

	public BaseMapDialog getMapOptionsWidget() {
		return baseMapPickerWidget;
	}

    public ReportElement getMapElement() {
//        MapReportElement element = new MapReportElement();
//        layoutForm.updateElement(element);
//
//        List<IndicatorDTO> sel = indicatorTree.getSelection();
//        BubbleMapLayer layer = new BubbleMapLayer();
//        
//        if (!sel.isEmpty()) {
//            layer.addIndicatorId(sel.get(0).getId());
//        }
//
//        symbolForm.updateLayer(layer);
//        element.addLayer(layer);
//
//        datePanel.updateFilter(element.getFilter());
//
//        for (AdminEntityDTO entity : adminPanel.getSelection()) {
//            element.getFilter().addRestriction(DimensionType.AdminLevel, entity.getId());
//        }
//        return element;
    	return null;
    }

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MapReportElement> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public MapReportElement getValue() {
		return map;
	}

	@Override
	public void setValue(MapReportElement value) {
		this.map=value;
	}

	@Override
	public void setValue(MapReportElement value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}


//    public boolean validate() {
//        if (indicatorTree.getSelection().size() == 0) {
//
//            MessageBox.alert(I18N.CONSTANTS.appTitle(), I18N.CONSTANTS.pleaseSelectIndicator(), null);
//            return false;
//        }
//        return true;
//    }
}
