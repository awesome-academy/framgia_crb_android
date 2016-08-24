package framgia.vn.framgiacrb.asyntask;

import android.os.AsyncTask;

import java.util.List;

import framgia.vn.framgiacrb.CrbApplication;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.utils.NotificationUtil;

/**
 * Created by framgia on 17/08/2016.
 */
public class RegisterNotificationAsynTask extends AsyncTask<List, Void, Void> {
    @Override
    protected Void doInBackground(List... params) {
        int length = params[0].size();
        for (int i = 0; i < length; i++) {
            Event event = (Event) params[0].get(i);
            NotificationUtil
                .registerNotificationEventTime(CrbApplication.getInstanceContext(), event);
        }
        return null;
    }
}
