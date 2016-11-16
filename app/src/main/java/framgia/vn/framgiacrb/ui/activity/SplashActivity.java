package framgia.vn.framgiacrb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.Session;
import framgia.vn.framgiacrb.data.remote.EventRepositories;
import framgia.vn.framgiacrb.listener.OnLoadEventListener;
import framgia.vn.framgiacrb.utils.Connectivity;
import framgia.vn.framgiacrb.utils.GoogleCalendarUtil;

/**
 * Created by nghicv on 15/09/2016.
 */
public class SplashActivity extends AppCompatActivity implements OnLoadEventListener {
    private EventRepositories mEventRepositories;
    private Calendar mCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadDatas();
    }

    private void loadDatas() {
        mEventRepositories = new EventRepositories();
        mEventRepositories.setOnLoadEventListener(this);
        mCalendar = new Calendar();
        mCalendar.setId(Session.sCalendarId);
        if (Connectivity.isConnected(this) && Connectivity.isConnectedFast(this)) {
            mEventRepositories.getEventsByCalendar(Session.sAuthToken, mCalendar, this);
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            Toast.makeText(this, getString(R.string.message_not_connect), Toast.LENGTH_SHORT)
                .show();
            finish();
        }
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onError() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        finish();
    }
}
