package framgia.vn.framgiacrb.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.Calendar;
import framgia.vn.framgiacrb.data.model.LoginResponse;
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
public class LoginActivity extends Activity implements Realm.Transaction.OnSuccess{
    private EditText mEditTextEmail, mEditTextPassword;
    private Button mButtonLogin, mButtonLoginFb, mButtonLoginGg;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmail = (EditText) findViewById(R.id.edit_email);
        mEditTextPassword = (EditText) findViewById(R.id.edit_password);
        mButtonLogin = (Button) findViewById(R.id.btn_login);
        mButtonLoginFb = (Button) findViewById(R.id.btn_facebook);
        mButtonLoginGg = (Button) findViewById(R.id.btn_google);
        mProgressDialog = new ProgressDialog(LoginActivity.this);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean isValidEmail =  ValidationLogin.isValidEmail(LoginActivity.this, mEditTextEmail);
                boolean isValidPass = ValidationLogin.isValidatePassword(LoginActivity.this, mEditTextPassword);

                if (!NetworkUtil.isInternetConnected(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
                } else {
                    if (isValidEmail && isValidPass){
                        mProgressDialog.setMessage(Constant.LOADING);
                        mProgressDialog.show();
                        getDataFromInternet(user());

                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.error_email_invalid), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void getDataFromInternet(User user){

        ServiceBuilder.getService().authenticate(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                mProgressDialog.cancel();
                if (response.body() == null) {
                    Toast.makeText(LoginActivity.this, R.string.error_email_invalid, Toast.LENGTH_SHORT).show();
                } else if (response.body().getMessage() != null && response.body().getMessage().equals(Constant.LOGIN_SUCCESS)) {
                    User userLogin = response.body().getUser();
                    Session.sAuthToken = userLogin.getAuth_token();
                    List<Calendar> listUserCalendar = userLogin.getUserCalendars();
                    List<Calendar> listShareUserCalendar = userLogin.getShareUserCalendars();
                    List<Calendar> listCalendar = new ArrayList<Calendar>();
                    for (int i = 0; i < listUserCalendar.size(); i++) {
                        Calendar calendar = new Calendar();
                        calendar.setId(listUserCalendar.get(i).getId());
                        calendar.setUserId(listUserCalendar.get(i).getUserId());
                        calendar.setPermissionId(listUserCalendar.get(i).getPermissionId());
                        calendar.setColorId(listUserCalendar.get(i).getColorId());
                        calendar.setName(listShareUserCalendar.get(i).getName());
                        calendar.setStatus(listShareUserCalendar.get(i).getStatus());
                        calendar.setDefault(listShareUserCalendar.get(i).isDefault());
                        calendar.setDescription(listShareUserCalendar.get(i).getDescription());
                        listCalendar.add(calendar);
                    }
                    Realm realm = Realm.getDefaultInstance();
                    new EventRepositoriesLocal(realm).addCalendars(listCalendar, LoginActivity.this);
                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(MainActivity.NAME_TITLE, userLogin.getName());
                    editor.putString(MainActivity.EMAIL_TITLE, userLogin.getEmail());
                    editor.commit();
                }
                else {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_email_invalid), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mProgressDialog.cancel();
                Toast.makeText(LoginActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public User user(){
        User user = new User();
        user.setEmail(mEditTextEmail.getText().toString());
        user.setPassword(mEditTextPassword.getText().toString());
        return user;
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
