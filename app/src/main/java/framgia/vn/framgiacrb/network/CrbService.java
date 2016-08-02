package framgia.vn.framgiacrb.network;

import framgia.vn.framgiacrb.data.model.CreateEventResponse;
import framgia.vn.framgiacrb.data.model.LoginResponse;
import framgia.vn.framgiacrb.data.model.NewEvent;
import framgia.vn.framgiacrb.data.model.ResposeDTO;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("events.json/")
    Call<CreateEventResponse> createEvent(@Body NewEvent event);
}
