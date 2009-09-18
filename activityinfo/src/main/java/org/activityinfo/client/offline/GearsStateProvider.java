package org.activityinfo.client.offline;

import com.extjs.gxt.ui.client.state.Provider;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.core.client.GWT;
/*
 * @author Alex Bertram
 */

public class GearsStateProvider extends Provider {

    Database db;

    public GearsStateProvider(Database db) throws DatabaseException {
        this.db = db;

        try {
            this.db.execute("create table if not exists state (key text primary key, value text)");
        } catch (DatabaseException e) {
            GWT.log("GearsStateProvider: Could not create state table", e);
            throw(e);
        }
    }

    @Override
    protected void clearKey(String name) {
        try {
            db.execute("delete from state where key = ?", name);
        } catch (DatabaseException e) {
            GWT.log("GearsStateProvider: clear Key failed", e);
        }
    }

    @Override
    protected String getValue(String name) {
        try {
            ResultSet rs = db.execute("select value from state where key = ?", name);
            if(rs.isValidRow()) {
                return rs.getFieldAsString(0);
            } else {
                return null;
            }
        } catch (DatabaseException e) {
            GWT.log("GearsStateProvider: getValue failed", e);
            return null;
        }
    }

    @Override
    protected void setValue(String name, String value) {
        try {
            db.execute("update state set value = ? where key = ?", value, name);
            if(db.getRowsAffected() == 0) {
                db.execute("insert into state (key, value) values (?, ?)", value, name);
            }
        } catch(DatabaseException e) {
            GWT.log("GearsStateProvider: setValue failed", e);
        }
    }
}
