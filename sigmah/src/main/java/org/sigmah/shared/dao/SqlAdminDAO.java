package org.sigmah.shared.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.BoundingBoxDTO;
import org.sigmah.shared.report.model.DimensionType;

public class SqlAdminDAO {
	
	
	public static List<AdminEntityDTO> query(Connection connection, GetAdminEntities cmd) {

        SqlQueryBuilder query =
            SqlQueryBuilder.select("AdminEntity.AdminEntityId, AdminEntity.Name, AdminEntity.AdminLevelId, " +
            		"AdminEntity.AdminEntityParentId, AdminEntity.X1, AdminEntity.Y1, AdminEntity.X2, AdminEntity.Y2 ")
                .from("AdminEntity")
                .orderBy("AdminEntity.Name");

        if(cmd.getCountryId() != null) {
        	query.from(" LEFT JOIN AdminLevel ON (AdminLevel.AdminLevelId=AdminEntity.adminLevelId) ");
        	query.where("AdminLevel.CountryId").equalTo(cmd.getCountryId());
        	
        	if(cmd.getParentId() == null && cmd.getLevelId() == null) {
        		query.where("AdminLevel.ParentId is null");
        	}
        }
        
        if(cmd.getLevelId() != null) {
            query.where("AdminEntity.AdminLevelId").equalTo(cmd.getLevelId());
        }

        if(cmd.getParentId() != null) {
            query.where("AdminEntity.AdminEntityParentId").equalTo(cmd.getParentId());
        }
        
        if(cmd.getFilter() != null && cmd.getFilter().isRestricted(DimensionType.Activity)) {
        	SqlQueryBuilder subQuery = SqlQueryBuilder.select("link.AdminEntityId")
        		.from(" LocationAdminLink link " +
        			" LEFT JOIN Location ON (link.LocationId = Location.LocationId) " +
        			" LEFT JOIN Site ON (Location.LocationId = Site.LocationId) ")
        		.where("Site.ActivityId").in(cmd.getFilter().getRestrictions(DimensionType.Activity));
        	
        	query.where("AdminEntity.AdminEntityId").in(subQuery);
        }
        
        final List<AdminEntityDTO> entities = new ArrayList<AdminEntityDTO>();

        query.forEachResult(connection, new SqlQueryBuilder.ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                AdminEntityDTO entity = new AdminEntityDTO();
                entity.setId(rs.getInt(1));
                entity.setName(rs.getString(2));
                entity.setLevelId(rs.getInt(3));

                int parentId = rs.getInt(4);
                if(!rs.wasNull()) {
                    entity.setParentId(parentId);
                }

                BoundingBoxDTO bounds = new BoundingBoxDTO();
                bounds.setX1(rs.getDouble(5));
                bounds.setY1(rs.getDouble(6));
                bounds.setX2(rs.getDouble(7));
                bounds.setY2(rs.getDouble(8));
                if(!rs.wasNull()) {
                    entity.setBounds(bounds);
                }

                entities.add(entity);
            }
        });
		return entities;
	}

}
