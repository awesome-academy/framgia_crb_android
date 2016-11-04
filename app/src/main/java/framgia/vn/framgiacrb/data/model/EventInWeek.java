package framgia.vn.framgiacrb.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lethuy on 05/07/2016.
 */
public class EventInWeek {
    private String mTitle;
    private String mDesciption;
    private Date mDateFinish;
    private Date mHourFinish;
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        this.mTitle = title;
    }
    public String getDesciption() {
        return mDesciption;
    }
    public void setDesciption(String desciption) {
        this.mDesciption = desciption;
    }
    public Date getDateFinish() {
        return mDateFinish;
    }
    public void setDateFinish(Date dateFinish) {
        this.mDateFinish = dateFinish;
    }
    public Date getHourFinish() {
        return mHourFinish;
    }
    public void setHourFinish(Date hourFinish) {
        this.mHourFinish = hourFinish;
    }
    public EventInWeek(String title, String desciption, Date dateFinish,
                       Date hourFinish) {
        super();
        this.mTitle = title;
        this.mDesciption = desciption;
        this.mDateFinish = dateFinish;
        this.mHourFinish = hourFinish;
    }
    public EventInWeek() {
        super();
    }

    public String getDateFormat(Date d)
    {
        SimpleDateFormat dft=new
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dft.format(d);
    }

    public String getHourFormat(Date d)
    {
        SimpleDateFormat dft=new
                SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dft.format(d);
    }
    @Override
    public String toString() {
        return this.mTitle+"-"+
                getDateFormat(this.mDateFinish)+"-"+
                getHourFormat(this.mHourFinish);
    }

}
