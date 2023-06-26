package com.example.timer.utils;

public class TimerUtils {

    private static final long MINUTES_TO_HOUR = 60L;
    private static final long SECONDS_TO_MINUTES = 60L;

    private TimerUtils() {
        // only for deserialization
    }

    public static Integer getMilliSecondsForTimeUnit(String editText, TimerValues timerValues) {
        int value = getTimeOrZero(editText);

        switch (timerValues) {
            case HOUR:
                value = Math.min(value, TimerValues.HOUR.getMaxValue());
                break;
            case MINUTE:
            case SECOND:
                value = Math.min(value, TimerValues.MINUTE.getMaxValue());
        }

        return value;
    }

    public static long getTotalMilliSeconds(int hours, int minutes, int seconds) {
        return ((hours * SECONDS_TO_MINUTES * MINUTES_TO_HOUR) + (minutes * MINUTES_TO_HOUR) + seconds) * 1000L;
    }

    public static int getTimeOrZero(String editText) {
        try {
            return Integer.parseInt(editText);
        } catch (Exception e) {
            return 0;
        }
    }

}
