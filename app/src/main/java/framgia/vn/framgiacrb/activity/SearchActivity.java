package framgia.vn.framgiacrb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.adapter.CustomRecyclerAdapter;
import framgia.vn.framgiacrb.data.Data;

/**
 * Created by lethuy on 04/07/2016.
 */
public class SearchActivity extends Activity implements OnClickListener{
    private EditText mEditSearch;
    private ImageButton mImageButtonBack, mImageButtonSearch;
    private RecyclerView mRecycler;

    private List<Data> listData = new ArrayList<>();
    private CustomRecyclerAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditSearch = (EditText) findViewById(R.id.edit_Search);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);

        mRecycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);

        adapter = new CustomRecyclerAdapter(listData);
        mRecycler.setAdapter(adapter);

        mImageButtonSearch = (ImageButton) findViewById(R.id.btn_search);
        mImageButtonSearch.setOnClickListener(this);

        mImageButtonBack = (ImageButton) findViewById(R.id.btn_back);
        mImageButtonBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void addItem() {

        // get data.
        Data dataToAdd = new Data(mEditSearch.getText().toString());

        listData.add(dataToAdd);

        // Update adapter.
        adapter = new CustomRecyclerAdapter(listData);
        mRecycler.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        addItem();
    }
}
