package org.activityinfo.server.entity.change;

public class ChangeException extends RuntimeException {

    private ChangeFailureType failureType;
    private String property;
    
    public ChangeException(ChangeFailureType failureType) {
        super(failureType.toString());
        this.failureType = failureType;
    }

    public ChangeException(ChangeFailureType failureType, Exception cause) {
        super(failureType.toString(), cause);
        this.failureType = failureType;
    }
    
    public ChangeException(ChangeFailureType failureType, String property) {
        super(failureType + " (" + property + ")");
        this.failureType = failureType;
        this.property = property;
    }
    
    public ChangeException(Exception e) {
        this(ChangeFailureType.SERVER_FAULT, e);
    }

    public ChangeFailureType getFailureType() {
        return failureType;
    }
    
    public String getProperty() {
        return property;
    }
}
