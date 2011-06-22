package org.sigmah.shared.command.result;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

import org.sigmah.shared.dto.BoundingBoxDTO;

public class MapTree implements CommandResult {
	
	public static class Node<C extends Node> {
		private String label;
		private BoundingBoxDTO bounds;
		private double centerLat;
		private double centerLng;
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
		public BoundingBoxDTO getBounds() {
			return bounds;
		}
		public void setBounds(BoundingBoxDTO bounds) {
			this.bounds = bounds;
		}
		public double getCenterLat() {
			return centerLat;
		}
		public void setCenterLat(double centerLat) {
			this.centerLat = centerLat;
		}
		public double getCenterLng() {
			return centerLng;
		}
		public void setCenterLng(double centerLng) {
			this.centerLng = centerLng;
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
		
	}
	
	
	private Node root;

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
}
