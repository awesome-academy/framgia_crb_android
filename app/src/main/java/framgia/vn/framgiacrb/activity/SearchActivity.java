package framgia.vn.framgiacrb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.adapter.SearchEventAdapter;
import framgia.vn.framgiacrb.asyntask.SearchEventAsynTask;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.ui.listener.AsyncTaskFinishListener;
import io.realm.RealmList;

/**
 * Created by lethuy on 04/07/2016.
 */
public class SearchActivity extends AppCompatActivity implements AsyncTaskFinishListener {
    private Toolbar mToolbar;
    public RecyclerView mRecycler;
    private SearchView mSearchView;
    private SearchView.OnQueryTextListener mSearchViewListener;
    private LinearLayoutManager layoutManager;
    private SearchEventAdapter mAdapter;
    private String mTextSearch;
    private RealmList mRealmList = new RealmList();
    public static int sMonth = Constant.INVALID_INDEX;
    public static int sYear = Constant.INVALID_INDEX;
    public static boolean sIsHasMoreEvent = true;
    public static boolean sAsyncTaskFinish = true;
    public static boolean sNotNeedYear = false;
    private boolean mHaveListener = false;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView
        .OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            Log.d("tag", "run in onscroll");
            super.onScrolled(recyclerView, dx, dy);
            int totalItemCount = layoutManager.getItemCount();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1;
            if (sAsyncTaskFinish && sIsHasMoreEvent && dy >= 0 &&
                totalItemCount == lastVisibleItem) {
                sMonth++;
                if (sMonth == 13) {
                    sNotNeedYear = false;
                    sMonth = 1;
                    sYear++;
                }
                Log.d("month", Integer.toString(sMonth));
                Log.d("year", Integer.toString(sYear));
                if (!mTextSearch.equals("")) {
                    sAsyncTaskFinish = false;
                    SearchEventAsynTask searchEventAsynTask = new SearchEventAsynTask
                        (SearchActivity.this, mRealmList);
                    searchEventAsynTask.setListener(SearchActivity.this);
                    searchEventAsynTask.execute(mTextSearch);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        mAdapter = new SearchEventAdapter(SearchActivity.this, mRealmList);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search)
            .getActionView();
        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!sAsyncTaskFinish) {
                    return false;
                }
                mTextSearch = query;
                mRealmList.clear();
                sNotNeedYear = false;
                sIsHasMoreEvent = true;
                sMonth = Constant.INVALID_INDEX;
                if (mHaveListener) {
                    mRecycler.removeOnScrollListener(mOnScrollListener);
                    mHaveListener = false;
                }
                if (!query.equals("")) {
                    SearchEventAsynTask searchEventAsynTask = new SearchEventAsynTask
                        (SearchActivity.this, mRealmList);
                    searchEventAsynTask.setListener(SearchActivity.this);
                    searchEventAsynTask.execute(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinish(RealmList list) {
        Log.d("list size", Integer.toString(list.size()));
        sAsyncTaskFinish = true;
        if (mAdapter.data.size() == list.size() && sIsHasMoreEvent && mHaveListener) {
            mOnScrollListener.onScrolled(mRecycler, 0, 0);
            mRecycler.removeOnScrollListener(mOnScrollListener);
            mHaveListener = false;
        } else if (!mHaveListener) {
            mRecycler.addOnScrollListener(mOnScrollListener);
            mHaveListener = true;
        }
        if (!sIsHasMoreEvent) {
            mRecycler.removeOnScrollListener(mOnScrollListener);
            mHaveListener = false;
        }
        mRealmList = list;
        mAdapter.data = list;
        mAdapter.notifyDataSetChanged();
    }
}
