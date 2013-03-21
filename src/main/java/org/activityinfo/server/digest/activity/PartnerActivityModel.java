package org.activityinfo.server.digest.activity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.activityinfo.server.database.hibernate.entity.Partner;

public class PartnerActivityModel implements Comparable<PartnerActivityModel> {
    private final DatabaseModel databaseModel;
    private final Partner partner;
    private final Set<ActivityMap> activityMaps;

    public PartnerActivityModel(DatabaseModel databaseModel, Partner partner) {
        this.databaseModel = databaseModel;
        this.partner = partner;
        this.activityMaps = new HashSet<ActivityMap>();

        databaseModel.addPartnerActivityModel(this);
    }

    public DatabaseModel getDatabaseModel() {
        return databaseModel;
    }

    public Partner getPartner() {
        return partner;
    }

    public void addActivityMap(ActivityMap activityMap) {
        activityMaps.add(activityMap);
    }

    public Set<ActivityMap> getActivityMaps() {
        return activityMaps;
    }

    public Map<Integer, Integer> getTotalActivityMap() {
        return ActivityMap.getTotalActivityMap(activityMaps, databaseModel);
    }
    
    @Override
    public int compareTo(PartnerActivityModel o) {
        return partner.getName().compareTo(o.partner.getName());
    }
}
