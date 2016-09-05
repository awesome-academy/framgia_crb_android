package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lethuy on 29/07/2016.
 */
public class NewEvent {

    @SerializedName("auth_token")
    private String mAuthToken;

    @SerializedName("event")
    private Event mEvent;

    public NewEvent(String mAuthToken, Event mEvent) {
        this.mAuthToken = mAuthToken;
        this.mEvent = mEvent;
    }

    public Event getEvent() {
        return mEvent;
    }
}
