package org.activityinfo.server.mail;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Bounce {
    
    @JsonProperty("ID")
    private String id;
    
    @JsonProperty("Type")
    private String type;
    
    @JsonProperty("TypeCode")
    private int typeCode;
    
    @JsonProperty("Tag")
    private String tag;
    
    @JsonProperty("MessageID")
    private String messageId;
    
    @JsonProperty("Email")
    private String email;
    
    @JsonProperty("BouncedAt")
    private String bounceDate;
    
    @JsonProperty("Details")
    private String details;
    
    @JsonProperty("DumpAvailable")
    private boolean dumpAvailable;
    
    @JsonProperty("CanActivate")
    private boolean canActivate;
    
    @JsonProperty("Subject")
    private String subject;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getTag() {
        return tag;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getEmail() {
        return email;
    }

    public String getBounceDate() {
        return bounceDate;
    }

    public String getDetails() {
        return details;
    }

    public boolean isDumpAvailable() {
        return dumpAvailable;
    }

    public boolean isCanActivate() {
        return canActivate;
    }

    public String getSubject() {
        return subject;
    }
}
