package framgia.vn.framgiacrb.data.model;

import java.util.Date;

import framgia.vn.framgiacrb.utils.TimeUtils;

/**
 * Created by framgia on 11/11/2016.
 */
public class GoogleEvent {
    private String mTitle;
    private String mDescription;
    private Date mStartTime;
    private Date mFinishTime;
    private String mColor;
    private Date mEndRepeat;
    private long mDuration;
    private String mRule;

    public String getIsAllDay() {
        return mIsAllDay;
    }

    public void setIsAllDay(String isAllDay) {
        mIsAllDay = isAllDay;
    }

    private String mIsAllDay;

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public String getRule() {
        return mRule;
    }

    public void setRule(String rule) {
        mRule = rule;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public Date getEndRepeat() {
        return mEndRepeat;
    }

    public void setEndRepeat(String endRepeat) {
        mEndRepeat = (endRepeat == null ? mStartTime : TimeUtils.convertMillionStringToDate
            (endRepeat));
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

    public void setStartTime(String startTime) {
        mStartTime = TimeUtils.convertMillionStringToDate(startTime);
    }

    public Date getFinishTime() {
        return mFinishTime;
    }

    public void setFinishTime(String finishTime) {
        mFinishTime = (finishTime == null ? mStartTime : TimeUtils.convertMillionStringToDate
            (finishTime));
    }
}
