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

    public Place() {
    }

    public Place(Place place) {
        this.mId = place.getId();
        this.mName = place.getName();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}