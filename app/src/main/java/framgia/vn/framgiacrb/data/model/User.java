package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lethuy on 26/07/2016.
 */
public class User extends RealmObject{

    @SerializedName("id")
    @PrimaryKey
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("avatar")
    private String mAvatar;

    @SerializedName("auth_token")
    private String mAutToken;

    @SerializedName("google_calendar_id")
    private String mGoogleCalendarId;

    @SerializedName("user_calendars")
    private RealmList<Calendar> mUserCalendars;

    @SerializedName("shared_calendars")
    private RealmList<Calendar> mShareUserCalendars;

    public User() {}

    public User(int id, String name, String email, String avatar, String auth_token) {
        this.mId = id;
        this.mName = name;
        this.mEmail = email;
        this.mAvatar = avatar;
        this.mAutToken = auth_token;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        this.mAvatar = avatar;
    }

    public String getAuth_token() {
        return mAutToken;
    }

    public void setAuth_token(String auth_token) {
        this.mAutToken = auth_token;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public List<Calendar> getUserCalendars() {
        return mUserCalendars;
    }

    public void setUserCalendars(RealmList<Calendar> userCalendars) {
        mUserCalendars = userCalendars;
    }

    public RealmList<Calendar> getShareUserCalendars() {
        return mShareUserCalendars;
    }

    public void setShareUserCalendars(RealmList<Calendar> shareUserCalendars) {
        mShareUserCalendars = shareUserCalendars;
    }
}
