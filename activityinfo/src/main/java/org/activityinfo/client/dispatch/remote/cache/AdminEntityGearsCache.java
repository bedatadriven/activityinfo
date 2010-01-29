package org.activityinfo.client.dispatch.remote.cache;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.user.client.Window;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.Bounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends the default in-memory AdminEntityCache to store the results of
 * GetAdminEntities commands in a client-side Gears database.
 * <p/>
 * Note, this implementation caches AdminEntities in a user-independent local database called
 * "admin." This needs to be reconciled with the more general offline approach.
 *
 * @author Alex Bertram
 */
public class AdminEntityGearsCache extends AdminEntityCache {

    protected Database db;

    public AdminEntityGearsCache(DispatchEventSource service) {
        super(service);

        // Create the database if it doesn't exist.
        try {
            db = Factory.getInstance().createDatabase();
            db.open("admin");
            db.execute("create table if not exists AdminEntity (AdminLevelId int, AdminEntityId int, Name varchar(50), X1 float, Y1 float, X2 float, Y2 float, int Generation)");

            Log.debug("AdminEntityGearsCache initialized successfully");

        } catch (GearsException e) {
            Log.debug("AdminEntityGearsCache initialization failed", e);
        }
    }

    @Override
    public CommandProxyResult<AdminEntityResult> execute(GetAdminEntities cmd) {

        // first try from the in-memory cache
        AdminEntityResult inmemResult = (AdminEntityResult) super.fetch(cmd);
        if (inmemResult != null)
            return new CommandProxyResult(inmemResult);

        if (db == null)
            return CommandProxyResult.couldNotExecute();

        try {

            ResultSet rs;

            if (cmd.getParentId() == null) {
                rs = db.execute("select AdminEntityId, Name, X1, Y1, X2, Y2 from AdminEntities where AdminLevelId = ? order by name",
                        Integer.toString(cmd.getLevelId()));
            } else {
                rs = db.execute("select AdminEntityId, Name, X1, Y1, X2, Y2 from AdminEntities where AdminLevelId = ? and AdminParentId = ?",
                        Integer.toString(cmd.getLevelId()),
                        cmd.getParentId().toString());
            }


            List<AdminEntityModel> entities = new ArrayList<AdminEntityModel>();

            for (int i = 0; rs.isValidRow(); ++i, rs.next()) {
                AdminEntityModel entity = new AdminEntityModel();
                entity.setId(rs.getFieldAsInt(0));
                entity.setLevelId(cmd.getLevelId());
                entity.setParentId(cmd.getParentId());

                try {
                    double x1, y1, x2, y2;
                    x1 = rs.getFieldAsDouble(2);
                    y1 = rs.getFieldAsDouble(3);
                    x2 = rs.getFieldAsDouble(4);
                    y2 = rs.getFieldAsDouble(5);
                    entity.setBounds(new Bounds(x1, y1, x2, y2));
                } catch (Exception ex) {

                }
                entities.add(entity);
            }
            rs.close();

            if (entities.size() == 0)
                return null;
            else {
                AdminEntityResult result = new AdminEntityResult(entities);
                cache(cmd, result);
                return new CommandProxyResult<AdminEntityResult>(result);
            }

        } catch (DatabaseException e) {

            return null;

        }
    }

    @Override
    public void onSuccess(GetAdminEntities command, CommandResult result) {
        super.onSuccess(command, result);

        try {

            for (AdminEntityModel entity : ((AdminEntityResult) result).getData()) {

                Bounds bounds = entity.getBounds();

                db.execute("insert into AdminEntity (AdminEntityId, AdminLevelId, AdminParentId, Name, X1, Y1, X2, Y2) values (?, ?, ?, ?, ?, ?, ?, ?)",
                        Integer.toString(entity.getId()),
                        Integer.toString(entity.getLevelId()),
                        entity.getParentId() == null ? "" : Integer.toString(entity.getParentId()),
                        entity.getName(),
                        bounds != null ? Double.toString(bounds.x1) : "",
                        bounds != null ? Double.toString(bounds.y1) : "",
                        bounds != null ? Double.toString(bounds.x2) : "",
                        bounds != null ? Double.toString(bounds.y2) : "");
            }

        } catch (DatabaseException e) {
            Window.alert(e.toString());
        }
    }
}
