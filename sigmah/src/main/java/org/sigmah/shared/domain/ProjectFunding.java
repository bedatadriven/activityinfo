package org.sigmah.shared.domain;

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
 * Represent a funding link between two projects.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "project_funding")
public class ProjectFunding implements Serializable {

    private static final long serialVersionUID = -4772203000134747144L;

    private Integer id;
    private Project funding;
    private Project funded;
    private Double percentage;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_funding")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_project_funding", nullable = false)
    public Project getFunding() {
        return funding;
    }

    public void setFunding(Project funding) {
        this.funding = funding;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_project_funded", nullable = false)
    public Project getFunded() {
        return funded;
    }

    public void setFunded(Project funded) {
        this.funded = funded;
    }

    @Column(name = "percentage")
    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
