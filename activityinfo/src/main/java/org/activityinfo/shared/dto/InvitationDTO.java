package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;
/*
 * @author Alex Bertram
 */

public class InvitationDTO extends BaseModel {

    public InvitationDTO() {
    }

    public void setUserId(int userId) {
        set("userId", userId);
    }

    public int getUserId() {
        return (Integer) get("userId");
    }

    public String getUserEmail() {
        return get("userEmail");
    }

    public void setUserEmail(String email) {
        set("userEmail", email);
    }

    public void setUserName(String name) {
        set("userName", name);
    }

    public String getUserName() {
        return get("userName");
    }


    public Integer getSubscriptionFrequency() {
        return get("subscriptionFrequency");
    }

    public void setSubscriptionFrequency(Integer frequency) {
        set("subscriptionFrequency", frequency);
    }

    public Integer getSubscriptionDay() {
        return get("subscriptionDay");
    }

    public void setSubscriptionDay(Integer day) {
        set("subscriptionDay", day);
    }
    
}
