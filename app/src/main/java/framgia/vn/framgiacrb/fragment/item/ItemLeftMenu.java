package framgia.vn.framgiacrb.fragment.item;

/**
 * Created by lucky_luke on 7/20/2016.
 */
public class ItemLeftMenu {
    private int mImageResource;
    private String mTitle;
    private String mEmail;
    private int mCalendarId;

    public ItemLeftMenu() {
    }

    public int getImageResource() {
        return mImageResource;
    }

    public void setImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    public int getCalendarId() {
        return mCalendarId;
    }

    public void setCalendarId(int mCalendarId) {
        this.mCalendarId = mCalendarId;
    }
}
