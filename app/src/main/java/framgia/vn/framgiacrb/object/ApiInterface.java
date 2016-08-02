package framgia.vn.framgiacrb.object;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lethuy on 26/07/2016.
 */
public interface ApiInterface {
    @GET("events/{id}.json")
    Call<EventDetailResponse> getEventDetails(@Path("id") int id,
                                              @Query("auth_token") String authToken);
}
