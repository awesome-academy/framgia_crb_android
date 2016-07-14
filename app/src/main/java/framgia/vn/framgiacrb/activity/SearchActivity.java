package framgia.vn.framgiacrb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.adapter.CustomRecyclerAdapter;
import framgia.vn.framgiacrb.data.Data;

/**
 * Created by lethuy on 04/07/2016.
 */
public class SearchActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecycler;
    private SearchView mSearchView;
    private SearchView.OnQueryTextListener mSearchViewListener;
    private List<Data> listData = new ArrayList<>();
    private CustomRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        adapter = new CustomRecyclerAdapter(listData);
        mRecycler.setAdapter(adapter);
    }

    public void addItem(String textSearch) {
        // get data.
        Data dataToAdd = new Data(textSearch);
        listData.add(dataToAdd);
        // Update adapter.
        adapter.notifyDataSetChanged();
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
                addItem(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                addItem(newText);
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
