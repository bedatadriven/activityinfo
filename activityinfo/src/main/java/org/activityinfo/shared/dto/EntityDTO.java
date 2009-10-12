package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 *
 * A data transfer object with a one-to-one relationship with
 * a database entity.
 *
 * @author Alex Bertram
 */
public interface EntityDTO extends DTO, ModelData {

    public int getId();
    
    public String getEntityName();
}
