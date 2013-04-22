package org.activityinfo.server.endpoint.rest.model;

import java.util.List;

public class UpdatedAdminLevel {
    private String name;
    private List<UpdatedAdminEntity> entities;
    private VersionMetadata versionMetadata;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UpdatedAdminEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<UpdatedAdminEntity> entities) {
        this.entities = entities;
    }

    public VersionMetadata getVersionMetadata() {
        return versionMetadata;
    }

    public void setVersionMetadata(VersionMetadata versionMetadata) {
        this.versionMetadata = versionMetadata;
    }
}
