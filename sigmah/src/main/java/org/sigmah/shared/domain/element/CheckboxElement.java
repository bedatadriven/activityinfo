package org.sigmah.shared.domain.element;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Checkbox element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "checkbox_element")
public class CheckboxElement extends FlexibleElement {

    private static final long serialVersionUID = -9203240565522245252L;

    @Override
    @Transient
    public boolean isHistorable() {
        return true;
    }
}
