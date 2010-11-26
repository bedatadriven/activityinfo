/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Report element entity.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
@Table(name = "report_element")
public class ReportElement extends FlexibleElement {
    private static final long serialVersionUID = 1L;

    /**
     * Link to the ProjectReportModel that will be used by the report contained
     * by this element.
     */
    private Integer modelId;

    @Column(name = "model_id", nullable = true)
    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
}
