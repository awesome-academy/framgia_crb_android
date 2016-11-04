package framgia.vn.framgiacrb.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import framgia.vn.framgiacrb.ui.fragment.CalendarFragment;

/**
 * Created by lucky_luke on 7/5/2016.
 */
public class CalendarPagerAdapter extends FragmentPagerAdapter{
    private static final int MIN_YEAR = 1970;
    private static final int MAX_YEAR = 2050;

    public CalendarPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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
        return CalendarFragment.newInstance(year, month);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
