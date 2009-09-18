package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.*;

import com.extjs.gxt.ui.client.data.RpcMap;

import java.util.Map;


/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CreateEntity implements Command<CreateResult> {


    private String entityName;
    private RpcMap properties;

    private AdminEntityModel entity_;
    private PartnerModel partner_;
    private LocationTypeModel locationType_;

    public CreateEntity() {

    }

    public CreateEntity(String entityName, Map<String, ?> properties) {
        this.entityName = entityName;
        this.properties = new RpcMap();
        this.properties.putAll(properties);
    }

    public CreateEntity(EntityDTO entity) {
        this.entityName = entity.getEntityName();
        this.properties = new RpcMap();
        this.properties.putAll(entity.getProperties());
    }

    public String getEntityName() {
        return entityName;
    }

    protected void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public RpcMap getProperties() {
        return properties;
    }

    protected void setProperties(RpcMap properties) {
        this.properties = properties;
    }

    public static Command<CreateResult> Activity(UserDatabaseDTO db, ActivityModel act) {
        CreateEntity cmd = new CreateEntity("Activity", act.getProperties());
        cmd.properties.put("databaseId", db.getId());
        return cmd;
    }

    public static CreateEntity Site(SiteModel newSite) {
        CreateEntity cmd = new CreateEntity("Site", newSite.getProperties());
        cmd.properties.put("activityId", newSite.getActivityId());

        return cmd;
    }
}
