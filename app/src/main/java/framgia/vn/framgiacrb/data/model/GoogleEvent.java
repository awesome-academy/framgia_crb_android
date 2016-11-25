package framgia.vn.framgiacrb.data.model;

import java.util.Date;

import framgia.vn.framgiacrb.utils.GoogleCalendarUtil;
import framgia.vn.framgiacrb.utils.RFC5545Reader;
import framgia.vn.framgiacrb.utils.TimeUtils;

/**
 * Created by framgia on 11/11/2016.
 */
public class GoogleEvent {
    private int mId;
    private String mTitle;
    private String mDescription;
    private Date mStartTime;
    private Date mFinishTime;
    private String mColor;
    private Date mEndRepeat;
    private long mRepeatEvery;
    private String mRule;

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    private String mDuration;

    public String getCalendarName() {
        return mCalendarName;
    }

    public void setCalendarName(String calendarName) {
        mCalendarName = calendarName;
    }

    private String mCalendarName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getIsAllDay() {
        return mIsAllDay;
    }

    public void setIsAllDay(String isAllDay) {
        mIsAllDay = isAllDay;
    }

    private String mIsAllDay;

    public long getRepeatEvery() {
        return mRepeatEvery;
    }

    public void setRepeatEvery(String rDate) {
        if (rDate != null) mRepeatEvery = Long.getLong(rDate);
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

    public void setEndRepeat() {
        mEndRepeat = GoogleCalendarUtil.getEndRepeat(mRule);
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
        if (mDuration != null) {
            mFinishTime = new Date(mStartTime.getTime() + RFC5545Reader.getDurationInSecond
                (mDuration));
        } else {
            mFinishTime =
                finishTime == null ? mStartTime : TimeUtils.convertMillionStringToDate(finishTime);
        }
    }
}