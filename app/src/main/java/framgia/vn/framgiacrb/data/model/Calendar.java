package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nghicv on 20/07/2016.
 */
public class Calendar {

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("status")
    private int mStatus;

    @SerializedName("is_default")
    private boolean mIsDefault;
}
