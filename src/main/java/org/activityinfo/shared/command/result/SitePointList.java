/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.SitePointDTO;
import org.activityinfo.shared.util.mapping.Extents;

/**
 * @author Alex Bertram
 */
public class SitePointList implements CommandResult {

    private Extents bounds;
    private List<SitePointDTO> points;

    private SitePointList() {

    }

    public SitePointList(Extents bounds, List<SitePointDTO> points) {
        this.bounds = bounds;
        this.points = points;
    }

    public Extents getBounds() {
        return bounds;
    }

    public void setBounds(Extents bounds) {
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
