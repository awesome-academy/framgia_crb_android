package framgia.vn.framgiacrb.ui.listener;

import io.realm.RealmList;

public interface AsyncTaskFinishListener {
    void onFinish(RealmList list);
}