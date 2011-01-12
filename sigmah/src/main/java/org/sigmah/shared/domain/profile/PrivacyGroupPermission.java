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
 * Wrap a permission linked to a privacy group.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "privacy_group_permission")
public class PrivacyGroupPermission implements Serializable {

    private static final long serialVersionUID = 2124906244118541577L;

    private Integer id;
    private Profile profile;
    private PrivacyGroup privacyGroup;
    private PrivacyGroupPermissionEnum permission;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_permission")
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_privacy_group", nullable = false)
    public PrivacyGroup getPrivacyGroup() {
        return privacyGroup;
    }

    public void setPrivacyGroup(PrivacyGroup privacyGroup) {
        this.privacyGroup = privacyGroup;
    }

    @Column(name = "permission", nullable = false)
    @Enumerated(EnumType.STRING)
    public PrivacyGroupPermissionEnum getPermission() {
        return permission;
    }

    public void setPermission(PrivacyGroupPermissionEnum permission) {
        this.permission = permission;
    }
}
