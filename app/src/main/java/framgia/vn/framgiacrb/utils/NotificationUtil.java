package framgia.vn.framgiacrb.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import framgia.vn.framgiacrb.services.AlarmReceiver;

/**
 * Created by framgia on 18/07/2016.
 */
public class NotificationUtil {
    public static final int TIME_EARLY = 5;

    public static void registerNotificationEventTime(Context context, Calendar time) {
        time.add(Calendar.MINUTE, -TIME_EARLY);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }
}
