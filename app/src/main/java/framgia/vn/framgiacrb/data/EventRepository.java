package framgia.vn.framgiacrb.data;

import java.util.Calendar;
import java.util.List;

import framgia.vn.framgiacrb.data.model.Event;

/**
 * Created by nghicv on 20/07/2016.
 */
public interface EventRepository {
    void insertEvent(Event event);
    void deleteEvent(Event event);
    void updateEvent(Event event);
    List<Event> getAllEvents(int userId);
    List<Event> getEventsByCalendar(Calendar calendar);
    Event getEventById(int id);
}
