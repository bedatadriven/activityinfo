package org.activityinfo.client.util;

import com.google.gwt.user.client.Timer;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GWTTimerImpl implements ITimer {

    @Override
    public void schedule(int delayMillis, final Callback callback) {
        Timer timer = new Timer() {

            @Override
            public void run() {
                callback.run();
            }
        };
        timer.schedule(delayMillis);
    }

    @Override
    public void scheduleRepeating(int periodMillis, final Callback callback) {
           Timer timer = new Timer() {

            @Override
            public void run() {
                callback.run();
            }
        };
        timer.scheduleRepeating(periodMillis);
    }
}
