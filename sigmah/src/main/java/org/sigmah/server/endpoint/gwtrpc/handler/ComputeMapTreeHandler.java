package org.sigmah.server.endpoint.gwtrpc.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;
import org.sigmah.shared.command.ComputeMapTree;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.MapTree;
import org.sigmah.shared.dao.SqlQueryBuilder;
import org.sigmah.shared.dao.SqlQueryBuilder.ResultHandler;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.BoundingBoxDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class ComputeMapTreeHandler implements CommandHandler<ComputeMapTree>{

	private final HibernateEntityManager entityManager;
	
	@Inject
	public ComputeMapTreeHandler(HibernateEntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public CommandResult execute(ComputeMapTree cmd, User user)
			throws CommandException {
		
		final MapTree result = new MapTree();
		
		entityManager.getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				result.setRoot(buildTree(connection));
			}
		});
		
		return result;
				
	}
	
	private class TreeBuilder {
		MapTree.Node rootNode = new MapTree.Node();
		Map<Integer, MapTree.EntityNode> entityNodes = new HashMap<Integer, MapTree.EntityNode>();
		Map<Integer, MapTree.CountryNode> countryNodes = new HashMap<Integer, MapTree.CountryNode>();

		
		public MapTree.EntityNode getEntityNode(int id) {
			
			MapTree.EntityNode node = entityNodes.get(id);
			if(node == null) {
				node = new MapTree.EntityNode(id);
				entityNodes.put(id, node);
			}
			return node;
		}
		
		public MapTree.CountryNode getCountryNode(int countryId) {
			MapTree.CountryNode node = countryNodes.get(countryId);
			if(node == null) {
				node = new MapTree.CountryNode(countryId);
				node.setParent(rootNode);
				rootNode.addChild(node);
				countryNodes.put(countryId, node);
			}
			return node;		
		
		}
		
		public MapTree.Node getRoot() {
			return rootNode;
		}
		
	}
	
	private MapTree.Node buildTree(Connection connection) {

		final TreeBuilder treeBuilder = new TreeBuilder();
		
		SqlQueryBuilder.select(
				"ae.AdminEntityId", 
				"ae.AdminLevelId", 
				"ae.AdminEntityParentId",
				"ae.Name",
				"al.CountryId", 
				"ae.X1",
				"ae.Y1",
				"ae.X2",
				"ae.Y2",
				"count(s.SiteId)",
				"avg(l.x)",
				"avg(l.y)",
				"min(l.X)",
				"max(l.X)",
				"min(l.Y)",
				"max(l.Y)")
			.from("AdminEntity ae")
			.innerJoin("AdminLevel al").on("ae.AdminLevelId = al.AdminLevelId")
			.innerJoin("LocationAdminLink link").on("ae.AdminEntityId = link.AdminEntityId")
			.innerJoin("Location l").on("link.LocationId = l.LocationId")
			.innerJoin("Site s").on("s.LocationId = l.LocationId")
			.groupBy("ae.AdminEntityId, ae.AdminLevelId, ae.AdminEntityParentId, al.CountryId, ae.X1, ae.X2, ae.Y1, ae.Y2")
			.forEachResult(connection, new ResultHandler() {
				
				@Override
				public void handle(ResultSet rs) throws SQLException {
					MapTree.EntityNode node = treeBuilder.getEntityNode(rs.getInt(1));
					node.setLabel(rs.getString(4));
					node.setBounds(boundsFromRs(rs, 6));
					node.setPointCount(rs.getInt(10));

					int levelId = rs.getInt(2);
					int adminParentId = rs.getInt(3);
					if(rs.wasNull()) {
						node.setParent(treeBuilder.getCountryNode(rs.getInt(5)).getChild(levelId));
					} else {
						node.setParent(treeBuilder.getEntityNode(adminParentId).getChild(levelId));
					}
					node.getParent().addChild(node);
				}
			});
		
		return treeBuilder.getRoot();
	}
	
	private BoundingBoxDTO boundsFromRs(ResultSet rs, int startIndex) throws SQLException {
		BoundingBoxDTO bounds = new BoundingBoxDTO();
		bounds.setX1(rs.getDouble(startIndex+0));
		if(rs.wasNull()) {
			return null;
		}
		bounds.setY1(rs.getDouble(startIndex+1));
		if(rs.wasNull()) {
			return null;
		}
		bounds.setX2(rs.getDouble(startIndex+2));
		if(rs.wasNull()) {
			return null;
		}
		bounds.setY2(rs.getDouble(startIndex+3));
		if(rs.wasNull()) {
			return null;
		}
		return bounds;
	}

	private Double doubleFromRs(ResultSet rs, int index) throws SQLException {
		double value = rs.getDouble(index);
		if(rs.wasNull()) {
			return null;
		}
		return value;
	}
}
