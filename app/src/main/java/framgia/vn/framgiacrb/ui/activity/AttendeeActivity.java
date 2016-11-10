package framgia.vn.framgiacrb.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.data.local.RealmController;
import io.realm.Realm;

/**
 * Created by lethuy on 19/07/2016.
 */
public class AttendeeActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private EditText mEditAttendee;
    private Toolbar mToolbar;
    private final String MESSAGE = "MESSAGE";
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee);
        initViews();
    }

    private void initViews() {
        mRealm = RealmController.getInstance().getRealm();
        mToolbar = (Toolbar) findViewById(R.id.toolbar_repeat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_attendee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                setMessageResult(true);
                finish();
                break;
            case android.R.id.home:
                setMessageResult(false);
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMessageResult(boolean isSave) {
        if (!isSave) {
            setResult(Activity.RESULT_CANCELED);
            return;
        }
        String message = mEditAttendee.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(MESSAGE, message);
        setResult(Activity.RESULT_OK, intent);
    }
}
