package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nghicv on 20/07/2016.
 */
public class Event extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("start_date")
    private Date mStartTime;

    @SerializedName("finish_date")
    private Date mFinishTime;

    @SerializedName("status")
    private String mStatus;

    @SerializedName("repeat_type")
    private String mRepeatType;

    @SerializedName("repeat_every")
    private int mRepeatEvery;

    @SerializedName("end_date")
    private Date mEndDate;

    @SerializedName("exception_date")
    private Date mExceptionDate;

    @SerializedName("type")
    private int mType;

    @SerializedName("event_id")
    private int mEventId;

    @SerializedName("color_id")
    private int mColorId;

    public int getColorId() {
        return mColorId;
    }

    public void setColorId(int colorId) {
        mColorId = colorId;
    }
    @SerializedName("all_day")
    private boolean mAll_day;

    @SerializedName("user_id")
    private int mUser_id;

    @SerializedName("calendar_id")
    private int mCalendar_id;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date startTime) {
        mStartTime = startTime;
    }

    public Date getFinishTime() {
        return mFinishTime;
    }

    public void setFinishTime(Date finishTime) {
        mFinishTime = finishTime;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(String repeatType) {
        mRepeatType = repeatType;
    }

    public int getRepeatEvery() {
        return mRepeatEvery;
    }

    public void setRepeatEvery(int repeatEvery) {
        mRepeatEvery = repeatEvery;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public Date getExceptionDate() {
        return mExceptionDate;
    }

    public void setExceptionDate(Date exceptionDate) {
        mExceptionDate = exceptionDate;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getEventId() {
        return mEventId;
    }

    public void setEventId(int eventId) {
        mEventId = eventId;
    }


    public boolean isAll_day() {
        return mAll_day;
    }

    public void setAll_day(boolean mAll_day) {
        this.mAll_day = mAll_day;
    }

    public int getUser_id() {
        return mUser_id;
    }

    public void setUser_id(int mUser_id) {
        this.mUser_id = mUser_id;
    }

    public int getCalendar_id() {
        return mCalendar_id;
    }

    public void setCalendar_id(int mCalendar_id) {
        this.mCalendar_id = mCalendar_id;
    }
}
