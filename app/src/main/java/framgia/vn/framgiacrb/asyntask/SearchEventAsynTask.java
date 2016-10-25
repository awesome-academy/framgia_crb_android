package framgia.vn.framgiacrb.asyntask;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import framgia.vn.framgiacrb.activity.SearchActivity;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.object.EventField;
import framgia.vn.framgiacrb.ui.listener.AsyncTaskFinishListener;
import framgia.vn.framgiacrb.utils.SearchUtil;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

/**
 * Created by framgia on 14/09/2016.
 */
public class SearchEventAsynTask extends AsyncTask<String, Void, Void> implements EventField {
    private Activity mActivity;
    private RealmList mRealmList = new RealmList();
    private AsyncTaskFinishListener mListener;

    public SearchEventAsynTask(Activity activity, RealmList realmList) {
        mActivity = activity;
        mRealmList.addAll(realmList);
    }

    public void setListener(AsyncTaskFinishListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected Void doInBackground(String... params) {
        List list = Realm.getDefaultInstance().where(Event.class)
            .contains(EVENT_TITLE_FIELD, params[0], Case.INSENSITIVE)
            .or()
            .contains(EVENT_DESCRIPTION_FIELD, params[0], Case.INSENSITIVE)
            .findAllSorted(EVENT_START_DATE_FIELD, Sort.ASCENDING);
        if (list.size() == 0) {
            SearchActivity.sIsHasMoreEvent = false;
            return null;
        }
        mRealmList.addAll(SearchUtil.editListDataSearch((OrderedRealmCollection<Event>) list));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mListener != null) {
            mListener.onFinish(mRealmList);
        }
    }
}