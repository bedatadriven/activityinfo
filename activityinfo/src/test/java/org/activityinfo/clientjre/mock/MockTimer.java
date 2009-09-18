package org.activityinfo.clientjre.mock;

import org.activityinfo.client.util.ITimer;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockTimer implements ITimer {

    @Override
    public void schedule(int delayMillis, Callback callback) {
        callback.run();
    }

    @Override
    public void scheduleRepeating(int periodMillis, Callback callback) {
        while(true) {
            callback.run();
        }
    }
}
