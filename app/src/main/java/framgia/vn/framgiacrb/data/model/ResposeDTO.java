package framgia.vn.framgiacrb.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nghicv on 27/07/2016.
 */
public class ResposeDTO {

    @SerializedName("message")
    private String mMessage;

    @SerializedName("events")
    private List<Event> mEvents;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
    }
}
