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
public class IndicatorsListValueDTO extends BaseModelData implements EntityDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;
	
	@Override
	public String getEntityName() {
		return "Indicators list value";
	}
	
	// Indicators list value id
	@Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }
    
	// Indicators list value list id
    public int getIdList() {
        return (Integer) get("idList");
    }

    public void setIdList(int idList) {
        set("idList", idList);
    }
	
	// Indicator reference
    
}
