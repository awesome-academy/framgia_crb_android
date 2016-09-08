package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by lethuy on 06/09/2016.
 */
public class RepeatOnAttribute extends RealmObject{
    @SerializedName("1")
    private DayOfWeekId mRepeatOnAttribute1;

    @SerializedName("2")
    private DayOfWeekId mRepeatOnAttribute2;

    @SerializedName("3")
    private DayOfWeekId mRepeatOnAttribute3;

    @SerializedName("4")
    private DayOfWeekId mRepeatOnAttribute4;

    @SerializedName("5")
    private DayOfWeekId mRepeatOnAttribute5;

    @SerializedName("6")
    private DayOfWeekId mRepeatOnAttribute6;

    @SerializedName("7")
    private DayOfWeekId mRepeatOnAttribute7;

    public DayOfWeekId getRepeatOnAttribute1() {
        return mRepeatOnAttribute1;
    }

    public void setRepeatOnAttribute1(DayOfWeekId mRepeatOnAttribute1) {
        this.mRepeatOnAttribute1 = mRepeatOnAttribute1;
    }

    public DayOfWeekId getRepeatOnAttribute2() {
        return mRepeatOnAttribute2;
    }

    public void setRepeatOnAttribute2(DayOfWeekId mRepeatOnAttribute2) {
        this.mRepeatOnAttribute2 = mRepeatOnAttribute2;
    }

    public DayOfWeekId getRepeatOnAttribute3() {
        return mRepeatOnAttribute3;
    }

    public void setRepeatOnAttribute3(DayOfWeekId mRepeatOnAttribute3) {
        this.mRepeatOnAttribute3 = mRepeatOnAttribute3;
    }

    public DayOfWeekId getRepeatOnAttribute4() {
        return mRepeatOnAttribute4;
    }

    public void setRepeatOnAttribute4(DayOfWeekId mRepeatOnAttribute4) {
        this.mRepeatOnAttribute4 = mRepeatOnAttribute4;
    }

    public DayOfWeekId getRepeatOnAttribute5() {
        return mRepeatOnAttribute5;
    }

    public void setRepeatOnAttribute5(DayOfWeekId mRepeatOnAttribute5) {
        this.mRepeatOnAttribute5 = mRepeatOnAttribute5;
    }

    public DayOfWeekId getRepeatOnAttribute6() {
        return mRepeatOnAttribute6;
    }

    public void setRepeatOnAttribute6(DayOfWeekId mRepeatOnAttribute6) {
        this.mRepeatOnAttribute6 = mRepeatOnAttribute6;
    }

    public DayOfWeekId getRepeatOnAttribute7() {
        return mRepeatOnAttribute7;
    }

    public void setRepeatOnAttribute7(DayOfWeekId mRepeatOnAttribute7) {
        this.mRepeatOnAttribute7 = mRepeatOnAttribute7;
    }
}
