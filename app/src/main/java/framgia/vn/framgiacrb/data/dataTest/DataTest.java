package framgia.vn.framgiacrb.data.dataTest;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.activity.MainActivity;
import framgia.vn.framgiacrb.data.enums.ExceptionType;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.DayOfWeekId;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.RepeatOnAttribute;
import framgia.vn.framgiacrb.data.model.Session;
import framgia.vn.framgiacrb.utils.TimeUtils;
import io.realm.Realm;

/**
 * Created by framgia on 08/09/2016.
 */
public class DataTest {
    public static final String DAILY = "daily";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";
    public static final String WEEKLY = "weekly";
    public static final String NO_REPEAT = "no repeat";
    public static final String UPDATED = "updated";
    private static Realm sRealm = Realm.getDefaultInstance();
    private static EventRepositoriesLocal sEventRepositoriesLocal =
        new EventRepositoriesLocal(sRealm);

    public static void createEvent(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
            MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(UPDATED, false)) {
            for (int i = 1; i < 6; i++) {
                Calendar calendarRepeat = Calendar.getInstance();
                calendarRepeat.set(Calendar.HOUR_OF_DAY, 18 - i % 18);
                calendarRepeat.set(Calendar.MINUTE, 0);
                calendarRepeat.set(Calendar.SECOND, 0);
                Date startRepeat = calendarRepeat.getTime();
                Date startTime = calendarRepeat.getTime();
                calendarRepeat.set(Calendar.HOUR_OF_DAY, 20);
                calendarRepeat.set(Calendar.MINUTE, 0);
                Date finishTime = calendarRepeat.getTime();
                calendarRepeat.add(Calendar.DAY_OF_MONTH, 1000);
                calendarRepeat.set(Calendar.HOUR_OF_DAY, 22);
                calendarRepeat.set(Calendar.MINUTE, 0);
                Date endRepeat = calendarRepeat.getTime();
                Event event = createEvent("test " + i, "" + i, startTime, finishTime, startRepeat,
                    endRepeat);
                createEventByExceptionType(event, 1);
                createEventByExceptionType(event, 8);
                createEventByExceptionType(event, 12);
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(UPDATED, true);
            editor.apply();
            createCalender(context);
        }
    }

    private static void createEventByExceptionType(Event event, int dayOfMonth) {
        if (!TextUtils.equals(event.getRepeatType(), NO_REPEAT)) {
            sRealm.beginTransaction();
            Event eventException = sRealm.createObject(Event.class);
            eventException.setId("exception " + event.getId() + dayOfMonth);
            eventException.setTitle(event.getTitle() + " = " + dayOfMonth);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startTime = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            Date finishTime = calendar.getTime();
            eventException.setStartTime(startTime);
            eventException.setFinishTime(finishTime);
            eventException.setRepeatEvery(event.getRepeatEvery());
            eventException.setRepeatType(event.getRepeatType());
            eventException.setColorId(event.getColorId());
            eventException.setParentId(event.getId());
            eventException.setExceptionTime(startTime);
            switch (dayOfMonth) {
                case 1:
                    eventException.setExceptionType(ExceptionType.EDIT_ONLY.getValues());
                    break;
                case 8:
                    eventException.setExceptionType(ExceptionType.DELETE_ONLY.getValues());
                    break;
                case 12:
                    eventException.setRepeatOnAttribute(event.getRepeatOnAttribute());
                    eventException.setStartRepeat(startTime);
                    eventException.setEndRepeat(event.getEndRepeat());
                    eventException.setExceptionType(ExceptionType.EDIT_ALL_FOLLOW.getValues());
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    event.setEndRepeat(calendar.getTime());
                    break;
            }
            sRealm.commitTransaction();
        }
    }

