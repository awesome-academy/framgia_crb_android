package framgia.vn.framgiacrb.utils;

import java.sql.Time;

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
        list.addAll(data);
        Event yearInSearch = new Event();
        if(list.size() == 0) {
            return list;
        }
        String startYear = TimeUtils.toYear(list.get(0).getStartTime());
        yearInSearch.setTitle(DEFINE_YEAR);
        yearInSearch.setDescription(TimeUtils.toYear(list.get(0).getStartTime()));
        list.add(0, yearInSearch);
        int length = list.size();
        for(int i = 2; i < length; i++) {
            if(!(TimeUtils.toYear(list.get(i).getStartTime()).equals(startYear))) {
                startYear = TimeUtils.toYear(list.get(i).getStartTime());
                Event yearInSearchNext = new Event();
                yearInSearchNext.setTitle(DEFINE_YEAR);
                yearInSearchNext.setDescription(startYear);
                list.add(i, yearInSearchNext);
            }
        }
        return list;
    }
}
