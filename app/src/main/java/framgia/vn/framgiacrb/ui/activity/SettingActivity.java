package framgia.vn.framgiacrb.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.GoogleCalendar;
import framgia.vn.framgiacrb.utils.GoogleCalendarUtil;

public class SettingActivity extends PreferenceActivity implements
    Preference.OnPreferenceChangeListener {
    private Toolbar mToolbar;
    private ArrayList mListCalendar = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_setting);
        initViews();
    }

    private void initViews() {
        List<GoogleCalendar> calendarList = GoogleCalendarUtil.getAllCalendarName(this);
        for (GoogleCalendar googleCalendar : calendarList) {
            mListCalendar.add(googleCalendar.getAccountName());
            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(this);
            checkBoxPreference.setKey(googleCalendar.getAccountName());
            checkBoxPreference.setDefaultValue(true);
            checkBoxPreference.setTitle(googleCalendar.getAccountName());
            checkBoxPreference.setOnPreferenceChangeListener(this);
            getPreferenceScreen().addPreference(checkBoxPreference);
        }
        CheckBoxPreference checkBoxBirthday = (CheckBoxPreference) findPreference(getResources()
            .getString(R.string.birthday_key));
        checkBoxBirthday.setDefaultValue(true);
        CheckBoxPreference checkBoxHoliday = (CheckBoxPreference) findPreference(getResources()
            .getString(R.string.holiday_key));
        checkBoxHoliday.setDefaultValue(true);
        CheckBoxPreference checkBoxReminder = (CheckBoxPreference) findPreference(getResources()
            .getString(R.string.reminder_key));
        checkBoxReminder.setDefaultValue(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LinearLayout root =
            (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        mToolbar =
            (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(mToolbar, 0);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putCharSequenceArrayListExtra(Constant.Intent.INTENT_LIST_CALENDAR,
                    mListCalendar);
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.GoogleCalendar
                    .PREF_SAVE_ACCOUNT, MODE_PRIVATE);
                Set accountSet = new HashSet<String>();
                accountSet.addAll(mListCalendar);
                sharedPreferences.edit()
                    .putStringSet(Constant.GoogleCalendar.STRING_ACCOUNT_KEY, accountSet)
                    .commit();
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (!(Boolean) newValue) {
            mListCalendar.remove(preference.getKey());
        } else mListCalendar.add(preference.getKey());
        return true;
    }
}
