package framgia.vn.framgiacrb.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.LoginResponse;
import framgia.vn.framgiacrb.data.model.Place;
import framgia.vn.framgiacrb.data.model.Session;
import framgia.vn.framgiacrb.data.model.User;
import framgia.vn.framgiacrb.network.ServiceBuilder;
import framgia.vn.framgiacrb.utils.NetworkUtil;
import framgia.vn.framgiacrb.utils.ValidationLogin;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lethuy on 01/07/2016.
 */
public class LoginActivity extends Activity implements Realm.Transaction.OnSuccess,
    View.OnClickListener {
    private EditText mEditTextEmail, mEditTextPassword;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
    private List<Calendar> mListUserCalendar;
    private List<Calendar> mListShareUserCalendar;
    private List<Calendar> mListCalendar;
    private User mUserLogin;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences =
            getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Session.AUTHTOKEN)) {
            Session.sAuthToken = sharedPreferences.getString(Session.AUTHTOKEN, null);
            Session.sCalendarId = sharedPreferences.getInt(Session.CALENDAR_ID, -1);
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        mEditTextEmail = (EditText) findViewById(R.id.edit_email);
        mEditTextPassword = (EditText) findViewById(R.id.edit_password);
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    private void login() {
        boolean isValidEmail =
            ValidationLogin.isValidEmail(LoginActivity.this, mEditTextEmail);
        boolean isValidPass =
            ValidationLogin.isValidatePassword(LoginActivity.this, mEditTextPassword);
        if (!NetworkUtil.isInternetConnected(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, getString(R.string.msg_no_internet),
                Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail || !isValidPass) {
            Toast.makeText(LoginActivity.this, getString(R.string.error_email_invalid),
                Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog.setMessage(Constant.Message.LOADING);
        mProgressDialog.show();
        getDataFromInternet(user());
    }

    public void getDataFromInternet(User user) {
        ServiceBuilder.getService().authenticate(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                mProgressDialog.cancel();
                if (response.body() == null) {
                    Toast.makeText(LoginActivity.this, R.string.error_email_invalid,
                        Toast.LENGTH_SHORT).show();
                } else if (response.body().getMessage() != null &&
                    response.body().getMessage().equals(Constant.Message.LOGIN_SUCCESS)) {
                    mUserLogin = response.body().getUser();
                    Session.sAuthToken = mUserLogin.getAuth_token();
                    SharedPreferences sharedPreferences =
                        getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Session.AUTHTOKEN, Session.sAuthToken);
                    editor.apply();
                    mListUserCalendar = mUserLogin.getUserCalendars();
                    mListShareUserCalendar = mUserLogin.getShareUserCalendars();
                    mListCalendar = new ArrayList<>();
                    new EventRepositoriesLocal(Realm.getDefaultInstance())
                        .clearCalendarFromDatabase(new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                saveCalendarToDatabase();
                            }
                        });
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_email_invalid),
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mProgressDialog.cancel();
                Toast.makeText(LoginActivity.this, getString(R.string.error), Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }

    private void saveCalendarToDatabase() {
        for (int i = 0; i < mListUserCalendar.size(); i++) {
            Calendar calendar = new Calendar();
            calendar.setId(mListUserCalendar.get(i).getId());
            calendar.setUserId(mListUserCalendar.get(i).getUserId());
            calendar.setPermissionId(mListUserCalendar.get(i).getPermissionId());
            calendar.setColorId(mListUserCalendar.get(i).getColorId());
            calendar.setCalendarId(mListUserCalendar.get(i).getCalendarId());
            calendar.setName(mListShareUserCalendar.get(i).getName());
            calendar.setStatus(mListShareUserCalendar.get(i).getStatus());
            calendar.setDefault(mListShareUserCalendar.get(i).isDefault());
            calendar.setDescription(mListShareUserCalendar.get(i).getDescription());
            mListCalendar.add(calendar);
        }
        Realm realm = Realm.getDefaultInstance();
        new EventRepositoriesLocal(realm).addCalendars(mListCalendar, LoginActivity.this);
        Session.sCalendarId = mListUserCalendar.get(0).getCalendarId();
        SharedPreferences sharedPreferences =
            getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.NAME_TITLE, mUserLogin.getName());
        editor.putString(MainActivity.EMAIL_TITLE, mUserLogin.getEmail());
        editor.putInt(Session.CALENDAR_ID, Session.sCalendarId);
        editor.apply();
    }

    public User user() {
        User user = new User();
        user.setEmail(mEditTextEmail.getText().toString());
        user.setPassword(mEditTextPassword.getText().toString());
        return user;
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        getPlaceFromServer(Session.sAuthToken);
        getUserFromServer(Session.sAuthToken);
        startActivity(intent);
        finish();
    }

    public void getPlaceFromServer(String authToken) {
        ServiceBuilder.getService().listPlace(authToken).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, final Response<List<Place>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        new EventRepositoriesLocal(Realm.getDefaultInstance())
                            .clearPlaceFromDatabase(new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Realm realm = Realm.getDefaultInstance();
                                    new EventRepositoriesLocal(realm).addPlaces(response.body(),
                                        new Realm.Transaction.OnSuccess() {
                                            @Override
                                            public void onSuccess() {
                                            }
                                        });
                                }
                            });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
            }
        });
    }

    public void getUserFromServer(String authToken) {
        ServiceBuilder.getService().listAttendee(authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, final Response<List<User>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        new EventRepositoriesLocal(Realm.getDefaultInstance())
                            .clearPlaceFromDatabase(new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Realm realm = Realm.getDefaultInstance();
                                    new EventRepositoriesLocal(realm).addUser(response.body(),
                                        new Realm.Transaction.OnSuccess() {
                                            @Override
                                            public void onSuccess() {
                                            }
                                        });
                                }
                            });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_email_invalid),
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            login();
        }
    }
}
