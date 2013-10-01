package org.activityinfo.client.importer.column;

import java.util.Map;

import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.google.common.collect.Maps;

public class AttributeBinding extends ColumnBinding {

    private final AttributeGroupDTO group;
    private final Map<String, Integer> map;
    
    public AttributeBinding(AttributeGroupDTO group) {
        this.group = group;
        
        map = Maps.newHashMap();
        for(AttributeDTO attribute : group.getAttributes()) {
            map.put(attribute.getName(), attribute.getId());
        }
    }

    @Override
    public String getLabel() {
        return group.getName();
    }

    @Override
    public void bindSite(String string, SiteDTO site) {
        Integer attributeId = map.get(string);
        if(attributeId != null) {
            site.setAttributeValue(attributeId, true);
        }
    }
}
