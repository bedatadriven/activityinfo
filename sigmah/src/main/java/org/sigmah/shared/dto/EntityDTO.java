package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 *
 * A data transfer object with a one-to-one relationship with
 * a JPA @Entity.
 *
 * @author Alex Bertram
 */
public interface EntityDTO extends DTO, ModelData {

    /**
     * @return the corresponding entity's @Id
     */
    public int getId();

    /**
     * @return the entity's JPA name
     */
    public String getEntityName();
}
