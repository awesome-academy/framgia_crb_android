package framgia.vn.framgiacrb.object;

import framgia.vn.framgiacrb.data.model.LoginResponse;
import framgia.vn.framgiacrb.data.model.UserLogin;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by lethuy on 26/07/2016.
 */
public interface ApiInterface {
    @POST("sessions/")
    Call<UserLogin> authenticate(@Header("email") String email, @Header("password") String password);

    @POST("sessions/")
    Call<LoginResponse> getMessage(@Header("user") String user, @Header("message") String message);
}
