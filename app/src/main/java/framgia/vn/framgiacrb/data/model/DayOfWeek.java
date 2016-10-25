package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by framgia on 25/10/2016.
 */
public class DayOfWeek extends RealmObject {
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;

    public DayOfWeek() {
    }

    public DayOfWeek(int id, String name) {
        mId = id;
        mName = name;
    }

    public DayOfWeek(DayOfWeek dayOfWeek) {
        this.mId = dayOfWeek.getId();
        this.mName = dayOfWeek.getName();
    }

    public static RealmList<DayOfWeek> cloneListDayOfWeek(RealmList<DayOfWeek> dayOfWeeks) {
        RealmList<DayOfWeek> result = new RealmList<>();
        for (DayOfWeek attendee : dayOfWeeks) {
            result.add(new DayOfWeek(attendee));
        }
        return result;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
