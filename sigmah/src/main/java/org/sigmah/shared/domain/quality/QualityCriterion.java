package org.sigmah.shared.domain.quality;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Quality criterion entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "quality_criterion")
public class QualityCriterion implements Serializable {

    private static final long serialVersionUID = -9015626378861486393L;

    private Integer id;
    private String code;
    private String label;
    private QualityFramework qualityFramework;
    private QualityCriterion parentCriterion;
    private List<QualityCriterion> subCriteria;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_quality_criterion")
    public Integer getId() {
        return id;
    }

    @Column(name = "label", nullable = false, columnDefinition = "TEXT")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "code", nullable = false, length = 8192)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_quality_framework", nullable = true)
    public QualityFramework getQualityFramework() {
        return qualityFramework;
    }

    public void setQualityFramework(QualityFramework qualityFramework) {
        this.qualityFramework = qualityFramework;
    }

    @ManyToOne(optional = true)
    @JoinTable(name = "quality_criterion_children", joinColumns = { @JoinColumn(name = "id_quality_criterion_child") }, inverseJoinColumns = { @JoinColumn(name = "id_quality_criterion") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
            "id_quality_criterion", "id_quality_criterion_child" }) })
    public QualityCriterion getParentCriterion() {
        return parentCriterion;
    }

    public void setParentCriterion(QualityCriterion parentCriterion) {
        this.parentCriterion = parentCriterion;
    }

    @OneToMany(mappedBy = "parentCriterion", cascade = CascadeType.ALL)
    public List<QualityCriterion> getSubCriteria() {
        return subCriteria;
    }

    public void setSubCriteria(List<QualityCriterion> subCriteria) {
        this.subCriteria = subCriteria;
    }
}
