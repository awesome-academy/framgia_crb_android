package framgia.vn.framgiacrb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nghicv on 18/07/2016.
 */
public class TimeUtils {
    public static final String DATE_INPUT = "yyy-MM-dd";
    public static final String DATE_OUTPUT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DAY_FORMAT = "d";
    public static final String MONTH_FORMAT = "MMM";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String DATE_FORMAT_TOOLBAR = "d MMMM yyyy";
    private static final String DEVIDE_TIME = " - ";
    private static final String FORMAT_OTHER_YEAR = "HH:mm EEE dd MMM yyyy";
    private static final String FORMAT_THIS_YEAR_OTHER_DAY = "HH:mm EEE dd MMM";
    private static final String FORMAT_TODAY = "HH:mm";

    public static String toStringDate(long milisec) {
        return new SimpleDateFormat(DATE_OUTPUT, Locale.getDefault()).format(new Date(milisec));
    }

    public static long toTime(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_INPUT, Locale.ENGLISH);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    public static Date stringToDate(String dateString, String dateInput) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateInput);
        Date date;
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
        return new SimpleDateFormat(DAY_FORMAT).format(date);
    }
    public static String toMonth(Date date) {
        return new SimpleDateFormat(MONTH_FORMAT).format(date);
    }
    public static String toYear(Date date) {
        return new SimpleDateFormat(YEAR_FORMAT).format(date);
    }

    public static Date convertDateFormat(String date, String givenFormat, String resultFormat) {
        String result = "";
        Date resultDate;
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;

        try {
            sdf = new SimpleDateFormat(givenFormat);
            sdf1 = new SimpleDateFormat(resultFormat);
            result = sdf1.format(sdf.parse(date));
            resultDate = sdf1.parse(result);
        }
        catch(Exception e) {
            e.printStackTrace();
            return new Date();
        }
        finally {
            sdf=null;
            sdf1=null;
        }
        return resultDate;
    }

    public static String getTimeStringBeauty(Date date) {
        if(date == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(calendar.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR)) {
            return new SimpleDateFormat(FORMAT_OTHER_YEAR, Locale.US).format(date);
        }
        if(calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
            && calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            return new SimpleDateFormat(FORMAT_TODAY, Locale.getDefault()).format(date);
        }
        return new SimpleDateFormat(FORMAT_THIS_YEAR_OTHER_DAY, Locale.getDefault()).format(date);
    }

    public static String createAmountTime(Date startDate, Date endDate) {
        return getTimeStringBeauty(startDate) + DEVIDE_TIME + getTimeStringBeauty(endDate);
    }
    @SuppressWarnings("deprecation")
    public static Date formatDateTime(Date date, Date time) {
        date.setHours(time.getHours());
        date.setMinutes(time.getMinutes());
        return date;
    }
}
