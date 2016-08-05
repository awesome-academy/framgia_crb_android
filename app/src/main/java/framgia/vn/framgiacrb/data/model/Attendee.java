package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nghicv on 04/08/2016.
 */
public class Attendee extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private int mId;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("user_id")
    private int mUserId;

    @SerializedName("event_id")
    private int mEventId;
}