    private static void createCalender(Context context) {
        sRealm.beginTransaction();
        framgia.vn.framgiacrb.data.model.Calendar calendar = sRealm.createObject(
            framgia.vn.framgiacrb.data.model.Calendar.class);
        calendar.setId(1);
        calendar.setUserId("Vũ Tuấn Anh");
        calendar.setPermissionId(1);
        calendar.setName("Vũ Tuấn Anh");
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHAREPREFF,
            Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Session.CALENDAR_ID, calendar.getId());
        editor.apply();
        sRealm.commitTransaction();
    }

    public static Event createEvent(String title, String id, Date startTime, Date finishTime, Date
        startRepeat, Date endRepeat) {
        sRealm.beginTransaction();
        Event event = sRealm.createObject(Event.class);
        event.setTitle(title);
        event.setId(id);
        switch (id) {
            case "4":
                event.setRepeatType(NO_REPEAT);
                event.setTitle(NO_REPEAT);
                break;
            case "3":
            case "15":
            case "20":
                event.setRepeatType(MONTHLY);
                event.setTitle(MONTHLY);
                break;
            case "5":
                event.setRepeatType(YEARLY);
                event.setTitle(YEARLY);
                break;
            case "2":
                event.setRepeatType(WEEKLY);
                event.setTitle(WEEKLY);
                List<String> listDayOfWeekRepeat = new ArrayList<>();
                listDayOfWeekRepeat.add("3");
                listDayOfWeekRepeat.add("5");
                listDayOfWeekRepeat.add("6");
                listDayOfWeekRepeat.add("7");
                RepeatOnAttribute repeatOnAttribute = sRealm.createObject(RepeatOnAttribute.class);
                if (listDayOfWeekRepeat.size() != 0) {
                    for (int i = 0; i < listDayOfWeekRepeat.size(); i++) {
                        DayOfWeekId dayOfWeekId = sRealm.createObject(DayOfWeekId.class);
                        dayOfWeekId.setDayOfWeekId(listDayOfWeekRepeat.get(i));
                        switch (i) {
                            case 0:
                                repeatOnAttribute.setRepeatOnAttribute1(dayOfWeekId);
                                break;
                            case 1:
                                repeatOnAttribute.setRepeatOnAttribute2(dayOfWeekId);
                                break;
                            case 2:
                                repeatOnAttribute.setRepeatOnAttribute3(dayOfWeekId);
                                break;
                            case 3:
                                repeatOnAttribute.setRepeatOnAttribute4(dayOfWeekId);
                                break;
                            case 4:
                                repeatOnAttribute.setRepeatOnAttribute5(dayOfWeekId);
                                break;
                            case 5:
                                repeatOnAttribute.setRepeatOnAttribute6(dayOfWeekId);
                                break;
                            case 6:
                                repeatOnAttribute.setRepeatOnAttribute7(dayOfWeekId);
                                break;
                        }
                    }
                }
                event.setRepeatOnAttribute(repeatOnAttribute);
                break;
            default:
                event.setRepeatType(DAILY);
                event.setTitle(DAILY);
                break;
        }
        if (!event.getRepeatType().equals(NO_REPEAT)) {
            event.setStartRepeat(startRepeat);
            event.setEndRepeat(endRepeat);
            event.setRepeatEvery(1);
        }
        event.setStartTime(startTime);
        event.setFinishTime(finishTime);
        event.setColorId(10);
        sRealm.commitTransaction();
        return event;
    }

    public static List<Event> getGenCodeEvent(Date date) {
        List<Event> genEventList = new ArrayList<>();
        genEventList.addAll(sEventRepositoriesLocal.getEventByDate(date));
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
            eventGen = new Event();
            startTime = getTimeRepeat(date, startTime);
            finishTime = getTimeRepeat(date, finishTime);
            eventGen.setStartTime(startTime);
            eventGen.setFinishTime(finishTime);
            eventGen.setId(event.getId());
            eventGen.setTitle(event.getTitle());
            eventGen.setStartRepeat(event.getStartRepeat());
            eventGen.setEndRepeat(event.getEndRepeat());
            eventGen.setRepeatEvery(repeatEvery);
            eventGen.setRepeatType(repeatType);
            eventGen.setColorId(event.getColorId());
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
        RepeatOnAttribute repeatOnAttribute = event.getRepeatOnAttribute();
        List<DayOfWeekId> dayOfWeekIdList = new ArrayList<>();
        if (repeatOnAttribute != null) {
            dayOfWeekIdList.add(repeatOnAttribute.getRepeatOnAttribute1());
            dayOfWeekIdList.add(repeatOnAttribute.getRepeatOnAttribute2());
            dayOfWeekIdList.add(repeatOnAttribute.getRepeatOnAttribute3());
            dayOfWeekIdList.add(repeatOnAttribute.getRepeatOnAttribute4());
            dayOfWeekIdList.add(repeatOnAttribute.getRepeatOnAttribute5());
            dayOfWeekIdList.add(repeatOnAttribute.getRepeatOnAttribute6());
            dayOfWeekIdList.add(repeatOnAttribute.getRepeatOnAttribute7());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfWeekSearch = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        for (DayOfWeekId dayOfWeekId : dayOfWeekIdList) {
            if (dayOfWeekId != null) {
                String dayOfWeekNow = dayOfWeekId.getDayOfWeekId();
                if (TextUtils.equals(dayOfWeekNow, dayOfWeekSearch)) return true;
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
            eventGen = new Event();
            startTime = getTimeRepeat(date, startTime);
            finishTime = getTimeRepeat(date, finishTime);
            eventGen.setStartTime(startTime);
            eventGen.setFinishTime(finishTime);
            eventGen.setId(event.getId());
            eventGen.setTitle(event.getTitle());
            eventGen.setStartRepeat(event.getStartRepeat());
            eventGen.setEndRepeat(event.getEndRepeat());
            eventGen.setRepeatEvery(repeatEvery);
            eventGen.setRepeatType(repeatType);
            eventGen.setColorId(event.getColorId());
            if (event.getParentId() == null) {
                eventGen = checkException(date, event, eventGen);
            }
        }
        return eventGen;
    }

    private static Event checkException(Date date, Event event, Event eventGen) {
        List<Event> eventChangeList = sEventRepositoriesLocal.getEventByParentId(event.getId());
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
