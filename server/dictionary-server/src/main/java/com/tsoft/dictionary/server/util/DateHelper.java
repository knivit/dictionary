package com.tsoft.dictionary.server.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public final class DateHelper {
    private DateHelper() { }

    public static long getCurrentDateGMTAsLong() {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        return calendar.getTimeInMillis();
    }

    public static String dateGMTAsString(long dateGMT) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.US);
        calendar.setTimeInMillis(dateGMT);
        return String.format(Locale.US, "%c", calendar.getTime());
    }
}
