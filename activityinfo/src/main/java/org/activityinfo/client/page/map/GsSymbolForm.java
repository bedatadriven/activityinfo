package org.activityinfo.client.page.map;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.i18n.client.NumberFormat;
import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.widget.ColorField;
import org.activityinfo.client.page.common.widget.MappingComboBox;
import org.activityinfo.shared.report.model.BubbleMapLayer;
/*
 * @author Alex Bertram
 */

public class GsSymbolForm extends FormPanel {

    private NumberField maxRadiusField;
    private NumberField minRadiusField;
    private ColorField colorField;
    private MappingComboBox<Integer> clusterCombo;

    public GsSymbolForm() {

        setHeading("Sélectionner le symbol");
        setIcon(Application.ICONS.graduatedSymbol());

        setLabelWidth(100);
        setFieldWidth(200);

        colorField = new ColorField();
        colorField.setFieldLabel(Application.CONSTANTS.color()); 
        colorField.setValue("0000BB");
        this.add(colorField);

        minRadiusField = new NumberField();
        minRadiusField.setAllowDecimals(false);
        minRadiusField.setAllowBlank(false);
        minRadiusField.setFormat(NumberFormat.getFormat("0"));
        minRadiusField.setValue(5);
        minRadiusField.setFieldLabel("Radius minimum");
        this.add(minRadiusField);

        maxRadiusField = new NumberField();
        maxRadiusField.setAllowDecimals(false);
        maxRadiusField.setAllowBlank(false);
        maxRadiusField.setFormat(NumberFormat.getFormat("0"));
        maxRadiusField.setValue(15);
        maxRadiusField.setFieldLabel("Radius maximum");
        this.add(maxRadiusField);
                                                                             
        clusterCombo = new MappingComboBox<Integer>();
        clusterCombo.add(0, "Aucune");
        clusterCombo.add(1, "Automatique");
        clusterCombo.setMappedValue(1);
        clusterCombo.setFieldLabel("Clustering");
        this.add(clusterCombo);

    }

    public void updateLayer(BubbleMapLayer layer) {
        layer.setMaxRadius(maxRadiusField.getValue().intValue());
        layer.setMinRadius(minRadiusField.getValue().intValue());
        layer.setDefaultColor(colorField.getIntValue());
        layer.setClustered(clusterCombo.getMappedValue() == 1);
    }

}
