package framgia.vn.framgiacrb.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.local.RealmController;
import framgia.vn.framgiacrb.data.model.DayOfWeek;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.utils.GoogleCalendarUtil;
import framgia.vn.framgiacrb.utils.NotificationUtil;

/**
 * Created by framgia on 18/07/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String INTENT_CONTENT = "content";
    public static final String INTENT_NOTIFICATION_ID = "notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        int eventId = intent.getIntExtra(Constant.Intent.INTENT_ID_EVENT, 0);
        Event event;
        if (intent.getBooleanExtra(Constant.Intent.INTENT_IS_GOOGLE_EVENT, false)) {
            event = GoogleCalendarUtil.getEventById(context, eventId);
        } else {
            event = RealmController.getInstance().getEventById(eventId);
        }
        Event eventChild = new Event(event);
        eventChild
            .setStartTime((Date) intent.getSerializableExtra(Constant.Intent.INTENT_START_TIME));
        String repeatType = eventChild.getRepeatType();
        if (repeatType == null) {
            pushNotification(context, intent, eventChild);
        } else {
            handleRepeat(context, intent);
        }
    }

    private void handleRepeat(Context context, Intent intent) {
        int eventId = intent.getIntExtra(Constant.Intent.INTENT_ID_EVENT, 0);
        Event event;
        if (intent.getBooleanExtra(Constant.Intent.INTENT_IS_GOOGLE_EVENT, false)) {
            event = GoogleCalendarUtil.getEventById(context, eventId);
        } else {
            event = RealmController.getInstance().getEventById(eventId);
        }
        Event eventChild = new Event(event);
        Date startTime = (Date) intent.getSerializableExtra(Constant.Intent.INTENT_START_TIME);
        eventChild.setStartTime(startTime);
        pushNotification(context, intent, eventChild);
        Date childStartTime =
            genStartTime(startTime, eventChild);
        if (eventChild.getEndRepeat() == null || childStartTime.getTime() <= (eventChild
            .getEndRepeat().getTime())) {
            eventChild.setStartTime(childStartTime);
            NotificationUtil.registerNotificationEventTime(context, eventChild);
        }
    }

    private Date genStartTime(Date startTimePrevious, Event eventChild) {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(startTimePrevious);
        switch (eventChild.getRepeatType()) {
            case Constant.Time.REPEAT_DAILY:
                startTime.add(Calendar.DAY_OF_YEAR, eventChild.getRepeatEvery());
                break;
            case Constant.Time.REPEAT_MONTHLY:
                startTime.add(Calendar.MONTH, eventChild.getRepeatEvery());
                break;
            case Constant.Time.REPEAT_YEARLY:
                startTime.add(Calendar.YEAR, eventChild.getRepeatEvery());
                break;
            case Constant.Time.REPEAT_WEEKLY:
                startTime.setTime(getNextDayNotifyForWeekly(eventChild, startTime).getTime());
                break;
        }
        startTime.add(Calendar.MILLISECOND, NotificationUtil.TIME_EARLY);
        return startTime.getTime();
    }

    private Calendar getNextDayNotifyForWeekly(Event eventChild, Calendar startTimePrevious) {
        startTimePrevious.add(Calendar.MILLISECOND, NotificationUtil.TIME_EARLY);
        Calendar calendarResult = Calendar.getInstance();
        calendarResult.setTime(startTimePrevious.getTime());
        calendarResult.add(Calendar.WEEK_OF_YEAR, eventChild.getRepeatEvery());
        Calendar calendarCompare = Calendar.getInstance();
        calendarCompare.setTime(startTimePrevious.getTime());
        for (DayOfWeek dayOfWeek : eventChild.getDayOfWeeks()) {
            switch (dayOfWeek.getId()) {
                case Calendar.SUNDAY:
                    calendarCompare.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    break;
                case Calendar.MONDAY:
                    calendarCompare.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    break;
                case Calendar.TUESDAY:
                    calendarCompare.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    break;
                case Calendar.WEDNESDAY:
                    calendarCompare.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    break;
                case Calendar.THURSDAY:
                    calendarCompare.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    break;
                case Calendar.FRIDAY:
                    calendarCompare.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    break;
                case Calendar.SATURDAY:
                    calendarCompare.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    break;
            }
            if (calendarCompare.after(startTimePrevious)
                && calendarCompare.before(calendarResult)) {
                calendarResult.setTime(calendarCompare.getTime());
            }
        }
        calendarResult.add(Calendar.MILLISECOND, -NotificationUtil.TIME_EARLY);
        return calendarResult;
    }

    private void pushNotification(Context context, Intent intent, Event eventChild) {
        NotificationUtil.pushNotification(context
            , eventChild.getTitle()
            , intent.getStringExtra(INTENT_CONTENT)
            , eventChild.getId()
            , intent.getIntExtra(AlarmReceiver.INTENT_NOTIFICATION_ID, 0)
            , eventChild.getStartTime()
            , eventChild.getFinishTime()
            , eventChild.isGoogleEvent());
    }
}
