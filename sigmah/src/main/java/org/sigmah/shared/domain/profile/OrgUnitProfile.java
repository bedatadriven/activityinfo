package org.sigmah.shared.domain.profile;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.User;

/**
 * A profile which associates a user to an organizational unit.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "user_unit")
public class OrgUnitProfile implements Serializable {

    private static final long serialVersionUID = 1736608073907371468L;

    private Integer id;
    private User user;
    private OrgUnit orgUnit;
    private List<Profile> profiles;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user_unit")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_org_unit", nullable = false)
    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    @ManyToMany
    @JoinTable(name = "user_unit_profiles", joinColumns = { @JoinColumn(name = "id_user_unit") }, inverseJoinColumns = { @JoinColumn(name = "id_profile") }, uniqueConstraints = { @UniqueConstraint(columnNames = {
            "id_user_unit", "id_profile" }) })
    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
