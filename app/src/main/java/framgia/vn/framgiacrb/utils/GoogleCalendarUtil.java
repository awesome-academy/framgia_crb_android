package framgia.vn.framgiacrb.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.DayOfWeek;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.GoogleCalendar;
import framgia.vn.framgiacrb.data.model.GoogleEvent;
import framgia.vn.framgiacrb.ui.activity.MainActivity;
import io.realm.RealmList;

/**
 * Created by framgia on 11/11/2016.
 */
public class GoogleCalendarUtil {
    public static final String[] CALENDAR_PROJECTION = new String[]{
        CalendarContract.Calendars._ID,  //0
        CalendarContract.Calendars.ACCOUNT_NAME //1
    };
    public static final int PROJECTION_CALENDAR_ID_INDEX = 0;
    public static final int PROJECTION_CALENDAR_ACCOUNT_NAME_INDEX = 1;
    public static final String[] EVENT_PROJECTION = new String[]{
        CalendarContract.Events.TITLE,             // 0
        CalendarContract.Events.DESCRIPTION,      // 1
        CalendarContract.Events.DTSTART,         // 2
        CalendarContract.Events.DTEND,          // 3
        CalendarContract.Events.CALENDAR_COLOR_KEY,   // 4
        CalendarContract.Events.LAST_DATE,    //5
        CalendarContract.Events.RDATE, // 6
        CalendarContract.Events.RRULE,  // 7
        CalendarContract.Events.ALL_DAY, // 8
        CalendarContract.Events._ID, // 9
        CalendarContract.Events.ACCOUNT_NAME, // 10
        CalendarContract.Events.DURATION //11
    };
    // The indices for the projection array above.
    private static final int PROJECTION_TITLE_INDEX = 0;
    private static final int PROJECTION_DESCRIPTION_INDEX = 1;
    private static final int PROJECTION_DTSTART_INDEX = 2;
    private static final int PROJECTION_DTEND_INDEX = 3;
    private static final int PROJECTION_EVENT_COLOR_INDEX = 4;
    private static final int PROJECTION_LAST_DATE_INDEX = 5;
    private static final int PROJECTION_RDATE_INDEX = 6;
    private static final int PROJECTION_RRULE_INDEX = 7;
    private static final int PROJECTION_ALL_DAY_INDEX = 8;
    private static final int PROJECTION_ID_INDEX = 9;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 10;
    private static final int PROJECTION_DURATION_INDEX = 11;
    public static final String[] REMINDERS_PROJECTION = new String[]{
        CalendarContract.Reminders.EVENT_ID,  //0
    };
    public static final int PROJECTION_REMINDERS_ID_INDEX = 0;

