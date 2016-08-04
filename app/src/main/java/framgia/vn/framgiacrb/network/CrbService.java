package framgia.vn.framgiacrb.network;

import framgia.vn.framgiacrb.data.model.CreateEventResponse;
import framgia.vn.framgiacrb.data.model.LoginResponse;
import framgia.vn.framgiacrb.data.model.NewEvent;
import framgia.vn.framgiacrb.data.model.ResposeDTO;
import framgia.vn.framgiacrb.data.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by nghicv on 18/07/2016.
 */
public interface CrbService {
    @GET("events/")
    Call<ResposeDTO> listEvents(@Query("auth_token") String auth, @Query("calendars") int calendar);

    @POST("sessions/")
    Call<LoginResponse> authenticate(@Body User user);

    @POST("events.json/")
    Call<CreateEventResponse> createEvent(@Body NewEvent event);
}
