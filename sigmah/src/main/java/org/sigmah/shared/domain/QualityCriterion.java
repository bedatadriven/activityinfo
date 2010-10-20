package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.List;

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
    private String label;
    private QualityFramework parentFramework;
    private List<QualityCriterion> subCriterions;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_quality_framework", nullable = false)
    public QualityFramework getParentFramework() {
        return parentFramework;
    }

    public void setParentFramework(QualityFramework parentFramework) {
        this.parentFramework = parentFramework;
    }

    @OneToMany
    @JoinTable(name = "quality_criterion_children", joinColumns = { @JoinColumn(name = "id_quality_criterion") }, inverseJoinColumns = { @JoinColumn(name = "id_quality_criterion_child") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
            "id_quality_criterion", "id_quality_criterion_child" }) })
    public List<QualityCriterion> getSubCriterions() {
        return subCriterions;
    }

    public void setSubCriterions(List<QualityCriterion> subCriterions) {
        this.subCriterions = subCriterions;
    }
}
