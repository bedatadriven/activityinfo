package org.activityinfo.client.local.sync;


public class SyncException extends RuntimeException {

    private final SyncErrorType type;

    public SyncException(SyncErrorType type) {
        super(type.name());
        this.type = type;
    }

    public SyncException(SyncErrorType type, Throwable caught) {
        this(type);
    }

    public SyncErrorType getType() {
        return type;
    }
}
