package framgia.vn.framgiacrb.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nghicv on 18/07/2016.
 */
public class ServiceBuilder {
    public static final String BASE_URL = "";

    public static final int TOKEN_EXPIRED = 401;

    private static Retrofit sInstance;
    private static CrbService sService;

    private static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (sInstance == null) {
            sInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return sInstance;
    }

    public static CrbService getService() {
        if (sService == null) {
            sService = getRetrofit().create(CrbService.class);
        }

        return sService;
    }
}
