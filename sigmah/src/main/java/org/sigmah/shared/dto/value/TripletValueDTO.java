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
public class TripletValueDTO extends BaseModelData implements EntityDTO, ListElementItemDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "value.TripletValue";
    }

    // Triplet value id
    @Override
    public int getId() {
        Integer id = (Integer) get("id");
        return id == null ? 0 : id;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Triplets list id
    public int getIdList() {
        Integer idList = (Integer) get("idList");
        return idList == null ? 0 : idList;
    }

    public void setIdList(int listId) {
        set("idList", listId);
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

    public int getIndex() {
        Integer index = (Integer) get("index");
        return index == null ? 0 : index;
    }

    public void setIndex(int index) {
        set("index", index);
    }
}
