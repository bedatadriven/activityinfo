package org.sigmah.client.offline;

import com.extjs.gxt.ui.client.state.Provider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.inject.Inject;

/**
 *
 * Implements a GXT state <code>Provider</code> using a local database.
 *
 * @author Alex Bertram
 */
public class DatabaseStateProvider extends Provider {

    Database db;

    @Inject
    public DatabaseStateProvider(Database db) {
        this.db = db;

        try {
            this.db.execute("create table if not exists state (key text primary key, value text)");
        } catch (DatabaseException e) {
            GWT.log("DatabaseStateProvider: Could not create state table", e);
            this.db = null;
        }
    }

    @Override
    protected void clearKey(String name) {
        if(db==null) {
            return;
        }
        try {
            db.execute("delete from state where key = ?", name);
        } catch (DatabaseException e) {
            GWT.log("DatabaseStateProvider: clear Key failed", e);
        }
    }

    @Override
    protected String getValue(String name) {
        if(db==null) {
            return null;
        }
        try {
            ResultSet rs = db.execute("select value from state where key = ?", name);
            if(rs.isValidRow()) {
                return rs.getFieldAsString(0);
            } else {
                return null;
            }
        } catch (DatabaseException e) {
            GWT.log("DatabaseStateProvider: getValue failed", e);
            return null;
        }
    }

    @Override
    protected void setValue(String name, String value) {
        if(db==null) {
            return;
        }
        try {
            db.execute("update state set value = ? where key = ?", value, name);
            if(db.getRowsAffected() == 0) {
                db.execute("insert into state (key, value) values (?, ?)", name, value);
            }
        } catch(DatabaseException e) {
            GWT.log("DatabaseStateProvider: setValue failed", e);
        }
    }
}
