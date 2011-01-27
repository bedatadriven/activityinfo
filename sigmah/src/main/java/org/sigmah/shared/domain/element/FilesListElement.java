package org.sigmah.shared.domain.element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Files list element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "files_list_element")
public class FilesListElement extends FlexibleElement {

    private static final long serialVersionUID = 4866208826790848338L;

    private Integer limit;

    @Column(name = "max_limit", nullable = true)
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
