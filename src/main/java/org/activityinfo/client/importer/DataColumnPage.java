package org.activityinfo.client.importer;

import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.common.collect.Lists;
import com.google.gwt.i18n.client.DateTimeFormat;


public class DataColumnPage extends WizardPage {

    private ImportModel model;
    private ColumnComboBox startDateCombo;
    private ColumnComboBox endDateComboBox;

    public DataColumnPage(ImportModel model) {
        this.model = model;
        
        setLayout(new FormLayout());
      
        startDateCombo = new ColumnComboBox();
        startDateCombo.setFieldLabel(I18N.CONSTANTS.startDate());
        add(startDateCombo);
        
        endDateComboBox = new ColumnComboBox();
        endDateComboBox.setFieldLabel(I18N.CONSTANTS.endDate());
        add(endDateComboBox);
        
    }

    @Override
    public void beforeShow() {
        startDateCombo.setStore(model.getData().createColumnStore());
        endDateComboBox.setStore(model.getData().createColumnStore());
    }

    public List<DateColumnBinder<SiteDTO>> getSiteBinders() {
        DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd");
        List<DateColumnBinder<SiteDTO>> binders = Lists.newArrayList();
        binders.add(new DateColumnBinder<SiteDTO>(startDateCombo.getValue().getColumnIndex(), "date1", format));
        binders.add(new DateColumnBinder<SiteDTO>(endDateComboBox.getValue().getColumnIndex(), "date2", format));
        return binders;
    }
}
