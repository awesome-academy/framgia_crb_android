package framgia.vn.framgiacrb.data.dataTest;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.activity.MainActivity;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.DayOfWeekId;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.RepeatOnAttribute;
import framgia.vn.framgiacrb.data.model.Session;
import io.realm.Realm;

/**
 * Created by framgia on 08/09/2016.
 */
public class DataTest {
    private static Realm sRealm = Realm.getDefaultInstance();
    public static final String DAILY = "daily";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";
    public static final String WEEKLY = "weekly";
    public static final String NO_REPEAT = "no repeat";
    public static final String UPDATED = "updated";
    public static final String EDIT_ONLY = "edit_only";
    public static final String EDIT_ALL_FOLLOW = "edit_all_follow";
    public static final String EDIT_ALL = "edit_all";

    public static void createEvent(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
            MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(UPDATED, false)) {
            for (int i = 1; i < 300; i++) {
                Calendar calendarRepeat = Calendar.getInstance();
                calendarRepeat.set(Calendar.HOUR_OF_DAY, 18 - i % 18);
                calendarRepeat.set(Calendar.MINUTE, 0);
                calendarRepeat.set(Calendar.SECOND, 0);
                //
                Date startRepeat = calendarRepeat.getTime();
                Date startTime = calendarRepeat.getTime();
                startTime.setTime(calendarRepeat.getTimeInMillis());
                //
                calendarRepeat.set(Calendar.HOUR_OF_DAY, 20);
                calendarRepeat.set(Calendar.MINUTE, 0);
                Date finishTime = calendarRepeat.getTime();
                //
                calendarRepeat.add(Calendar.DAY_OF_MONTH, 100 + i);
                calendarRepeat.set(Calendar.HOUR_OF_DAY, 12);
                calendarRepeat.set(Calendar.MINUTE, 0);
                Date endRepeat = calendarRepeat.getTime();
                //
                createEvent("test " + i, "" + i, startTime, finishTime, startRepeat, endRepeat);
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(UPDATED, true);
            editor.apply();
            createCalender(context);
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

    public static void createEvent(String title, String id, Date startTime, Date finishTime, Date
        startRepeat, Date endRepeat) {
        sRealm.beginTransaction();
        Event event = sRealm.createObject(Event.class);
        event.setTitle(title);
        event.setId(id);
        // test: 09/09/2016
        switch (id) {
            case "4":
                event.setRepeatType(NO_REPEAT);
                break;
            case "10":
            case "15":
            case "20":
                event.setRepeatType(MONTHLY);
                break;
            case "5":
                event.setRepeatType(YEARLY);
                break;
            case "12":
                event.setRepeatType(WEEKLY);
                List<String> listDayOfWeekRepeat = new ArrayList<>();
                listDayOfWeekRepeat.add("2");
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
                break;
        }
        if (!event.getRepeatType().equals(NO_REPEAT)) {
            event.setStartRepeat(startRepeat);
            event.setEndRepeat(endRepeat);
            event.setRepeatEvery(1);
        }
        //
        event.setStartTime(startTime);
        event.setFinishTime(finishTime);
        event.setColorId(10);
        sRealm.commitTransaction();
    }

    public static List<Event> getGenCodeEvent(Date date) {
        List<Event> genEventList = new ArrayList<>();
        EventRepositoriesLocal eventRepositoriesLocal = new EventRepositoriesLocal(sRealm);
        genEventList.addAll(eventRepositoriesLocal.getEventByDate(date));
        List<Event> eventRepeatList = eventRepositoriesLocal.getAllEventRepeatByDate(date);
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
        return genEventList;
    }

    private static Event getGenEventWeekly(Event event, Date date, String repeatType) {
        Date startTime = event.getStartTime();
        Date finishTime = event.getFinishTime();
        int repeatEvery = event.getRepeatEvery();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekOfYearDate = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(startTime);
        int weekOfYearStartTime = calendar.get(Calendar.WEEK_OF_YEAR);
        while (weekOfYearStartTime < weekOfYearDate) {
            startTime = getTime(startTime, repeatType, repeatEvery);
            calendar.setTime(startTime);
            weekOfYearStartTime = calendar.get(Calendar.WEEK_OF_YEAR);
        }
        if (compareWeek(date, startTime) && isRepeatOnAttribute(event, date)) {
            Event eventGen = new Event();
            eventGen.setStartTime(startTime);
            //
            eventGen.setFinishTime(getTime(finishTime, repeatType, repeatEvery));
            //
            eventGen.setId(event.getId());
            eventGen.setTitle(event.getTitle());
            eventGen.setStartRepeat(event.getStartRepeat());
            eventGen.setEndRepeat(event.getEndRepeat());
            eventGen.setRepeatEvery(repeatEvery);
            eventGen.setRepeatType(repeatType);
            //
            eventGen.setColorId(event.getColorId());
            //
            return eventGen;
        }
        return null;
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
        if (compareDate(date, startTime)) {
            Event eventGen = new Event();
            eventGen.setStartTime(startTime);
            //
            eventGen.setFinishTime(getTime(finishTime, repeatType, repeatEvery));
            //
            eventGen.setId(event.getId());
            eventGen.setTitle(event.getTitle());
            eventGen.setStartRepeat(event.getStartRepeat());
            eventGen.setEndRepeat(event.getEndRepeat());
            eventGen.setRepeatEvery(repeatEvery);
            eventGen.setRepeatType(repeatType);
            //
            eventGen.setColorId(event.getColorId());
            return eventGen;
        }
        return null;
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

    public static boolean compareDate(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
            calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH));
    }

    private static boolean compareWeek(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR));
    }

    public static List<Event> getAllEvent() {
        return sRealm.where(Event.class).findAll();
    }
}
