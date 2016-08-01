package framgia.vn.framgiacrb.object;

import com.google.gson.annotations.SerializedName;

import framgia.vn.framgiacrb.data.model.Event;

/**
 * Created by framgia on 29/07/2016.
 */
public class EventDetailResponse {
    @SerializedName("message")
    private String mMessage;
    @SerializedName("event")
    private Event mEvent;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event mEvent) {
        this.mEvent = mEvent;
    }
}
