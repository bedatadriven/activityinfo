package org.activityinfo.client.local.sync;

import com.bedatadriven.rebar.appcache.client.AppCacheException;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;

public enum SyncErrorType {

    CONNECTION_PROBLEM,
    
    NEW_VERSION,
    
    INVALID_AUTH,
    
    APPCACHE_TIMEOUT, 
    
    UNEXPECTED_EXCEPTION;
    
    public static SyncErrorType fromException(Throwable caught) {
        if(caught instanceof SyncException) {
            return ((SyncException) caught).getType();
        }
        if(caught instanceof IncompatibleRemoteServiceException) {
            return SyncErrorType.NEW_VERSION;
        } else if(caught instanceof InvocationException) {
            return SyncErrorType.CONNECTION_PROBLEM;
        } else if(caught instanceof AppCacheException) {
            AppCacheException ace = (AppCacheException) caught;
            switch(ace.getErrorType()) {
            case CONNECTION:
                return SyncErrorType.CONNECTION_PROBLEM;
            case OBSOLETE:
                // the cache gets marked as obsolete if the user's auth expires
                return SyncErrorType.INVALID_AUTH;
            case TIMEOUT:
                return SyncErrorType.APPCACHE_TIMEOUT;
            }
        }
        return SyncErrorType.UNEXPECTED_EXCEPTION;
    }
    
}
