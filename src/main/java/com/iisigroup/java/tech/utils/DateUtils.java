package com.iisigroup.java.tech.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class DateUtils.
 */
public class DateUtils {

    /**
     * Instantiates a new date utils.
     */
    private DateUtils() {
    }

    /** The sdf. */
    private static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /** The sdf time. */
    private static ThreadLocal<SimpleDateFormat> sdfTime = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss.SSS");
        }
    };

    /**
     * Gets the now date.
     * 
     * @return the now date
     */
    public static String getNowDate() {

        return formatByyyyyMMdd(new Date());

    }

    /**
     * Gets the now time.
     * 
     * @return the now time
     */
    public static String getNowTime() {
        final SimpleDateFormat threadSdf = sdfTime.get();
        return threadSdf.format(new Date());

    }

    /**
     * Format byyyyy m mdd.
     * 
     * @param date
     *            the date
     * @return the string
     */
    public static String formatByyyyyMMdd(final Date date) {
        final SimpleDateFormat threadSdf = sdf.get();
        return threadSdf.format(date);
    }
}
