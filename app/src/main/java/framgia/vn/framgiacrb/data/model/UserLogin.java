package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lethuy on 26/07/2016.
 */
public class UserLogin implements Serializable{
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("avatar")
    private String mAvatar;
    @SerializedName("auth_token")
    private String mAuth_token;
    @SerializedName("message")
    private String mMessage;

    public UserLogin() {}

    public UserLogin(int id, String name, String email, String avatar, String auth_token) {
        this.mId = id;
        this.mName = name;
        this.mEmail = email;
        this.mAvatar = avatar;
        this.mAuth_token = auth_token;
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
        return mAuth_token;
    }

    public void setAuth_token(String auth_token) {
        this.mAuth_token = auth_token;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
