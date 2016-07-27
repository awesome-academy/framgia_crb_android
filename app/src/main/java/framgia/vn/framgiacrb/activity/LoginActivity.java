package framgia.vn.framgiacrb.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.constant.Constant;
import framgia.vn.framgiacrb.data.model.LoginResponse;
import framgia.vn.framgiacrb.data.model.UserLogin;
import framgia.vn.framgiacrb.object.ApiInterface;
import framgia.vn.framgiacrb.utils.NetworkUtil;
import framgia.vn.framgiacrb.utils.ValidationLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lethuy on 01/07/2016.
 */
public class LoginActivity extends Activity {
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
                        getDataFromInternet();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.error_email_invalid), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void getDataFromInternet(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);

        Call<UserLogin> call = service.authenticate(mEditTextEmail.getText().toString(),
                mEditTextPassword.getText().toString());

        Call<LoginResponse> loginResponseCall = service.getMessage(mEditTextEmail.getText().toString(),
                mEditTextPassword.getText().toString());

        call.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                mProgressDialog.cancel();
                if (response.body() == null) {
                    Toast.makeText(LoginActivity.this, R.string.error_email_invalid, Toast.LENGTH_SHORT).show();
                } else if (response.body().getMessage() != null && response.body().getMessage().equals(Constant.LOGIN_SUCCESS)) {
                    UserLogin userLogin = new UserLogin();
                    try {
                        int id = response.body().getId();
                        String name = response.body().getName();
                        String email = response.body().getEmail();
                        String avatar = response.body().getAvatar();
                        String auth_token = response.body().getAuth_token();

                        userLogin.setId(id);
                        userLogin.setName(name);
                        userLogin.setEmail(email);
                        userLogin.setAvatar(avatar);
                        userLogin.setAuth_token(auth_token);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(Constant.KEY_NAME, userLogin);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_email_invalid), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {

            }
        });
    }

}
