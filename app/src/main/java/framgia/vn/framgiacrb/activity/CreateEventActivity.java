package framgia.vn.framgiacrb.activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.CreateEventResponse;
import framgia.vn.framgiacrb.data.model.DayOfWeekId;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.NewEvent;
import framgia.vn.framgiacrb.data.model.Place;
import framgia.vn.framgiacrb.data.model.RepeatOnAttribute;
import framgia.vn.framgiacrb.data.model.Session;
import framgia.vn.framgiacrb.data.model.User;
import framgia.vn.framgiacrb.network.ServiceBuilder;
import framgia.vn.framgiacrb.object.EventInWeek;
import framgia.vn.framgiacrb.utils.DialogUtils;
import framgia.vn.framgiacrb.utils.TimeUtils;
import framgia.vn.framgiacrb.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lethuy on 05/07/2016.
 */
public class CreateEventActivity extends AppCompatActivity implements View.OnTouchListener {
    private EditText mEdtOption, mEdtTitle, mEdtDesciption, mStartEditText, mEndEditText;
    private ImageButton mButtonSave;
    private Switch mSwitchAlarm;
    private Spinner mRepeatSpinner, mRepeatEverySpinner;
    private DatePickerDialog mDatePickerDialog, mCreateDatePickerDialog;
    private LinearLayout mDayOfWeekLinearLayout;
    private TextView mTxtDateStart, mTxtTimeStart, mTxtDateFinish, mTxtTimeFinish, mTxtRepeat,
        mTxtNewEvent;
    private TextView mTxtAttendee, mTxtPlace;
    private Spinner mSpinerCalendar;
    private EditText mStartRepeat, mEndRepeat;
    private Calendar mCal, mCalendarStart, mCalendarFinish, mCalendarEnd;
    private Date mDateStart, mDateFinish, mHourFinish, mDateEventStartRepeat,
        mDateEventFinishRepeat;
    private Date mTimeEventStart, mTimeEventFinish;
    private ArrayList<String> mListData;
    private Toolbar mToolbar;
    private ArrayList<String> mListPlace;
    private ArrayList<String> mUserAttendee;
    private ArrayList<String> mListUser;
    private boolean[] mSaveUserAttendee;
    ArrayList<EventInWeek> arrJob = new ArrayList<EventInWeek>();
    ArrayAdapter<EventInWeek> adapter = null;
    private int mCurrentIndexOfSpinnerChoice = 0;
    private int mRepeatEvery = 1;
    private String mRepeatType = "";
    private String mPlace = "Manila";
    private String mUser;
    private String mPlaceId = "1";
    private String mUserId = "1";
    private RealmResults<Place> mPlaces;
    private RealmResults<User> mAttendees;
    private ArrayList<String> mListDayOfWeekRepeat = new ArrayList<>();
    boolean isRepeat;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
            (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
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
        mCalendarStart = Calendar.getInstance();
        mCalendarFinish = Calendar.getInstance();
        mCalendarEnd = Calendar.getInstance();
        mTxtDateStart = (TextView) findViewById(R.id.txt_DateStart);
        mTxtDateFinish = (TextView) findViewById(R.id.txt_DateFinish);
        mTxtTimeStart = (TextView) findViewById(R.id.txt_timeStart);
        mTxtTimeFinish = (TextView) findViewById(R.id.txt_TimeFinish);
        mTxtNewEvent = (TextView) findViewById(R.id.txt_NewEvent);
        mSwitchAlarm = (Switch) findViewById(R.id.sw_SwitchAlarm);
        mTxtRepeat = (TextView) findViewById(R.id.txt_Repeat);
        mTxtAttendee = (TextView) findViewById(R.id.txt_Attendee);
        mTxtAttendee.setMovementMethod(new ScrollingMovementMethod());
        mTxtPlace = (TextView) findViewById(R.id.txt_Place);
        mEdtTitle = (EditText) findViewById(R.id.edit_Title_New_Event);
        mEdtDesciption = (EditText) findViewById(R.id.edt_Detail);
        mUserAttendee = new ArrayList<>();
        CardView cardView = (CardView) findViewById(R.id.card_view2);
        cardView.setOnTouchListener(this);
        mTxtNewEvent.setText(R.string.new_event);
        mSwitchAlarm.setChecked(false);
        mSwitchAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnable = !mSwitchAlarm.isChecked();
                mTxtDateStart.setEnabled(isEnable);
                mTxtDateFinish.setEnabled(isEnable);
                mTxtTimeStart.setEnabled(isEnable);
                mTxtTimeFinish.setEnabled(isEnable);
            }
        });
        mTxtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlaceFromServerIfNotExist();
                listPlace();
            }
        });
        mTxtAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserFromServerIfNotExist();
                listAttendee();
            }
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrJob);
        mListData = new ArrayList<>();
        RealmResults<framgia.vn.framgiacrb.data.model.Calendar> result =
            new EventRepositoriesLocal(Realm.getDefaultInstance()).getAllCalendars();
        for (int i = 0; i < result.size(); i++) {
            mListData.add(result.get(i).getName());
        }
        mSpinerCalendar = (Spinner) findViewById(R.id.spin_Calendar);
        mSpinerCalendar.getBackground()
            .setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
            mListData);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinerCalendar.setAdapter(adapter);
        getDefaultInForm();
        addEventFormWidgets();
        selectRepeat();
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
                String title = mEdtTitle.getText().toString();
                if (!title.equals("")) {
                    getDataFromService(createEvent());
                } else {
                    Toast.makeText(CreateEventActivity.this, R.string.not_be_empty,
                        Toast.LENGTH_LONG).show();
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                String message = data.getStringExtra(Constant.MESSAGE);
                mTxtAttendee.setText(message);
            } else if (requestCode == 3) {
                String message = data.getStringExtra(Constant.MESSAGE);
                mTxtPlace.setText(message);
            }
        }
    }

    public void getDefaultInForm() {
        mCal = Calendar.getInstance();
        SimpleDateFormat dateFormat =
            new SimpleDateFormat(Constant.FORMAT_DATE, Locale.getDefault());
        String strDate = dateFormat.format(mCal.getTime());
        mTxtDateStart.setText(strDate);
        mTxtDateFinish.setText(strDate);
        dateFormat = new SimpleDateFormat(Constant.FORMAT_TIME, Locale.getDefault());
        String strTime = dateFormat.format(mCal.getTime());
        mTxtTimeStart.setText(strTime);
        mCal.add(Calendar.HOUR_OF_DAY, 1);
        strTime = dateFormat.format(mCal.getTime());
        mTxtTimeFinish.setText(strTime);
        dateFormat = new SimpleDateFormat(Constant.FORMAT_TIME, Locale.getDefault());
        mTxtTimeStart.setTag(dateFormat.format(mCal.getTime()));
        mCal.add(Calendar.HOUR_OF_DAY, 1);
        mTxtTimeFinish.setTag(dateFormat.format(mCal.getTime()));
        mTxtPlace.setText(mPlace);
    }

    public void addEventFormWidgets() {
        mTxtDateStart.setOnClickListener(new MyButtonEvent());
        mTxtTimeStart.setOnClickListener(new MyButtonEvent());
        mTxtDateFinish.setOnClickListener(new MyButtonEvent());
        mTxtTimeFinish.setOnClickListener(new MyButtonEvent());
    }

    public void selectRepeat() {
        mTxtRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogRepeat md = new MyDialogRepeat(mCurrentIndexOfSpinnerChoice);
                md.show(CreateEventActivity.this.getFragmentManager(), Constant.MESSAGE);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!(v instanceof EditText)) {
            hideSoftKeyboard(CreateEventActivity.this);
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

    private class MyDialogRepeat extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
        private int mCurrentItemSelectedOnSpinnerChoice;
        private AlertDialog mDialog;

        public MyDialogRepeat(int currentIndex) {
            this.mCurrentItemSelectedOnSpinnerChoice = currentIndex;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.activity_repeat, null, false);
            mRepeatSpinner = (Spinner) v.findViewById(R.id.repeat_spinner);
            mRepeatSpinner.setSelection(0);
            mRepeatEverySpinner = (Spinner) v.findViewById(R.id.repeat_every_spinner);
            mRepeatSpinner.setSelection(this.mCurrentItemSelectedOnSpinnerChoice);
            mStartRepeat = (EditText) v.findViewById(R.id.start_edit_text);
            mEndEditText = (EditText) v.findViewById(R.id.end_edit_text);
            // Setup listener
            mRepeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                                           long id) {
                    mCurrentItemSelectedOnSpinnerChoice = position;
                    if (parent.getSelectedItem().toString().equalsIgnoreCase(Constant.NO_REPEAT) ||
                        (!parent.getSelectedItem().toString()
                            .equalsIgnoreCase(Constant.NO_REPEAT) &&
                            !mEndEditText.getText().toString().isEmpty())) {
                        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                    if (parent.getSelectedItem().toString().equalsIgnoreCase(Constant.NO_REPEAT)) {
                        mRepeatEverySpinner.setEnabled(false);
                        mEndEditText.setEnabled(false);
                    } else {
                        mRepeatEverySpinner.setEnabled(true);
                        mEndEditText.setEnabled(true);
                    }
                    boolean isShowDayOfWeek =
                        mRepeatSpinner.getSelectedItem().equals(Constant.WEEKLY);
                    showDayOfWeek(isShowDayOfWeek);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            Calendar now = Calendar.getInstance();
            if (mDateStart != null) now.setTime(mDateStart);
            mStartEditText = (EditText) v.findViewById(R.id.start_edit_text);
            mStartEditText.setText(Utils.formatDate(CreateEventActivity.this,
                now.get(Calendar.DAY_OF_MONTH),
                now.get(Calendar.MONTH) + 1,
                now.get(Calendar.YEAR)
            ));
            mEndEditText = (EditText) v.findViewById(R.id.end_edit_text);
            mEndEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar now = Calendar.getInstance();
                    mDatePickerDialog = DatePickerDialog.newInstance(
                        MyDialogRepeat.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                    );
                    mDatePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
                }
            });
            mDayOfWeekLinearLayout = (LinearLayout) v.findViewById(R.id.day_of_week_container);
            if (!mRepeatSpinner.getSelectedItem().equals(Constant.WEEKLY))
                mDayOfWeekLinearLayout.setVisibility(View.INVISIBLE);
            builder.setView(v)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mCurrentIndexOfSpinnerChoice = mCurrentItemSelectedOnSpinnerChoice;
                        mTxtRepeat
                            .setText(mRepeatSpinner.getSelectedItem().toString().toLowerCase());
                        mRepeatEvery =
                            Integer.parseInt(mRepeatEverySpinner.getSelectedItem().toString());
                        mRepeatType = mTxtRepeat.getText().toString();
                        if (!mRepeatSpinner.getSelectedItem().toString()
                            .equalsIgnoreCase(Constant.NO_REPEAT)) {
                            isRepeat = true;
                        } else {
                            isRepeat = false;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
            mDialog = builder.create();
            return mDialog;
        }

        @Override
        public void onStart() {
            super.onStart();
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        private void showDayOfWeek(boolean isShow) {
            if (isShow) {
                mDayOfWeekLinearLayout.animate().alpha(1.0f).setDuration(Constant.SHOW_DURATION)
                    .setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                mDayOfWeekLinearLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }
                    );
            } else {
                mDayOfWeekLinearLayout.animate().alpha(0.0f).setDuration(Constant.HIDE_DURATION)
                    .setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mDayOfWeekLinearLayout.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }
                    );
            }
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            mCalendarEnd.set(Calendar.YEAR, year);
            mCalendarEnd.set(Calendar.MONTH, monthOfYear);
            mCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (mCalendarEnd.before(Calendar.getInstance())) {
                Toast.makeText(CreateEventActivity.this, R.string.unable, Toast.LENGTH_SHORT)
                    .show();
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                mEndEditText.setText("");
            } else {
                mEndEditText.setText(Utils.formatDate(CreateEventActivity.this,
                    dayOfMonth, monthOfYear + 1, year
                ));
                mDateEventFinishRepeat = mCalendarEnd.getTime();
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }
    }

    public void clickCheckBox(View v) {
        Toast
            .makeText(CreateEventActivity.this, "Clicked on checkbox on Dialog", Toast.LENGTH_SHORT)
            .show();
        switch (v.getId()) {
            case R.id.sun_checkbox:
                mListDayOfWeekRepeat.add(Constant.SUNDAY);
                break;
            case R.id.mon_checkbox:
                mListDayOfWeekRepeat.add(Constant.MONDAY);
                break;
            case R.id.tue_checkbox:
                mListDayOfWeekRepeat.add(Constant.TUESDAY);
                break;
            case R.id.wed_checkbox:
                mListDayOfWeekRepeat.add(Constant.WEDNESDAY);
                break;
            case R.id.thu_checkbox:
                mListDayOfWeekRepeat.add(Constant.THURSDAY);
                break;
            case R.id.fri_checkbox:
                mListDayOfWeekRepeat.add(Constant.FRIDAY);
                break;
            case R.id.sat_checkbox:
                mListDayOfWeekRepeat.add(Constant.SATURDAY);
                break;
        }
    }

    public void showDateStartPickerDialog() {
        String s = mTxtDateStart.getText() + "";
        String strArrtmp[] = s.split("-");
        int thang = Integer.parseInt(strArrtmp[0]) - 1;
        int ngay = Integer.parseInt(strArrtmp[1]);
        int nam = Integer.parseInt(strArrtmp[2]);
        mCreateDatePickerDialog = DatePickerDialog.newInstance(
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mTxtDateStart.setText(
                        Utils.formatDate(CreateEventActivity.this, (monthOfYear + 1), dayOfMonth,
                            year));
                    mCal.set(year, monthOfYear, dayOfMonth);
                    mDateStart = mCal.getTime();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mCalendarStart = (Calendar) calendar.clone();
                    mDateFinish = (Date) mDateStart.clone();
                    mTxtDateFinish.setText(mTxtDateStart.getText().toString());
                }
            },
            nam,
            thang,
            ngay
        );
        mCreateDatePickerDialog
            .show(CreateEventActivity.this.getFragmentManager(), "CreateEventStartDate");
    }

    public void showDateFinishPickerDialog() {
        String s = mTxtDateFinish.getText() + "";
        String strArrtmp[] = s.split("-");
        int thang = Integer.parseInt(strArrtmp[0]) - 1;
        int ngay = Integer.parseInt(strArrtmp[1]);
        int nam = Integer.parseInt(strArrtmp[2]);
        mCreateDatePickerDialog = DatePickerDialog.newInstance(
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mTxtDateFinish.setText(
                        (Utils.formatDate(CreateEventActivity.this, (monthOfYear + 1), dayOfMonth,
                            year)));
                    mCal.set(year, monthOfYear, dayOfMonth);
                    mDateFinish = mCal.getTime();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mCalendarFinish = (Calendar) calendar.clone();
                    mDateStart = (Date) mDateFinish.clone();
                    mTxtDateStart.setText(mTxtDateFinish.getText().toString());
                }
            },
            nam,
            thang,
            ngay
        );
        mCreateDatePickerDialog
            .show(CreateEventActivity.this.getFragmentManager(), "CreateEventFinishDate");
    }

    public void showTimeStartPickerDialog() {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                mTxtTimeStart.setText
                    (Utils.formatTime(CreateEventActivity.this, hourOfDay, minute));
                mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCal.set(Calendar.MINUTE, minute);
                mHourFinish = mCal.getTime();
                mTimeEventStart = mCal.getTime();
                if (mTimeEventFinish == null) mTimeEventFinish = new Date();
                if (mTimeEventStart == null) mTimeEventStart = new Date();
                boolean result = Utils.isBeforeHourInDate(mTimeEventFinish, mTimeEventStart);
                if (result) {
                    mTxtTimeFinish
                        .setText(Utils.formatTime(CreateEventActivity.this, hourOfDay + 1, minute));
                }
            }
        };
        String s = mTxtTimeStart.getText().toString().trim();
        String strArr[] = s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(
            CreateEventActivity.this,
            callback, gio, phut, true);
        time.setTitle(R.string.select_time_start);
        time.show();
    }

    public void showTimeFinishPickerDialog() {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                mTxtTimeFinish.setText
                    (Utils.formatTime(CreateEventActivity.this, hourOfDay, minute));
                mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCal.set(Calendar.MINUTE, minute);
                mHourFinish = mCal.getTime();
                mTimeEventFinish = mCal.getTime();
                if (mTimeEventFinish == null) mTimeEventFinish = new Date();
                if (mTimeEventStart == null) mTimeEventStart = new Date();
                boolean result = Utils.isBeforeHourInDate(mTimeEventFinish, mTimeEventStart);
                if (result) {
                    mTxtTimeStart.setText
                        (Utils.formatTime(CreateEventActivity.this, hourOfDay - 1, minute));
                }
            }
        };
        String s = mTxtTimeFinish.getText().toString().trim();
        String strArr[] = s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(
            CreateEventActivity.this,
            callback, gio, phut, true);
        time.setTitle(R.string.select_time_finish);
        time.show();
    }

    public void getDataFromService(final NewEvent newEvent) {
        DialogUtils.showProgressDialog(this);
        ServiceBuilder.getService().createEvent(newEvent)
            .enqueue(new Callback<CreateEventResponse>() {
                @Override
                public void onResponse(Call<CreateEventResponse> call,
                                       Response<CreateEventResponse> response) {
                    if (response.body() == null) {
                        String error = null;
                        try {
                            error = Utils.getStringFromJson(response.errorBody().string());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (error.equals(Constant.NOT_AUTHENTICATION)) {
                            logout();
                            CreateEventActivity.this.finish();
                        } else {
                            Toast.makeText(CreateEventActivity.this, R.string.create_error,
                                Toast.LENGTH_SHORT).show();
                        }
                        DialogUtils.dismissProgressDialog();
                    } else if (response.body().getMessage().equals(Constant.SUCCESS)) {
                        Toast.makeText(CreateEventActivity.this, R.string.success,
                            Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK, new Intent());
                        CreateEventActivity.this.finish();
                    }
                }

                @Override
                public void onFailure(Call<CreateEventResponse> call, Throwable t) {
                    DialogUtils.dismissProgressDialog();
                    DialogUtils.showErrorAlert(CreateEventActivity.this,
                        getString(R.string.error_create_event));
                }
            });
    }

    private void logout() {
        Toast.makeText(CreateEventActivity.this, Constant.MESSAGE_NOT_AUTHENTICATION,
            Toast.LENGTH_SHORT).show();
        new EventRepositoriesLocal(Realm.getDefaultInstance())
            .clearDatabase(new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(CreateEventActivity.this, "Logout Success!", Toast.LENGTH_SHORT)
                        .show();
                }
            });
        SharedPreferences sharedPreferences =
            getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(CreateEventActivity.this, LoginActivity.class));
        finish();
    }

    public NewEvent createEvent() {
        Event event = new Event();
        event.setTitle(mEdtTitle.getText().toString());
        Calendar calendarStartFinishTime = Calendar.getInstance();
        String[] tmp = mTxtTimeStart.getText().toString().trim().split(":");
        calendarStartFinishTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmp[0]));
        calendarStartFinishTime.set(Calendar.MINUTE, Integer.parseInt(tmp[1]));
        mTimeEventStart = calendarStartFinishTime.getTime();
        tmp = mTxtTimeFinish.getText().toString().trim().split(":");
        calendarStartFinishTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmp[0]));
        calendarStartFinishTime.set(Calendar.MINUTE, Integer.parseInt(tmp[1]));
        mTimeEventFinish = calendarStartFinishTime.getTime();
        if (null == mDateStart) mDateStart = new Date();
        if (null == mDateFinish) mDateFinish = new Date();
        if (isRepeat) {
            if (mTxtRepeat.getText().toString().equalsIgnoreCase(Constant.WEEKLY)) {
                RepeatOnAttribute repeatOnAttribute = new RepeatOnAttribute();
                if (mListDayOfWeekRepeat.size() != 0) {
                    for (int i = 0; i < mListDayOfWeekRepeat.size(); i++) {
                        DayOfWeekId dayOfWeekId = new DayOfWeekId();
                        dayOfWeekId.setDayOfWeekId(mListDayOfWeekRepeat.get(i));
                        switch (i) {
                            case 0:
                                repeatOnAttribute.setRepeatOnAttribute1(dayOfWeekId);
                                break;
                            case 1:
                                repeatOnAttribute.setRepeatOnAttribute2(dayOfWeekId);
                                break;
                            case 2:
                                repeatOnAttribute.setRepeatOnAttribute3(dayOfWeekId);
                                break;
                            case 3:
                                repeatOnAttribute.setRepeatOnAttribute4(dayOfWeekId);
                                break;
                            case 4:
                                repeatOnAttribute.setRepeatOnAttribute5(dayOfWeekId);
                                break;
                            case 5:
                                repeatOnAttribute.setRepeatOnAttribute6(dayOfWeekId);
                                break;
                            case 6:
                                repeatOnAttribute.setRepeatOnAttribute7(dayOfWeekId);
                                break;
                        }
                    }
                } else {
                    DayOfWeekId dayOfWeekId = new DayOfWeekId();
                    Calendar calendar = Calendar.getInstance();
                    dayOfWeekId
                        .setDayOfWeekId(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
                    repeatOnAttribute.setRepeatOnAttribute1(dayOfWeekId);
                }
                event.setRepeatOnAttribute(repeatOnAttribute);
            }
            event.setStartRepeat(mDateStart);
            event.setEndRepeat(mDateEventFinishRepeat);
            event.setRepeatEvery(mRepeatEvery);
            event.setRepeatType(mRepeatType);
        }
        event.setStartTime(TimeUtils.formatDateTime(mDateStart, mTimeEventStart));
        event.setFinishTime(TimeUtils.formatDateTime(mDateFinish, mTimeEventFinish));
        event.setCalendarId(Session.sCalendarId);
        return new NewEvent(Session.sAuthToken, event);
    }

    public void listPlace() {
        new AlertDialog.Builder(CreateEventActivity.this)
            .setTitle("Place")
            .setItems(mListPlace.toArray(new String[mListPlace.size()]),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlace = mListPlace.get(which);
                        mTxtPlace.setText(mPlace);
                        mPlaceId = String.valueOf(mPlaces.get(which).getId());
                    }
                })
            .show();
    }

    public void listAttendee() {
        if (mSaveUserAttendee == null) mSaveUserAttendee = new boolean[mListUser.size()];
        mTxtAttendee.setText("");
        mUserAttendee.clear();
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
        builder.setTitle(R.string.select_attendee);
        builder
            .setMultiChoiceItems(mListUser.toArray(new String[mListUser.size()]), mSaveUserAttendee,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        mSaveUserAttendee[indexSelected] = isChecked;
                        if (isChecked) {
                            mUserAttendee.add(mListUser.get(indexSelected));
                        } else {
                            int index = getIndexOfUserInList(mListUser.get(indexSelected));
                            if (index != -1)
                                mUserAttendee.remove(index);
                        }
                    }
                })
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    if (mTxtAttendee.getText().toString().isEmpty())
                        for (String s : mUserAttendee) {
                            mTxtAttendee.setText(
                                mTxtAttendee.getText().toString() +
                                    (mTxtAttendee.getText().toString().isEmpty() ? "" : ", ")
                                    + s);
                        }
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    mTxtAttendee.setText(R.string.no_attendee);
                }
            }).show();
    }

    private int getIndexOfUserInList(String name) {
        for (int i = 0; i < mUserAttendee.size(); i++) {
            if (name.equalsIgnoreCase(mUserAttendee.get(i))) return i;
        }
        return -1;
    }

    private void getPlaceFromServerIfNotExist() {
        mPlaces = new EventRepositoriesLocal(Realm.getDefaultInstance()).getAllPlaces();
        addPlaceData(mPlaces);
    }

    private void getUserFromServerIfNotExist() {
        mAttendees = new EventRepositoriesLocal(Realm.getDefaultInstance()).getAllUsers();
        addUserData(mAttendees);
    }

    private void addPlaceData(RealmResults<Place> places) {
        mListPlace = new ArrayList<>();
        for (Place place : places) {
            mListPlace.add(place.getName());
        }
    }

    private void addUserData(RealmResults<User> users) {
        mListUser = new ArrayList<>();
        for (User user : users) {
            mListUser.add(user.getName());
        }
    }
}
