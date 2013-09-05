package org.activityinfo.client.importer;

import java.util.List;
import java.util.Map;

import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.IndicatorGroup;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class IndicatorColumnPage extends WizardPage {

    private Map<Integer, ColumnComboBox> comboBoxes = Maps.newHashMap();
    private ImportModel model;
    
    public IndicatorColumnPage(ActivityDTO activity, ImportModel model) {
        this.model = model;
        
        setScrollMode(Scroll.AUTOY);
        
        TableLayout layout = new TableLayout(2);
        layout.setCellPadding(5);
        layout.setCellVerticalAlign(Style.VerticalAlignment.TOP);

        setLayout(layout);
        setStyleAttribute("fontSize", "8pt");
        setScrollMode(Scroll.AUTOY);

        for (IndicatorGroup group : activity.groupIndicators()) {

            if (group.getName() != null) {
                addGroupHeader(group.getName());
            }

            for (IndicatorDTO indicator : group.getIndicators()) {
                if(indicator.getAggregation() != IndicatorDTO.AGGREGATE_SITE_COUNT) {
                    addIndicator(indicator);
                }
            }
        }   
    }


    private void addGroupHeader(String name) {

        TableData layoutData = new TableData();
        layoutData.setColspan(2);

        Text header = new Text(name);
        header.setStyleAttribute("fontSize", "9pt");
        header.setStyleAttribute("fontWeight", "bold");
        header.setStyleAttribute("marginTop", "6pt");

        add(header, layoutData);
    }
    

    private void addIndicator(IndicatorDTO indicator) {

        String name = indicator.getName();
        if (indicator.isMandatory()) {
            name += " *";
        }
        Text indicatorLabel = new Text(Format.htmlEncode(name));
        indicatorLabel.setStyleAttribute("fontSize", "9pt");
        add(indicatorLabel);

        ColumnComboBox indicatorField = new ColumnComboBox();
        indicatorField.setName(indicator.getPropertyName());
        indicatorField.setWidth(250);
        if (indicator.isMandatory()) {
            indicatorField.setAllowBlank(false);
        }
        
        add(indicatorField);

        comboBoxes.put(indicator.getId(), indicatorField);
    }


    @Override
    public void beforeShow() {
        for(ColumnComboBox comboBox : comboBoxes.values()) {
            comboBox.setStore(model.getData().createColumnStore());
        }
    }

    public List<IndicatorColumnBinder> getBinders() {
        List<IndicatorColumnBinder> binders = Lists.newArrayList();
        for(Map.Entry<Integer, ColumnComboBox> entry : comboBoxes.entrySet()) {
            ColumnComboBox comboBox = entry.getValue();
            if(comboBox.getValue() != null) {
                binders.add(new IndicatorColumnBinder(comboBox.getValue().getColumnIndex(), entry.getKey()));
            }
        }
        return binders;
    }
    
}
