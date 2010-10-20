package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Quality framework entity.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "quality_framework")
public class QualityFramework implements Serializable {

    private static final long serialVersionUID = -3263048667330191985L;

    private Integer id;
    private String label;
    private List<QualityCriterion> criterions = new ArrayList<QualityCriterion>();

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_quality_criterion")
    public Integer getId() {
        return id;
    }

    @Column(name = "label", nullable = false, length = 1024)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @OneToMany(mappedBy = "parentFramework", cascade = CascadeType.ALL)
    @OrderBy("label asc")
    public List<QualityCriterion> getCriterions() {
        return criterions;
    }

    public void setCriterions(List<QualityCriterion> criterions) {
        this.criterions = criterions;
    }
}
