package com.lexicalscope.svm.vm;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Search limit that ensures that the execution does not exceed a specified time.
 */
public class TimerSearchLimit implements SearchLimits {
    /**
     * Threshold in milliseconds that the execution should not exceed.
     */
    public final long thresholdMillis;

    /**
     * Timer that schedules the timeout.
     */
    private Timer timer;

    /**
     * True if the vm should keep on executing.
     */
    private boolean withinLimits = true;

    public TimerSearchLimit(long thresholdMillis) {
        this.thresholdMillis = thresholdMillis;
        reset();
    }

    public static TimerSearchLimit limitByTime(int seconds) {
        return new TimerSearchLimit(seconds * 1000);
    }

    @Override
    public boolean withinLimits() {
        return withinLimits;
    }

    @Override
    public void searchedState() {
    }

    @Override
    public void reset() {
        done();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                withinLimits = false;
            }
        }, thresholdMillis);
    }

    @Override public void done() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
