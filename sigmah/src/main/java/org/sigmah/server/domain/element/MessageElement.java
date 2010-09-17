package org.sigmah.server.domain.element;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Message element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "message_element")
public class MessageElement extends FlexibleElement {

	private static final long serialVersionUID = -9203240565522245252L;

}
