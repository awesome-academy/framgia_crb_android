package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lethuy on 27/07/2016.
 */
public class LoginResponse implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private String user;

    public LoginResponse(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
