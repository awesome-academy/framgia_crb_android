package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lethuy on 29/07/2016.
 */
public class CreateEventResponse {
    @SerializedName("message")
    private String mMessage;
    @SerializedName("event")
    private Event mUser;
    @SerializedName("errors")
    private String mError;

    public CreateEventResponse() {
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public Event getUser() {
        return mUser;
    }

    public void setUser(Event mUser) {
        this.mUser = mUser;
    }

    public String getError() {
        return mError;
    }

    public void setError(String mError) {
        this.mError = mError;
    }
}
