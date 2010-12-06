package org.sigmah.shared.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Defines the model for an org unit.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "org_unit_model")
public class OrgUnitModel implements Serializable {

    private static final long serialVersionUID = -722132644240828016L;

    private Integer id;
    private String name;
    private OrgUnitBanner banner;
    private OrgUnitDetails details;
    private Boolean hasBudget = false;
    private Boolean hasSite = false;
    private Integer minLevel;
    private Integer maxLevel;
    private String title;
    private Boolean canContainProjects = true;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "org_unit_model_id")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 8192)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "title", nullable = false, length = 8192)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @OneToOne(mappedBy = "orgUnitModel", cascade = CascadeType.ALL)
    public OrgUnitBanner getBanner() {
        return banner;
    }

    public void setBanner(OrgUnitBanner banner) {
        this.banner = banner;
    }

    @OneToOne(mappedBy = "orgUnitModel", cascade = CascadeType.ALL)
    public OrgUnitDetails getDetails() {
        return details;
    }

    public void setDetails(OrgUnitDetails details) {
        this.details = details;
    }

    @Column(name = "has_budget")
    public Boolean getHasBudget() {
        return hasBudget;
    }

    public void setHasBudget(Boolean hasBudget) {
        this.hasBudget = hasBudget;
    }

    @Column(name = "has_site")
    public Boolean getHasSite() {
        return hasSite;
    }

    public void setHasSite(Boolean hasSite) {
        this.hasSite = hasSite;
    }

    @Column(name = "min_level")
    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    @Column(name = "max_level")
    public Integer getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Column(name = "can_contain_projects", nullable = false)
    public Boolean getCanContainProjects() {
        return canContainProjects;
    }

    public void setCanContainProjects(Boolean canContainProjects) {
        this.canContainProjects = canContainProjects;
    }
}
