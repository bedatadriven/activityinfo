package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
/*
 * @author Alex Bertram
 */

public class MapIconModel extends BaseModelData {


    public MapIconModel() {
    }

    public MapIconModel(String id) {
        setId(id);
    }

    public void setId(String name) {
        set("id", name);
    }

    public String getId() {
        return get("id");
    }

}
