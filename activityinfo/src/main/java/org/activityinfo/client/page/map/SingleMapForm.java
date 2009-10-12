package org.activityinfo.client.page.map;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.google.inject.Inject;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.filter.AdminFilterPanel;
import org.activityinfo.client.page.common.filter.DateRangePanel;
import org.activityinfo.client.page.common.filter.IndicatorTreePanel;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.i18n.UIConstants;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.GsMapLayer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.ReportElement;

import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class SingleMapForm extends ContentPanel implements MapForm {

    protected final CommandService service;
    protected final UIConstants messages;
    protected final IconImageBundle icons;

    protected AccordionLayout accordianLayout;
    protected IndicatorTreePanel indicatorTree;
    protected GsSymbolForm symbolForm;
    protected AdminFilterPanel adminPanel;
    protected LayoutForm layoutForm;
    protected DateRangePanel datePanel;

    @Inject
    public SingleMapForm(CommandService service, UIConstants messages, IconImageBundle icons) {
        this.service = service;
        this.messages = messages;
        this.icons = icons;

        setHeading("Parametres");

        accordianLayout = new AccordionLayout();
        setLayout(accordianLayout);

        layoutForm = new LayoutForm(service);
        add(layoutForm);

        indicatorTree = new IndicatorTreePanel(service, false);
        indicatorTree.setHeading(messages.indicators());
        indicatorTree.setIcon(icons.indicator());
        indicatorTree.setHeaderVisible(true);
        add(indicatorTree);

        symbolForm = new GsSymbolForm();
        add(symbolForm);

        adminPanel = new AdminFilterPanel(this.service);
        add(adminPanel);

        datePanel = new DateRangePanel();
        add(datePanel);

        accordianLayout.setActiveItem(indicatorTree);
    }

    public void setSchema(Schema schema) {
      //  indicatorTree.setSchema(schema);
    }

    public ReportElement getMapElement() {
        MapElement element = new MapElement();
        layoutForm.updateElement(element);

        List<Integer> indicators = new ArrayList<Integer>();
        indicators.add(indicatorTree.getSelection().get(0).getId());

        GsMapLayer layer = new GsMapLayer();
        layer.setIndicatorIds(indicators);

        symbolForm.updateLayer(layer);

        element.addLayer(layer);

        for(AdminEntityModel entity : adminPanel.getSelection()) {
            element.getFilter().addRestriction(DimensionType.AdminLevel, entity.getId());
        }

        return element;
    }

    public boolean validate() {
        if(indicatorTree.getSelection().size() == 0) {

            MessageBox.alert("ActivityInfo", "Veuillez selectionner un indicateur, svp",null);
            return false;
        }
        return true;
    }
}
