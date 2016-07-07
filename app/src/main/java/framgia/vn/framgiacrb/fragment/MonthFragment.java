package framgia.vn.framgiacrb.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.adapter.MonthPagerAdapter;

/**
 * Created by lucky_luke on 7/6/2016.
 */
public class MonthFragment extends Fragment{
    private HashSet<Date> mEvents;
    private ViewPager mViewPager;
    private MonthPagerAdapter mMonthPagerAdapter;

    public void setEvents(HashSet<Date> events) {
        this.mEvents = events;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.month_fragment, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.month_viewpager);
        mMonthPagerAdapter = new MonthPagerAdapter(getActivity().getSupportFragmentManager(), null);
        mViewPager.setAdapter(mMonthPagerAdapter);
        Calendar calendar = Calendar.getInstance();
        mViewPager.setCurrentItem((calendar.get(Calendar.YEAR) - MonthPagerAdapter.MIN_YEAR)*12 + calendar.get(Calendar.MONTH));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getActivity(), (position / 12) + 1970 + " " + (position % 12 + 1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return v;
    }
}
