package framgia.vn.framgiacrb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nghicv on 18/07/2016.
 */
public class TimeUtils {
    public static final String DATE_INPUT = "yyy-MM-dd";
    public static final String DATE_OUTPUT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DAY_FORMAT_SINGLE = "d";
    public static final String DAY_FORMAT_COUPLE = "dd";
    public static final String DAY_IN_WEEK_STRING_FORMAT = "EEE";
    public static final String MONTH_STRING_FORMAT = "MMM";
    public static final String MONTH_NUMBER_FORMAT = "MM";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String DATE_FORMAT_TOOLBAR = "d MMMM yyyy";
    private static final String DEVIDE_TIME = " - ";
    private static final String FORMAT_OTHER_YEAR = "HH:mm EEE dd MMM yyyy";
    private static final String FORMAT_THIS_YEAR_OTHER_DAY = "HH:mm EEE dd MMM";
    private static final String FORMAT_TODAY = "HH:mm";
    private static final String GMT_0 = "Europe/London";
    public static final String DATE_STRING_FORMAT = "d MMM yyyy";

    public static Date stringToDate(String dateString, String dateInput) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateInput, Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    public static String toStringDate(Date date) {
        return new SimpleDateFormat(DATE_OUTPUT).format(date);
    }

    public static String toStringDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date formatDate(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_INPUT);
        return format.parse(format.format(date));
    }

    public static String toStringTime(Date date) {
        return new SimpleDateFormat(TIME_FORMAT).format(date);
    }

    public static String toDay(Date date) {
        return new SimpleDateFormat(DAY_FORMAT_SINGLE).format(date);
    }

    public static String toMonth(Date date) {
        return new SimpleDateFormat(MONTH_STRING_FORMAT).format(date);
    }

    public static String toYear(Date date) {
        return new SimpleDateFormat(YEAR_FORMAT).format(date);
    }

    public static String getTimeStringBeauty(Date date) {
        if (date == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR)) {
            return new SimpleDateFormat(FORMAT_OTHER_YEAR, Locale.US).format(date);
        }
        if (calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
            && calendar.get(Calendar.DAY_OF_MONTH) ==
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            return new SimpleDateFormat(FORMAT_TODAY, Locale.getDefault()).format(date);
        }
        return new SimpleDateFormat(FORMAT_THIS_YEAR_OTHER_DAY, Locale.getDefault()).format(date);
    }

    public static String createAmountTime(Date startDate, Date endDate) {
        return getTimeStringBeauty(startDate) + DEVIDE_TIME + getTimeStringBeauty(endDate);
    }

    public static Date formatDateTime(Date date, Date time) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
        return calendar.getTime();
    }

    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TOOLBAR, Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean compareWeek(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR));
    }

    public static boolean compareDate(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
            && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
            && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static Date genFinishTime(Date trueStartTime, Date oldFinishTime) {
        Calendar result = Calendar.getInstance();
        result.setTime(trueStartTime);
        Calendar oldFinishCalendar = Calendar.getInstance();
        oldFinishCalendar.setTime(oldFinishTime);
        result.set(Calendar.HOUR_OF_DAY, oldFinishCalendar.get(Calendar.HOUR_OF_DAY));
        result.set(Calendar.MINUTE, oldFinishCalendar.get(Calendar.MINUTE));
        result.set(Calendar.SECOND, oldFinishCalendar.get(Calendar.SECOND));
        return result.getTime();
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static Date convertToTimeZoneZero(Date date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(GMT_0));
        String dateInZeroTimeZone = simpleDateFormat.format(date);
        return new SimpleDateFormat().parse(dateInZeroTimeZone);
    }
    public static Date convertMillionStringToDate(String million) {
        return new Date(Long.parseLong(million));
    }
}
