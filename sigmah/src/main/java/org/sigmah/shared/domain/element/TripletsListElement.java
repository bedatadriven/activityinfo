package org.sigmah.shared.domain.element;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sigmah.shared.command.result.ValueResultUtils;
import org.sigmah.shared.domain.value.ListEntity;
import org.sigmah.shared.domain.value.TripletValue;

/**
 * Triplets list element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "triplets_list_element")
public class TripletsListElement extends FlexibleElement {

    private static final long serialVersionUID = 1816428096000083612L;

    @Override
    @Transient
    public boolean isHistorable() {
        return true;
    }

    @Override
    @Transient
    public String asHistoryToken(ListEntity value) {

        if (!(value instanceof TripletValue)) {
            return null;
        }

        final TripletValue tValue = (TripletValue) value;
        return ValueResultUtils.mergeElements(tValue.getCode(), tValue.getName(), tValue.getPeriod());
    }
}
