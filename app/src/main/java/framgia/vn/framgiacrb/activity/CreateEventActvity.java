package framgia.vn.framgiacrb.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.CreateEventResponse;
import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.NewEvent;
import framgia.vn.framgiacrb.data.model.Session;
import framgia.vn.framgiacrb.network.ServiceBuilder;
import framgia.vn.framgiacrb.object.EventInWeek;
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
public class CreateEventActvity extends AppCompatActivity implements View.OnTouchListener {
    private final String MESSAGE = "MESSAGE";
    private final String FORMAT_DATE = "dd-MM-yyyy";
    private final String FORMAT_TIME = "HH:mm";
    private final String FORMAT_TIME_A = "hh:mm a";
    private final String TIME_AM = "AM";
    private final String TIME_PM = "PM";
    private final String SUCCESS = "Create Event Success!";
    private final String NOT_AUTHENTICATION = "Not authenticated";
    private final String MESSAGE_NOT_AUTHENTICATION = "This account has been login in other device";
    private ImageButton mImageButtonBack;
    private EditText mEdtOption, mEdtTitle, mEdtDesciption;
    private ImageButton mButtonSave;
    private RadioButton mRadioPhut, mRadioGio, mRadioNgay, mRadioTuan, mRadioTB, mRadioEmail;
    private Switch mSwitchAlarm;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton1, mRadioButton2, mRadioButton3, mRadioButton4;
    private TextView mTxtDateStart, mTxtTimeStart, mTxtDateFinish, mTxtTimeFinish, mTxtRepeat, mTxtNewEvent;
    private TextView mTxtOption, mTxtAttendee, mTxtPlace;
    private Spinner mSpinerCalendar;

    private Calendar mCal, mCalendarStart, mCalendarFinish;
    private Date mDateFinish, mHourFinish;

    private ArrayList<String> mListData;

    private Toolbar mToolbar;

    ArrayList<EventInWeek> arrJob = new ArrayList<EventInWeek>();
    ArrayAdapter<EventInWeek> adapter = null;

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

        mCalendarStart = Calendar.getInstance();
        mCalendarFinish = Calendar.getInstance();

        mTxtDateStart = (TextView) findViewById(R.id.txt_DateStart);
        mTxtDateFinish = (TextView) findViewById(R.id.txt_DateFinish);
        mTxtTimeStart = (TextView) findViewById(R.id.txt_timeStart);
        mTxtTimeFinish = (TextView) findViewById(R.id.txt_TimeFinish);
        mTxtNewEvent = (TextView) findViewById(R.id.txt_NewEvent);

        mSwitchAlarm = (Switch) findViewById(R.id.sw_SwitchAlarm);

        mTxtRepeat = (TextView) findViewById(R.id.txt_Repeat);
        mTxtAttendee = (TextView) findViewById(R.id.txt_Attendee);
        mTxtPlace = (TextView) findViewById(R.id.txt_Place);

        mEdtTitle = (EditText) findViewById(R.id.edit_Title_New_Event);
        mEdtDesciption = (EditText) findViewById(R.id.edt_Detail);

        CardView cardView = (CardView) findViewById(R.id.card_view2);
        cardView.setOnTouchListener(this);

        mTxtNewEvent.setText(R.string.new_event);

