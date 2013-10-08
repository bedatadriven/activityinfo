package org.activityinfo.server.digest.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.activityinfo.server.database.hibernate.entity.Partner;
import org.activityinfo.server.database.hibernate.entity.SiteHistory;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.digest.DigestModel;
import org.activityinfo.server.util.date.DateCalc;

public class ActivityDigestModel implements DigestModel {
    private final User user;
    private final Date date;
    private final int days;
    private final long from;
    private final Set<DatabaseModel> databases;

    public ActivityDigestModel(User user, Date date, int days) {
        this.user = user;
        this.date = date;
        this.days = days;
        this.from = DateCalc.daysAgo(date, days).getTime();
        this.databases = new TreeSet<DatabaseModel>();
    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public int getDays() {
        return days;
    }

    public long getFrom() {
        return from;
    }

    public Date getFromDate() {
        return new Date(from);
    }

    @Override
    public boolean hasData() {
        return !databases.isEmpty();
    }

    public void addDatabase(DatabaseModel databaseModel) {
        databases.add(databaseModel);
    }

    public Collection<DatabaseModel> getActiveDatabases() {
        List<DatabaseModel> activeDatabases = new ArrayList<DatabaseModel>();
        for (DatabaseModel db : databases) {
            if (db.isActive()) {
                activeDatabases.add(db);
            }
        }
        return activeDatabases;
    }

    public Collection<DatabaseModel> getInactiveDatabases() {
        List<DatabaseModel> inactiveDatabases = new ArrayList<DatabaseModel>();
        for (DatabaseModel db : databases) {
            if (!db.isActive()) {
                inactiveDatabases.add(db);
            }
        }
        return inactiveDatabases;
    }

    public static class DatabaseModel implements Comparable<DatabaseModel> {
        private final ActivityDigestModel model;
        private final UserDatabase database;
        private final SiteHistory lastEdit;

        private ActivityMap ownerActivityMap;
        private final Set<PartnerActivityModel> partnerActivityModels;

        public DatabaseModel(ActivityDigestModel model, UserDatabase database, SiteHistory lastEdit) {
            this.model = model;
            this.database = database;
            this.lastEdit = lastEdit;
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

        public SiteHistory getLastEdit() {
            return lastEdit;
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

        public Collection<PartnerActivityModel> getPartnerActivityModels() {
            return partnerActivityModels;
        }

        public boolean isActive() {
            if (ownerActivityMap.hasActivity()) {
                return true;
            }

            for (PartnerActivityModel partnerModel : partnerActivityModels) {
                if (partnerModel.hasActivity()) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public int compareTo(DatabaseModel o) {
            int result = database.getName().compareTo(o.database.getName());
            if (result == 0) {
                // For some reason these databases have the same name. This is probably not correct,
                // but let's compare on id anyway to make this situation visible.
                result = ((Integer) database.getId()).compareTo(o.database.getId());
            }
            return result;
        }
    }

    public static class PartnerActivityModel implements Comparable<PartnerActivityModel> {
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

        public Collection<ActivityMap> getActivityMaps() {
            return activityMaps;
        }

        public Map<Integer, Integer> getTotalActivityMap() {
            Map<Integer, Integer> totals = new HashMap<Integer, Integer>();

            int size = databaseModel.getModel().getDays();
            if (activityMaps != null && !activityMaps.isEmpty()) {
                size = activityMaps.iterator().next().getMap().keySet().size();
            }

            for (int i = 0; i < size; i++) {
                totals.put(i, 0);
            }

            for (ActivityMap map : activityMaps) {
                for (Entry<Integer, Integer> act : map.getMap().entrySet()) {
                    totals.put(act.getKey(), totals.get(act.getKey()) + act.getValue());
                }
            }

            return totals;
        }

        @Override
        public int compareTo(PartnerActivityModel o) {
            return partner.getName().compareTo(o.partner.getName());
        }

        public boolean hasActivity() {
            for (ActivityMap map : activityMaps) {
                if (map.hasActivity()) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class ActivityMap implements Comparable<ActivityMap> {
        private final DatabaseModel databaseModel;
        private final User user;
        private final Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        public ActivityMap(DatabaseModel databaseModel, User user, List<SiteHistory> histories) {
            this.databaseModel = databaseModel;
            this.user = user;

            for (int i = 0; i < databaseModel.getModel().getDays(); i++) {
                map.put(i, 0);
            }

            if (histories != null && !histories.isEmpty()) {
                for (SiteHistory history : histories) {
                    int daysBetween = DateCalc.absoluteDaysBetween(databaseModel.getModel().getDate(),
                        history.getTimeCreated());
                    Integer old = map.get(daysBetween);
                    map.put(daysBetween, old + 1);
                }
            }
        }

        public DatabaseModel getDatabaseModel() {
            return databaseModel;
        }

        public User getUser() {
            return user;
        }

        public Map<Integer, Integer> getMap() {
            return map;
        }

        public boolean hasActivity() {
            for (Integer value : map.values()) {
                if (value > 0) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int compareTo(ActivityMap o) {
            return user.getName().compareTo(o.getUser().getName());
        }
    }
}
