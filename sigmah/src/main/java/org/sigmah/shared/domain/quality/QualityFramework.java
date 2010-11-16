package org.sigmah.shared.domain.quality;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    private List<CriterionType> types;
    private List<QualityCriterion> criteria;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_quality_framework")
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

    @OneToMany(mappedBy = "qualityFramework", cascade = CascadeType.ALL)
    public List<CriterionType> getTypes() {
        return types;
    }

    public void setTypes(List<CriterionType> types) {
        this.types = types;
    }

    @OneToMany(mappedBy = "qualityFramework", cascade = CascadeType.ALL)
    public List<QualityCriterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<QualityCriterion> criteria) {
        this.criteria = criteria;
    }
}
