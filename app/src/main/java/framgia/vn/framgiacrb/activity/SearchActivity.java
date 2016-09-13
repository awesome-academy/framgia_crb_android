package framgia.vn.framgiacrb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.adapter.SearchEventAdapter;
import framgia.vn.framgiacrb.object.RealmController;
import io.realm.Realm;

/**
 * Created by lethuy on 04/07/2016.
 */
public class SearchActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecycler;
    private SearchView mSearchView;
    private SearchView.OnQueryTextListener mSearchViewListener;
    private RecyclerView.LayoutManager layoutManager;
    private Realm mRealm;
    private SearchEventAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {
        mRealm = RealmController.with(this).getRealm();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
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
                if(!query.equals("")) {
                    mAdapter = new SearchEventAdapter(SearchActivity.this, RealmController.with
                        (SearchActivity.this).searchEvent(query));
                    mRecycler.setAdapter(mAdapter);
                } else {
                    mRecycler.setAdapter(null);
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
}
