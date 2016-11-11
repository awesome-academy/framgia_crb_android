package framgia.vn.framgiacrb.data.local;

import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.CalendarField;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.EventField;
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

    public RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {
        if (instance == null) {
            instance = new RealmController();
        }
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
            .findAllSorted(EVENT_START_DATE_FIELD, Sort.ASCENDING);
    }

    public Event getEventById(int id) {
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
