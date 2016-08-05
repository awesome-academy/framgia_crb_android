package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
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

    public static String getLisAttendee(RealmList<Attendee> list) {
        int length = list.size();
        String attendees = "";
        for (int i = 0; i < length; i++) {
            attendees += list.get(i).getEmail() + "\n";
        }
        return attendees;
    }

    public String getEmail() {
        return mEmail;
    }
}
