package framgia.vn.framgiacrb.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        NotificationUtil.pushNotification(context, intent.getStringExtra(INTENT_TITLE), intent
                .getStringExtra(INTENT_CONTENT), intent.getStringExtra(Constant.ID_KEY),
            intent.getIntExtra(AlarmReceiver.INTENT_NOTIFICATION_ID, 0));
    }
}
