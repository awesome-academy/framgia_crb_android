package framgia.vn.framgiacrb.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import framgia.vn.framgiacrb.CrbApplication;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.utils.GoogleCalendarUtil;
import framgia.vn.framgiacrb.utils.NotificationUtil;

/**
 * Created by framgia on 04/01/2017.
 */
public class GoogleCalendarChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List listGoogleCalendar = GoogleCalendarUtil.getAllGoogleEvent(CrbApplication
            .getInstanceContext());
        SharedPreferences sharedPreferences = context
            .getSharedPreferences(Constant.GoogleCalendar.PREF_SAVE_ACCOUNT, Context.MODE_PRIVATE);
        int size = listGoogleCalendar.size();
        if (sharedPreferences.getInt(Constant.SharePreferenceKey
            .OLD_NUM_GOOGLE_EVENT, 0) < size) {
            sharedPreferences.edit()
                .putInt(Constant.SharePreferenceKey.OLD_NUM_GOOGLE_EVENT, size)
                .commit();
            Event newGoogleEvent = (Event) listGoogleCalendar.get(size - 1);
            NotificationUtil.registerNotificationEventTime(context, newGoogleEvent);
        }
    }
}
