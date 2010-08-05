/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.value;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public class TripletValueDTO extends BaseModelData implements EntityDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;
	
	@Override
	public String getEntityName() {
		return "Triplet value";
	}
	
	// Triplet value id
	@Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }
	
	// Triplets list id
    public int getListId() {
        return (Integer) get("listId");
    }

    public void setListId(int listId) {
        set("listId", listId);
    }
    
    // Triplet value code
    public String getCode() {
        return get("code");
    }

    public void setCode(String code) {
        set("code", code);
    }
    
    // Triplet value name
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }
    
    // Triplet value period
    public String getPeriod() {
        return get("period");
    }

    public void setPeriod(String period) {
        set("period", period);
    }
    
}
