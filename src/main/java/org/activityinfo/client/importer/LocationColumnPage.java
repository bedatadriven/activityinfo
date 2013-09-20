package org.activityinfo.client.importer;

import java.util.List;
import java.util.Map;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.CoordinateField.Axis;
import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.command.MatchLocation;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LocationColumnPage extends WizardPage {

    private ImportModel model;

    private ColumnComboBox nameColumn;
    private ColumnComboBox latColumn;
    private ColumnComboBox lngColumn;
    private Map<AdminLevelDTO, ColumnComboBox> levelColumns = Maps.newHashMap();
    
    public LocationColumnPage(ActivityDTO activity, ImportModel model) {
        this.model = model;
        
        setLayout(new FormLayout());
        
        nameColumn = new ColumnComboBox();
        nameColumn.setFieldLabel(activity.getLocationType().getName());
        add(nameColumn);
        
        latColumn = new ColumnComboBox();
        latColumn.setFieldLabel(I18N.CONSTANTS.latitude());
        add(latColumn);
        
        lngColumn = new ColumnComboBox();
        lngColumn.setFieldLabel(I18N.CONSTANTS.longitude());
        add(lngColumn);
        
        for(AdminLevelDTO level : activity.getAdminLevels()) {
            ColumnComboBox levelCombo = new ColumnComboBox();
            levelCombo.setFieldLabel(level.getName());
            add(levelCombo);
            levelColumns.put(level, levelCombo);
        }
        
    }

    @Override
    public void beforeShow() {
        nameColumn.setStore(model.getData().createColumnStore());
        latColumn.setStore(model.getData().createColumnStore());
        lngColumn.setStore(model.getData().createColumnStore());
        for(ColumnComboBox combo : levelColumns.values()) {
            combo.setStore(model.getData().createColumnStore());
        }
    }
    
    public List<ColumnBinder<MatchLocation>> getLocationBinders() {
        List<ColumnBinder<MatchLocation>> binders = Lists.newArrayList();
        binders.add(new LocationNameBinder(nameColumn.getValue().getColumnIndex()));
        if(latColumn.getValue() != null && lngColumn.getValue() != null) {
            binders.add(new CoordinateColumnBinder(latColumn.getValue().getColumnIndex(), Axis.LATITUDE));
            binders.add(new CoordinateColumnBinder(lngColumn.getValue().getColumnIndex(), Axis.LONGITUDE));
        }
        for(AdminLevelDTO level : levelColumns.keySet()) {
            ImportColumnModel column = levelColumns.get(level).getValue();
            if(column != null) {
                binders.add(new AdminNameBinder(column.getColumnIndex(), level.getId()));
            }
        }
        return binders;
    }
    
}
