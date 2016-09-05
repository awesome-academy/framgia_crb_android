package framgia.vn.framgiacrb.asyntask;

import android.os.AsyncTask;

import java.util.List;

import framgia.vn.framgiacrb.CrbApplication;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.utils.NotificationUtil;
import io.realm.Realm;

/**
 * Created by framgia on 17/08/2016.
 */
public class RegisterNotificationAsyncTask extends AsyncTask<List, Void, Void> {
    private boolean mIsLogin;

    public RegisterNotificationAsyncTask(boolean isLogin) {
        mIsLogin = isLogin;
    }

    @Override
    protected Void doInBackground(List... params) {
        List<Event> eventList = mIsLogin ? params[0] : Realm.getDefaultInstance().where(Event.class)
            .findAll();
        for (Event event : eventList) {
            NotificationUtil
                .registerNotificationEventTime(CrbApplication.getInstanceContext(), event);
        }
        return null;
    }
}
