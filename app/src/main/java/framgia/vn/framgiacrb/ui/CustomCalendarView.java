package framgia.vn.framgiacrb.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import framgia.vn.framgiacrb.R;

/**
 * Created by lucky_luke on 7/3/2016.
 */
public class CustomCalendarView extends LinearLayout {
    private LinearLayout mHeader;
    private ImageView mBtnPrev;
    private ImageView mBtnNext;
    private TextView mTxtDate;
    private GridView mGrid;

    // current displayed month
    Calendar currentDate = Calendar.getInstance();

    // event handling
    EventHandler eventHandler = null;

    // how many days to show, default to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    String dateFormat;

    // season's rainbow
    int[] rainbow = new int[] {
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };

    // month-season, association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load component XML Layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this);
        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();
        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarDateElement);
        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarDateElement_dateFormat);
            if (null == dateFormat) {
                dateFormat = DATE_FORMAT;
            }
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        mHeader = (LinearLayout) findViewById(R.id.calendar_header);
        mBtnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        mBtnNext = (ImageView) findViewById(R.id.calendar_next_button);
        mTxtDate = (TextView) findViewById(R.id.calendar_date_display);
        mGrid = (GridView) findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        mBtnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });
        // subtract one month and refresh UI
        mBtnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });
        // long-pressing a day
        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // handle long-press
                if (null == eventHandler) return false;
                eventHandler.onDayLongPress((Date)parent.getItemAtPosition(position));
                return true;
            }
        });
    }

    public void updateCalendar() {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in mGrid
     */
    @SuppressWarnings("deprecation")
    public void updateCalendar(HashSet<Date> events) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        // update mGrid
        // a good place to customize how days are displayed is the Adapter
        mGrid.setAdapter(new CalendarAdapter(getContext(), cells, events));
        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        mTxtDate.setText(sdf.format(currentDate.getTime()));
        // set mHeader color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];
        mHeader.setBackgroundColor(getResources().getColor(color));
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
            convertView.setBackgroundResource(0);
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
                ((TextView) convertView).setTextColor(getResources().getColor(R.color.greyed_out));
            } else if (date.getDate() == today.getDate()) {
                // if it is today, set it to blue/bold
                ((TextView) convertView).setTypeface(null, Typeface.BOLD);
                ((TextView) convertView).setTextColor(getResources().getColor(R.color.today));
            }
            // set text
            ((TextView) convertView).setText(String.valueOf(date.getDate()));
            return convertView;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {
        void onDayLongPress(Date date);
    }
}
