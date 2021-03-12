package com.sword.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Util class in common
 *
 * @author max
 */
public class Util {
    public static String formatLogTime(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss.S");
        f.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return f.format(date);
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return f.format(date);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        f.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return f.format(date);
    }

    public static Date parseDateTime(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (Exception e) {
            Logger.console(e.getMessage());
        }
        return date;
    }

    public static String today() {
        return Util.formatDate(new Date());
    }

    public static String now() {
        return Util.formatDateTime(new Date());
    }

    public static String logTime() {
        return Util.formatLogTime(new Date());
    }

    public static String[] split(String srcStr, int separate) {
        if (srcStr == null || separate <= 0) return null;

        String[] splits = new String[separate + 1];

        int one = srcStr.length() / separate;
        for (int n = 0; n < separate; n++) {
            splits[n] = srcStr.substring(n * one, (n + 1) * one);
        }
        if (srcStr.length() % separate > 0)
            splits[separate] = srcStr.substring(separate * one);
        else
            splits[separate] = "";

        return splits;
    }

    public static String concat(String[] splits) {
        String retStr = "";
        for (int p = 0; p < splits.length; p++) {
            if (splits[p] != null) retStr += splits[p];
        }
        return retStr;
    }
}
