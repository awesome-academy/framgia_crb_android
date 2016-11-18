package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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

    public Attendee() {
    }

    public Attendee(Attendee attendee) {
        this.mId = attendee.getId();
        this.mEmail = attendee.getEmail();
        this.mUserId = attendee.getUserId();
        this.mEventId = attendee.getEventId();
    }

    public static RealmList<Attendee> cloneListAttendee(RealmList<Attendee> attendeeList) {
        RealmList<Attendee> result = new RealmList<>();
        for (Attendee attendee : attendeeList) {
            result.add(new Attendee(attendee));
        }
        return result;
    }

    public static String getLisAttendee(RealmList<Attendee> list) {
        if (list == null) {
            return "";
        }
        int length = list.size();
        String attendees = "";
        for (int i = 0; i < length; i++) {
            attendees += list.get(i).getEmail() + "\n";
        }
        return attendees;
    }

    private String getEmail() {
        return mEmail;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getEventId() {
        return mEventId;
    }

    public void setEventId(int eventId) {
        mEventId = eventId;
    }
}