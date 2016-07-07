package framgia.vn.framgiacrb.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import framgia.vn.framgiacrb.object.EventInWeek;

/**
 * Created by lethuy on 05/07/2016.
 */
public class CreateEventActvity extends Activity {

    private ImageButton mImageButtonBack, mImageButtonAlarm, mImageButtonRepeat, mIamgeButtonSave;
    private EditText mEdtEvent, mEdtDesciption;
    private Switch mSwitchAlarm;
    private TextView mTxtDateStart, mTxtTimeStart, mTxtDateFinish, mTxtTimeFinish, mTxtRepeat, mTxtNewEvent;
    private Spinner mSpinerCalendar;

    ArrayList<EventInWeek> arrJob = new ArrayList<EventInWeek>();
    ArrayAdapter<EventInWeek> adapter = null;
    private Calendar mCal;
    private Date mDateFinish, mHourFinish;

    ArrayList<String> lstData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        mTxtDateStart = (TextView) findViewById(R.id.txt_DateStart);
        mTxtDateFinish = (TextView) findViewById(R.id.txt_DateFinish);
        mTxtTimeStart = (TextView) findViewById(R.id.txt_timeStart);
        mTxtTimeFinish = (TextView) findViewById(R.id.txt_TimeFinish);
        mTxtNewEvent = (TextView) findViewById(R.id.txt_NewEvent);

        mTxtNewEvent.setText(R.string.new_event);

        mImageButtonBack = (ImageButton) findViewById(R.id.btn_back);
        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter = new ArrayAdapter<EventInWeek>
                (this, android.R.layout.simple_list_item_1, arrJob);

        lstData = new ArrayList<>();
        lstData.add("Lê Thị Thúy");
        lstData.add("Cấn Văn Nghị");
        lstData.add("Mai Đại Diện");
        lstData.add("Đặng Anh Quân");
        lstData.add("Nguyễn Văn Toàn");
        mSpinerCalendar = (Spinner) findViewById(R.id.spin_Calendar);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstData);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinerCalendar.setAdapter(adapter);

        getDefaultInfor();
        addEventFormWidgets();
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
                    hourTam = hourTam - 12;
                mTxtTimeStart.setText
                        (hourTam + ":" + minute + (hourOfDay>12?" PM":" AM"));
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
                        (hourTam + ":" + minute + (hourOfDay>12?" PM":" AM"));
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

}
