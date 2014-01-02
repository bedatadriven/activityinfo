package org.activityinfo.server.entity.change;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class ChangeException extends RuntimeException {

    private ChangeFailureType failureType;
    private String property;
    private Set<? extends ConstraintViolation<?>> violations;
    
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
    
    public ChangeException(String property, Set<? extends ConstraintViolation<?>> violations) {
        super(ChangeFailureType.CONSTRAINT_VIOLATION + " (" + property + ": " + toString(violations) + ")");
        this.failureType = ChangeFailureType.CONSTRAINT_VIOLATION;
        this.property = property;
        this.violations = violations;
    }
    
    private static String toString(Set<? extends ConstraintViolation<?>> violations) {
        StringBuilder sb = new StringBuilder();
        for(ConstraintViolation<?> violation : violations) {
            if(sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(violation.getMessage());
        }
        return sb.toString();
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
