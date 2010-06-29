package org.sigmah.client.page.map;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.google.inject.Inject;
import org.sigmah.client.Application;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.report.model.BubbleMapLayer;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.MapElement;
import org.sigmah.shared.report.model.ReportElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class SingleMapForm extends ContentPanel implements MapForm {

    protected final Dispatcher service;
    protected final UIConstants messages;
    protected final IconImageBundle icons;

    protected AccordionLayout accordianLayout;
    protected IndicatorTreePanel indicatorTree;
    protected BubbleLayerForm symbolForm;
    protected AdminFilterPanel adminPanel;
    protected LayoutForm layoutForm;
    protected DateRangePanel datePanel;

    @Inject
    public SingleMapForm(Dispatcher service, UIConstants messages, IconImageBundle icons) {
        this.service = service;
        this.messages = messages;
        this.icons = icons;

        setHeading(Application.CONSTANTS.settings());

        accordianLayout = new AccordionLayout();
        setLayout(accordianLayout);

        indicatorTree = new IndicatorTreePanel(service, false);
        indicatorTree.setHeading(messages.indicators());
        indicatorTree.setIcon(icons.indicator());
        indicatorTree.setHeaderVisible(true);
        add(indicatorTree);

        layoutForm = new LayoutForm(service);
        add(layoutForm);

        symbolForm = new BubbleLayerForm();
        add(symbolForm);

        adminPanel = new AdminFilterPanel(this.service);
        add(adminPanel);

        datePanel = new DateRangePanel();
        add(datePanel);

//        accordianLayout.setActiveItem(indicatorTree);
    }

    public ReportElement getMapElement() {
        MapElement element = new MapElement();
        layoutForm.updateElement(element);

        List<Integer> indicators = new ArrayList<Integer>();
        List<IndicatorDTO> sel = indicatorTree.getSelection();
        if (sel.size() != 0) {
            indicators.add(sel.get(0).getId());
        }

        BubbleMapLayer layer = new BubbleMapLayer();
        layer.setIndicatorIds(indicators);

        symbolForm.updateLayer(layer);
        element.addLayer(layer);

        datePanel.updateFilter(element.getFilter());

        for (AdminEntityDTO entity : adminPanel.getSelection()) {
            element.getFilter().addRestriction(DimensionType.AdminLevel, entity.getId());
        }

        return element;
    }

    /**
     * Public for testing
     *
     * @return the indicator tree panel
     */
    public IndicatorTreePanel getIndicatorTree() {
        return indicatorTree;
    }

    public boolean validate() {
        if (indicatorTree.getSelection().size() == 0) {

            MessageBox.alert(Application.CONSTANTS.appTitle(), Application.CONSTANTS.pleaseSelectIndicator(), null);
            return false;
        }
        return true;
    }
}
