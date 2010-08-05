package org.sigmah.server.domain.element;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Indicators list element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "indicators_list_element")
public class IndicatorsListElement extends FlexibleElement {

	private static final long serialVersionUID = -5664112184584673986L;

}
