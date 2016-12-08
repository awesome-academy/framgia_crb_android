package framgia.vn.framgiacrb.data.model;

/**
 * Created by framgia on 06/12/2016.
 */
public class GoogleCalendar {
    private int mId;
    private String mAccountName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String accountName) {
        mAccountName = accountName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GoogleCalendar) {
            GoogleCalendar googleCalendar = (GoogleCalendar) o;
            return mAccountName.equals(googleCalendar.getAccountName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mAccountName.hashCode();
    }
}
