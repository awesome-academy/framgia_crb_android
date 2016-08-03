package framgia.vn.framgiacrb.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by framgia on 03/08/2016.
 */
public class EventParcelabler implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EventParcelabler> CREATOR =
        new Parcelable.Creator<EventParcelabler>() {
            @Override
            public EventParcelabler createFromParcel(Parcel in) {
                return new EventParcelabler(in);
            }

            @Override
            public EventParcelabler[] newArray(int size) {
                return new EventParcelabler[size];
            }
        };
    private String mId;
    private String mTitle;
    private String mDescription;
    private Date mStartTime;
    private Date mFinishTime;
    private String status;
    private String mRepeatType;
    private int mRepeatEvery;
    private Date mEndDate;
    private Date mExceptionDate;
    private int mType;
    private int mEventId;

    public EventParcelabler(String mId, String mTitle, String mDescription
        , Date mStartTime, Date mFinishTime, String status, String mRepeatType
        , int mRepeatEvery, Date mEndDate, Date mExceptionDate, int mType, int mEventId) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mStartTime = mStartTime;
        this.mFinishTime = mFinishTime;
        this.status = status;
        this.mRepeatType = mRepeatType;
        this.mRepeatEvery = mRepeatEvery;
        this.mEndDate = mEndDate;
        this.mExceptionDate = mExceptionDate;
        this.mType = mType;
        this.mEventId = mEventId;
    }

    protected EventParcelabler(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        long tmpMStartTime = in.readLong();
        mStartTime = tmpMStartTime != -1 ? new Date(tmpMStartTime) : null;
        long tmpMFinishTime = in.readLong();
        mFinishTime = tmpMFinishTime != -1 ? new Date(tmpMFinishTime) : null;
        status = in.readString();
        mRepeatType = in.readString();
        mRepeatEvery = in.readInt();
        long tmpMEndDate = in.readLong();
        mEndDate = tmpMEndDate != -1 ? new Date(tmpMEndDate) : null;
        long tmpMExceptionDate = in.readLong();
        mExceptionDate = tmpMExceptionDate != -1 ? new Date(tmpMExceptionDate) : null;
        mType = in.readInt();
        mEventId = in.readInt();
    }

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
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeLong(mStartTime != null ? mStartTime.getTime() : -1L);
        dest.writeLong(mFinishTime != null ? mFinishTime.getTime() : -1L);
        dest.writeString(status);
        dest.writeString(mRepeatType);
        dest.writeInt(mRepeatEvery);
        dest.writeLong(mEndDate != null ? mEndDate.getTime() : -1L);
        dest.writeLong(mExceptionDate != null ? mExceptionDate.getTime() : -1L);
        dest.writeInt(mType);
        dest.writeInt(mEventId);
    }
}