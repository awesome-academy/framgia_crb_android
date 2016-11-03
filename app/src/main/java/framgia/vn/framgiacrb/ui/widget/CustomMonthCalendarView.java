package framgia.vn.framgiacrb.ui.widget;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.util.Calendar;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.ui.adapter.CalendarPagerAdapter;

/**
 * Created by lucky_luke on 7/5/2016.
 */
public class CustomMonthCalendarView extends FrameLayout{
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapterViewPager;

    public CustomMonthCalendarView(Context context) {
        super(context);
        initUi(context);
    }

    public CustomMonthCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUi(context);
    }

    public CustomMonthCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUi(context);
    }

    private void initUi(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_calendar_view, this);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    public void setAdapter(FragmentManager fragmentManager) {
        mAdapterViewPager = new CalendarPagerAdapter(fragmentManager);
        mViewPager.setAdapter(mAdapterViewPager);
        Calendar calendar = Calendar.getInstance();
        mViewPager.setCurrentItem((calendar.get(Calendar.YEAR) - 1970)*12 + calendar.get(Calendar.MONTH));
    }
}
