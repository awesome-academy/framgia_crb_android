package framgia.vn.framgiacrb.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import framgia.vn.framgiacrb.constant.Constant;

/**
 * Created by nghicv on 18/07/2016.
 */
public class TimeUtils {
    public static final String DATE_INPUT = "dd-MM-yyyy";
    public static final String DATE_OUTPUT = "dd-MM-yyyy";
    public static String toStringDate(long milisec) {
        String dateString = new SimpleDateFormat(DATE_OUTPUT).format(new Date(milisec));
        return dateString;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_INPUT, Locale.getDefault());
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

    public static List<Date> generateRangeDate() {
        long s = System.currentTimeMillis();
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date startDate = stringToDate(Constant.START_DATE);
        calendar.setTime(startDate);
        while (calendar.getTime().before(stringToDate(Constant.END_DATE))) {
            calendar.add(Calendar.DATE, 1);
            dates.add(calendar.getTime());
        }
        return dates;
    }
}
