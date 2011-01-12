package org.sigmah.shared.domain.profile;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Defines a global permission to be contained in a profile.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "global_permission")
public class GlobalPermission implements Serializable {

    private static final long serialVersionUID = -2678220725834763884L;

    private Integer id;
    private Profile profile;
    private GlobalPermissionEnum permission;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_global_permission")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_profile", nullable = false)
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Column(name = "permission", nullable = false)
    @Enumerated(EnumType.STRING)
    public GlobalPermissionEnum getPermission() {
        return permission;
    }

    public void setPermission(GlobalPermissionEnum permission) {
        this.permission = permission;
    }
}