    public static List getAllGoogleEventNoRepeatByDate(Activity activity, Date today, String
        calendarAccount) {
        List eventList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            StringBuilder condition = new StringBuilder(CalendarContract.Events.VISIBLE);
            condition.append(Constant.GoogleCalendar.EQUAL_CONDITION);
            condition.append(Constant.GoogleCalendar.IS_VISIBLE_TRUE);
            if (!MainActivity.sIsAllCalendar) {
                condition.append(Constant.GoogleCalendar.CONNECT_CONDITION);
                condition.append(CalendarContract.Calendars.ACCOUNT_NAME);
                condition.append(Constant.GoogleCalendar.EQUAL_CONDITION);
                condition.append(Constant.GoogleCalendar.QUOTE_VALUE);
                condition.append(calendarAccount);
                condition.append(Constant.GoogleCalendar.QUOTE_VALUE);
            }
            Cursor cursor = contentResolver
                .query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION,
                    condition.toString(),
                    null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    GoogleEvent googleEvent = new GoogleEvent();
                    googleEvent.setId(cursor.getInt(PROJECTION_ID_INDEX));
                    googleEvent.setTitle(cursor.getString(PROJECTION_TITLE_INDEX));
                    googleEvent.setDescription(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
                    googleEvent.setStartTime(cursor.getString(PROJECTION_DTSTART_INDEX));
                    googleEvent.setFinishTime(cursor.getString(PROJECTION_DTEND_INDEX));
                    googleEvent.setColor(cursor.getString(PROJECTION_EVENT_COLOR_INDEX));
                    googleEvent.setRule(cursor.getString(PROJECTION_RRULE_INDEX));
                    googleEvent.setIsAllDay(cursor.getString(PROJECTION_ALL_DAY_INDEX));
                    if ((googleEvent.getRule() == null &&
                        TimeUtils.compareDate(googleEvent.getStartTime(), today))) {
                        Event event = new Event(googleEvent);
                        eventList.add(event);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                .requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR},
                    Constant.RequestCode.PERMISSIONS_READ_CALENDAR);
        }
        return eventList;
    }

    public static Event getEventById(Context context, int Id) {
        Event event = null;
        ContentResolver contentResolver = context.getContentResolver();
        StringBuilder condition = new StringBuilder(CalendarContract.Events._ID);
        condition.append(Constant.GoogleCalendar.EQUAL_CONDITION);
        condition.append(Integer.toString(Id));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = contentResolver
                .query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, condition.toString(),
                    null, null);
            if (cursor != null && cursor.moveToFirst()) {
                GoogleEvent googleEvent = new GoogleEvent();
                googleEvent.setId(cursor.getInt(PROJECTION_ID_INDEX));
                googleEvent.setTitle(cursor.getString(PROJECTION_TITLE_INDEX));
                googleEvent.setDescription(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
                googleEvent.setStartTime(cursor.getString(PROJECTION_DTSTART_INDEX));
                googleEvent.setFinishTime(cursor.getString(PROJECTION_DTEND_INDEX));
                googleEvent.setColor(cursor.getString(PROJECTION_EVENT_COLOR_INDEX));
                googleEvent.setRule(cursor.getString(PROJECTION_RRULE_INDEX));
                googleEvent.setIsAllDay(cursor.getString(PROJECTION_ALL_DAY_INDEX));
                googleEvent.setCalendarName(cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX));
                event = new Event(googleEvent);
                cursor.close();
            }
        }
        return event;
    }

    public static List getAllGoogleEventRepeatByDate(Activity activity, Date
        today, String calendarAccount) {
        List eventList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            StringBuilder condition = new StringBuilder(CalendarContract.Events.VISIBLE);
            condition.append(Constant.GoogleCalendar.EQUAL_CONDITION);
            condition.append(Constant.GoogleCalendar.IS_VISIBLE_TRUE);
            if (!MainActivity.sIsAllCalendar) {
                condition.append(Constant.GoogleCalendar.CONNECT_CONDITION);
                condition.append(CalendarContract.Calendars.ACCOUNT_NAME);
                condition.append(Constant.GoogleCalendar.EQUAL_CONDITION);
                condition.append(Constant.GoogleCalendar.QUOTE_VALUE);
                condition.append(calendarAccount);
                condition.append(Constant.GoogleCalendar.QUOTE_VALUE);
            }
            Cursor cursor = contentResolver
                .query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION,
                    condition.toString(),
                    null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    GoogleEvent googleEvent = new GoogleEvent();
                    googleEvent.setDuration(cursor.getString(PROJECTION_DURATION_INDEX));
                    googleEvent.setId(cursor.getInt(PROJECTION_ID_INDEX));
                    googleEvent.setTitle(cursor.getString(PROJECTION_TITLE_INDEX));
                    googleEvent.setDescription(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
                    googleEvent.setStartTime(cursor.getString(PROJECTION_DTSTART_INDEX));
                    googleEvent.setFinishTime(cursor.getString(PROJECTION_DTEND_INDEX));
                    googleEvent.setColor(cursor.getString(PROJECTION_EVENT_COLOR_INDEX));
                    googleEvent.setRule(cursor.getString(PROJECTION_RRULE_INDEX));
                    googleEvent.setIsAllDay(cursor.getString(PROJECTION_ALL_DAY_INDEX));
                    if (googleEvent.getRule() != null) {
                        Event event = new Event(googleEvent);
                        if (event.getStartTime().getTime() <= today.getTime()
                            && (event.getEndRepeat() == null ||
                            event.getEndRepeat().getTime() >= today.getTime()))
                            eventList.add(event);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                .requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR},
                    Constant.RequestCode.PERMISSIONS_READ_CALENDAR);
        }
        return eventList;
    }

    public static String getRepeatType(String rule) {
        if (rule == null) return null;
        String[] listRule = rule.split(Constant.Format.RULE_SPLIT);
        for (String ruleChild : listRule) {
            String[] rulePair = ruleChild.split(Constant.Format.RULE_SPLIT_RESULT);
            if (rulePair[Constant.GoogleCalendar.RULE_NAME_INDEX]
                .equals(Constant.GoogleCalendar.RULE_FREQ))
                switch (rulePair[Constant.GoogleCalendar.RULE_VALUE_INDEX]) {
                    case Constant.GoogleCalendar.REPEAT_DAILY:
                        return Constant.Time.REPEAT_DAILY;
                    case Constant.GoogleCalendar.REPEAT_WEEKLY:
                        return Constant.Time.REPEAT_WEEKLY;
                    case Constant.GoogleCalendar.REPEAT_MONTHLY:
                        return Constant.Time.REPEAT_MONTHLY;
                    case Constant.GoogleCalendar.REPEAT_YEARLY:
                        return Constant.Time.REPEAT_YEARLY;
                    default:
                        return null;
                }
        }
        return null;
    }

    public static int getRepeatEvery(String rule) {
        if (rule == null) return Constant.GoogleCalendar.REPEAT_EVERY_FOR_NO_REPEAT;
        String[] listRule = rule.split(Constant.Format.RULE_SPLIT);
        for (String ruleChild : listRule) {
            String[] rulePair = ruleChild.split(Constant.Format.RULE_SPLIT_RESULT);
            if (rulePair.length != Constant.GoogleCalendar.RULE_ENOUGH_NAME_VALUE)
                return Constant.GoogleCalendar.REPEAT_EVERY_FOR_NO_REPEAT;
            if (rulePair[Constant.GoogleCalendar.RULE_NAME_INDEX]
                .equals(Constant.GoogleCalendar.RULE_INTERVAL)) {
                return Integer.parseInt(rulePair[Constant.GoogleCalendar.RULE_VALUE_INDEX]);
            }
        }
        return Constant.GoogleCalendar.REPEAT_EVERY_DEFAULT;
    }

    public static Date getEndRepeat(String rule) {
        if (rule == null) return null;
        String[] listRule = rule.split(Constant.Format.RULE_SPLIT);
        for (String ruleChild : listRule) {
            String[] rulePair = ruleChild.split(Constant.Format.RULE_SPLIT_RESULT);
            if (rulePair.length != Constant.GoogleCalendar.RULE_ENOUGH_NAME_VALUE) return null;
            if (rulePair[Constant.GoogleCalendar.RULE_NAME_INDEX].equals(Constant.GoogleCalendar
                .RULE_UNTIL)) {
                SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat(Constant.GoogleCalendar.FORMAT_DATE);
                try {
                    return simpleDateFormat
                        .parse(rulePair[Constant.GoogleCalendar.RULE_VALUE_INDEX]);
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static RealmList<DayOfWeek> getListDayOfWeek(String rule) {
        if (rule == null) return null;
        String[] listRule = rule.split(Constant.Format.RULE_SPLIT);
        for (String ruleChild : listRule) {
            String[] rulePair = ruleChild.split(Constant.Format.RULE_SPLIT_RESULT);
            if (rulePair.length != Constant.GoogleCalendar.RULE_ENOUGH_NAME_VALUE) return null;
            if (rulePair[Constant.GoogleCalendar.RULE_NAME_INDEX].equals(Constant.GoogleCalendar
                .RULE_BYDAY)) {
                String[] listDays = rulePair[Constant.GoogleCalendar.RULE_VALUE_INDEX].split
                    (Constant.Format.RULE_SPLIT_DAY_OF_WEEK);
                RealmList listDayOfWeek = new RealmList();
                for (String day : listDays) {
                    switch (day) {
                        case Constant.GoogleCalendar.SUNDAY:
                            listDayOfWeek.add(new DayOfWeek(Calendar.SUNDAY, Constant.Time.SUNDAY));
                            break;
                        case Constant.GoogleCalendar.MONDAY:
                            listDayOfWeek.add(new DayOfWeek(Calendar.MONDAY, Constant.Time.MONDAY));
                            break;
                        case Constant.GoogleCalendar.TUESDAY:
                            listDayOfWeek
                                .add(new DayOfWeek(Calendar.TUESDAY, Constant.Time.TUESDAY));
                            break;
                        case Constant.GoogleCalendar.WEDNESDAY:
                            listDayOfWeek
                                .add(new DayOfWeek(Calendar.WEDNESDAY, Constant.Time.WEDNESDAY));
                            break;
                        case Constant.GoogleCalendar.THURSDAY:
                            listDayOfWeek
                                .add(new DayOfWeek(Calendar.THURSDAY, Constant.Time.THURSDAY));
                            break;
                        case Constant.GoogleCalendar.FRIDAY:
                            listDayOfWeek.add(new DayOfWeek(Calendar.FRIDAY, Constant.Time.FRIDAY));
                            break;
                        case Constant.GoogleCalendar.SATURDAY:
                            listDayOfWeek
                                .add(new DayOfWeek(Calendar.SATURDAY, Constant.Time.SATURDAY));
                            break;
                    }
                }
                return listDayOfWeek;
            }
        }
        return null;
    }

    public static List getAllCalendarName(Activity activity) {
        List result = new ArrayList();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            Cursor cursor =
                activity.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,
                    CALENDAR_PROJECTION,
                    CalendarContract.Calendars.VISIBLE + Constant.GoogleCalendar.EQUAL_CONDITION +
                        Constant.GoogleCalendar.IS_VISIBLE_TRUE,
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    GoogleCalendar googleCalendar = new GoogleCalendar();
                    googleCalendar.setId(cursor.getInt(PROJECTION_CALENDAR_ID_INDEX));
                    googleCalendar
                        .setAccountName(cursor.getString(PROJECTION_CALENDAR_ACCOUNT_NAME_INDEX));
                    result.add(googleCalendar);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return removeSameCalendar(result);
    }

    private static List removeSameCalendar(List listCalendar) {
        Set<GoogleCalendar> set = new LinkedHashSet<>(listCalendar);
        listCalendar.clear();
        listCalendar.addAll(set);
        return listCalendar;
    }

     // TODO: next version
    /*public static List getReminderNoRepeatByDate(Activity activity, Date date) {
        List result = new ArrayList();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            Cursor cursor =
                activity.getContentResolver().query(CalendarContract.Reminders.CONTENT_URI,
                    REMINDERS_PROJECTION,
                    null,
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Event googleReminder =
                        getEventById(activity, cursor.getInt(PROJECTION_REMINDERS_ID_INDEX));
                    if (googleReminder.getRepeatType() == null
                        && TimeUtils.compareDate(googleReminder.getStartTime(), date))
                        result.add(googleReminder);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return result;
    }

    public static List getReminderRepeatByDate(Activity activity, Date date) {
        List result = new ArrayList();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            Cursor cursor =
                activity.getContentResolver().query(CalendarContract.Reminders.CONTENT_URI,
                    REMINDERS_PROJECTION,
                    null,
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Event googleReminder =
                        getEventById(activity, cursor.getInt(PROJECTION_REMINDERS_ID_INDEX));
                    if (googleReminder.getRepeatType() != null
                        && TimeUtils.compareDate(googleReminder.getStartTime(), date))
                        result.add(googleReminder);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return result;
    }*/

    public static RealmList getEventByTitle(Activity activity, String title) {
        RealmList eventList = new RealmList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            StringBuilder condition = new StringBuilder(CalendarContract.Events.VISIBLE);
            condition.append(Constant.GoogleCalendar.EQUAL_CONDITION);
            condition.append(Constant.GoogleCalendar.IS_VISIBLE_TRUE);
            condition.append(Constant.GoogleCalendar.CONNECT_CONDITION);
            condition.append(CalendarContract.Events.TITLE);
            condition.append(Constant.GoogleCalendar.LIKE_CONDITION);
            condition.append(Constant.GoogleCalendar.QUOTE_VALUE);
            condition.append(Constant.GoogleCalendar.PERCENT_STRING);
            condition.append(title);
            condition.append(Constant.GoogleCalendar.PERCENT_STRING);
            condition.append(Constant.GoogleCalendar.QUOTE_VALUE);
            Cursor cursor = contentResolver
                .query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION,
                    condition.toString(),
                    null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    GoogleEvent googleEvent = new GoogleEvent();
                    googleEvent.setId(cursor.getInt(PROJECTION_ID_INDEX));
                    googleEvent.setTitle(cursor.getString(PROJECTION_TITLE_INDEX));
                    googleEvent.setDescription(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
                    googleEvent.setStartTime(cursor.getString(PROJECTION_DTSTART_INDEX));
                    googleEvent.setFinishTime(cursor.getString(PROJECTION_DTEND_INDEX));
                    googleEvent.setColor(cursor.getString(PROJECTION_EVENT_COLOR_INDEX));
                    googleEvent.setRule(cursor.getString(PROJECTION_RRULE_INDEX));
                    googleEvent.setIsAllDay(cursor.getString(PROJECTION_ALL_DAY_INDEX));
                    Event event = new Event(googleEvent);
                    eventList.add(event);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                .requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR},
                    Constant.RequestCode.PERMISSIONS_READ_CALENDAR);
        }
        return eventList;
    }
}
