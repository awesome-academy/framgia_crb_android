package framgia.vn.framgiacrb.fragment.item;

/**
 * Created by nghicv on 04/07/2016.
 */
public class ItemMonth {
    private int mMonth;
    private String mStringMonth;
    private int mYear;

    public ItemMonth(int month, int year) {
        mMonth = month;
        mYear = year;
    }

    public ItemMonth(int month, String stringMonth, int year) {
        mMonth = month;
        mStringMonth = stringMonth;
        mYear = year;
    }

    public String getStringMonth() {
        return mStringMonth;
    }

    public void setStringMonth(String stringMonth) {
        mStringMonth = stringMonth;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }
}
