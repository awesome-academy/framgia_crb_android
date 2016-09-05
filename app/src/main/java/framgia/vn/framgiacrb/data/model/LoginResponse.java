package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lethuy on 27/07/2016.
 */
public class LoginResponse implements Serializable {
    @SerializedName("message")
    private String mMessage;
    @SerializedName("user")
    private User mUser;

    public LoginResponse(){}

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}
