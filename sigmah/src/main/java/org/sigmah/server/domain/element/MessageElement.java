package org.sigmah.server.domain.element;

import javax.persistence.Column;
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

	private String style;

	@Column(name = "style", nullable = true, length = 1024)
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
