package framgia.vn.framgiacrb.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import framgia.vn.framgiacrb.asyntask.RegisterNotificationAsyncTask;
import framgia.vn.framgiacrb.data.model.Event;
import io.realm.Realm;

/**
 * Created by framgia on 18/07/2016.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    public static final String BOOT_COMPLETED_INTENT = "android.intent.action.BOOT_COMPLETED";
    public static final String REBOOT_COMPLETED_INTENT = "android.intent.action.REBOOT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_COMPLETED_INTENT) ||
            intent.getAction().equals(REBOOT_COMPLETED_INTENT)) {
            RegisterNotificationAsyncTask registerNotificationAsyncTask =
                new RegisterNotificationAsyncTask();
            registerNotificationAsyncTask
                .execute(Realm.getDefaultInstance().where(Event.class).findAll());
        }
    }
}
