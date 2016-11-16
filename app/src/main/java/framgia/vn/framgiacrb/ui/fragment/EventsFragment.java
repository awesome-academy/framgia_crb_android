package framgia.vn.framgiacrb.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.Session;
import framgia.vn.framgiacrb.data.remote.EventRepositories;
import framgia.vn.framgiacrb.listener.OnCloseToolbarListener;
import framgia.vn.framgiacrb.listener.OnLoadEventListener;
import framgia.vn.framgiacrb.ui.activity.CreateEventActivity;
import framgia.vn.framgiacrb.ui.activity.DetailActivity;
import framgia.vn.framgiacrb.ui.activity.MainActivity;
import framgia.vn.framgiacrb.ui.adapter.ListEventAdapter;
import framgia.vn.framgiacrb.ui.fragment.item.ItemMonth;
import framgia.vn.framgiacrb.ui.widget.MonthView;
import framgia.vn.framgiacrb.utils.Connectivity;
import framgia.vn.framgiacrb.utils.RenderEventUtil;
import framgia.vn.framgiacrb.utils.SimpleItemTouchHelperCallback;
import framgia.vn.framgiacrb.utils.TimeUtils;
import io.realm.Realm;

/**
 * Created by nghicv on 04/07/2016.
 */
public class EventsFragment extends Fragment implements OnLoadEventListener {
    public static final int REQUEST_CODE = 1;
    public static final int OFFSET_ITEM_TODAY = 60;
    private View mViewEvents;
    private RecyclerView mRecyclerViewEvents;
    private FloatingActionButton mFloatingActionButton;
    private SwipeRefreshLayout mRefreshLayout;
    private ListEventAdapter mAdapter;
    private List<Object> mDatas = new ArrayList<>();
    private int mFirstMonth;
    private int mPositionToday;
    private int mFirstYear;
    private int mLastMonth;
    private int mLastYear;
    private boolean isLoading;
    private int mOldDataSize;
    private EventRepositories mEventRepositories;
    private EventRepositoriesLocal mEventRepositoriesLocal;
    private Realm mRealm;
    private BroadcastReceiver mBroadcastReceiverToday;
    private BroadcastReceiver mBroadcastReceiverToDate;
    private framgia.vn.framgiacrb.data.model.Calendar mCalendar;
    private OnCloseToolbarListener mOnCloseToolbarListener;
    private Date mCurrentDate;
    private int mCurrentPosition;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewEvents = inflater.inflate(R.layout.fragment_events, container, false);
        initViews();
        mCalendar = new framgia.vn.framgiacrb.data.model.Calendar();
        mCalendar.setId(Session.sCalendarId);
        mRealm = Realm.getDefaultInstance();
        mEventRepositoriesLocal = new EventRepositoriesLocal(mRealm);
        mEventRepositories = new EventRepositories();
        mEventRepositories.setOnLoadEventListener(this);
        if (MainActivity.sCurrentDate == null) {
            try {
                initDatas();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            loadDatasRestore();
        }
        mBroadcastReceiverToday = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MainActivity.ACTION_TODAY)) {
                    scrollToposition(mPositionToday, OFFSET_ITEM_TODAY);
                }
            }
        };
        mBroadcastReceiverToDate = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MainActivity.ACTION_SCROLL_DAY)) {
                    String timeString = intent.getStringExtra(MonthView.TITLE);
                    Date date = TimeUtils.stringToDate(timeString, TimeUtils.DATE_STRING_FORMAT);
                    int month = Integer
                        .parseInt(android.text.format.DateFormat
                            .format(TimeUtils.MONTH_NUMBER_FORMAT, date).toString());
                    int year =
                        Integer.parseInt(DateFormat.format(TimeUtils.YEAR_FORMAT, date).toString());
                    int position = -1;
                    if ((month > mLastMonth && year == mLastYear) ||
                        (month == 1 && year > mLastYear)) {
                        try {
                            loadDatasForNextMonth();
                            mAdapter.notifyDataSetChanged();
                            position = findPositionByDate(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if ((month < mFirstMonth && year == mFirstYear) ||
                        (month == 12 && year < mFirstYear)) {
                        position = 0;
                        try {
                            loadDatasForPrevMonth();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        position = findPositionByDate(date);
                    }
                    scrollToposition(position, 0);
                }
            }
        };
        getActivity()
            .registerReceiver(mBroadcastReceiverToday, new IntentFilter(MainActivity.ACTION_TODAY));
        getActivity().registerReceiver(mBroadcastReceiverToDate,
            new IntentFilter(MainActivity.ACTION_SCROLL_DAY));
        return mViewEvents;
    }

    public void setOnCloseToolbarListener(OnCloseToolbarListener listener) {
        mOnCloseToolbarListener = listener;
    }

    private void loadDatas() {
        if (Connectivity.isConnected(getActivity()) &&
            Connectivity.isConnectedFast(getActivity())) {
            mEventRepositories.getEventsByCalendar(Session.sAuthToken, mCalendar, getActivity());
        } else {
            try {
                initDatas();
                setRefreshing(false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), getActivity().getString(R.string.message_not_connect),
                Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDatasRestore() {
        try {
            initDatas();
            int left = 0;
            int right = mDatas.size();
            restoreData(left, right);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setRefreshing(false);
    }

    private void initViews() {
        mRefreshLayout =
            (SwipeRefreshLayout) mViewEvents.findViewById(R.id.swipe_refresh_layout_events);
        mRecyclerViewEvents = (RecyclerView) mViewEvents.findViewById(R.id.rv_events);
        mRecyclerViewEvents.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mOnCloseToolbarListener.onCloseToolbar(true);
                return false;
            }
        });
        mRecyclerViewEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFloatingActionButton = (FloatingActionButton) mViewEvents.findViewById(R.id.fab);
        mAdapter = new ListEventAdapter(getActivity(), mDatas);
        mRecyclerViewEvents.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback =
            new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerViewEvents);
        mAdapter.setOnEventSelectedListener(new ListEventAdapter.OnEventSelectedListener() {
            @Override
            public void onSelected(int idSelected, int position) {
                Event event = (Event) mDatas.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constant.Intent.INTENT_ID_EVENT, idSelected);
                intent.putExtra(Constant.Intent.INTENT_START_TIME, event.getStartTime());
                intent.putExtra(Constant.Intent.INTENT_FINISH_TIME, event.getFinishTime());
                getActivity().startActivity(intent);
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), CreateEventActivity.class),
                    REQUEST_CODE);
            }
        });
        mRecyclerViewEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findLastVisibleItemPosition();
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstVisibleItemPosition();
                if (dy > 0) {
                    setDateForCalendar(firstVisibleItem);
                    if (totalItemCount - 10 < lastVisibleItem + 1) {
                        if (!isLoading) {
                            isLoading = true;
                            try {
                                loadDatasForNextMonth();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                            isLoading = false;
                        }
                    }
                } else {
                    setDateForCalendar(firstVisibleItem);
                    if (10 > firstVisibleItem) {
                        if (!isLoading) {
                            isLoading = true;
                            try {
                                loadDatasForPrevMonth();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            isLoading = false;
                        }
                    }
                }
            }
        });
    }

    private void initDatas() throws ParseException {
        mDatas.clear();
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        int month = Integer.parseInt(
            android.text.format.DateFormat.format(TimeUtils.MONTH_NUMBER_FORMAT, today).toString());
        mLastMonth = month;
        mFirstMonth = month;
        String stringMonth =
            android.text.format.DateFormat.format(TimeUtils.MONTH_STRING_FORMAT, today).toString();
        mLastYear =
            Integer.parseInt(
                android.text.format.DateFormat.format(TimeUtils.YEAR_FORMAT, today).toString());
        mFirstYear = mLastYear;
        calendar.set(mLastYear, month - 1, 1);
        Date date = calendar.getTime();
        mDatas.add(new ItemMonth(month, stringMonth, mLastYear));
        boolean isTimelineAdded = false;
        while (month < mLastMonth + 1) {
            mDatas.add(date);
            if (date.equals(today)) {
                mPositionToday = mDatas.size();
                List<Event> events = RenderEventUtil.getGenCodeEvent(getActivity(), TimeUtils
                    .formatDate(date));
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    if (event.getStartTime().getTime() > today.getTime() && !isTimelineAdded) {
                        mDatas.add(null);
                        isTimelineAdded = true;
                    }
                    mDatas.add(event);
                }
                if (!isTimelineAdded) {
                    mDatas.add(null);
                }
            } else {
                mDatas.addAll(RenderEventUtil.getGenCodeEvent(getActivity(), TimeUtils.formatDate
                    (date)));
            }
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            month = Integer
                .parseInt(android.text.format.DateFormat.format(TimeUtils.MONTH_NUMBER_FORMAT, date)
                    .toString());
        }
        mAdapter.notifyDataSetChanged();
        if (mPositionToday > Constant.Number.MAX_POSITION_TODAY) {
            loadDatasForNextMonth();
        }
        scrollToposition(mPositionToday, OFFSET_ITEM_TODAY);
    }

    private synchronized void loadDatasForNextMonth() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        if (mLastMonth < 12) {
            mLastMonth += 1;
        } else {
            mLastMonth = 1;
            mLastYear += 1;
        }
        int month = mLastMonth;
        calendar.set(mLastYear, month - 1, 1);
        Date date = calendar.getTime();
        String stringMonth =
            android.text.format.DateFormat.format(TimeUtils.MONTH_STRING_FORMAT, date).toString();
        mDatas.add(new ItemMonth(month, stringMonth, mLastYear));
        while ((month < mLastMonth + 1) && !(mLastMonth == 12 && month == 1)) {
            mDatas.add(date);
            mDatas.addAll(RenderEventUtil.getGenCodeEvent(getActivity(), TimeUtils.formatDate
                (date)));
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            month = Integer
                .parseInt(android.text.format.DateFormat.format(TimeUtils.MONTH_NUMBER_FORMAT, date)
                    .toString());
        }
    }

    private synchronized void loadDatasForPrevMonth() throws ParseException {
        mOldDataSize = mDatas.size();
        Calendar calendar = Calendar.getInstance();
        if (mFirstMonth > 1) {
            mFirstMonth -= 1;
        } else {
            mFirstMonth = 12;
            mFirstYear -= 1;
        }
        int month = mFirstMonth;
        calendar.set(mFirstYear, month - 1, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date date = calendar.getTime();
        String stringMonth =
            android.text.format.DateFormat.format(TimeUtils.MONTH_STRING_FORMAT, date).toString();
        while ((month >= mFirstMonth) && !(mFirstMonth == 1 && month == 12)) {
            mDatas.add(0, date);
            mDatas.addAll(1, RenderEventUtil.getGenCodeEvent(getActivity(), TimeUtils.formatDate
                (date)));
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();
            month = Integer
                .parseInt(android.text.format.DateFormat.format(TimeUtils.MONTH_NUMBER_FORMAT, date)
                    .toString());
        }
        mDatas.add(0, new ItemMonth(mFirstMonth, stringMonth, mFirstYear));
        mAdapter.notifyItemRangeInserted(0, mDatas.size() - mOldDataSize);
        mPositionToday = mPositionToday + mDatas.size() - mOldDataSize;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
        getActivity().unregisterReceiver(mBroadcastReceiverToday);
        getActivity().unregisterReceiver(mBroadcastReceiverToDate);
    }

    private void setDateForCalendar(int position) {
        Object object = mDatas.get(position);
        if (object instanceof Date) {
            mCurrentDate = (Date) object;
            MainActivity.sCurrentDate = mCurrentDate;
            mCurrentPosition = position;
            ((MainActivity) getActivity())
                .setSubTitle(TimeUtils.toStringDate(mCurrentDate, TimeUtils.DATE_FORMAT_TOOLBAR));
        }
    }

    public void refreshData() {
        setRefreshing(true);
        loadDatas();
    }

    private void setRefreshing(final boolean isRefreshing) {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    private int findPositionByDate(Date date) {
        int left = 0;
        int right = mDatas.size();
        try {
            if (mCurrentDate.before(TimeUtils.formatDate(date))) {
                left = mCurrentPosition;
                for (int i = left; i <= right; i++) {
                    if (mDatas.get(i) instanceof Date && TimeUtils.formatDate((Date) mDatas.get(i))
                        .equals(TimeUtils.formatDate(date)))
                        return i;
                }
            } else {
                right = mCurrentPosition;
                for (int i = right; i >= left; i--) {
                    if (mDatas.get(i) instanceof Date && TimeUtils.formatDate((Date) mDatas.get(i))
                        .equals(TimeUtils.formatDate(date)))
                        return i;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshData();
        }
    }

    private void scrollToposition(int position, int offSet) {
        LinearLayoutManager linearLayoutManager =
            (LinearLayoutManager) mRecyclerViewEvents.getLayoutManager();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int offSetPx =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, offSet, metrics);
        linearLayoutManager.scrollToPositionWithOffset(position, (int) offSetPx);
    }

    private void restoreData(int left, int right) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(MainActivity.sCurrentDate);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            if ((month == mLastMonth && year == mLastYear) ||
                (month == mFirstMonth && year == mFirstYear)) {
                int position = findDate(left, right, MainActivity.sCurrentDate);
                scrollToposition(position, 0);
                return;
            }
            if ((month > mLastMonth && mLastYear == year) ||
                (month <= mLastMonth && year > mLastYear)) {
                left = mDatas.size();
                loadDatasForNextMonth();
                restoreData(left, mDatas.size());
            } else {
                loadDatasForPrevMonth();
                restoreData(left, mDatas.size() - right);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private int findDate(int left, int right, Date date) {
        for (int i = left; i < right; i++) {
            try {
                if (mDatas.get(i) instanceof Date &&
                    TimeUtils.formatDate((Date) mDatas.get(i)).equals(TimeUtils.formatDate(date)))
                    return i;
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    @Override
    public void onSuccess() {
        try {
            initDatas();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setRefreshing(false);
    }

    @Override
    public void onError() {
        Toast.makeText(getActivity(), getString(R.string.message_error), Toast.LENGTH_SHORT).show();
    }
}
