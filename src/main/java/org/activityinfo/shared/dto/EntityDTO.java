package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.ModelData;
/*
 * @author Alex Bertram
 */

public interface EntityDTO extends DTO, ModelData {

    public int getId();
    
    public String getEntityName();
}
