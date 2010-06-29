/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import org.sigmah.shared.report.content.PivotContent;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class that exposes properties common to 
 * the PivotTable and PivotChart elements
 * 
 * @author Alex Bertram
 *
 */
public abstract class PivotElement<ContentT extends PivotContent> extends ReportElement<ContentT> {


	/**
	 * Returns the set of all dimensions that figure in the 
	 * pivot table/chart, whether in the row, column, horizantal
	 * axis, etc.
	 * 
	 * @return
	 */
	public abstract Set<Dimension> allDimensions();

	/**
	 * Returns the set of all dimension <i>types</i> that figures in the 
	 * pivot table/chart. See {@link #allDimensions()}
	 * 
	 * @return
	 */
	public Set<DimensionType> allDimensionTypes() {
		Set<DimensionType> set = new HashSet<DimensionType>();
		
		for(Dimension dimension : allDimensions()) {
			set.add(dimension.getType());
		}
		return set;
	}

    public ContentT getContent() {
        return content;
    }

    public void setContent(ContentT content) {
        this.content = content;
    }
}
