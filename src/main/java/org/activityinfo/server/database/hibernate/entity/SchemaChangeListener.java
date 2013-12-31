package org.activityinfo.server.database.hibernate.entity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class SchemaChangeListener {

    @PreUpdate
    @PrePersist
    @PreRemove
    public void updateDatabaseTimestamp(SchemaElement element) {
        element.findOwningDatabase().setLastSchemaUpdate(new Date());
    }

}
