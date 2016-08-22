package framgia.vn.framgiacrb.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import framgia.vn.framgiacrb.CrbApplication;
import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.activity.DetailActivity;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.services.AlarmReceiver;

/**
 * Created by framgia on 18/07/2016.
 */
public class NotificationUtil {
    public static final int TIME_EARLY = 1000 * 60 * 5;
    public static final String NOTIFICATION_ID = "notificationNumber";

    public static void registerNotificationEventTime(Context context, Date timeReal, String title,
                                                     String content, String eventId) {
        if (timeReal.before(Calendar.getInstance().getTime())) {
            return;
        }
        Date time = (Date) timeReal.clone();
        time.setTime(time.getTime() - TIME_EARLY);
        SharedPreferences prefs = context.getSharedPreferences(Activity.class.getSimpleName(),
            Context.MODE_PRIVATE);
        int notificationNumber = prefs.getInt(NOTIFICATION_ID, 0);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra(AlarmReceiver.INTENT_TITLE, title);
        alarmIntent.putExtra(AlarmReceiver.INTENT_CONTENT, content);
        alarmIntent.putExtra(AlarmReceiver.INTENT_NOTIFICATION_ID, notificationNumber);
        alarmIntent.putExtra(Constant.ID_KEY, eventId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context
            , notificationNumber, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, time.getTime(), pendingIntent);
        SharedPreferences.Editor editor = prefs.edit();
        notificationNumber++;
        editor.putInt(NOTIFICATION_ID, notificationNumber);
        editor.commit();
    }

    public static void pushNotification(Context context, String title, String content, String
        eventId, int notificationNumber) {
        SharedPreferences prefs = context.getSharedPreferences(Activity.class.getSimpleName(),
            Context.MODE_PRIVATE);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Constant.ID_KEY, eventId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
            notificationNumber, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_today_white_24dp)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationNumber, builder.build());
    }
    public static void clearNotification() {
        NotificationManager notificationManager = (NotificationManager)
            CrbApplication.getInstanceContext().
            getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
