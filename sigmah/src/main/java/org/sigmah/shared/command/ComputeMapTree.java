package org.sigmah.shared.command;

import org.sigmah.shared.command.result.MapTree;
import org.sigmah.shared.dao.Filter;

/**
 * Computes a heirarchy of points visible to the user that can be used
 * for incremental construction of multi-resolution maps.
 * 
 * @author alexander
 *
 */
public class ComputeMapTree implements Command<MapTree> {

	/**
	 * the filter to be applied to the tree
	 */
	private Filter filter;
	
}
