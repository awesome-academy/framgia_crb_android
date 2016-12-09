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
        public static final String INTENT_IS_GOOGLE_EVENT = "intent_is_google_event";
        public static final String INTENT_LIST_CALENDAR = "intent_list_calendar";
    }

    public class Number {
        public static final int SHOW_DURATION = 1000;
        public static final int HIDE_DURATION = 500;
        public static final int INVALID_INDEX = -1;
        public static final int MAX_POSITION_TODAY = 20;
        public static final int NUM_MONTH_IN_YEAR = 12;
        public static final int MAX_LEFT_MENU_ITEM = 4;
        public static final int MIN_MONTH = 1;
    }

    public class Format {
        public static final String LINE_BREAK = "\n";
        public static final String FORMAT_DATE = "MM-dd-yyyy";
        public static final String FORMAT_TIME = "HH:mm";
        public static final String AMOUNT_DEVIDE = "-";
        public static final String RULE_SPLIT = ";";
        public static final String RULE_SPLIT_RESULT = "=";
        public static final String RULE_SPLIT_DAY_OF_WEEK = ",";
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
        public static final String IS_VISIBLE_TRUE = "1";
        public static final String REPEAT_DAILY = "DAILY";
        public static final String REPEAT_MONTHLY = "MONTHLY";
        public static final String REPEAT_YEARLY = "YEARLY";
        public static final String REPEAT_WEEKLY = "WEEKLY";
        public static final String RULE_INTERVAL = "INTERVAL";
        public static final String RULE_FREQ = "FREQ";
        public static final String RULE_UNTIL = "UNTIL";
        public static final String RULE_BYDAY = "BYDAY";
        public static final int RULE_NAME_INDEX = 0;
        public static final int RULE_VALUE_INDEX = 1;
        public static final String FORMAT_DATE = "yyyymmdd'T'hhmmss'Z'";
        public static final int RULE_ENOUGH_NAME_VALUE = 2;
        public static final int REPEAT_EVERY_FOR_NO_REPEAT = 0;
        public static final int REPEAT_EVERY_DEFAULT = 1;
        public static final String SUNDAY = "SU";
        public static final String MONDAY = "MO";
        public static final String TUESDAY = "TU";
        public static final String WEDNESDAY = "WE";
        public static final String THURSDAY = "TH";
        public static final String FRIDAY = "FR";
        public static final String SATURDAY = "SA";
        public static final String ALL_CALENDAR = "All calendar";
        public static final String CONNECT_CONDITION = " AND ";
        public static final String EQUAL_CONDITION = " = ";
        public static final String QUOTE_VALUE = "'";
    }

    public class Url {
        public static final String BASE_URL = "http://crb.framgia.vn/api/";
    }

    public class RequestCode {
        public static final int SETTING = 1;
        public static final int CHOOSE_ATTENDEE = 2;
        public static final int CHOOSE_PLACE = 3;
        public static final int PERMISSIONS_READ_CALENDAR = 4;
    }

    public class Tag {
        public static final String MONTH_VIEW_TAG = "month";
    }

    public static final int[] color =
        {R.color.color_event_1, R.color.color_event_2, R.color.color_event_3,
            R.color.color_event_4, R.color.color_event_5, R.color.color_event_6,
            R.color.color_event_7,
            R.color.color_event_8, R.color.color_event_9, R.color.color_event_10,
            R.color.color_event_11,
            R.color.color_event_12};
}
