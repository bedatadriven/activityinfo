package org.sigmah.shared.domain.category;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Category items associated to a global category type.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "category_element")
public class CategoryElement implements Serializable {

    private static final long serialVersionUID = -421149745257304446L;

    private Integer id;
    private String label;
    private CategoryType parentType;
    private String color;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_category_element")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "label", columnDefinition = "TEXT", nullable = false)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_category_type", nullable = false)
    public CategoryType getParentType() {
        return parentType;
    }

    public void setParentType(CategoryType parentType) {
        this.parentType = parentType;
    }

    @Column(name = "color_hex", nullable = false, length = 6)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
