package framgia.vn.framgiacrb.listener;

import io.realm.RealmList;

public interface AsyncTaskFinishListener {
    void onFinish(RealmList list);
}