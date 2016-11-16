package framgia.vn.framgiacrb.constant;

import framgia.vn.framgiacrb.R;

/**
 * Created by nghicv on 18/07/2016.
 */
public class Constant {
    public class Message {
        public static final String MESSAGE = "MESSAGE";
        public static final String LOADING = "Loading...";
        public static final String LOGIN_SUCCESS = "Login Success!";
        public static final String SUCCESS = "Create Event Success!";
        public static final String NOT_AUTHENTICATION = "Not authenticated";
        public static final String MESSAGE_NOT_AUTHENTICATION =
            "This account has been login in other device";
    }

    public class Intent {
        public static final String INTENT_ID_EVENT = "intent_id_event";
        public static final String INTENT_START_TIME = "intent_start_time";
        public static final String INTENT_FINISH_TIME = "intent_finish_time";
        public static final String INTENT_IS_GOOGLE_EVENT = "is_google_event";
    }

    public class Number {
        public static final int SHOW_DURATION = 1000;
        public static final int HIDE_DURATION = 500;
        public static final int INVALID_INDEX = -1;
        public static final int MAX_POSITION_TODAY = 20;
    }

    public class Format {
        public static final String LINE_BREAK = "\n";
        public static final String FORMAT_DATE = "MM-dd-yyyy";
        public static final String FORMAT_TIME = "HH:mm";
        public static final String AMOUNT_DEVIDE = "-";
    }

    public class Time {
        public static final String SUNDAY = "sunday";
        public static final String MONDAY = "monday";
        public static final String TUESDAY = "tuesday";
        public static final String WEDNESDAY = "wednesday";
        public static final String THURSDAY = "thursday";
        public static final String FRIDAY = "friday";
        public static final String SATURDAY = "saturday";
        public static final String WEEKLY = "Weekly";
        public static final String NO_REPEAT = "No repeat";
        public static final String REPEAT_DAILY = "daily";
        public static final String REPEAT_WEEKLY = "weekly";
        public static final String REPEAT_MONTHLY = "monthly";
        public static final String REPEAT_YEARLY = "yearly";
    }

    public class GoogleCalendar {
        public static final String IS_ALL_DAY_TRUE = "1";
    }

    public class Url {
        public static final String BASE_URL = "http://crb.framgia.vn/api/";
    }

    public class RequestCode {
        public static final int CHOOSE_ATTENDEE = 2;
        public static final int CHOOSE_PLACE = 3;
        public static final int PERMISSIONS_READ_CALENDAR = 4;
    }

    public static final int[] color =
        {R.color.color_event_1, R.color.color_event_2, R.color.color_event_3,
            R.color.color_event_4, R.color.color_event_5, R.color.color_event_6,
            R.color.color_event_7,
            R.color.color_event_8, R.color.color_event_9, R.color.color_event_10,
            R.color.color_event_11,
            R.color.color_event_12};
}
