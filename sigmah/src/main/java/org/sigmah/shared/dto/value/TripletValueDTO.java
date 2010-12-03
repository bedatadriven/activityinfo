/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.value;

import org.sigmah.shared.dto.element.handler.ValueEvent.ChangeType;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class TripletValueDTO extends BaseModelData implements ListEntityDTO, ListableValue {

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

    @Override
    public int getIndex() {
        final Object index = get("index");
        return index != null ? (Integer) index : -1;
    }

    @Override
    public void setIndex(int index) {
        set("index", index);
    }

    // Chnage type for history.
    public ChangeType getType() {
        return get("type");
    }

    public void setType(ChangeType type) {
        set("type", type);
    }
}
