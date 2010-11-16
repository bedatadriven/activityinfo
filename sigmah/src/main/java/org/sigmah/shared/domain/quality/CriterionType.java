package org.sigmah.shared.domain.quality;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "quality_criterion_type")
public class CriterionType implements Serializable {

    private static final long serialVersionUID = 6055173450423740216L;

    private Integer id;
    private QualityFramework qualityFramework;
    private String label;
    private Integer level;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_criterion_type")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public QualityFramework getQualityFramework() {
        return qualityFramework;
    }

    public void setQualityFramework(QualityFramework qualityFramework) {
        this.qualityFramework = qualityFramework;
    }

    @Column(name = "level", nullable = false)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
