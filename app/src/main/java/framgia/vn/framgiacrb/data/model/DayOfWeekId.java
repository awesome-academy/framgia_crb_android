package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by lethuy on 06/09/2016.
 */
public class DayOfWeekId extends RealmObject{
    @SerializedName("days_of_week_id")
    private String mDayOfWeekId;

    public String getDayOfWeekId() {
        return mDayOfWeekId;
    }

    public void setDayOfWeekId(String mDayOfWeekId) {
        this.mDayOfWeekId = mDayOfWeekId;
    }
}
