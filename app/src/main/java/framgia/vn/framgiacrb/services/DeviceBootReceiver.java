package framgia.vn.framgiacrb.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by framgia on 18/07/2016.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    public static final String BOOT_COMPLETED_INTENT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_COMPLETED_INTENT)) {
        }
    }
}
