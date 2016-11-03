package framgia.vn.framgiacrb.utils;

import android.app.AlarmManager;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.ui.activity.SearchActivity;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.DayOfWeek;
import framgia.vn.framgiacrb.data.model.Event;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;

/**
 * Created by framgia on 09/08/2016.
 */
public class SearchUtil {
    public static final String DEFINE_YEAR = "item_year";
    public static final int COUNT_MONTH_IN_YEAR = 12;

    public static final RealmList<Event> editListDataSearch(OrderedRealmCollection<Event> data) {
        if (SearchActivity.sMonth == Constant.INVALID_INDEX) {
            Event event = data.get(0);
            SearchActivity.sMonth = TimeUtils.getMonth(event.getStartTime());
            SearchActivity.sYear = TimeUtils.getYear(event.getStartTime());
        }
        RealmList<Event> list = new RealmList();
        list.addAll(generateEvent(data));
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                return lhs.getStartTime().compareTo(rhs.getStartTime());
            }
        });
        if (list.size() == 0) {
            Date finalDate = getFinalEventDay(data);
            if (SearchActivity.sYear > TimeUtils.getYear(finalDate)) {
                SearchActivity.sIsHasMoreEvent = false;
            }
            if (SearchActivity.sMonth > TimeUtils.getMonth(finalDate)
                && SearchActivity.sYear == TimeUtils.getYear(finalDate)) {
                SearchActivity.sIsHasMoreEvent = false;
            }
            return list;
        }
        Event firstEvent = list.get(0);
        if (SearchActivity.sNotNeedYear && TimeUtils.getYear(firstEvent.getStartTime()) ==
            SearchActivity.sYear) {
            return list;
        }
        SearchActivity.sNotNeedYear = true;
        Event yearInSearch = new Event();
        String startYear = TimeUtils.toYear(list.get(0).getStartTime());
        yearInSearch.setTitle(DEFINE_YEAR);
        yearInSearch.setDescription(TimeUtils.toYear(list.get(0).getStartTime()));
        list.add(0, yearInSearch);
        int length = list.size();
        for (int i = 2; i < length; i++) {
            if (!(TimeUtils.toYear(list.get(i).getStartTime()).equals(startYear))) {
                startYear = TimeUtils.toYear(list.get(i).getStartTime());
                Event yearInSearchNext = new Event();
                yearInSearchNext.setTitle(DEFINE_YEAR);
                yearInSearchNext.setDescription(startYear);
                list.add(i, yearInSearchNext);
            }
        }
        return list;
    }

    public static RealmList<Event> generateEvent(OrderedRealmCollection<Event> data) {
        RealmList<Event> list = new RealmList();
        int lenth = data.size();
        for (int i = 0; i < lenth; i++) {
            Event event = data.get(i);
            if (TimeUtils.getMonth(event.getStartTime()) > SearchActivity.sMonth
                && TimeUtils.getYear(event.getStartTime()) >= SearchActivity.sYear) {
                return list;
            }
            if (TimeUtils.getMonth(event.getStartTime()) == SearchActivity.sMonth
                && TimeUtils.getYear(event.getStartTime()) == SearchActivity.sYear
                && ((event.getRepeatType() == null)
                || event.getRepeatType().equals(Constant.REPEAT_DAILY))) {
                list.add(new Event(event));
            }
            if (event.getRepeatType() == null) {
                continue;
            }
            if (event.getRepeatType().equals(Constant.REPEAT_DAILY) && event.getEndRepeat() !=
                null) {
                Date childEventDate = event.getStartTime();
                Date childEventFinishTime = event.getFinishTime();
                while ((event.getEndRepeat().getTime() - childEventDate.getTime())
                    >= (AlarmManager.INTERVAL_DAY * event.getRepeatEvery())) {
                    childEventDate.setTime(childEventDate.getTime()
                        + AlarmManager.INTERVAL_DAY * event.getRepeatEvery());
                    childEventFinishTime.setTime(childEventFinishTime.getTime()
                        + AlarmManager.INTERVAL_DAY * event.getRepeatEvery());
                    Event childEvent = new Event(event);
                    childEvent.setStartTime(new Date(childEventDate.getTime()));
                    childEvent.setFinishTime(new Date(childEventFinishTime.getTime()));
                    if (TimeUtils.getMonth(childEventDate) > SearchActivity.sMonth
                        && TimeUtils.getYear(childEventDate) >= SearchActivity.sYear) {
                        break;
                    }
                    if (TimeUtils.getMonth(childEventDate) == SearchActivity.sMonth
                        && TimeUtils.getYear(childEventDate) == SearchActivity.sYear) {
                        list.add(childEvent);
                    }
                }
            }
            if (event.getRepeatType().equals(Constant.REPEAT_WEEKLY)) {
                list.addAll(genEventWeekly(event));
            }
            if (event.getRepeatType().equals(Constant.REPEAT_MONTHLY)) {
                Event eventMonthly = genEventForMonthly(event);
                if (eventMonthly != null) {
                    list.add(eventMonthly);
                }
            }
            if (event.getRepeatType().equals(Constant.REPEAT_YEARLY)) {
                Event eventYearly = genEventForYearly(event);
                if (eventYearly != null) {
                    list.add(eventYearly);
                }
            }
        }
        return list;
    }

    public static RealmList<Event> genEventWeekly(Event event) {
        RealmList listEvent = new RealmList();
        List<DayOfWeek> dayOfWeeks = event.getDayOfWeeks();
        if (dayOfWeeks == null) {
            return listEvent;
        }
        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            listEvent.addAll(getListEventByDayOfWeekId(event, dayOfWeek));
        }
        return listEvent;
    }

    public static RealmList<Event> getListEventByDayOfWeekId(Event event, DayOfWeek dayOfWeek) {
        RealmList listEvent = new RealmList();
        if (dayOfWeek == null) {
            return listEvent;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartRepeat());
        switch (dayOfWeek.getId()) {
            case Calendar.SUNDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            case Calendar.MONDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case Calendar.TUESDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case Calendar.WEDNESDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case Calendar.THURSDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case Calendar.FRIDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case Calendar.SATURDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
        }
        if (SearchActivity.sMonth == 1) {
            int i = 0;
            i++;
        }
        while ((TimeUtils.getMonth(calendar.getTime()) + TimeUtils.getYear(calendar.getTime()) *
            COUNT_MONTH_IN_YEAR)
            <= (SearchActivity.sMonth + SearchActivity.sYear * COUNT_MONTH_IN_YEAR)) {
            if (calendar.getTime().after(event.getStartRepeat())
                && calendar.getTime().before(event.getEndRepeat())
                && TimeUtils.getMonth(calendar.getTime()) == SearchActivity.sMonth
                && TimeUtils.getYear(calendar.getTime()) == SearchActivity.sYear) {
                Event suitableEvent = new Event(event);
                suitableEvent.setStartTime(calendar.getTime());
                suitableEvent.setFinishTime(TimeUtils.genFinishTime(calendar.getTime(),
                    event.getFinishTime()));
                listEvent.add(suitableEvent);
            }
            calendar.add(Calendar.WEEK_OF_YEAR, event.getRepeatEvery());
        }
        return listEvent;
    }

    public static Event genEventForMonthly(Event event) {
        int month = TimeUtils.getMonth(event.getStartRepeat());
        int year = TimeUtils.getYear(event.getStartRepeat());
        int endYear = TimeUtils.getYear(event.getEndRepeat());
        int endMonth = TimeUtils.getMonth(event.getEndRepeat());
        if (SearchActivity.sYear >= endYear && SearchActivity.sMonth > endMonth) {
            return null;
        }
        if (endYear >= SearchActivity.sYear
            && (Math.abs(SearchActivity.sMonth - month + (SearchActivity.sYear - year) * 12) % event
            .getRepeatEvery
                () == 0)
            ) {
            Event result = new Event(event);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getStartTime());
            calendar.set(Calendar.MONTH, SearchActivity.sMonth - 1);
            calendar.set(Calendar.YEAR, SearchActivity.sYear);
            result.setStartTime(calendar.getTime());
            result.setFinishTime(TimeUtils.genFinishTime(calendar.getTime(), event.getFinishTime
                ()));
            return result;
        }
        return null;
    }

    public static Event genEventForYearly(Event event) {
        int month = TimeUtils.getMonth(event.getStartRepeat());
        if (month != SearchActivity.sMonth) {
            return null;
        }
        int year = TimeUtils.getYear(event.getStartRepeat());
        int endYear = TimeUtils.getYear(event.getEndRepeat());
        if (SearchActivity.sYear <= endYear
            && (Math.abs(SearchActivity.sYear - year) % event.getRepeatEvery() == 0)) {
            Event result = new Event(event);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getStartTime());
            calendar.set(Calendar.YEAR, SearchActivity.sYear);
            result.setStartTime(calendar.getTime());
            result.setFinishTime(TimeUtils.genFinishTime(calendar.getTime(), event.getFinishTime
                ()));
            return result;
        }
        return null;
    }

    public static Date getFinalEventDay(OrderedRealmCollection<Event> data) {
        Date date = new Date();
        date.setTime(data.get(0).getEndRepeat().getTime());
        int length = data.size();
        if (length == 1) {
            return date;
        }
        for (int i = 1; i < length; i++) {
            if (data.get(i).getEndRepeat() != null && date.before(data.get(i).getEndRepeat())) {
                date.setTime(data.get(i).getEndRepeat().getTime());
            }
        }
        return date;
    }
}
