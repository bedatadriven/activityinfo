package org.sigmah.shared.command.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sigmah.shared.report.content.LatLng;
import org.sigmah.shared.report.content.Point;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;
import org.sigmah.shared.util.mapping.TileMath;


public class MapTree implements CommandResult {
	
	public static class Node<C extends Node> {
		private String label;
		private BoundingBoxDTO adminBounds;
		private BoundingBoxDTO siteBounds;
		private LatLng center;
		private int pointCount;
		private Node<?> parent;
		protected List<C> children = new ArrayList<C>(0);
		private int childCount;
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		
		/**
		 * 
		 * @return the bounding box defined for this administrative 
		 * entity. will be null if the bounding box for this entity is unknown
		 * or not available.
		 */
		public BoundingBoxDTO getAdminBounds() {
			return adminBounds;
		}
		
		public void setAdminBounds(BoundingBoxDTO bounds) {
			this.adminBounds = bounds;
		}
		
		/**
		 * 
		 * @return the bounding box of the site points that fall within this 
		 * bounding box. Will be null if none of the sites in this entity have 
		 * 
		 */
		public BoundingBoxDTO getSiteBounds() {
			return siteBounds;
		}
		public void setSiteBounds(BoundingBoxDTO siteBounds) {
			this.siteBounds = siteBounds;
		}
		public LatLng getCenter() {
			return center;
		}
		public void setCenter(LatLng center) {
			this.center = center;
		}
		public int getPointCount() {
			return pointCount;
		}
		public void setPointCount(int pointCount) {
			this.pointCount = pointCount;
		}
		public Node getParent() {
			return parent;
		}
		public void setParent(Node parent) {
			this.parent = parent;
		}		
		public List<C> getChildren() {
			return children;
		}
		
		public void setChildren(List<C> children) {
			this.children = children;
		}
		
		public void addChild(C node) {
			this.children.add(node);
		}
		
		public int getChildCount() {
			return childCount;
		}
		public void setChildCount(int childCount) {
			this.childCount = childCount;
		}
		@Override
		public String toString() {
			return "Node" + Long.toHexString(hashCode());
		}
		public boolean hasBounds() {
			return adminBounds != null;
		}
		
		/**
		 * 
		 * @return computes the bounds of this node by recursively adding the bounds of
		 * all descendant nodes
		 */
		public BoundingBoxDTO computeBounds() {
			BoundingBoxDTO bbox = new BoundingBoxDTO(adminBounds == null ? 
					BoundingBoxDTO.empty() : adminBounds);
			expand(bbox, this);
			return bbox;
			
		}
		private void expand(BoundingBoxDTO bbox, Node<?> node) {
			for(Node child : node.getChildren()) {
				if(child.hasBounds()) {
					bbox.grow(child.getAdminBounds());
				}
				expand(bbox, child);
			}
		}
		
		/**
		 * Recursively corrects any site points that may be out of order. 
		 * These should all be validated at data entry but there is
		 * some crap still left in the database that needs to be cleaned out. 
		 */
		public void normalizeGeography(BoundingBoxDTO parentBounds) {
			
			BoundingBoxDTO myMaxBounds = adminBounds == null ? parentBounds :
						parentBounds.intersect(adminBounds);
			
			if(adminBounds != null && !parentBounds.contains(adminBounds) ) {
				adminBounds = myMaxBounds;
			}
			if(siteBounds != null &&
					!myMaxBounds.contains(siteBounds)) {
				siteBounds = myMaxBounds.intersect(siteBounds);
			}
			
			if(!myMaxBounds.contains(center)) {
				center = myMaxBounds.centroid();
			}
			
			for(C child : children) {
				child.normalizeGeography(myMaxBounds);
			}
		}
		
