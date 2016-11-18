package framgia.vn.framgiacrb.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

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
        CalendarContract.Events.DURATION, // 6
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
    private static final int PROJECTION_DURATION_INDEX = 6;
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
                    CalendarContract.Events.VISIBLE + " = 1",
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
                    googleEvent.setEndRepeat(cursor.getString(PROJECTION_LAST_DATE_INDEX));
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
                googleEvent.setEndRepeat(cursor.getString(PROJECTION_LAST_DATE_INDEX));
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
}