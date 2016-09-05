package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by nghicv on 04/08/2016.
 */
public class Place extends RealmObject {

    @SerializedName("id")
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
}
