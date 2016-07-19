package framgia.vn.framgiacrb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.activity.CreateEventActvity;
import framgia.vn.framgiacrb.activity.DetailActivity;
import framgia.vn.framgiacrb.adapter.ListEventAdapter;
import framgia.vn.framgiacrb.fragment.item.ItemDate;
import framgia.vn.framgiacrb.fragment.item.ItemEvent;
import framgia.vn.framgiacrb.fragment.item.ItemMonth;
import framgia.vn.framgiacrb.utils.SimpleItemTouchHelperCallback;

/**
 * Created by nghicv on 04/07/2016.
 */
public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View mViewEvents;
    private RecyclerView mRecyclerViewEvents;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFloatingActionButton;
    private ListEventAdapter mAdapter;
    private List<Object> mDatas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewEvents = inflater.inflate(R.layout.fragment_events, container, false);
        initViews();
        return mViewEvents;
    }

    private void initViews() {
        mRecyclerViewEvents = (RecyclerView) mViewEvents.findViewById(R.id.rv_events);
        mRecyclerViewEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) mViewEvents.findViewById(R.id.swipe_refresh_layout);
        mFloatingActionButton = (FloatingActionButton) mViewEvents.findViewById(R.id.fab);
        mAdapter = new ListEventAdapter(getActivity(), mDatas);
        initDatas();
        mRecyclerViewEvents.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerViewEvents);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnEventSelectedListener(new ListEventAdapter.OnEventSelectedListener() {
            @Override
            public void onSelected(int position) {
                startActivity(new Intent(getActivity(), DetailActivity.class));
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateEventActvity.class));
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
                int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (dy > 0) {
                    if (totalItemCount -10 <= lastVisibleItem + 1) {
                        Object object = mDatas.get(mDatas.size() - 1);
                        if (object instanceof ItemDate) {
                            ItemDate itemDate = (ItemDate) object;
                            Date date = itemDate.getDate();
                            Calendar calendar = Calendar.getInstance();

                        }
                    }


                } else {

                }
            }
        });
    }

    private void initDatas() {
        mDatas.clear();
        Calendar calendar = Calendar.getInstance();
        java.util.Date today = calendar.getTime();

        for (int j = 0 ; j < 5; j++) {
            int month = Integer.parseInt(android.text.format.DateFormat.format("MM", today).toString());
            int year = Integer.parseInt(android.text.format.DateFormat.format("yyyy", today).toString());
            for (int i = 1; i < 31; i++) {
                if (i <= 1) {
                    mDatas.add(new ItemMonth(month, year));
                } else if (i % 3 == 0 || i == 2) {
                    mDatas.add(new ItemDate(today));
                    calendar.add(Calendar.DATE, 1);
                    today = calendar.getTime();
                } else {
                    mDatas.add(new ItemEvent(new ItemDate(today)));
                }
            }
        }
    }


    @Override
    public void onRefresh() {
        initDatas();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mAdapter.notifyDataSetChanged();
    }
}
