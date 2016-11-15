package framgia.vn.framgiacrb.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.constant.ExceptionType;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.DayOfWeek;
import framgia.vn.framgiacrb.data.model.Event;
import io.realm.Realm;

/**
 * Created by framgia on 03/10/2016.
 */
public class RenderEventUtil {
    public static final String DAILY = "daily";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";
    public static final String WEEKLY = "weekly";
    public static final String NO_REPEAT = "no repeat";
    public static final String UPDATED = "updated";
    private static Realm sRealm = Realm.getDefaultInstance();
    private static EventRepositoriesLocal sEventRepositoriesLocal =
        new EventRepositoriesLocal(sRealm);

    public static List<Event> getGenCodeEvent(Activity activity, Date date) {
        List<Event> genEventList = new ArrayList<>();
        List<Event> eventInDatabase = new ArrayList<>();
        eventInDatabase.addAll(sEventRepositoriesLocal.getEventByDate(date));
        for (Event event : eventInDatabase) {
            if (event.getRepeatType() == null) {
                genEventList.add(event);
            }
        }
        List<Event> eventRepeatList = sEventRepositoriesLocal.getAllEventRepeatByDate(date);
        Event eventGen = null;
        for (Event event : eventRepeatList) {
            String repeatType = event.getRepeatType();
            switch (repeatType) {
                case DAILY:
                case MONTHLY:
                case YEARLY:
                    eventGen = getGenEventDayMonthYear(event, date, repeatType);
                    break;
                case WEEKLY:
                    eventGen = getGenEventWeekly(event, date, repeatType);
                    break;
            }
            if (eventGen != null) {
                genEventList.add(eventGen);
            }
        }
        genEventList.addAll(GoogleCalendarUtil.getAllGoogleEventNoRepeatByDate(activity, date));
        Collections.sort(genEventList, new Comparator<Event>() {
                @Override
                public int compare(Event event1, Event event2) {
                    return event1.getStartTime().compareTo(event2.getStartTime());
                }
            }
        );
        return genEventList;
    }

    private static Event getGenEventWeekly(Event event, Date date, String repeatType) {
        Date startTime = event.getStartTime();
        Date finishTime = event.getFinishTime();
        int repeatEvery = event.getRepeatEvery();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int yearDate = calendar.get(Calendar.YEAR);
        int weekOfYearDate = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(startTime);
        int yearStartTime = calendar.get(Calendar.YEAR);
        int weekOfYearStartTime = calendar.get(Calendar.WEEK_OF_YEAR);
        if (yearDate > yearStartTime) {
            while (weekOfYearDate < weekOfYearStartTime) {
                startTime = getTime(startTime, repeatType, repeatEvery);
                calendar.setTime(startTime);
                weekOfYearStartTime = calendar.get(Calendar.WEEK_OF_YEAR);
            }
            while (weekOfYearStartTime < weekOfYearDate) {
                startTime = getTime(startTime, repeatType, repeatEvery);
                calendar.setTime(startTime);
                weekOfYearStartTime = calendar.get(Calendar.WEEK_OF_YEAR);
            }
        } else if (yearDate == yearStartTime) {
            while (weekOfYearStartTime < weekOfYearDate) {
                startTime = getTime(startTime, repeatType, repeatEvery);
                calendar.setTime(startTime);
                weekOfYearStartTime = calendar.get(Calendar.WEEK_OF_YEAR);
            }
        }
        Event eventGen = null;
        if (TimeUtils.compareWeek(date, startTime) && isRepeatOnAttribute(event, date)) {
            eventGen = new Event(event);
            startTime = getTimeRepeat(date, startTime);
            finishTime = getTimeRepeat(date, finishTime);
            eventGen.setStartTime(startTime);
            eventGen.setFinishTime(finishTime);
            if (event.getParentId() == null) {
                eventGen = checkException(date, event, eventGen);
            }
        }
        return eventGen;
    }

    private static Date getTimeRepeat(Date date, Date dateSet) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateSet);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private static boolean isRepeatOnAttribute(Event event, Date date) {
        List<DayOfWeek> dayOfWeeks = event.getDayOfWeeks();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            if (dayOfWeek != null) {
                if (dayOfWeek.getId() == calendar.get(Calendar.DAY_OF_WEEK)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Event getGenEventDayMonthYear(Event event, Date date, String repeatType) {
        Date startTime = event.getStartTime();
        Date finishTime = event.getFinishTime();
        int repeatEvery = event.getRepeatEvery();
        while (!startTime.after(date)) {
            startTime = getTime(startTime, repeatType, repeatEvery);
        }
        Event eventGen = null;
        if (TimeUtils.compareDate(date, startTime)) {
            eventGen = new Event(event);
            startTime = getTimeRepeat(date, startTime);
            finishTime = getTimeRepeat(date, finishTime);
            eventGen.setStartTime(startTime);
            eventGen.setFinishTime(finishTime);
            if (event.getParentId() != null) {
                eventGen = checkException(date, event, eventGen);
            }
        }
        return eventGen;
    }

    private static Event checkException(Date date, Event event, Event eventGen) {
        List<Event> eventChangeList =
            sEventRepositoriesLocal.getEventByParentId(event.getParentId());
        for (Event eventChange : eventChangeList) {
            switch (ExceptionType.getExceptionType(eventChange.getExceptionType())) {
                case DELETE_ONLY:
                    if (TimeUtils.compareDate(date, eventChange.getStartTime())) {
                        eventGen = null;
                    }
                    break;
                case EDIT_ONLY:
                    if (TimeUtils.compareDate(date, eventChange.getStartTime())) {
                        if (eventGen != null) {
                            eventGen.setId(eventChange.getId());
                            eventGen.setTitle(eventChange.getTitle());
                            eventGen.setStartTime(eventChange.getStartTime());
                            eventGen.setFinishTime(eventChange.getFinishTime());
                            eventGen.setColorId(eventChange.getColorId());
                        }
                    }
                    break;
            }
        }
        return eventGen;
    }

    private static Date getTime(Date time, String repeatType, int repeatEvery) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        switch (repeatType) {
            case DAILY:
                calendar.add(Calendar.DAY_OF_MONTH, repeatEvery);
                break;
            case MONTHLY:
                calendar.add(Calendar.MONTH, repeatEvery);
                break;
            case YEARLY:
                calendar.add(Calendar.YEAR, repeatEvery);
                break;
            case WEEKLY:
                calendar.add(Calendar.WEEK_OF_YEAR, repeatEvery);
                break;
        }
        return calendar.getTime();
    }
}
