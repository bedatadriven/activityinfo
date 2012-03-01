/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.SitePointDTO;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

/**
 * @author Alex Bertram
 */
public class SitePointList implements CommandResult {

    private BoundingBoxDTO bounds;
    private List<SitePointDTO> points;

    private SitePointList() {

    }

    public SitePointList(BoundingBoxDTO bounds, List<SitePointDTO> points) {
        this.bounds = bounds;
        this.points = points;
    }

    public BoundingBoxDTO getBounds() {
        return bounds;
    }

    public void setBounds(BoundingBoxDTO bounds) {
        this.bounds = bounds;
    }

    public List<SitePointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<SitePointDTO> points) {
        this.points = points;
    }
    
    public static SitePointList fromSitesList(List<SiteDTO> sites) {
    	SitePointList result = new SitePointList();
    	
    	if (result.getPoints() == null) {
    		result.setPoints(new ArrayList<SitePointDTO>());
    	}
    	
    	if (sites != null) {
	    	for (SiteDTO site : sites) {
	    		 result.getPoints().add(SitePointDTO.fromSite(site));
	    	}
    	}
    	
    	return result;
    }
}
