package framgia.vn.framgiacrb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Attendee;
import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.object.RealmController;
import framgia.vn.framgiacrb.utils.TimeUtils;

public class DetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        findView();
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        mFab = (FloatingActionButton) findViewById(R.id.fab_edit);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                intent.putExtra(Constant.ID_KEY, getIntent().getStringExtra(Constant.ID_KEY));
                startActivity(intent);
            }
        });
        int eventId = getIntent().getIntExtra(Constant.ID_KEY, Constant.INVALID_INDEX);
        Date startTime = (Date) getIntent().getSerializableExtra(Constant.INTENT_START_TIME);
        Date finishTime = (Date) getIntent().getSerializableExtra(Constant.INTENT_FINISH_TIME);
        Event eventParent = RealmController.with(this).getEventById(eventId);
        if (eventParent == null) {
            return;
        }
        Event event = new Event(eventParent);
        event.setStartTime(startTime);
        event.setFinishTime(finishTime);
        if (event.getDescription() != null && !event.getDescription().equals("")) {
            TextView description = (TextView) findViewById(R.id.title_description);
            description.setText(event.getDescription());
        } else {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_description);
            relativeLayout.setVisibility(View.GONE);
        }
        TextView timeText = (TextView) findViewById(R.id.textView_time);
        timeText.setText(TimeUtils.createAmountTime(event.getStartTime(), event.getFinishTime()));
        getSupportActionBar().setTitle(event.getTitle() == null ? "" : event.getTitle());
        TextView calendarTv = (TextView) findViewById(R.id.textView_calendar);
        Calendar calendar = RealmController.with(this)
            .getCalenderByid(event.getCalendarId());
        calendarTv.setText(calendar.getName());
        if (Attendee.getLisAttendee(event.getAttendees()) != "") {
            TextView listAttendee = (TextView) findViewById(R.id.attendee_list);
            listAttendee.setText(Attendee.getLisAttendee(event.getAttendees()));
        } else {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_attendee);
            relativeLayout.setVisibility(View.GONE);
        }
        if (event.getColorId() != null) {
            View color = findViewById(R.id.view_color);
            try {
                color.setBackgroundColor(Color.parseColor(event.getColorId()));
            } catch (IllegalArgumentException e) {
                color.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }
        }
        if (event.getRepeatType() != null) {
            TextView repeatTv = (TextView) findViewById(R.id.textView_repeat);
            repeatTv.setText(event.getRepeatType());
        } else {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_repeat);
            relativeLayout.setVisibility(View.GONE);
        }
        if (event.getPlace() != null) {
            TextView placeTv = (TextView) findViewById(R.id.textView_place);
            placeTv.setText(event.getPlace().getName());
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById
                (R.id.collapsing_toolbar);
            collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, Constant
                .color[event.getPlace().getId() - 1]));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(
                    ContextCompat.getColor(this, Constant.color[event.getPlace().getId() - 1]));
            }
        } else {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_place);
            relativeLayout.setVisibility(View.GONE);
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById
                (R.id.collapsing_toolbar);
            try {
                collapsingToolbarLayout.setBackgroundColor(Color.parseColor(event.getColorId()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.parseColor(event.getColorId()));
                }
            } catch (IllegalArgumentException e) {
            }
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
                        // TODO: 20/09/2016
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
