package org.sigmah.server.domain.element;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Budget distribution element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "budget_distribution_element")
public class BudgetDistributionElement extends FlexibleElement {

	private static final long serialVersionUID = 7802749241559520698L;

}
