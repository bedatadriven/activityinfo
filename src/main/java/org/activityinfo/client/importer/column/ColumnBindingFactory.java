package org.activityinfo.client.importer.column;

import java.util.List;

import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.IndicatorDTO;

import com.google.common.collect.Lists;

public class ColumnBindingFactory {

    public static List<ColumnBinding> forActivity(ActivityDTO activity) {
        List<ColumnBinding> bindings = Lists.newArrayList();
        bindings.add(Ignore.INSTANCE);
        bindings.add(new DateBinding());
      
        for(AdminLevelDTO level : activity.getAdminLevels()) {
            bindings.add(new AdminBinding(level));
        }
        
        if(!activity.getLocationType().isAdminLevel()) {
            bindings.add(new LocationNameBinding(activity.getLocationType()));
        }
        
        for(IndicatorDTO indicator : activity.getIndicators()) {
            bindings.add(new IndicatorBinding(indicator));
        }
        
        for(AttributeGroupDTO attributeGroup : activity.getAttributeGroups()) {
            bindings.add(new AttributeBinding(attributeGroup));
        }
        
        bindings.add(new CommentsBinding());
        
        return bindings;
    }
    
}
