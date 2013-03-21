package org.activityinfo.server.digest.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.digest.DigestDateUtil;

public class ActivityDigestModel {
    private final User user;
    private final Date date;
    private final int days;
    private final long from;
    private final Set<DatabaseModel> databases;

    public ActivityDigestModel(User user, Date date, int days) {
        this.user = user;
        this.date = date;
        this.days = days;
        this.from = DigestDateUtil.daysAgo(date, days).getTime();
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

    public void addDatabase(DatabaseModel databaseModel) {
        databases.add(databaseModel);
    }

    public List<DatabaseModel> getActiveDatabases() {
        List<DatabaseModel> activeDatabases = new ArrayList<DatabaseModel>();
        for (DatabaseModel db : databases) {
            if (db.isActive()) {
                activeDatabases.add(db);
            }
        }
        return activeDatabases;
    }

    public List<DatabaseModel> getInactiveDatabases() {
        List<DatabaseModel> inactiveDatabases = new ArrayList<DatabaseModel>();
        for (DatabaseModel db : databases) {
            if (!db.isActive()) {
                inactiveDatabases.add(db);
            }
        }
        return inactiveDatabases;
    }
}
