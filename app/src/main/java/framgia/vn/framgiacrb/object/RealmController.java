package framgia.vn.framgiacrb.object;

import android.app.Activity;
import android.app.Application;

import framgia.vn.framgiacrb.data.model.Event;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by framgia on 26/07/2016.
 */
public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }
    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }
    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }
    public RealmResults<Event> getAllEvent() {
        return realm.where(Event.class).findAll();
    }
    public RealmResults<Event> searchEvent(String textSearch) {
        return realm.where(Event.class)
            .contains("author", "Author 0")
            .or()
            .contains("title", "Realm")
            .findAll();
    }
}
