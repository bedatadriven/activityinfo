package org.sigmah.shared.domain.profile;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Defines a privacy group to be contained in a profile.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "privacy_group")
public class PrivacyGroup implements Serializable {

    private static final long serialVersionUID = -4389579698326568953L;

    private Integer id;
    private Integer code;
    private String title;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_privacy_group")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "code", nullable = false)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Column(name = "title", nullable = false, length = 8192)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