        mSwitchAlarm.setChecked(false);
        mSwitchAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAnable = !mSwitchAlarm.isChecked();
                mTxtDateStart.setEnabled(isAnable);
                mTxtDateFinish.setEnabled(isAnable);
                mTxtTimeStart.setEnabled(isAnable);
                mTxtTimeFinish.setEnabled(isAnable);
            }
        });

        mTxtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventActvity.this, PlaceActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        mTxtAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventActvity.this, AttendeeActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        adapter = new ArrayAdapter<EventInWeek>
                (this, android.R.layout.simple_list_item_1, arrJob);

        mListData = new ArrayList<>();
        RealmResults<framgia.vn.framgiacrb.data.model.Calendar> result =
                new EventRepositoriesLocal(Realm.getDefaultInstance()).getAllCalendars();

        for (int i = 0; i < result.size(); i++) {
            mListData.add(result.get(i).getName());
        }

        mSpinerCalendar = (Spinner) findViewById(R.id.spin_Calendar);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                mListData);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinerCalendar.setAdapter(adapter);

        getDefaultInfor();
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
                    finish();
                } else {
                    Toast.makeText(CreateEventActvity.this, R.string.not_be_empty, Toast.LENGTH_LONG).show();
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
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                String message = data.getStringExtra(MESSAGE);
                mTxtAttendee.setText(message);
            } else if (requestCode == 3) {
                String message = data.getStringExtra(MESSAGE);
                mTxtPlace.setText(message);
            }
        }
    }

    public void getDefaultInfor() {
        mCal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat(FORMAT_DATE, Locale.getDefault());
        String strDate = dft.format(mCal.getTime());
        mTxtDateStart.setText(strDate);
        mTxtDateFinish.setText(strDate);
        dft = new SimpleDateFormat(FORMAT_TIME_A, Locale.getDefault());
        String strTime = dft.format(mCal.getTime());
        mTxtTimeStart.setText(strTime);
        mTxtTimeFinish.setText(strTime);
        dft = new SimpleDateFormat(FORMAT_TIME, Locale.getDefault());
        mTxtTimeStart.setTag(dft.format(mCal.getTime()));
        mTxtTimeFinish.setTag(dft.format(mCal.getTime()));
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
                MyDialogRepeat md = new MyDialogRepeat();
                md.show(CreateEventActvity.this.getFragmentManager(), MESSAGE);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!(v instanceof EditText)) {
            hideSoftKeyboard(CreateEventActvity.this);
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

    private class MyDialogRepeat extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.activity_repeat, null, false);
            mRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
            mRadioButton1 = (RadioButton) v.findViewById(R.id.radio1);
            mRadioButton2 = (RadioButton) v.findViewById(R.id.radio2);
            mRadioButton3 = (RadioButton) v.findViewById(R.id.radio3);
            mRadioButton4 = (RadioButton) v.findViewById(R.id.radio4);
            mTxtOption = (TextView) v.findViewById(R.id.txtOption);
            mTxtOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialogOption mdo = new MyDialogOption();
                    mdo.show(CreateEventActvity.this.getFragmentManager(), MESSAGE);
                    Toast.makeText(CreateEventActvity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    MyDialogRepeat.this.dismiss();
                }
            });
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio1:
                            mTxtRepeat.setText(mRadioButton1.getText().toString());
                            break;
                        case R.id.radio2:
                            mTxtRepeat.setText(mRadioButton2.getText().toString());
                            break;
                        case R.id.radio3:
                            mTxtRepeat.setText(mRadioButton3.getText().toString());
                            break;
                        case R.id.radio4:
                            mTxtRepeat.setText(mRadioButton4.getText().toString());
                            break;
                        case R.id.txtOption:
                            Toast.makeText(CreateEventActvity.this, MESSAGE, Toast.LENGTH_SHORT).toString();
                            break;
                    }
                }
            });

            builder.setView(v)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }
    }

    private class MyDialogOption extends DialogFragment {
        String result, result1, result2;
        RadioGroup mGroup1, mGroup2;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.activity_repeat_option, null, false);
            mGroup1 = (RadioGroup) v.findViewById(R.id.radioGroup1);
            mGroup2 = (RadioGroup) v.findViewById(R.id.radioGroup2);
            mEdtOption = (EditText) v.findViewById(R.id.edtOption);
            mRadioPhut = (RadioButton) v.findViewById(R.id.radioPhut);
            mRadioGio = (RadioButton) v.findViewById(R.id.radioGio);
            mRadioNgay = (RadioButton) v.findViewById(R.id.radioNgay);
            mRadioTuan = (RadioButton) v.findViewById(R.id.radioTuan);
            mRadioTB = (RadioButton) v.findViewById(R.id.radioNotifi);
            mRadioEmail = (RadioButton) v.findViewById(R.id.radioEmail);
            result1 = mRadioPhut.getText().toString();
            result2 = mRadioTB.getText().toString();
            mGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (mRadioGio.isChecked()) {
                        mRadioGio.setTextColor(Color.BLUE);
                        mRadioPhut.setTextColor(Color.BLACK);
                        mRadioNgay.setTextColor(Color.BLACK);
                        mRadioTuan.setTextColor(Color.BLACK);
                        result1 = mRadioGio.getText().toString();
                    }
                    if (mRadioPhut.isChecked()) {
                        mRadioPhut.setTextColor(Color.BLUE);
                        mRadioGio.setTextColor(Color.BLACK);
                        mRadioNgay.setTextColor(Color.BLACK);
                        mRadioTuan.setTextColor(Color.BLACK);
                        result1 = mRadioPhut.getText().toString();

                    }
                    if (mRadioNgay.isChecked()) {
                        mRadioNgay.setTextColor(Color.BLUE);
                        mRadioPhut.setTextColor(Color.BLACK);
                        mRadioGio.setTextColor(Color.BLACK);
                        mRadioTuan.setTextColor(Color.BLACK);
                        result1 = mRadioNgay.getText().toString();

                    }
                    if (mRadioTuan.isChecked()) {
                        mRadioTuan.setTextColor(Color.BLUE);
                        mRadioPhut.setTextColor(Color.BLACK);
                        mRadioNgay.setTextColor(Color.BLACK);
                        mRadioGio.setTextColor(Color.BLACK);
                        result1 = mRadioTuan.getText().toString();

                    }
                }
            });

            mGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (mRadioTB.isChecked()) {
                        mRadioTB.setTextColor(Color.BLUE);
                        mRadioEmail.setTextColor(Color.BLACK);
                        result2 = mRadioTB.getText().toString();
                    }
                    if (mRadioEmail.isChecked()) {
                        mRadioEmail.setTextColor(Color.BLUE);
                        mRadioTB.setTextColor(Color.BLACK);
                        result2 = mRadioEmail.getText().toString();
                    }
                }
            });

            builder.setView(v)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            result = mEdtOption.getText().toString() + " " + result1 + " " + result2;
                            mTxtRepeat.setText(result);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }

    }

    public void showDateStartPickerDialog() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                mTxtDateStart.setText(
                        (Utils.formatDate(dayOfMonth, (monthOfYear + 1), year)));
                mCal.set(year, monthOfYear, dayOfMonth);
                mDateFinish = mCal.getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendarStart = (Calendar) calendar.clone();
            }
        };

        String s = mTxtDateStart.getText() + "";
        String strArrtmp[] = s.split("-");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                CreateEventActvity.this,
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
                        (Utils.formatDate(dayOfMonth, (monthOfYear + 1), year)));
                mCal.set(year, monthOfYear, dayOfMonth);
                mDateFinish = mCal.getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendarFinish = (Calendar) calendar.clone();
                if (mCalendarFinish.compareTo(mCalendarStart) < 0) {
                    Toast.makeText(CreateEventActvity.this, R.string.unable, Toast.LENGTH_SHORT).show();
                    mTxtDateFinish.setText(Utils.formatDate(mCalendarStart.get(Calendar.DAY_OF_MONTH),
                            mCalendarStart.get(Calendar.MONTH) + 1, mCalendarStart.get(Calendar.YEAR)));
                }
            }
        };
        String s = mTxtDateFinish.getText() + "";
        String strArrtmp[] = s.split("-");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                CreateEventActvity.this,
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
                    hourTam -= 12;
                mTxtTimeStart.setText
                        (Utils.formatTime(hourTam, minute) + (hourOfDay > 12 ? TIME_PM : TIME_AM));
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
                CreateEventActvity.this,
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
                        (Utils.formatTime(hourTam, minute) + (hourOfDay > 12 ? TIME_PM : TIME_AM));
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
                CreateEventActvity.this,
                callback, gio, phut, true);
        time.setTitle(R.string.select_time_finish);
        time.show();
    }

    public void getDataFromService(final NewEvent newEvent) {
        ServiceBuilder.getService().createEvent(newEvent).enqueue(new Callback<CreateEventResponse>() {
            @Override
            public void onResponse(Call<CreateEventResponse> call, Response<CreateEventResponse> response) {
                if (response.body() == null) {
                    String error = null;
                    try {
                        error = Utils.getStringFromJson(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (error.equals(NOT_AUTHENTICATION)) {
                        logout();
                    } else {
                        Toast.makeText(CreateEventActvity.this, R.string.create_error, Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage().equals(SUCCESS)) {
                    Toast.makeText(CreateEventActvity.this, R.string.success, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateEventResponse> call, Throwable t) {

            }
        });
    }

    private void logout() {
        Toast.makeText(CreateEventActvity.this, MESSAGE_NOT_AUTHENTICATION, Toast.LENGTH_SHORT).show();
        new EventRepositoriesLocal(Realm.getDefaultInstance()).clearDatabase(new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(CreateEventActvity.this, "Logout Success!", Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(CreateEventActvity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public NewEvent createEvent() {
        Event event = new Event();
        event.setTitle(mEdtTitle.getText().toString());
        String dateStart = mTxtDateStart.getText().toString() + " "
                + mTxtTimeStart.getText().toString();
        dateStart = dateStart.substring(0, dateStart.length() - 2);
        dateStart = dateStart.trim();
        event.setStartTime(TimeUtils.stringToDate(dateStart, "dd-MM-yyyy H:mm"));
        String dateFinish = mTxtDateFinish.getText().toString() + " " + mTxtTimeFinish.getText().toString();
        dateFinish = dateFinish.substring(0, dateFinish.length() - 2);
        dateFinish = dateFinish.trim();
        event.setFinishTime(TimeUtils.stringToDate(dateFinish, "dd-MM-yyyy H:mm"));
        event.setCalendarId(Session.sCalendarId);
        return new NewEvent(Session.sAuthToken, event);
    }
}
