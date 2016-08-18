package framgia.vn.framgiacrb.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import framgia.vn.framgiacrb.CrbApplication;
import framgia.vn.framgiacrb.asyntask.RegisterNotificationAsynTask;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.object.RealmController;
import io.realm.Realm;

/**
 * Created by framgia on 18/07/2016.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    public static final String BOOT_COMPLETED_INTENT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_COMPLETED_INTENT)) {
            Realm realm = Realm.getDefaultInstance();
            RegisterNotificationAsynTask registerNotificationAsynTask =
                new RegisterNotificationAsynTask();
            registerNotificationAsynTask.execute(
                realm.where(Event.class).findAll());
        }
    }
}
