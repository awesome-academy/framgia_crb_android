package framgia.vn.framgiacrb.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.object.RealmController;

/**
 * Created by lethuy on 06/07/2016.
 */
public class EditActivity extends AppCompatActivity implements View.OnTouchListener {
    private ImageButton mImageButtonBack, mImageButtonAlarm, mImageButtonRepeat, mIamgeButtonSave;
    private EditText mEdtEvent, mEdtDesciption;
    private Switch mSwitchAlarm;
    private TextView mTxtDateStart, mTxtTimeStart, mTxtDateFinish, mTxtTimeFinish, mTxtRepeat, mTxtEditEvent;
    private Spinner mSpinerCalendar;

    ArrayList<String> lstData;

    private Toolbar mToolbar;

    private Calendar mCal;
    private Date mDateFinish, mHourFinish;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_event);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEdtEvent = (EditText) findViewById(R.id.edit_Title_New_Event);
        mTxtDateStart = (TextView) findViewById(R.id.txt_DateStart);
        mTxtDateFinish = (TextView) findViewById(R.id.txt_DateFinish);
        mTxtTimeStart = (TextView) findViewById(R.id.txt_timeStart);
        mTxtTimeFinish = (TextView) findViewById(R.id.txt_TimeFinish);
        mTxtRepeat = (TextView) findViewById(R.id.txt_Repeat);
        CardView cardView = (CardView) findViewById(R.id.card_view2);
        cardView.setOnTouchListener(this);

        mTxtEditEvent = (TextView) findViewById(R.id.txt_NewEvent);

        mTxtEditEvent.setText(R.string.edit_event);

        findView();
        getDefaultInfor();
        addEventFormWidgets();
    }

    private void findView() {
        String eventId = getIntent().getStringExtra(Constant.ID_KEY);
        Event event = RealmController.with(this).getEventById(eventId);
        mEdtEvent.setText(event.getTitle() == null ? "" : event.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_Save:
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

    public void getDefaultInfor() {
        mCal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = dft.format(mCal.getTime());
        mTxtDateStart.setText(strDate);
        mTxtDateFinish.setText(strDate);
        dft = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String strTime = dft.format(mCal.getTime());
        mTxtTimeStart.setText(strTime);
        mTxtTimeFinish.setText(strTime);
        dft = new SimpleDateFormat("HH:mm", Locale.getDefault());
        mTxtTimeStart.setTag(dft.format(mCal.getTime()));
        mTxtTimeFinish.setTag(dft.format(mCal.getTime()));
    }

    public void addEventFormWidgets() {
        mTxtDateStart.setOnClickListener(new MyButtonEvent());
        mTxtTimeStart.setOnClickListener(new MyButtonEvent());
        mTxtDateFinish.setOnClickListener(new MyButtonEvent());
        mTxtTimeFinish.setOnClickListener(new MyButtonEvent());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!(v instanceof EditText)) {
            hideSoftKeyboard(EditActivity.this);
        }
        return false;
    }

    private class MyButtonEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txt_DateStart:
                    showDateStartPickerDialog();
                    break;
                case R.id.txt_timeStart:
                    showTimeStartPickerDialog();
                    break;
                case R.id.txt_DateFinish:
                    showDateFinishPickerDialog();
                    break;
                case R.id.txt_TimeFinish:
                    showTimeFinishPickerDialog();
                    break;
            }
        }

    }

    public void showDateStartPickerDialog() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                mTxtDateStart.setText(
                        (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);
                mCal.set(year, monthOfYear, dayOfMonth);
                mDateFinish = mCal.getTime();
            }
        };

        String s = mTxtDateStart.getText() + "";
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                EditActivity.this,
                callback, nam, thang, ngay);
        pic.setTitle(R.string.select_date_start);
        pic.show();
    }

    public void showDateFinishPickerDialog() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                mTxtDateFinish.setText(
                        (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);
                mCal.set(year, monthOfYear, dayOfMonth);
                mDateFinish = mCal.getTime();
            }
        };
        String s = mTxtDateFinish.getText() + "";
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                EditActivity.this,
                callback, nam, thang, ngay);
        pic.setTitle(R.string.select_date_finish);
        pic.show();
    }


    public void showTimeStartPickerDialog() {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                String s = hourOfDay + ":" + minute;
                int hourTam = hourOfDay;
                if (hourTam > 12)
                    hourTam = hourTam - 12;
                mTxtTimeStart.setText
                        (hourTam + ":" + minute + (hourOfDay > 12 ? " PM" : " AM"));
                mTxtTimeStart.setTag(s);
                mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCal.set(Calendar.MINUTE, minute);
                mHourFinish = mCal.getTime();
            }
        };
        String s = mTxtTimeStart.getTag() + "";
        String strArr[] = s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(
                EditActivity.this,
                callback, gio, phut, true);
        time.setTitle(R.string.select_time_start);
        time.show();
    }

    public void showTimeFinishPickerDialog() {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                String s = hourOfDay + ":" + minute;
                int hourTam = hourOfDay;
                if (hourTam > 12)
                    hourTam = hourTam - 12;
                mTxtTimeFinish.setText
                        (hourTam + ":" + minute + (hourOfDay > 12 ? " PM" : " AM"));
                mTxtTimeFinish.setTag(s);
                mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCal.set(Calendar.MINUTE, minute);
                mHourFinish = mCal.getTime();
            }
        };
        String s = mTxtTimeFinish.getTag() + "";
        String strArr[] = s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(
                EditActivity.this,
                callback, gio, phut, true);
        time.setTitle(R.string.select_time_finish);
        time.show();
    }

}