		/**
		 * Checks to see if the children of this node overlap at a given
		 * zoom level.
		 * 
		 * @param zoomLevel
		 * @return
		 */
		public boolean childrenOverlap(int zoomLevel) {
			for(int i=0;i<children.size();++i) {
				for(int j=i+1;j<children.size();++j) {
					Point p_i = TileMath.fromLatLngToPixel(children.get(i).getCenter(), zoomLevel);
					Point p_j = TileMath.fromLatLngToPixel(children.get(j).getCenter(), zoomLevel);
					
					if(p_i.distance(p_j) < 5) {
						return true;
					}
					
				}
			}
			return false;
		}
		
		public Set<Node> nodesToPlot(int zoomLevel) {
			Set<Node> toPlot = new HashSet<Node>();
			for(Node entity : children) {
				toPlot.addAll(entity.nodesToPlot(zoomLevel));
			}
			return toPlot;
		}
		
	}
	
	public static class CountryNode extends Node<LevelNode> {
		
		private int countryId;

		public CountryNode(int id) {
			this.countryId = id;
		}

		public int getCountryId() {
			return countryId;
		}

		@Override
		public String toString() {
			return "C" + countryId;
		}
		
		/**
		 * Returns the level associated with the given AdminLevelId, creating
		 * a new node if necessary
		 * 
		 * @param levelId
		 * @return
		 */
		public LevelNode getChild(int levelId) {
			for(LevelNode child : children) {
				if(child.getAdminLevelId() == levelId) {
					return child;
				}
			}
			LevelNode child = new LevelNode(levelId);
			children.add(child);
			return child;
		}
		
		@Override
		public Set<Node> nodesToPlot(int zoomLevel) {
			// only one level can be mapped
			for(LevelNode levelChild : children) {
				if(!levelChild.childrenOverlap(zoomLevel)) {
					return levelChild.nodesToPlot(zoomLevel);
				}
			}
			// none of the child levels have candidates that can be mapped, 
			// we will have to make do with this node
			return Collections.singleton((Node)this);
		}
	}
	
	public static class LevelNode extends Node<EntityNode> {
		
		private int adminLevelId;

		public LevelNode(int adminLevelId) {
			super();
			this.adminLevelId = adminLevelId;
		}

		public int getAdminLevelId() {
			return adminLevelId;
		}

		public void setAdminLevelId(int adminLevelId) {
			this.adminLevelId = adminLevelId;
		}

		@Override
		public String toString() {
			return "L" + adminLevelId;
		}

		@Override
		public Set<Node> nodesToPlot(int zoomLevel) {
			Set<Node> toPlot = new HashSet<Node>();
			for(EntityNode entity : children) {
				toPlot.addAll(entity.nodesToPlot(zoomLevel));
			}
			return toPlot;
		}
		

		
	}
	
	public static class EntityNode extends Node<LevelNode> {
		private int adminEntityId;
		
		public EntityNode(int adminEntityId) {
			super();
			this.adminEntityId = adminEntityId;
		}

		public int getAdminEntityId() {
			return adminEntityId;
		}
		
		/**
		 * Returns the level associated with the given AdminLevelId, creating
		 * a new node if necessary
		 * 
		 * @param levelId
		 * @return
		 */
		public LevelNode getChild(int levelId) {
			for(LevelNode child : children) {
				if(child.getAdminLevelId() == levelId) {
					return child;
				}
			}
			LevelNode child = new LevelNode(levelId);
			children.add(child);
			return child;
		}

		@Override
		public String toString() {
			return getLabel() + "[" + adminEntityId + "]";
		}
		
		@Override
		public Set<Node> nodesToPlot(int zoomLevel) {
			// only one level can be mapped
			for(LevelNode levelChild : children) {
				if(!levelChild.childrenOverlap(zoomLevel)) {
					return levelChild.nodesToPlot(zoomLevel);
				}
			}
			// none of the child levels have candidates that can be mapped, 
			// we will have to make do with this node
			return Collections.singleton((Node)this);
		}
		
	}
	
	
	private Node root;

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
}
