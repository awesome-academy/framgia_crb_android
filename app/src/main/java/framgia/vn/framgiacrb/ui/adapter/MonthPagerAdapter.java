package framgia.vn.framgiacrb.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Date;
import java.util.HashSet;

import framgia.vn.framgiacrb.ui.fragment.ItemMonthFragment;

/**
 * Created by lucky_luke on 7/6/2016.
 */
public class MonthPagerAdapter extends FragmentStatePagerAdapter {
    public static final int MIN_YEAR = 1970;
    public static final int MAX_YEAR = 2050;

    private HashSet<Date> mEvents;

    public MonthPagerAdapter(FragmentManager fragmentManager, HashSet<Date> events) {
        super(fragmentManager);
        this.mEvents = events;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return (MAX_YEAR - MIN_YEAR) * 12;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        int year = (position / 12) + MIN_YEAR;
        int month = position % 12;
        return ItemMonthFragment.newInstance(year, month);
    }

    // Returns the page title for the top indicator
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return "Page " + position;
//    }
}
