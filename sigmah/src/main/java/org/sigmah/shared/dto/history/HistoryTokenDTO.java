package org.sigmah.shared.dto.history;

import java.util.Date;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity history.HistoryToken.
 * 
 * @author tmi
 * 
 */
public class HistoryTokenDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -93910171075526494L;

    @Override
    public String getEntityName() {
        return "history.HistoryToken";
    }

    // Token id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Token date.
    public Date getDate() {
        return get("date");
    }

    public void setDate(Date date) {
        set("date", date);
    }

    // Token raw value.
    public String getValue() {
        return get("value");
    }

    public void setValue(String value) {
        set("value", value);
    }

    // User email.
    public String getUserEmail() {
        return get("email");
    }

    public void setUserEmail(String email) {
        set("email", email);
    }

    // User first name.
    public String getUserFirstName() {
        return get("firstName");
    }

    public void setUserFirstName(String firstName) {
        set("firstName", firstName);
    }

    // User name.
    public String getUserName() {
        return get("name");
    }

    public void setUserName(String name) {
        set("name", name);
    }
}
