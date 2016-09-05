package framgia.vn.framgiacrb.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lethuy on 19/07/2016.
 */
public class Utils {
    public static final String ERROR_JSON = "errors";

    public static String formatTime(int hourOfDay, int minutes) {
        String formatHour, formatMinute;
        if (hourOfDay >= 10) {
            formatHour =  String.valueOf(hourOfDay);
        } else {
            formatHour = "0" + String.valueOf(hourOfDay);
        }

        if (minutes >= 10) {
            formatMinute = String.valueOf(minutes);
        } else {
            formatMinute = "0" + String.valueOf(minutes);
        }
        return formatHour + ":" + formatMinute  + " ";
    }

    public static String formatDate(int dayOfMonth, int monthOfYear, int year) {
        String formatDay, formatMonth;
        if (dayOfMonth >= 10) {
            formatDay =  String.valueOf(dayOfMonth);
        } else {
            formatDay = "0" + String.valueOf(dayOfMonth);
        }

        if (monthOfYear >= 10) {
            formatMonth = String.valueOf(monthOfYear);
        } else {
            formatMonth = "0" + String.valueOf(monthOfYear);
        }
        return formatDay + "-" + formatMonth  + "-" + year;
    }

    public static String getStringFromJson(String stringJson) throws JSONException {
        JSONObject errorJson = new JSONObject(stringJson);
        return errorJson.getString(ERROR_JSON);
    }

}
