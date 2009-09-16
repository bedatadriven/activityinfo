package org.activityinfo.client.util;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface ITimer {


    public interface Callback {
        public void run();
    }

    public void schedule(int delayMillis, Callback callback);

    public void scheduleRepeating(int periodMillis, Callback callback);
    
}
