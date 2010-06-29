/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.i18n.client.NumberFormat;
import org.sigmah.client.Application;
import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.client.page.common.widget.MappingComboBox;
import org.sigmah.shared.report.model.BubbleMapLayer;

/**
 * @author Alex Bertram
 */
public class BubbleLayerForm extends FormPanel {

    private NumberField maxRadiusField;
    private NumberField minRadiusField;
    private ColorField colorField;
    private MappingComboBox<Integer> clusterCombo;

    public BubbleLayerForm() {

        setHeading(Application.CONSTANTS.selectTheSymbol());
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
        minRadiusField.setFieldLabel(Application.CONSTANTS.radiusMinimum());
        this.add(minRadiusField);

        maxRadiusField = new NumberField();
        maxRadiusField.setAllowDecimals(false);
        maxRadiusField.setAllowBlank(false);
        maxRadiusField.setFormat(NumberFormat.getFormat("0"));
        maxRadiusField.setValue(15);
        maxRadiusField.setFieldLabel(Application.CONSTANTS.radiusMaximum());
        this.add(maxRadiusField);

        clusterCombo = new MappingComboBox<Integer>();
        clusterCombo.add(0, Application.CONSTANTS.none());
        clusterCombo.add(1, Application.CONSTANTS.automatic());
        clusterCombo.setMappedValue(1);
        clusterCombo.setFieldLabel(Application.CONSTANTS.clustering());
        this.add(clusterCombo);

    }

    public void updateLayer(BubbleMapLayer layer) {
        layer.setScaling(BubbleMapLayer.ScalingType.Graduated);
        layer.setMaxRadius(maxRadiusField.getValue().intValue());
        layer.setMinRadius(minRadiusField.getValue().intValue());
        layer.setDefaultColor(colorField.getIntValue());
        layer.setClustered(clusterCombo.getMappedValue() == 1);
    }

}
