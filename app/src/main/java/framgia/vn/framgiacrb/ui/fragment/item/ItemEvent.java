package framgia.vn.framgiacrb.ui.fragment.item;

import java.util.Date;

import framgia.vn.framgiacrb.data.model.Event;

/**
 * Created by nghicv on 04/07/2016.
 */
public class ItemEvent {
    private Date mDate;
    private Event mEvent;

    public ItemEvent(Date date) {
        mDate = date;
    }

    public ItemEvent(Date date, Event event) {
        mDate = date;
        mEvent = event;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
