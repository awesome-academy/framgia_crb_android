package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nghicv on 04/08/2016.
 */
public class Place extends RealmObject {

    @SerializedName("id")
    @PrimaryKey
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("user_id")
    private  int mUserId;

    public String getAddress() {
        return mAddress;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setId(int mId) {

        this.mId = mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }
}
