package framgia.vn.framgiacrb.fragment.item;

/**
 * Created by nghicv on 04/07/2016.
 */
public class ItemEvent {
    private ItemDate mDate;

    public ItemEvent(ItemDate date) {
        mDate = date;
    }

    public ItemDate getDate() {
        return mDate;
    }

    public void setDate(ItemDate date) {
        mDate = date;
    }
}
