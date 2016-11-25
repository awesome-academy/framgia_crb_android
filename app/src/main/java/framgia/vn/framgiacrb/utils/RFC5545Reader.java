package framgia.vn.framgiacrb.utils;

import android.app.AlarmManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by framgia on 25/11/2016.
 */
public class RFC5545Reader {
    public static final String DEFINE_WEEK = "W";
    public static final String DEFINE_DAY = "D";
    public static final String DEFINE_DAY_HAVE_TIME = "DT";
    public static final String DEFINE_HOUR = "H";
    public static final String DEFINE_MINUTE = "M";
    public static final String DEFINE_SECOND = "S";
    private static final int MIN_PARSE_DURATION_SIZE = 3;
    private static final int MILLISECOND_PER_SECOND = 1000;
    private static final int MILLISECOND_PER_MINUTE = 60 * MILLISECOND_PER_SECOND;
    private static final String PATTERN_DIVIDE_NUMBER_STRING = "[0-9]+|[a-z]+|[A-Z]+";

    // duration in RFC5545 format
    public static long getDurationInSecond(String duration) {
        long result = 0;
        List list = parse(duration);
        int length = list.size();
        if (length < MIN_PARSE_DURATION_SIZE) return result;
        int i = MIN_PARSE_DURATION_SIZE - 1;
        while (i < length) {
            int digit = Integer.parseInt((String) list.get(i - 1));
            switch ((String) list.get(i)) {
                case DEFINE_WEEK:
                    result += digit * Calendar.DAY_OF_WEEK *
                        AlarmManager.INTERVAL_DAY;
                    break;
                case DEFINE_DAY:
                case DEFINE_DAY_HAVE_TIME:
                    result += digit * AlarmManager.INTERVAL_DAY;
                    break;
                case DEFINE_HOUR:
                    result += digit * AlarmManager.INTERVAL_HOUR;
                    break;
                case DEFINE_MINUTE:
                    result += digit * MILLISECOND_PER_MINUTE;
                    break;
                case DEFINE_SECOND:
                    result += digit * MILLISECOND_PER_SECOND;
            }
            i += 2;
        }
        return result;
    }

    private static List<String> parse(String str) {
        List<String> output = new ArrayList<>();
        Matcher match = Pattern.compile(PATTERN_DIVIDE_NUMBER_STRING).matcher(str);
        while (match.find()) {
            output.add(match.group());
        }
        return output;
    }
}
