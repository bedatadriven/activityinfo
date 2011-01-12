package org.sigmah.shared.domain.profile;

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
 * Defines a permissions profile.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = 2985051353402191552L;

    private Integer id;
    private String name;
    private List<GlobalPermission> globalPermissions;
    private List<PrivacyGroupPermission> privacyGroupPermissions;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_profile")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 8196)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    public List<GlobalPermission> getGlobalPermissions() {
        return globalPermissions;
    }

    public void setGlobalPermissions(List<GlobalPermission> globalPermissions) {
        this.globalPermissions = globalPermissions;
    }

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    public List<PrivacyGroupPermission> getPrivacyGroupPermissions() {
        return privacyGroupPermissions;
    }

    public void setPrivacyGroupPermissions(List<PrivacyGroupPermission> privacyGroupPermissions) {
        this.privacyGroupPermissions = privacyGroupPermissions;
    }
}
