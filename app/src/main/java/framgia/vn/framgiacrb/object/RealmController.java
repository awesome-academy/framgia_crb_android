package framgia.vn.framgiacrb.object;

import android.app.Activity;
import android.app.Application;

import java.util.Date;

import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.Event;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by framgia on 26/07/2016.
 */
public class RealmController implements EventField, CalendarField {
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
    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
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
            .contains(EVENT_TITLE_FIELD, textSearch, Case.INSENSITIVE)
            .or()
            .contains(EVENT_DESCRIPTION_FIELD, textSearch, Case.INSENSITIVE)
            .findAll();
    }
    public Event getEventById(String id) {
        return realm.where(Event.class)
            .equalTo(EVENT_ID_FIELD, id)
            .findFirst();
    }
    public Calendar getCalenderByid(int calendarId) {
        return realm.where(Calendar.class)
            .equalTo(CALENDAR_ID_FIELD, calendarId)
            .findFirst();
    }
}
