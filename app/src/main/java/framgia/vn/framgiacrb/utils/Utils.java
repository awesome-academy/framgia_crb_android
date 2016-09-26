package framgia.vn.framgiacrb.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.services.AlarmReceiver;

/**
 * Created by lethuy on 19/07/2016.
 */
public class Utils {
    public static final String ERROR_JSON = "errors";

    public static String formatTime(Context context, int hourOfDay, int minutes) {
        return String.format(context.getResources().getString(R.string.time_picker_default),
            hourOfDay, minutes);
    }

    public static String formatDate(Context context, int dayOfMonth, int monthOfYear, int year) {
        return String.format(context.getResources().getString(R.string.date_picker_default),
            dayOfMonth, monthOfYear, year);
    }

    public static String getStringFromJson(String stringJson) throws JSONException {
        JSONObject errorJson = new JSONObject(stringJson);
        return errorJson.getString(ERROR_JSON);
    }

    public static int getColor(Context context, int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    public static boolean isBeforeHourInDate(Date date, Date compareDate) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar2.setTime(compareDate);
        return calendar1.getTimeInMillis() < calendar2.getTimeInMillis();
    }

    public static void cancelAllAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context
            .getSystemService(Context.ALARM_SERVICE);
        SharedPreferences prefs = context.getSharedPreferences(Activity.class.getSimpleName(),
            Context.MODE_PRIVATE);
        int notificationNumber = prefs.getInt(NotificationUtil.NOTIFICATION_ID, 0);
        Intent intent = new Intent(context, AlarmReceiver.class);
        for (int i = 0; i < notificationNumber; i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }
}
