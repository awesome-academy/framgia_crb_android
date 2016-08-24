package framgia.vn.framgiacrb.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import framgia.vn.framgiacrb.activity.MainActivity;
import framgia.vn.framgiacrb.R;

/**
 * Created by lucky_luke on 7/5/2016.
 */
public class CalendarFragment extends Fragment {

    // how many days to show, default to six weeks, 42 days
    private static final int DAYS_COUNT = 42;
    public static final String TITLE = "time";
    public static final String YEAR_EXTRA = "year";
    public static final String MONTH_EXTRA = "month";
    private Calendar calendar = Calendar.getInstance();

    private LinearLayout mHeader;
    private TextView mTxtDate;
    private GridView mGrid;

    LayoutInflater inflater;

    // season's rainbow
    final int[] rainbow = new int[] {
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };

    // month-season, association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    // newInstance constructor for creating fragment with arguments
    public static CalendarFragment newInstance(int year, int month) {
        CalendarFragment calendarFragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR_EXTRA, year);
        args.putInt(MONTH_EXTRA, month);
        calendarFragment.setArguments(args);
        return calendarFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_view, container, false);

        // layout is inflated, assign local variables to components
        mHeader = (LinearLayout) view.findViewById(R.id.calendar_header);
        mTxtDate = (TextView) view.findViewById(R.id.calendar_date_display);
        mGrid = (GridView) view.findViewById(R.id.calendar_grid);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(MainActivity.ACTION_BROADCAST);
                intent.putExtra(TITLE, MainActivity.dateFormat.format((Date) parent.getItemAtPosition(position)));
                CalendarFragment.this.getActivity().sendBroadcast(intent);
            }
        });
        updateCalendar();

        return view;
    }

    @SuppressWarnings("deprecation")
    private void updateCalendar() {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar currentDate = (Calendar) calendar.clone();
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = currentDate.get(Calendar.DAY_OF_WEEK) - 1;
        currentDate.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        while (cells.size() < DAYS_COUNT) {
            cells.add(currentDate.getTime());
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        // update mGrid
        // a good place to customize how days are displayed is the Adapter
        mGrid.setAdapter(new CalendarAdapter(getContext(), cells, null));
        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        mTxtDate.setText(sdf.format(calendar.getTime()));
        // set mHeader color according to current season
        int month = calendar.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];
        mHeader.setBackgroundColor(getContext().getResources().getColor(color));
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        // days with events
        HashSet<Date> eventDays;

        // for view inflation
        LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays) {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.control_calendar_day, parent, false);
            }

            // if this day has an event, specify event image
            convertView.setBackgroundColor(0);
            if (eventDays != null) {
                for (Date eventDate : eventDays) {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year) {
                        // mark this day for event
                        //convertView.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }
            // clear styling
            ((TextView) convertView).setTypeface(null, Typeface.NORMAL);
            ((TextView) convertView).setTextColor(Color.BLACK);
            if (date.getMonth() != today.getMonth() || date.getYear() != today.getYear()) {
                // if this day is outside current month, grey it out
                ((TextView) convertView).setTextColor(getContext().getResources().getColor(R.color.greyed_out));
            } else if (date.getDate() == today.getDate()) {
                // if it is today, set it to blue/bold
                ((TextView) convertView).setTypeface(null, Typeface.BOLD);
                ((TextView) convertView).setTextColor(Color.WHITE);
                ((TextView) convertView).setBackgroundResource(R.drawable.circle_selected);
            }
            // set text
            ((TextView) convertView).setText(String.valueOf(date.getDate()));
            return convertView;
        }
    }
}
