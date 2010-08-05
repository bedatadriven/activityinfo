package org.sigmah.server.domain.element;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Text area element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "textarea_element")
public class TextAreaElement extends FlexibleElement {

	private static final long serialVersionUID = 1147116003259320146L;

}
