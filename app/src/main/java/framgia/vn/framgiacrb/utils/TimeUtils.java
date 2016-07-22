package framgia.vn.framgiacrb.utils;

import android.text.format.DateFormat;
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
    public static final String DATE_INPUT = "yyy-MM-dd";
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

    public static Date formatDate(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_INPUT);
        Date dateFormat = new Date();
        dateFormat = format.parse(format.format(date));
        return dateFormat;
    }

}
