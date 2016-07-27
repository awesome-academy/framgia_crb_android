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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public boolean isDefault() {
        return mIsDefault;
    }

    public void setDefault(boolean aDefault) {
        mIsDefault = aDefault;
    }
}
