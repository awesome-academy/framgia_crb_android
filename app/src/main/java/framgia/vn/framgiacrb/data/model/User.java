package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

/**
 * Created by lethuy on 26/07/2016.
 */
public class User implements Serializable{
    @SerializedName("id")
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

    @SerializedName("user_calendars")
    private List<Calendar> mUserCalendars;

    @SerializedName("shared_calendars")
    private List<Calendar> mShareUserCalendars;

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

    public void setUserCalendars(List<Calendar> mUserCalendars) {
        this.mUserCalendars = mUserCalendars;
    }

    public List<Calendar> getShareUserCalendars() {
        return mShareUserCalendars;
    }

    public void setShareUserCalendars(List<Calendar> mShareUserCalendars) {
        this.mShareUserCalendars = mShareUserCalendars;
    }
}
