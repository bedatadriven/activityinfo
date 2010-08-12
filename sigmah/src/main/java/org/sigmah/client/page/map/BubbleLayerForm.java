/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.i18n.client.NumberFormat;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.widget.ColorField;
import org.sigmah.client.page.common.widget.MappingComboBox;
import org.sigmah.shared.report.model.BubbleMapLayer;

class BubbleLayerForm extends FormPanel {
    private NumberField maxRadiusField;
    private NumberField minRadiusField;
    private ColorField colorField;
    private MappingComboBox<Integer> clusterCombo;

    public BubbleLayerForm() {
        super();

        setHeading(I18N.CONSTANTS.selectTheSymbol());
        setIcon(IconImageBundle.ICONS.graduatedSymbol());

        setLabelWidth(100);
        setFieldWidth(200);

        colorField = new ColorField();
        colorField.setFieldLabel(I18N.CONSTANTS.color());
        colorField.setValue("0000BB");
        this.add(colorField);

        minRadiusField = new NumberField();
        minRadiusField.setAllowDecimals(false);
        minRadiusField.setAllowBlank(false);
        minRadiusField.setFormat(NumberFormat.getFormat("0"));
        minRadiusField.setValue(5);
        minRadiusField.setFieldLabel(I18N.CONSTANTS.radiusMinimum());
        this.add(minRadiusField);

        maxRadiusField = new NumberField();
        maxRadiusField.setAllowDecimals(false);
        maxRadiusField.setAllowBlank(false);
        maxRadiusField.setFormat(NumberFormat.getFormat("0"));
        maxRadiusField.setValue(15);
        maxRadiusField.setFieldLabel(I18N.CONSTANTS.radiusMaximum());
        this.add(maxRadiusField);

        clusterCombo = new MappingComboBox<Integer>();
        clusterCombo.add(0, I18N.CONSTANTS.none());
        clusterCombo.add(1, I18N.CONSTANTS.automatic());
        clusterCombo.setMappedValue(1);
        clusterCombo.setFieldLabel(I18N.CONSTANTS.clustering());
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
