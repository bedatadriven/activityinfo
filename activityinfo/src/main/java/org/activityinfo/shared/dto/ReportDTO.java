package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ReportDTO extends BaseModelData implements DTO {

    public ReportDTO() {

    }

    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public String getTitle() {
        return get("title");
    }

    public void setTitle(String value) {
        set("title", value);
    }

    public boolean isFinalized() {
        return (Boolean) get("finalized");
    }

    public void setFinalized(boolean value) {
        set("finalized", value);
    }


}
