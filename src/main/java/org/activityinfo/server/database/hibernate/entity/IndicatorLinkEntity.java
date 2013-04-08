package org.activityinfo.server.database.hibernate.entity;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "IndicatorLink")
public class IndicatorLinkEntity {
    @EmbeddedId
    private IndicatorLinkEntityId id;

    public IndicatorLinkEntity() {
    }

    public IndicatorLinkEntity(IndicatorLinkEntityId id) {
        this.id = id;
    }

    public IndicatorLinkEntityId getId() {
        return id;
    }

    public void setId(IndicatorLinkEntityId id) {
        this.id = id;
    }
}
