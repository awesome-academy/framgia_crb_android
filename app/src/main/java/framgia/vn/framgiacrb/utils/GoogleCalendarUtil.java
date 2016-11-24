package framgia.vn.framgiacrb.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.GoogleEvent;

/**
 * Created by framgia on 11/11/2016.
 */
public class GoogleCalendarUtil {
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
        CalendarContract.Events.ACCOUNT_NAME // 10
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

    public static List getAllGoogleEventNoRepeatByDate(Activity activity, Date today) {
        List eventList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = contentResolver
                .query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION,
                    CalendarContract.Events.VISIBLE + " = " +
                        Constant.GoogleCalendar.IS_VISIBLE_TRUE,
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

    public static Event getEventById(Activity activity, int Id) {
        Event event = null;
        ContentResolver contentResolver = activity.getContentResolver();
        StringBuilder condition = new StringBuilder(CalendarContract.Events._ID);
        condition.append(" = ");
        condition.append(Integer.toString(Id));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
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
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                .requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR},
                    Constant.RequestCode.PERMISSIONS_READ_CALENDAR);
        }
        return event;
    }

    public static List getAllGoogleEventRepeatByDate(Activity activity, Date
        today) {
        List eventList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = contentResolver
                .query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION,
                    CalendarContract.Events.VISIBLE + " = " +
                        Constant.GoogleCalendar.IS_VISIBLE_TRUE,
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
}