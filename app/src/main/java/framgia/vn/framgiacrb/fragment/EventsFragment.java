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
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.activity.CreateEventActvity;
import framgia.vn.framgiacrb.activity.DetailActivity;
import framgia.vn.framgiacrb.adapter.ListEventAdapter;
import framgia.vn.framgiacrb.fragment.item.Date;
import framgia.vn.framgiacrb.fragment.item.Event;
import framgia.vn.framgiacrb.fragment.item.Month;
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
    }

    private void initDatas() {
        mDatas.clear();
        for (int j = 0 ; j < 5; j++) {
            for (int i = 1; i < 31; i++) {
                if (i <= 1) {
                    mDatas.add(new Month());
                } else if (i % 3 == 0 || i == 2) {
                    mDatas.add(new Date());
                } else {
                    mDatas.add(new Event());
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
