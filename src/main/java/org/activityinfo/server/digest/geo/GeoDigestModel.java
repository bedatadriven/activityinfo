package org.activityinfo.server.digest.geo;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.digest.DigestDateUtil;
import org.activityinfo.server.digest.DigestModel;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.content.MapContent;
import org.apache.commons.lang.StringUtils;

public class GeoDigestModel implements DigestModel {
    private final User user;
    private final Date date;
    private final int days;
    private final long from;
    private SchemaDTO schemaDTO;

    private final Set<DatabaseModel> databases;

    public GeoDigestModel(User user, Date date, int days) {
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

    public SchemaDTO getSchemaDTO() {
        return schemaDTO;
    }

    public void setSchemaDTO(SchemaDTO schemaDTO) {
        this.schemaDTO = schemaDTO;
    }

    @Override
    public boolean hasData() {
        for (DatabaseModel db : databases) {
            if (!db.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void addDatabase(DatabaseModel databaseModel) {
        databases.add(databaseModel);
    }

    public Collection<DatabaseModel> getDatabases() {
        return databases;
    }

    public static class DatabaseModel implements Comparable<DatabaseModel> {
        private final GeoDigestModel model;
        private final UserDatabase database;
        private MapContent content;
        private String url;

        public DatabaseModel(GeoDigestModel model, UserDatabase database) {
            this.model = model;
            this.database = database;

            model.addDatabase(this);
        }

        public GeoDigestModel getModel() {
            return model;
        }

        public UserDatabase getDatabase() {
            return database;
        }

        public String getName() {
            return database.getName();
        }

        public MapContent getContent() {
            return content;
        }

        public void setContent(MapContent content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isRenderable() {
            return content != null && !content.getMarkers().isEmpty() && StringUtils.isNotBlank(url);
        }

        public boolean isEmpty() {
            return !isRenderable();
        }

        @Override
        public int compareTo(DatabaseModel o) {
            return database.getName().compareTo(o.database.getName());
        }
    }
}
