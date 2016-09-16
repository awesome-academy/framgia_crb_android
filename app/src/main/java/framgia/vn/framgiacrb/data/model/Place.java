package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by nghicv on 04/08/2016.
 */
public class Place extends RealmObject {

    public Place() {}

    public Place(Place place) {
            this.mId = place.getId();
            this.mName = place.getName();
            this.mUserId = place.getUserId();
            this.mAddress = place.getAddress();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

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
