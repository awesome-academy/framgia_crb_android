package framgia.vn.framgiacrb.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.local.RealmController;
import framgia.vn.framgiacrb.data.model.Event;
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
        Event event = RealmController.getInstance().getEventById(eventId);
        Event eventChild = new Event(event);
        eventChild
            .setStartTime((Date) intent.getSerializableExtra(Constant.Intent.INTENT_START_TIME));
        String repeatType = eventChild.getRepeatType();
        if (repeatType == null) {
            repeatType = Constant.Time.NO_REPEAT;
        }
        switch (repeatType) {
            case Constant.Time.NO_REPEAT:
                pushNotification(context, intent, eventChild);
                break;
            case Constant.Time.REPEAT_DAILY:
                handleDaily(context, intent);
                break;
            case Constant.Time.REPEAT_MONTHLY:
                handleMonthly(context, intent);
                break;
            case Constant.Time.REPEAT_YEARLY:
                handleYearly(context, intent);
            case Constant.Time.REPEAT_WEEKLY:
                handleWeekly(context, intent);
                break;
        }
    }

    private void handleDaily(Context context, Intent intent) {
        int eventId = intent.getIntExtra(Constant.Intent.INTENT_ID_EVENT, 0);
        Event event = RealmController.getInstance().getEventById(eventId);
        Event eventChild = new Event(event);
        Date startTime = (Date) intent.getSerializableExtra(Constant.Intent.INTENT_START_TIME);
        eventChild.setStartTime(startTime);
        pushNotification(context, intent, eventChild);
        Date childStartTime = genStartTime(startTime, eventChild.getRepeatEvery());
        if (eventChild.getEndRepeat() == null || childStartTime.getTime() <= (eventChild
            .getEndRepeat().getTime())) {
            eventChild.setStartTime(childStartTime);
            NotificationUtil.registerNotificationEventTime(context, eventChild);
        }
    }

    private Date genStartTime(Date startTimePrevious, int repeatEvery) {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(startTimePrevious);
        startTime.add(Calendar.DAY_OF_YEAR, repeatEvery);
        startTime.add(Calendar.MILLISECOND, NotificationUtil.TIME_EARLY);
        return startTime.getTime();
    }

    private void handleWeekly(Context context, Intent intent) {
    }

    private void handleMonthly(Context context, Intent intent) {
    }

    private void handleYearly(Context context, Intent intent) {
    }

    private void pushNotification(Context context, Intent intent, Event eventChild) {
        NotificationUtil.pushNotification(context
            , eventChild.getTitle()
            , intent.getStringExtra(INTENT_CONTENT)
            , eventChild.getId()
            , intent.getIntExtra(AlarmReceiver.INTENT_NOTIFICATION_ID, 0)
            , eventChild.getStartTime()
            , eventChild.getFinishTime());
    }
}
