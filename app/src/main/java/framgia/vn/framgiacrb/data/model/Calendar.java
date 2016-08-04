package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by nghicv on 20/07/2016.
 */
public class Calendar extends RealmObject{

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("permission_id")
    private int mPermissionId;

    @SerializedName("color_id")
    private int mColorId;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("status")
    private String mStatus;

    @SerializedName("is_default")
    private boolean mIsDefault;

    @SerializedName("user_id")
    private String mUserId;

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


    public boolean isDefault() {
        return mIsDefault;
    }

    public void setDefault(boolean aDefault) {
        mIsDefault = aDefault;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public int getPermissionId() {
        return mPermissionId;
    }

    public void setPermissionId(int id) {
        this.mPermissionId = id;
    }

    public int getColorId() {
        return mColorId;
    }

    public void setColorId(int mColorId) {
        this.mColorId = mColorId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
