package org.activityinfo.server.digest.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.server.database.hibernate.entity.SiteHistory;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.digest.DigestDateUtil;

public class ActivityMap implements Comparable<ActivityMap> {
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
                int daysBetween = DigestDateUtil.absoluteDaysBetween(databaseModel.getModel().getDate(),
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

    public static Map<Integer, Integer> getTotalActivityMap(Collection<ActivityMap> activities,
        DatabaseModel databaseModel) {
        Map<Integer, Integer> totals = new HashMap<Integer, Integer>();

        int size = databaseModel.getModel().getDays();
        if (activities != null && !activities.isEmpty()) {
            size = activities.iterator().next().map.keySet().size();
        }

        for (int i = 0; i < size; i++) {
            totals.put(i, 0);
        }

        for (ActivityMap activity : activities) {
            for (Entry<Integer, Integer> act : activity.map.entrySet()) {
                totals.put(act.getKey(), totals.get(act.getKey()) + act.getValue());
            }
        }

        return totals;
    }
}