/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model;


public class AdminDimension extends Dimension {

	private int levelId;
	
	// required for serialization 
    @SuppressWarnings("unused")
	private AdminDimension() {
    	super(DimensionType.AdminLevel);
    }

    public AdminDimension(int levelId) {
    	super(DimensionType.AdminLevel);
		this.levelId = levelId;
    }
    
	public AdminDimension(String caption, int levelId) {
		super(DimensionType.AdminLevel);
		this.set("caption",caption);	
		this.set("id", "admin_dim_"  + levelId); 
		this.levelId = levelId;
    }

	public int getLevelId() {
		return this.levelId;
	}

    @Override
	public boolean equals(Object other) {
		if(this==other) {
            return true;
        }
		if(other==null) {
            return false;
        }
		if(!(other instanceof AdminDimension)) {
            return false;
        }

		AdminDimension that = (AdminDimension)other;
		return this.levelId == that.levelId;
	}

    @Override
    public int hashCode() {
    	return levelId;
    }
}
