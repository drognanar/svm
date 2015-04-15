package com.lexicalscope.svm.vm;

/**
 * Search limit that ensures that the execution does not exceed a specified time.
 */
public class TimerSearchLimit implements SearchLimits {
    /**
     * Time in milliseconds when the execution started.
     */
    public long startTime;

    /**
     * Threshold in milliseconds that the execution should not exceed.
     */
    public final long thresholdMillis;

    public TimerSearchLimit(long thresholdMillis) {
        this.thresholdMillis = thresholdMillis;
        reset();
    }

    public static TimerSearchLimit limitByTime(int seconds) {
        return new TimerSearchLimit(seconds * 1000);
    }

    @Override
    public boolean withinLimits() {
        long currentTime = System.currentTimeMillis();
        return currentTime - startTime <= thresholdMillis;
    }

    @Override
    public void searchedState() {
    }

    @Override
    public void reset() {
        startTime = System.currentTimeMillis();
    }
}
