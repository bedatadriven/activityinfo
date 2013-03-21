package org.activityinfo.server.digest.activity;

import java.util.HashSet;
import java.util.Set;

import org.activityinfo.server.database.hibernate.entity.UserDatabase;

public class DatabaseModel implements Comparable<DatabaseModel> {
    private final ActivityDigestModel model;
    private final UserDatabase database;

    private ActivityMap ownerActivityMap;
    private final Set<PartnerActivityModel> partnerActivityModels;

    public DatabaseModel(ActivityDigestModel model, UserDatabase database) {
        this.model = model;
        this.database = database;
        this.partnerActivityModels = new HashSet<PartnerActivityModel>();

        model.addDatabase(this);
    }

    public ActivityDigestModel getModel() {
        return model;
    }

    public UserDatabase getDatabase() {
        return database;
    }

    public String getName() {
        return database.getName();
    }

    public void setOwnerActivityMap(ActivityMap ownerActivityMap) {
        this.ownerActivityMap = ownerActivityMap;
    }

    public ActivityMap getOwnerActivityMap() {
        return ownerActivityMap;
    }

    public void addPartnerActivityModel(PartnerActivityModel partnerActivityModel) {
        this.partnerActivityModels.add(partnerActivityModel);
    }

    public Set<PartnerActivityModel> getPartnerActivityModels() {
        return partnerActivityModels;
    }

    public boolean isActive() {
        if (ownerActivityMap.hasActivity()) {
            return true;
        }

        for (PartnerActivityModel partnerModel : partnerActivityModels) {
            for (ActivityMap map : partnerModel.getActivityMaps()) {
                if (map.hasActivity()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int compareTo(DatabaseModel o) {
        return database.getName().compareTo(o.database.getName());
    }
}