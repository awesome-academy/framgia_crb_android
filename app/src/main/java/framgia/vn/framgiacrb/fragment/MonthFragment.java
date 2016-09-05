package framgia.vn.framgiacrb.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import framgia.vn.framgiacrb.activity.MainActivity;
import framgia.vn.framgiacrb.adapter.MonthPagerAdapter;
import framgia.vn.framgiacrb.adapter.MonthToolbarPagerAdapter;
import framgia.vn.framgiacrb.ui.MonthView;

/**
 * Created by lucky_luke on 7/6/2016.
 */
public class MonthFragment extends Fragment{
    private HashSet<Date> mEvents;
    private ViewPager mViewPager;
    private MonthPagerAdapter mMonthPagerAdapter;
    private GoToDayReceiver mGoToDayReceiver;

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
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Calendar calendar = Calendar.getInstance();
        //mViewPager.setCurrentItem((calendar.get(Calendar.YEAR) - MonthPagerAdapter.MIN_YEAR)*12 + calendar.get(Calendar.MONTH));
        mGoToDayReceiver = new GoToDayReceiver();
        IntentFilter intentFilter = new IntentFilter(MainActivity.ACTION_TODAY);
        getActivity().registerReceiver(mGoToDayReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mGoToDayReceiver);
        mGoToDayReceiver = null;
    }

    private class GoToDayReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MainActivity.ACTION_TODAY)) {
                Calendar calendar = Calendar.getInstance();
                int year = intent.getIntExtra(MonthView.YEAR, calendar.get(Calendar.YEAR));
                int month = intent.getIntExtra(MonthView.MONTH, calendar.get(Calendar.MONTH));
                mViewPager.setCurrentItem((year- MonthToolbarPagerAdapter.MIN_YEAR)*12 + month);
            }
        }
    }
}
