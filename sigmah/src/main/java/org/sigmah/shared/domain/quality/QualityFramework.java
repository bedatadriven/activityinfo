package org.sigmah.shared.domain.quality;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
}
