package framgia.vn.framgiacrb.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.services.AlarmReceiver;

/**
 * Created by framgia on 18/07/2016.
 */
public class NotificationUtil {
    public static final int TIME_EARLY = 1000 * 60 * 5;
    public static final String NOTIFICATION_ID = "notificationNumber";

    public static void registerNotificationEventTime(Context context, Date time, String title, String content) {
        time.setTime(time.getTime() - TIME_EARLY);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra(AlarmReceiver.INTENT_TITLE, title);
        alarmIntent.putExtra(AlarmReceiver.INTENT_CONTENT, content);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, time.getTime(), pendingIntent);
    }

    public static void pushNotification(Context context, String title, String content) {
        SharedPreferences prefs = context.getSharedPreferences(Activity.class.getSimpleName(),
            Context.MODE_PRIVATE);
        int notificationNumber = prefs.getInt(NOTIFICATION_ID, 0);
        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_today_white_24dp)
                .setContentTitle(title)
                .setContentText(content);
        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationNumber, builder.build());
        SharedPreferences.Editor editor = prefs.edit();
        notificationNumber++;
        editor.putInt(NOTIFICATION_ID, notificationNumber);
        editor.commit();
    }
}
