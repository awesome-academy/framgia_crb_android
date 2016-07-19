package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by nghicv on 20/07/2016.
 */
public class Event {

    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("start_time")
    private Date mStartTime;

    @SerializedName("finish_time")
    private Date mFinishTime;

    @SerializedName("status")
    private String status;

    @SerializedName("repeat_type")
    private int mRepeatType;

    @SerializedName("repeat_every")
    private int mRepeatEvery;

    @SerializedName("end_date")
    private Date mEndDate;

    @SerializedName("exception_date")
    private Date mExceptionDate;

    @SerializedName("type")
    private int mType;
}
