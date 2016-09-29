package framgia.vn.framgiacrb.utils;

import android.app.AlarmManager;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import framgia.vn.framgiacrb.activity.SearchActivity;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.DayOfWeekId;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.RepeatOnAttribute;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;

/**
 * Created by framgia on 09/08/2016.
 */
public class SearchUtil {
    public static final String DEFINE_YEAR = "item_year";

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
            if (SearchActivity.sMonth > TimeUtils.getMonth(finalDate)
                && SearchActivity.sYear > TimeUtils.getYear(finalDate)) {
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
                && (event.getRepeatType().equals(Constant.NO_REPEAT)
                || event.getRepeatType().equals(Constant.REPEAT_DAILY))) {
                list.add(new Event(event));
            }
            if (event.getRepeatType().equals(Constant.NO_REPEAT)) {
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
        RepeatOnAttribute repeatOnAttribute = event.getRepeatOnAttribute();
        if (repeatOnAttribute == null) {
            return listEvent;
        }
        listEvent.addAll(getListEventByDayOfWeekId(event, event.getRepeatOnAttribute()
            .getRepeatOnAttribute1()));
        listEvent.addAll(getListEventByDayOfWeekId(event, event.getRepeatOnAttribute()
            .getRepeatOnAttribute2()));
        listEvent.addAll(getListEventByDayOfWeekId(event, event.getRepeatOnAttribute()
            .getRepeatOnAttribute3()));
        listEvent.addAll(getListEventByDayOfWeekId(event, event.getRepeatOnAttribute()
            .getRepeatOnAttribute4()));
        listEvent.addAll(getListEventByDayOfWeekId(event, event.getRepeatOnAttribute()
            .getRepeatOnAttribute5()));
        listEvent.addAll(getListEventByDayOfWeekId(event, event.getRepeatOnAttribute()
            .getRepeatOnAttribute6()));
        listEvent.addAll(getListEventByDayOfWeekId(event, event.getRepeatOnAttribute()
            .getRepeatOnAttribute7()));
        return listEvent;
    }

    public static RealmList<Event> getListEventByDayOfWeekId(Event event, DayOfWeekId dayOfWeekId) {
        RealmList listEvent = new RealmList();
        if (dayOfWeekId == null) {
            return listEvent;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartRepeat());
        switch (dayOfWeekId.getDayOfWeekId()) {
            case Constant.SUNDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            case Constant.MONDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case Constant.TUESDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case Constant.WEDNESDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case Constant.THURSDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case Constant.FRIDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case Constant.SATURDAY:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
        }
        while (TimeUtils.getMonth(calendar.getTime()) == SearchActivity.sMonth) {
            if (calendar.getTime().after(event.getStartRepeat())
                && calendar.getTime().before(event.getEndRepeat())) {
                Event suitableEvent = new Event(event);
                suitableEvent.setStartTime(calendar.getTime());
                suitableEvent.setFinishTime(TimeUtils.genFinishTime(calendar.getTime(),
                    event.getFinishTime()));
                listEvent.add(suitableEvent);
            }
            calendar.add(Calendar.WEEK_OF_MONTH, event.getRepeatEvery());
        }
        return listEvent;
    }

    public static Event genEventForMonthly(Event event) {
        int month = TimeUtils.getMonth(event.getStartRepeat());
        int endMonth = TimeUtils.getMonth(event.getEndRepeat());
        while (month < SearchActivity.sMonth && month <= endMonth) {
            month += event.getRepeatEvery();
        }
        if (month != SearchActivity.sMonth) {
            return null;
        }
        Event result = new Event(event);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartTime());
        calendar.set(Calendar.MONTH, month);
        result.setStartTime(calendar.getTime());
        result.setFinishTime(TimeUtils.genFinishTime(calendar.getTime(), event.getFinishTime
            ()));
        return result;
    }

    public static Event genEventForYearly(Event event) {
        int month = TimeUtils.getMonth(event.getStartRepeat());
        if (month != SearchActivity.sMonth) {
            return null;
        }
        int year = TimeUtils.getYear(event.getStartRepeat());
        int endYear = TimeUtils.getYear(event.getEndRepeat());
        while (year < SearchActivity.sYear && year < endYear) {
            year += event.getRepeatEvery();
        }
        if (year != SearchActivity.sYear) {
            return null;
        }
        Event result = new Event(event);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartTime());
        calendar.set(Calendar.YEAR, year);
        result.setStartTime(calendar.getTime());
        result.setFinishTime(TimeUtils.genFinishTime(calendar.getTime(), event.getFinishTime
            ()));
        return result;
    }

    public static Date getFinalEventDay(OrderedRealmCollection<Event> data) {
        Date date = new Date();
        date.setTime(data.get(0).getEndRepeat().getTime());
        int length = data.size();
        for (int i = 1; i < length; i++) {
            if (date.before(data.get(i).getEndRepeat())) {
                date.setTime(data.get(i).getEndRepeat().getTime());
            }
        }
        return date;
    }
}
