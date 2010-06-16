package org.activityinfo.shared.command.result;

/**
 * @author Alex Bertram
 */
public class SyncRegionUpdate implements CommandResult {

    String version;
    boolean complete;
    String sql;

    public SyncRegionUpdate() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
