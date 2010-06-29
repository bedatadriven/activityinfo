package org.sigmah.client.page.entry.editor;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.i18n.client.NumberFormat;
import org.sigmah.client.Application;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.IndicatorGroup;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class IndicatorFieldSet extends FieldSet {


    public IndicatorFieldSet(ActivityDTO activity) {

        TableLayout layout = new TableLayout(3);
        layout.setCellPadding(5);
        layout.setCellVerticalAlign(Style.VerticalAlignment.TOP);

        setHeading(Application.CONSTANTS.indicators());
        setLayout(layout);
        setCollapsible(false);
        setStyleAttribute("fontSize", "8pt");

        for(IndicatorGroup group : activity.groupIndicators()) {

            if(group.getName() != null) {
                addGroupHeader(group.getName());
            }

            for(IndicatorDTO indicator : group.getIndicators()) {

                if(indicator.getCollectIntervention()) {
                    addIndicator(indicator);
                }
            }
        }
    }

    protected void addGroupHeader(String name) {

        TableData layoutData = new TableData();
        layoutData.setColspan(3);

        Text header = new Text(name);
        header.setStyleAttribute("fontSize", "9pt");
        header.setStyleAttribute("fontWeight", "bold");
        header.setStyleAttribute("marginTop", "6pt");

        add(header, layoutData);
        
    }

    protected void addIndicator(IndicatorDTO indicator) {

        Text indicatorLabel = new Text(indicator.getName());
        indicatorLabel.setStyleAttribute("fontSize", "9pt");

        add(indicatorLabel);

        NumberField indicatorField = new NumberField();
        indicatorField.setName( indicator.getPropertyName() );
        indicatorField.setWidth(50);
        indicatorField.setFormat(NumberFormat.getFormat("0"));
        indicatorField.setStyleAttribute("textAlign", "right");

        if(indicator.getDescription() != null && !indicator.getDescription().isEmpty()) {
            ToolTipConfig tip = new ToolTipConfig();
            tip.setDismissDelay(0);
            tip.setShowDelay(100);
            tip.setText(indicator.getDescription());

            indicatorField.setToolTip(tip);
        }

        add(indicatorField);

        Text unitLabel = new Text(indicator.getUnits());
        unitLabel.setStyleAttribute("fontSize", "9pt");

        add(unitLabel);
    }


}
