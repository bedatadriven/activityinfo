package org.sigmah.shared.dto.history;

import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class HistoryTokenListDTO extends BaseModelData {

    private static final long serialVersionUID = 9137525003889062584L;

    // Token date.
    public Date getDate() {
        return get("date");
    }

    public void setDate(Date date) {
        set("date", date);
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

    // Tokens.
    public List<HistoryTokenDTO> getTokens() {
        return get("tokens");
    }

    public void setTokens(List<HistoryTokenDTO> tokens) {
        set("tokens", tokens);
    }
}
