package framgia.vn.framgiacrb.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;

/**
 * Created by framgia on 09/08/2016.
 */
public class SearchUtil {
    public static long ONE_DAY = 1000 * 60 * 60 * 24;
    public static final String DEFINE_YEAR = "item_year";

    public static final RealmList<Event> editListDataSearch(OrderedRealmCollection<Event> data) {
        RealmList<Event> list = new RealmList();
        list.addAll(generateEvent(data));
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
            list.add(event);
            if (event.getRepeatType() == Constant.NO_REPEAT) {
                continue;
            }
            if (event.getRepeatType().equals(Constant.REPEAT_DAILY)) {
                Date childEventDate = event.getStartTime();
                while ((event.getEndRepeat().getTime() - childEventDate.getTime())
                    >= (ONE_DAY * event.getRepeatEvery())) {
                    childEventDate.setTime(childEventDate.getTime()
                        + ONE_DAY * event.getRepeatEvery());
                    Event childEvent = new Event(event);
                    childEvent.setStartTime(new Date(childEventDate.getTime()));
                    list.add(childEvent);
                }
            }
            if(event.getRepeatType().equals(Constant.REPEAT_WEEKLY)) {}
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
