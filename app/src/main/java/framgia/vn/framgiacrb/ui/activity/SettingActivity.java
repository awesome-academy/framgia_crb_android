package framgia.vn.framgiacrb.ui.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.GoogleCalendar;
import framgia.vn.framgiacrb.utils.GoogleCalendarUtil;

public class SettingActivity extends PreferenceActivity
    implements Preference.OnPreferenceChangeListener {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_setting);
        initViews();
    }

    private void initViews() {
        ListPreference listPreference = (ListPreference) findPreference(getResources().getString(R
            .string.calendar_key));
        List<GoogleCalendar> calendarList = GoogleCalendarUtil.getAllCalendarName(this);
        List listEntry = new ArrayList();
        for (GoogleCalendar googleCalendar : calendarList) {
            listEntry.add(googleCalendar.getAccountName());
        }
        listEntry.add(0, Constant.GoogleCalendar.ALL_CALENDAR);
        CharSequence[] entries = (CharSequence[]) listEntry.toArray(new
            CharSequence[listEntry.size()]);
        listPreference.setEntries(entries);
        listPreference.setEntryValues(entries);
        listPreference.setSummary(Constant.GoogleCalendar.ALL_CALENDAR);
        listPreference.setDefaultValue(Constant.GoogleCalendar.ALL_CALENDAR);
        listPreference.setOnPreferenceChangeListener(this);
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
                setResult(Constant.RequestCode.SETTING);
                finish();
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary((String) newValue);
        return true;
    }
}
