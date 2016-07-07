package framgia.vn.framgiacrb.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.ui.MonthView;

/**
 * Created by lucky_luke on 7/6/2016.
 */
public class ItemMonthFragment extends Fragment {
    public static final String YEAR_EXTRA = "time";
    public static final String MONTH_EXTRA = "month";
    private Calendar mCalendar = Calendar.getInstance();
    private MonthView mMonthView;

    // newInstance constructor for creating fragment with arguments
    public static ItemMonthFragment newInstance(int year, int month) {
        ItemMonthFragment fragmentMonth = new ItemMonthFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR_EXTRA, year);
        args.putInt(MONTH_EXTRA, month);
        fragmentMonth.setArguments(args);
        return fragmentMonth;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int year = getArguments().getInt(YEAR_EXTRA);
        int month = getArguments().getInt(MONTH_EXTRA);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_view, container, false);
        mMonthView = (MonthView) view.findViewById(R.id.month_view);
        mMonthView.setTime(mCalendar);
        return view;
    }
}
