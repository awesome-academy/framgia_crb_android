package framgia.vn.framgiacrb.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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
import framgia.vn.framgiacrb.data.local.RealmController;
import framgia.vn.framgiacrb.data.model.Attendee;
import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.utils.GoogleCalendarUtil;
import framgia.vn.framgiacrb.utils.TimeUtils;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private boolean mIsGoogleCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        findViewById(R.id.fab_edit).setOnClickListener(this);
        int eventId = getIntent().getIntExtra(Constant.Intent.INTENT_ID_EVENT,
            Constant.Number.INVALID_INDEX);
        Date startTime = (Date) getIntent().getSerializableExtra(Constant.Intent.INTENT_START_TIME);
        Date finishTime =
            (Date) getIntent().getSerializableExtra(Constant.Intent.INTENT_FINISH_TIME);
        Event eventParent;
        mIsGoogleCalendar = getIntent().getBooleanExtra(Constant.Intent.INTENT_IS_GOOGLE_EVENT,
            false);
        if (mIsGoogleCalendar) {
            eventParent = GoogleCalendarUtil.getEventById(this, eventId);
        } else {
            eventParent = RealmController.getInstance().getEventById(eventId);
        }
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
        if (event.isAllDay()) {
            timeText.setText(TimeUtils.getTimeForAllDay(event.getStartTime()));
        } else {
            timeText
                .setText(TimeUtils.createAmountTime(event.getStartTime(), event.getFinishTime()));
        }
        getSupportActionBar().setTitle(event.getTitle() == null ? "" : event.getTitle());
        TextView calendarTv = (TextView) findViewById(R.id.textView_calendar);
        if (mIsGoogleCalendar) {
            calendarTv.setText(event.getGoogleCalendarName());
        } else {
            Calendar calendar = RealmController.getInstance()
                .getCalenderByid(event.getCalendarId());
            calendarTv.setText(calendar.getName());
        }
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_edit) {
            Intent intent = new Intent(DetailActivity.this, EditActivity.class);
            intent.putExtra(Constant.Intent.INTENT_ID_EVENT,
                getIntent().getStringExtra(Constant.Intent.INTENT_ID_EVENT));
            startActivity(intent);
        }
    }
}