package framgia.vn.framgiacrb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.object.RealmController;
import framgia.vn.framgiacrb.utils.TimeUtils;

public class DetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findView();
    }

    private void findView() {
        String eventId = getIntent().getStringExtra(Constant.ID_KEY);
        Event event = RealmController.with(this).getEventById(eventId);
        if(event != null) {
            TextView description = (TextView) findViewById(R.id.title_description);
            description.setText(
                event.getDescription() == null ? "" : event.getDescription());

            TextView startDate = (TextView) findViewById(R.id.txt_DateStart);
            startDate.setText(TimeUtils.toStringDate(event.getStartTime()));
            TextView startTime = (TextView) findViewById(R.id.txt_timeStart);
            startTime.setText(TimeUtils.toStringTime(event.getStartTime()));

            TextView endDate = (TextView) findViewById(R.id.txt_DateFinish);
            endDate.setText(TimeUtils.toStringDate(event.getFinishTime()));
            TextView endTime = (TextView) findViewById(R.id.txt_TimeFinish);
            endTime.setText(TimeUtils.toStringTime(event.getFinishTime()));

            TextView title = (TextView) findViewById(R.id.textview_event);
            title.setText(event.getTitle() == null ? "" : event.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle(R.string.question);
                builder.setMessage(R.string.delete);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.create().show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
