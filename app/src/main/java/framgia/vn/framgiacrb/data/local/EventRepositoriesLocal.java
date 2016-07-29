package framgia.vn.framgiacrb.data.local;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.data.EventRepository;
import framgia.vn.framgiacrb.data.OnLoadEventListener;
import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.Event;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by nghicv on 26/07/2016.
 */
public class EventRepositoriesLocal implements EventRepository{

    public static final String START_DATE_FIELD = "mStartTime";
    public static final String ID_FIELD = "mId";
    public static final String EVENT_ID_FIELD = "mEventId";

    private Realm mRealm;

    public EventRepositoriesLocal(Realm realm) {
        mRealm = realm;
    }

    @Override
    public void insertEvent(Event event) {
    }

    public void addEvents(final List<Event> events, final OnLoadEventListener onLoadEventListener) {

        final List<Event> realmEvents = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            if (!isExists(events.get(i))) {
                realmEvents.add(events.get(i));
            }
        }
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(realmEvents);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (onLoadEventListener != null) {
                    onLoadEventListener.onSuccess();
                }
            }
        });
    }

    @Override
    public void deleteEvent(Event event) {

    }

    @Override
    public void updateEvent(Event event) {

    }

    @Override
    public RealmResults<Event> getAllEvents(int userId) {
        return mRealm.where(Event.class).findAll();
    }

    @Override
    public void getEventsByCalendar(String token, Calendar calendar, Context context) {

    }

    @Override
    public Event getEventById(int id) {
        return mRealm.where(Event.class).findFirst();
    }

    @Override
    public RealmResults<Event> getEventByDate(Date date) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(java.util.Calendar.HOUR, 23);
        Date toDate = calendar.getTime();
        return mRealm.where(Event.class).between(START_DATE_FIELD, date, toDate)
                .findAll();
    }

    public boolean isExists(Event event) {
        return mRealm.where(Event.class).equalTo(EVENT_ID_FIELD, event.getEventId())
                .equalTo(START_DATE_FIELD, event.getStartTime()).count() != 0;
    }
}