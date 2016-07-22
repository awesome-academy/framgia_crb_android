package framgia.vn.framgiacrb.network;

import java.util.List;

import framgia.vn.framgiacrb.data.model.Event;
import framgia.vn.framgiacrb.data.model.LoginResponse;
import framgia.vn.framgiacrb.data.model.ResposeDTO;
import framgia.vn.framgiacrb.data.model.UserLogin;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by nghicv on 18/07/2016.
 */
public interface CrbService {
    @GET("events/")
    Call<ResposeDTO> listEvents(@Query("auth_token") String auth, @Query("calendars") int calendar);

    @POST("sessions/")
    Call<LoginResponse> authenticate(@Header("email") String email, @Header("password") String password);
}
