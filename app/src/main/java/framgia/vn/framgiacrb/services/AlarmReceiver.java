package framgia.vn.framgiacrb.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.utils.NotificationUtil;

/**
 * Created by framgia on 18/07/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String INTENT_TITLE = "title";
    public static final String INTENT_CONTENT = "content";
    public static final String INTENT_NOTIFICATION_ID = "notification id";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ((intent.getStringExtra(Constant.INTENT_REPEAT_TYPE)).equals(Constant.REPEAT_DAILY)
            && Calendar.getInstance().getTime()
            .after((Date) intent.getSerializableExtra(Constant.INTENT_END_REPEAT))) {
            int notificationNumber = intent.getIntExtra(AlarmReceiver.INTENT_NOTIFICATION_ID, 0);
            PendingIntent sender =
                PendingIntent.getBroadcast(context, notificationNumber, intent, PendingIntent
                    .FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        } else {
            NotificationUtil.pushNotification(context, intent.getStringExtra(INTENT_TITLE), intent
                    .getStringExtra(INTENT_CONTENT), intent.getStringExtra(Constant.ID_KEY),
                intent.getIntExtra(AlarmReceiver.INTENT_NOTIFICATION_ID, 0)
                , (Date) intent.getSerializableExtra(Constant.INTENT_START_DATE)
                , (Date) intent.getSerializableExtra(Constant.INTENT_FINISH_DATE));
        }
    }
}
