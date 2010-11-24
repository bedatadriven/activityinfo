package org.sigmah.shared.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.layout.Layout;

/**
 * Org unit banner entity.
 * 
 * @author tmi
 */
@Entity
@Table(name = "org_unit_banner")
public class OrgUnitBanner implements Serializable {

    private static final long serialVersionUID = -3799793707072202713L;

    private Integer id;
    private OrgUnitModel orgUnitModel;
    private Layout layout;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "banner_id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "id_org_unit_model")
    public OrgUnitModel getOrgUnitModel() {
        return orgUnitModel;
    }

    public void setOrgUnitModel(OrgUnitModel orgUnitModel) {
        this.orgUnitModel = orgUnitModel;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_layout", nullable = false)
    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}
