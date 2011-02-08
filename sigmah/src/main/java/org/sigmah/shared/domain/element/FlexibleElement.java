package org.sigmah.shared.domain.element;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sigmah.shared.domain.history.Historable;
import org.sigmah.shared.domain.profile.PrivacyGroup;
import org.sigmah.shared.domain.value.ListEntity;

/**
 * Flexible element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
// Uses a joined inheritance to map the different types of flexible elements.
// Each type of element is stored in a kind
// of sub-table. Each element, regardless of the type, is created with an unique
// identifier. This identifier is unique
// for all the flexible element types.
// An element is retrieved by join instructions on sub-tables and a constraint
// on the identifier.
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "flexible_element")
public abstract class FlexibleElement implements Serializable, Historable {

    private static final long serialVersionUID = -8754613123116586106L;

    private Long id;
    private String label;
    private Boolean validates = false;
    private PrivacyGroup privacyGroup;
    private Boolean amendable = false;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_flexible_element")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "label", nullable = true, columnDefinition = "TEXT")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "validates", nullable = false)
    public boolean isValidates() {
        return validates;
    }

    public void setValidates(boolean validates) {
        this.validates = validates;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_privacy_group", nullable = true)
    public PrivacyGroup getPrivacyGroup() {
        return privacyGroup;
    }

    public void setPrivacyGroup(PrivacyGroup privacyGroup) {
        this.privacyGroup = privacyGroup;
    }

    @Column(name = "amendable", nullable = false)
    public boolean isAmendable() {
        return amendable;
    }

    public void setAmendable(boolean amendable) {
        this.amendable = amendable;
    }

    @Override
    @Transient
    public boolean isHistorable() {
        // Doesn't manage history by default.
        return false;
    }

    @Override
    public String asHistoryToken(String value) {
        return value;
    }

    @Override
    public String asHistoryToken(ListEntity value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("id -> ");
        sb.append(id);
        sb.append(", ");
        sb.append("label -> ");
        sb.append(label);
        sb.append(", ");
        sb.append("validates -> ");
        sb.append(validates);

        return sb.toString();
    }
}
