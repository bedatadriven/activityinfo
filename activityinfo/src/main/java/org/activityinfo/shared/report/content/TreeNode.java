package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TreeNode<T extends TreeNode> implements Serializable {



	/**
	 * @return This node's children
	 */
	public abstract Collection<T> getChildren();
	
	/**
	 * @return The depth of the subtree starting at this node.
	 * 		(0 if this node has no children, 1 if all of it's children
	 * 			are leaves, 2 if this node has grand children, etc)
	 */
	
	public int getDepth() {
		return calculateDepth(0);
	}
	
	private int calculateDepth(int depth) {
		int maxChildDepth = depth;
		for(T child : getChildren()) {
			int childDepth = child.calculateDepth(depth+1);
			if(maxChildDepth < childDepth) {
				maxChildDepth = childDepth;
			}
		}
		return maxChildDepth;
	}
	
	public List<T> getChildList() {
		return new ArrayList<T>(getChildren());
	}
	
	/**
	 * 
	 * @return True if this node has no children
	 */
	public boolean isLeaf() {
		return getChildren().size() == 0; 
	}

	/**
	 * @return A list of all terminal (leaf) descendant nodes  
	 * 
	 */
	public List<T> getLeaves() {
		List<T> list = new ArrayList<T>();
		if(isLeaf()) {
			list.add((T) this);
		} else {
			findLeaves(list);
		}
		return list;
	}
	
	private void findLeaves(List<T> list) {
		for(T child : getChildren()) {
			if(child.isLeaf()) {
				list.add(child);
			} else {
				child.findLeaves(list);
			}
		}
	}
	
	/**
	 * Returns a list of descendant nodes at the given depth.
	 * 
	 * Examples 
	 * <ul>
	 * <li>depth = 0 returns the current node</li>
	 * <li>depth = 1 returns the current node's children</li>
	 * <li>depth = 2 returns the current node's grand-children</li>
	 * <li>etc</li>
	 * </ul>
	 * 
	 * @param depth
	 * @param placeholders if true, null values are added to the list when 
	 * 			a subtree has no nodes at the given depth
	 * @return
	 */
	public List<T> getDescendantsAtDepth(int depth, boolean placeholders) { 
		
		List<T> list = new ArrayList<T>();
		if(depth == 0) {
			list.add((T)this);
		} else { 
			findDescendantsAtDepthRecursively(list, depth, placeholders);
		}
		return list;		
	}
	
	private void findDescendantsAtDepthRecursively(List<T> list, int depth, boolean placeholders) {
		if(depth==1) {
			if(isLeaf()) {
				if(placeholders) list.add(null);
			} else {
				list.addAll(getChildren());
			}
		} else {
			if(isLeaf()) {
				if(placeholders) list.add(null);
			} else {
				for(T child : getChildren()) {
					child.findDescendantsAtDepthRecursively(list, depth-1, placeholders);
				}
			}
		}
	}
	
	/**
	 * Returns a list of descendant nodes at the given depth.
	 * 
	 * Examples 
	 * <ul>
	 * <li>depth = 0 returns the current node</li>
	 * <li>depth = 1 returns the current node's children</li>
	 * <li>depth = 2 returns the current node's grand-children</li>
	 * <li>etc</li>
	 * </ul>
	 * 
	 * @param depth
	 * @return
	 */
	public List<T> getDescendantsAtDepth(int depth) {
		return getDescendantsAtDepth(depth, false);		
	}

    public abstract String getLabel();
    
}
