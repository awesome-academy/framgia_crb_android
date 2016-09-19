package framgia.vn.framgiacrb.utils;

import android.app.AlarmManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import framgia.vn.framgiacrb.activity.SearchActivity;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;

/**
 * Created by framgia on 09/08/2016.
 */
public class SearchUtil {
    public static final String DEFINE_YEAR = "item_year";

    public static final RealmList<Event> editListDataSearch(OrderedRealmCollection<Event> data) {
        RealmList<Event> list = new RealmList();
        list.addAll(generateEvent(data));
        Event firstEvent = list.get(0);
        if (SearchActivity.sNotNeedYear && TimeUtils.getYear(firstEvent.getStartTime()) ==
            SearchActivity.sYear) {
            return list;
        }
        Event yearInSearch = new Event();
        if (list.size() == 0) {
            return list;
        }
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
                && TimeUtils.getYear(event.getStartTime()) == SearchActivity.sYear) {
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
            }
        }
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                return lhs.getStartTime().compareTo(rhs.getStartTime());
            }
        });
        return list;
    }
}
