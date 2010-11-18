package org.sigmah.shared.domain.element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Defines a flexible element which has no proper value but which is directly
 * linked to a property of the project.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "default_flexible_element")
public class DefaultFlexibleElement extends FlexibleElement {

    private static final long serialVersionUID = 8911957237038539783L;

    private DefaultFlexibleElementType type;

    @Column(name = "type", nullable = true)
    @Enumerated(EnumType.STRING)
    public DefaultFlexibleElementType getType() {
        return type;
    }

    public void setType(DefaultFlexibleElementType type) {
        this.type = type;
    }
}
