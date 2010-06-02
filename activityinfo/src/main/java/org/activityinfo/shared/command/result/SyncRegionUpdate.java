package org.activityinfo.shared.command.result;

/**
 * @author Alex Bertram
 */
public class SyncRegionUpdate implements CommandResult {

    String regionId;
    long version;
    String sql;

    public SyncRegionUpdate() {
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
